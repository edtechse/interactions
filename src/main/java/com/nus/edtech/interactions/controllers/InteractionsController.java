package com.nus.edtech.interactions.controllers;

import com.nus.edtech.interactions.dao.InteractionsEntity;
import com.nus.edtech.interactions.exceptions.InvalidRequestException;
import com.nus.edtech.interactions.models.Interactions;
import com.nus.edtech.interactions.services.InteractionsService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/interactions/")

public class InteractionsController {

    private static final String BLOGS_SERVICE = "blogsService";
    private static final String BLOGS_SERVICE_URL = "http://localhost:9001/v1/blogs/blog/";

    @Autowired
    private InteractionsService interactionsService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("all")
    public List<Interactions> getAllInteractions(){
        try {
            List<InteractionsEntity> responseBlogList = interactionsService.getAllInteractions();
            return mapperFacade.mapAsList(responseBlogList,Interactions.class);
        } catch (Exception ex) {
            throw ex;
        }

    }

    @GetMapping("interaction/{interactionid}")
    public InteractionsEntity getInteractionById(@PathVariable(value = "interactionid") String interactionId){
       if(interactionId == null)
           throw new InvalidRequestException("Invalid Interaction Id passed");
        try {
            return interactionsService.getInteractionById(interactionId);

        } catch (Exception ex) {
            throw ex;
        }

    }

    @DeleteMapping("interaction/{interactionid}")
    public boolean deleteInteractionById(@PathVariable(value = "interactionid") String interactionId) throws Exception {
        try {
            if (interactionId == null)
                throw new Exception("Empty interactionId sent for delete");
            interactionsService.deleteInteractionById(interactionId);
            return true;
        } catch (Exception ex) {
            throw new Exception("deleteInteractionById:: Failed to delete interaction due to " + ex.getMessage());
        }
    }

    @GetMapping("author/{authorid}")
    public List<String> getInteractionIdsByAuthor(@PathVariable(value = "authorid") String authorId) throws Exception {
        if (authorId == null)
            throw new Exception("Input author is null");
        return interactionsService.findInteractionsByAuthor(authorId);
    }

    @DeleteMapping("author/{authorid}")
    public boolean deleteInteractionByAuthor(@PathVariable(value = "authorid") String authorId) throws Exception {
        try {
            if (authorId == null)
                throw new Exception("Empty authorId sent for delete");
            interactionsService.deleteInteractionByAuthor(authorId);
            return true;
        } catch (Exception ex) {
            throw new Exception("deleteInteractionByAuthor:: Failed to delete interaction due to " + ex.getMessage());
        }
    }

    @PostMapping("{seedtype}/author/{author}")
    @CircuitBreaker(name= BLOGS_SERVICE, fallbackMethod= "interactionFallback")
    public String postInteraction(@PathVariable(value = "seedtype") String seedType,
                                   @PathVariable(value = "author") String author,
                                   @RequestBody Interactions requestInteraction) throws Exception {
        if (seedType == null || author == null || requestInteraction == null)
            throw new Exception("Input seed Type, author or request Interaction is null");
        try {
            requestInteraction.setSeedType(seedType);
            requestInteraction.setInteractionAuthor(author);
            String interactionId = UUID.randomUUID().toString().replaceAll("-","");
            requestInteraction.setInteractionId(interactionId);
            InteractionsEntity requestInteractionEntity = mapperFacade.map(requestInteraction, InteractionsEntity.class);
            interactionsService.postInteraction(requestInteractionEntity);
            restTemplate.put(BLOGS_SERVICE_URL+requestInteractionEntity.getSeedId()+"/interaction",
                    requestInteraction, String.class);
            return "Interaction added and blog updated successfully.";

        } catch (Exception ex) {
            throw ex;
        }
    }

    public String interactionFallback(Exception e){
        return "Interaction added, blog service is down";
    }
}
