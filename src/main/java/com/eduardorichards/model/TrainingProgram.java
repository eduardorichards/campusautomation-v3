package com.eduardorichards.model;

public class TrainingProgram {
    
    private final String title;
    private final String detailUrl;

    public TrainingProgram(String title, String detailUrl) {
        this.title = title;
        this.detailUrl = detailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDetailUrl() {
        return detailUrl;
    }
}
