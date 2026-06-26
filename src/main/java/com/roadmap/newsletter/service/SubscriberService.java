package com.roadmap.newsletter.service;

import com.roadmap.newsletter.entity.Subscriber;
import com.roadmap.newsletter.entity.Topic;
import com.roadmap.newsletter.model.subscriber.SubscriberRequest;
import com.roadmap.newsletter.model.subscriber.SubscriberResponse;
import com.roadmap.newsletter.model.topic.TopicData;
import com.roadmap.newsletter.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubscriberService {

    private SubscriberRepository subscriberRepository;
    private TopicService topicService;

    public SubscriberResponse signup(SubscriberRequest subscriberRequest) {
        // will add simple logic
        Optional<Subscriber> subscriber = subscriberRepository.findByEmail(subscriberRequest.getEmail());
        if (subscriber.isPresent()) {
            throw new ServiceException("User already exists");
        }
        saveSubscriberDetails(subscriberRequest);
        SubscriberResponse response = new SubscriberResponse();
        response.setMessage("Signup successful");
        return response;
    }

    public SubscriberResponse addNewTopic(SubscriberRequest subscriberRequest) {
        Subscriber subscriber = subscriberRepository.findByEmail(subscriberRequest.getEmail()).orElseThrow(() -> new ServiceException("User does not exist"));

        // subscriber can see topics
        List<TopicData> topics = topicService.getTopics().getData();
        List<TopicData> selectedTopics = new ArrayList<>();
        topics.forEach(topic -> {
            // select a topic and add to empty list
            if (subscriberRequest.getTopics().contains(topic.getName())) {
                selectedTopics.add(topic);
                log.info("subscriber selected: {}", topic.getName());
            }
          });
        //TODO subscriber selects day and time
        // get list of topic names
        List<String> filteredTopics = !selectedTopics.isEmpty() ? selectedTopics.stream().map(TopicData::getName).toList() : new ArrayList<>();
        //store subscriber details in subscriptions
        if(Objects.isNull(subscriber.getTopics()) || subscriber.getTopics().isEmpty()) {
            subscriber.setTopics(subscriberRequest.getTopics());
        } else {
            subscriber.getTopics().addAll(filteredTopics);
        }
        subscriber.setStatus("SUBSCRIBED");
        subscriberRepository.save(subscriber);

        SubscriberResponse response = new SubscriberResponse();
        response.setMessage("Subscription is ready.");
        response.setTopics(filteredTopics);
        return response;
    }

    private void saveSubscriberDetails(SubscriberRequest subscriberRequest) {
        Subscriber subscriber = new Subscriber();
        subscriber.setName(subscriberRequest.getName());
        subscriber.setEmail(subscriberRequest.getEmail());
        subscriber.setStatus("NEW");
        subscriberRepository.save(subscriber);
    }

    public SubscriberResponse deleteSubscriptionByTopic(String email, String topicString) {
        String[] topicArray = topicString.split(",");
        List<String> topics = List.of(topicArray);

        Subscriber subscriber = subscriberRepository.findByEmail(email).orElseThrow(() -> new ServiceException("No subscriber with given email"));
        List<String> existingTopics = subscriber.getTopics();
        List<String> updatedTopics = new ArrayList<>();
        existingTopics.forEach(topic -> {
              if (!topics.contains(topic)) {
                  updatedTopics.add(topic);
              }
        });
        subscriber.setTopics(updatedTopics);
        subscriberRepository.save(subscriber);

        SubscriberResponse response = new SubscriberResponse();
        response.setMessage("Topic(s) removed successfully.");
        return response;
    }

}
