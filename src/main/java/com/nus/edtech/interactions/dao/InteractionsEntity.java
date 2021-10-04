package com.nus.edtech.interactions.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import java.util.List;
import java.util.Set;


@DynamoDBTable(tableName = "Interactions")
public class InteractionsEntity {
    private String interactionId;
    private String interactionType;
    private int interactionValue;
    private String interactionAuthor;
    private String interactionCreationDate;
    private String seedType;
    private String seedId;

    @DynamoDBHashKey(attributeName = "interactionid")
    public String getInteractionId() {
        return interactionId;
    }
    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    @DynamoDBAttribute(attributeName = "interactiontype")
    public String getInteractionType() {
        return interactionType;
    }
    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    @DynamoDBAttribute(attributeName = "interactionvalue")
    public int getInteractionValue() {
        return interactionValue;
    }
    public void setInteractionValue(int interactionValue) {
        this.interactionValue = interactionValue;
    }

    @DynamoDBAttribute(attributeName = "interactionauthor")
    public String getInteractionAuthor() {
        return interactionAuthor;
    }

    public void setInteractionAuthor(String interactionAuthor) {
        this.interactionAuthor = interactionAuthor;
    }

    @DynamoDBAttribute(attributeName = "interactioncreationdate")
    public String getInteractionCreationDate() { return interactionCreationDate; }
    public void setInteractionCreationDate(String interactionCreationDate) {
        this.interactionCreationDate = interactionCreationDate;
    }

    @DynamoDBAttribute(attributeName = "seedtype")
    public String getSeedType() { return seedType; }
    public void setSeedType(String seedType) { this.seedType = seedType; }

    @DynamoDBAttribute(attributeName = "seedid")
    public String getSeedId() { return seedId; }
    public void setSeedId(String seedId) { this.seedId = seedId; }

    @Override
    public String toString() {
        return "Interaction [interactionId=" + interactionId + ", interactionType=" + interactionType + ", interactionValue=" + interactionValue
                + ", interactionAuthor=" + interactionAuthor + ", seedType=" + seedType + ", seedId=" + seedId + "]";
    }


}

