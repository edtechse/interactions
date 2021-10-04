package com.nus.edtech.interactions.services;

import com.nus.edtech.interactions.common.utils.InteractionsValidator;
import com.nus.edtech.interactions.dao.InteractionsEntity;
import com.nus.edtech.interactions.repositories.InteractionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
