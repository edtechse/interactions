package com.nus.edtech.interactions.common.utils;

import com.nus.edtech.interactions.dao.InteractionsEntity;
import com.nus.edtech.interactions.exceptions.InvalidRequestException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class InteractionsValidator {

    public void validateInputInteractions(InteractionsEntity interactionsEntity){
        if(StringUtils.isEmpty(interactionsEntity.getInteractionType()))
                    throw new InvalidRequestException("Invalid Interaction Type");
        if(interactionsEntity.getInteractionValue() < 0)
            throw new InvalidRequestException("Invalid Interaction Value");
        if(StringUtils.isEmpty(interactionsEntity.getInteractionAuthor()))
            throw new InvalidRequestException("Invalid Interaction Author");
        if(StringUtils.isEmpty(interactionsEntity.getInteractionCreationDate()))
            throw new InvalidRequestException("Invalid Creation Date");
    }
}
