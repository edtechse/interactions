package com.nus.edtech.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;

import java.io.Serializable;
import java.util.Set;

public class BlogsInteractionDto implements Serializable {
    private static final long serialVersionUID = 1059822129647939881L;

    private String blogId;
    private String interactionId;
    private String interactionType;
    private int interactionValue;

    public BlogsInteractionDto(String blogId, String interactionId, String interactionType, int interactionValue) {
        this.blogId = blogId;
        this.interactionId = interactionId;
        this.interactionType = interactionType;
        this.interactionValue = interactionValue;
    }

    public String getBlogId() {
        return blogId;
    }
    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

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

    @DynamoDBAttribute(attributeName = "interactionvalue")
    public int getInteractionValue() {
        return interactionValue;
    }
    public void setInteractionValue(int interactionValue) {
        this.interactionValue = interactionValue;
    }

    @Override
    public String toString() {
        return "Interaction [interactionId=" + interactionId + ", interactionType=" + interactionType + ", interactionValue=" + interactionValue
                + "]";
    }
}