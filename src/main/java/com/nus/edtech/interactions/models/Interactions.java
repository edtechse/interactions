package com.nus.edtech.interactions.models;

public class Interactions {

    private String interactionId;
    private String interactionType;
    private int interactionValue;
    private String interactionAuthor;
    private String interactionCreationDate;
    private String seedType;
    private String seedId;

    public String getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public int getInteractionValue() {
        return interactionValue;
    }

    public void setInteractionValue(int interactionValue) {
        this.interactionValue = interactionValue;
    }

    public String getInteractionAuthor() {
        return interactionAuthor;
    }

    public void setInteractionAuthor(String interactionAuthor) {
        this.interactionAuthor = interactionAuthor;
    }

    public String getSeedType() {
        return seedType;
    }

    public void setSeedType(String seedType) {
        this.seedType = seedType;
    }

    public String getSeedId() {
        return seedId;
    }

    public void setSeedId(String seedId) {
        this.seedId = seedId;
    }

    public String getInteractionCreationDate() {
        return interactionCreationDate;
    }

    public void setInteractionCreationDate(String interactionCreationDate) {
        this.interactionCreationDate = interactionCreationDate;
    }
}
