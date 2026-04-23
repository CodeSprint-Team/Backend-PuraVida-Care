package com.cenfotec.backendcodesprint.api.ChecklistController;

import com.cenfotec.backendcodesprint.logic.Model.ChecklistItem;
import com.cenfotec.backendcodesprint.logic.User.DTO.Checklist.ChecklistItemDTO;
import com.cenfotec.backendcodesprint.logic.User.DTO.Checklist.ChecklistProgressDTO;
import com.cenfotec.backendcodesprint.logic.User.Service.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/checklist")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ChecklistController {

    private final ChecklistService checklistService;

    @GetMapping("/items")
    public ResponseEntity<List<ChecklistItem>> getAllItems() {
        return ResponseEntity.ok(checklistService.getAllActiveItems());
    }

    @GetMapping("/items/category/{category}")
    public ResponseEntity<List<ChecklistItem>> getItemsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(checklistService.getItemsByCategory(category));
    }

    @GetMapping("/items/required")
    public ResponseEntity<List<ChecklistItem>> getRequiredItems() {
        return ResponseEntity.ok(checklistService.getRequiredItems());
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ChecklistItem> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(checklistService.getItemById(id));
    }

    @PostMapping("/items")
    public ResponseEntity<ChecklistItem> createItem(@RequestBody ChecklistItemDTO itemDTO) {
        return ResponseEntity.ok(checklistService.createItem(itemDTO));
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ChecklistItem> updateItem(@PathVariable Long id, @RequestBody ChecklistItemDTO itemDTO) {
        return ResponseEntity.ok(checklistService.updateItem(id, itemDTO));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        checklistService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/progress")
    public ResponseEntity<ChecklistProgressDTO> getProgress(@RequestBody Map<String, List<Long>> request) {
        List<Long> completedItemIds = request.get("completedItemIds");
        return ResponseEntity.ok(checklistService.getProgress(completedItemIds));
    }
}