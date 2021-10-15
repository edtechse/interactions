package com.nus.edtech.interactions.controllers;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.nus.edtech.interactions.dao.InteractionsEntity;
import com.nus.edtech.dto.BlogsInteractionDto;
import com.nus.edtech.interactions.exceptions.InvalidRequestException;
import com.nus.edtech.interactions.models.Interactions;
import com.nus.edtech.interactions.services.InteractionsService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.jms.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/interactions/")
public class InteractionsController {

    private static final String BLOGS_SERVICE = "blogsService";

    @Autowired
    private InteractionsService interactionsService;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("all")
    public List<Interactions> getAllInteractions(){
        try {
            List<InteractionsEntity> responseInteractionsList = interactionsService.getAllInteractions();
            return mapperFacade.mapAsList(responseInteractionsList,Interactions.class);
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
    public List<InteractionsEntity> getInteractionIdsByAuthor(@PathVariable(value = "authorid") String authorId) throws Exception {
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

    @GetMapping("{seedtype}/{seedid}")
    public List<InteractionsEntity> getInteractionIdsBySeedId(@PathVariable(value = "seedtype") String seedType,
                                                  @PathVariable(value = "seedid") String seedId) throws Exception {
        if (seedType == null || seedId == null)
            throw new Exception("Input seedType or seedId is null");
        return interactionsService.findInteractionsBySeedId(seedId);
    }

    @DeleteMapping("{seedtype}/{seedid}")
    public String deleteInteractionBySeedId(@PathVariable(value = "seedtype") String seedType,
                                             @PathVariable(value = "seedid") String seedId) throws Exception {
        try {
            if (seedType == null || seedId == null)
                throw new Exception("Input seedType or seedId is null");
            interactionsService.deleteInteractionBySeedId(seedId);
            return "Interactions of the seed are deleted";
        } catch (Exception ex) {
            throw new Exception("deleteInteractionBySeedId:: Failed to delete interaction due to " + ex.getMessage());
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

            BlogsInteractionDto blogInteraction =
                    new BlogsInteractionDto(
                            requestInteractionEntity.getSeedId(),
                            requestInteractionEntity.getInteractionId(),
                            requestInteractionEntity.getInteractionType(),
                            requestInteractionEntity.getInteractionValue());

            sendMessageToSQS(blogInteraction);

            return "Interaction added and blog updated successfully.";

        } catch (Exception ex) {
            throw ex;
        }
    }

    public String interactionFallback(Exception e){
        return "Interaction added, blog service is down";
    }


    private void sendMessageToSQS(BlogsInteractionDto blogInteraction) throws JMSException {
        // Create a new connection factory with all defaults (credentials and region) set automatically
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClientBuilder.defaultClient()
        );

        // Create the connection.
        SQSConnection connection = connectionFactory.createConnection();

        // Get the wrapped client
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

        // Create an SQS queue named MyQueue, if it doesn't already exist
        if (!client.queueExists("BlogsQueue")) {
            client.createQueue("BlogsQueue");
        }

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("BlogsQueue");

        // Create a producer for the 'MyQueue'
        MessageProducer producer = session.createProducer(queue);

        ObjectMessage objectMessage = session.createObjectMessage();
        objectMessage.setObject(blogInteraction);
        producer.send(objectMessage);

        session.close();
        connection.close();
    }
}
