package com.nus.edtech.interactions.repositories;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.nus.edtech.interactions.dao.InteractionsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InteractionsRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void saveInteraction(InteractionsEntity interactionsEntity){
        dynamoDBMapper.save(interactionsEntity);
    }

    public List<InteractionsEntity> findAllInteractions() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<InteractionsEntity> scanResult = dynamoDBMapper.scan(InteractionsEntity.class,scanExpression);
        return scanResult;
    }

    public InteractionsEntity findInteractionById(String interactionId) {
        return dynamoDBMapper.load(InteractionsEntity.class, interactionId);
    }
}
