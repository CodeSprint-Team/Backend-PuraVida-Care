package com.cenfotec.backendcodesprint.logic.User.Service;

import com.cenfotec.backendcodesprint.logic.Model.ChecklistItem;
import com.cenfotec.backendcodesprint.logic.User.DTO.Checklist.ChecklistItemDTO;
import com.cenfotec.backendcodesprint.logic.User.DTO.Checklist.ChecklistProgressDTO;
import com.cenfotec.backendcodesprint.logic.User.Repository.ChecklistItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChecklistService {

    private final ChecklistItemRepository checklistItemRepository;

    public List<ChecklistItem> getAllActiveItems() {
        return checklistItemRepository.findByIsActiveTrueOrderByOrderIndexAsc();
    }

    public List<ChecklistItem> getItemsByCategory(String category) {
        return checklistItemRepository.findByCategoryAndIsActiveTrueOrderByOrderIndexAsc(category);
    }

    public List<ChecklistItem> getRequiredItems() {
        return checklistItemRepository.findByRequiredTrueAndIsActiveTrue();
    }

    public ChecklistItem getItemById(Long id) {
        return checklistItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Checklist item not found"));
    }

    public ChecklistItem createItem(ChecklistItemDTO itemDTO) {
        ChecklistItem item = new ChecklistItem();
        item.setCategory(itemDTO.getCategory());
        item.setTitle(itemDTO.getTitle());
        item.setDescription(itemDTO.getDescription());
        item.setRequired(itemDTO.getRequired() != null ? itemDTO.getRequired() : true);
        item.setOrderIndex(itemDTO.getOrderIndex() != null ? itemDTO.getOrderIndex() : 0);
        item.setIsActive(true);
        return checklistItemRepository.save(item);
    }

    public ChecklistItem updateItem(Long id, ChecklistItemDTO itemDTO) {
        ChecklistItem item = getItemById(id);
        item.setTitle(itemDTO.getTitle());
        item.setDescription(itemDTO.getDescription());
        item.setRequired(itemDTO.getRequired());
        return checklistItemRepository.save(item);
    }

    public void deleteItem(Long id) {
        ChecklistItem item = getItemById(id);
        item.setIsActive(false);
        checklistItemRepository.save(item);
    }

    public ChecklistProgressDTO getProgress(List<Long> completedItemIds) {
        List<ChecklistItem> allItems = getAllActiveItems();
        List<ChecklistItem> requiredItems = allItems.stream()
                .filter(ChecklistItem::getRequired)
                .collect(Collectors.toList());

        int completedCount = (int) allItems.stream()
                .filter(item -> completedItemIds.contains(item.getId()))
                .count();

        int completedRequiredCount = (int) requiredItems.stream()
                .filter(item -> completedItemIds.contains(item.getId()))
                .count();

        // Progress by category
        Map<String, ChecklistProgressDTO.CategoryProgress> categoriesProgress = new HashMap<>();
        Map<String, List<ChecklistItem>> itemsByCategory = allItems.stream()
                .collect(Collectors.groupingBy(ChecklistItem::getCategory));

        for (Map.Entry<String, List<ChecklistItem>> entry : itemsByCategory.entrySet()) {
            String category = entry.getKey();
            List<ChecklistItem> categoryItems = entry.getValue();
            int categoryCompleted = (int) categoryItems.stream()
                    .filter(item -> completedItemIds.contains(item.getId()))
                    .count();

            ChecklistProgressDTO.CategoryProgress progress = new ChecklistProgressDTO.CategoryProgress();
            progress.setCategory(category);
            progress.setTotal(categoryItems.size());
            progress.setCompleted(categoryCompleted);
            progress.setPercentage(categoryItems.size() > 0
                    ? (categoryCompleted * 100 / categoryItems.size()) : 0);
            categoriesProgress.put(category, progress);
        }

        ChecklistProgressDTO progress = new ChecklistProgressDTO();
        progress.setTotalItems(allItems.size());
        progress.setCompletedItems(completedCount);
        progress.setTotalRequired(requiredItems.size());
        progress.setCompletedRequired(completedRequiredCount);
        progress.setProgressPercentage(requiredItems.size() > 0
                ? (completedRequiredCount * 100 / requiredItems.size()) : 0);
        progress.setCategoriesProgress(categoriesProgress);

        return progress;
    }
}
