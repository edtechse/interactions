package com.nus.edtech.interactions.services;

import com.nus.edtech.interactions.common.utils.InteractionsValidator;
import com.nus.edtech.interactions.dao.InteractionsEntity;
import com.nus.edtech.interactions.repositories.InteractionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InteractionsService {

    @Autowired
    private InteractionsRepository interactionsRepository;

    @Autowired
    private InteractionsValidator interactionsValidator;

    public void postInteraction(InteractionsEntity interactionsEntity){
        interactionsValidator.validateInputInteractions(interactionsEntity);
        interactionsRepository.saveInteraction(interactionsEntity);
    }

    public List<InteractionsEntity> getAllInteractions() {
        return interactionsRepository.findAllInteractions();
    }

    public InteractionsEntity getInteractionById(String interactionId) {
        return interactionsRepository.findInteractionById(interactionId);
    }

    public void deleteInteractionById(String interactionId) {
        InteractionsEntity interactionsEntity = interactionsRepository.findInteractionById(interactionId);
        interactionsRepository.deleteInteraction(interactionsEntity);
    }

    public void deleteInteractionByAuthor(String authorId) {
        List<InteractionsEntity> interactionsEntityList = interactionsRepository.findInteractionsByAuthor(authorId);
        for (InteractionsEntity interactionEntity : interactionsEntityList) {
            interactionsRepository.deleteInteraction(interactionEntity);
        }
    }

    public void deleteInteractionBySeedId(String seedId) {
        List<InteractionsEntity> interactionsEntityList = interactionsRepository.findInteractionsBySeedId(seedId);
        for (InteractionsEntity interactionEntity : interactionsEntityList) {
            interactionsRepository.deleteInteraction(interactionEntity);
        }
    }

    public List<String> findInteractionsByAuthor(String authorId) {
        List<InteractionsEntity> items = interactionsRepository.findInteractionsByAuthor(authorId);
        List<String> idList = new ArrayList<>();
        for(InteractionsEntity item: items) {
            idList.add(item.getInteractionId());
        }
        return idList;
    }

    public List<InteractionsEntity> findInteractionsBySeedId(String seedId) {
        return interactionsRepository.findInteractionsBySeedId(seedId);
    }
}
