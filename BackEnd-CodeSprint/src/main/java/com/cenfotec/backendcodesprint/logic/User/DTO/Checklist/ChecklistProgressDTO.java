package com.cenfotec.backendcodesprint.logic.User.DTO.Checklist;

import lombok.Data;
import java.util.Map;

@Data
public class ChecklistProgressDTO {
    private int totalItems;
    private int completedItems;
    private int totalRequired;
    private int completedRequired;
    private int progressPercentage;
    private Map<String, CategoryProgress> categoriesProgress;

    @Data
    public static class CategoryProgress {
        private String category;
        private int total;
        private int completed;
        private int percentage;
    }
}