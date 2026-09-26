package com.roadmap.newsletter.service;

import com.roadmap.newsletter.entity.Subscriber;
import com.roadmap.newsletter.model.subscriber.SubscriberRequest;
import com.roadmap.newsletter.model.subscriber.SubscriberResponse;
import com.roadmap.newsletter.model.subscriber.UserSignupEvent;
import com.roadmap.newsletter.model.topic.TopicData;
import com.roadmap.newsletter.repository.SubscriberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubscriberService {

    private SubscriberRepository subscriberRepository;
    private TopicService topicService;
    private ApplicationEventPublisher publisher;
    private JavaMailSender mailSender;

    @EventListener
    public void sendWelcomeEmail(UserSignupEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "UTF-8");
        String html = "<h1>Welcome to Roadmap Newsletter</h1>";
        mimeMessageHelper.setTo("firasdev29@gmail.com");
        message.setText(html);
        mailSender.send(message);
    }

    public SubscriberResponse subscribe(SubscriberRequest subscriberRequest) {
        // search for existing subscription
        Subscriber subscriber = subscriberRepository.findByEmail(subscriberRequest.getEmail()).orElseGet(Subscriber::new);
        // let user see topics
        List<TopicData> selectedTopics = new ArrayList<>();
        topicService.getTopics().getData()
          .forEach(topic -> {
            // user selects a topic, which is added to empty list
            if (Objects.nonNull(subscriberRequest.getTopics()) && subscriberRequest.getTopics().stream()
              .anyMatch(t -> StringUtils.equals(t, topic.getName()))) {
                selectedTopics.add(topic);
                log.info("topics: {}", topic.getName());
            }
        });
        //TODO subscriber selects day and time
        List<String> filteredTopics = !selectedTopics.isEmpty() ? selectedTopics.stream().map(TopicData::getName).collect(Collectors.toList()) : new ArrayList<>();
        if (Objects.isNull(subscriberRequest.getTopics()) || subscriberRequest.getTopics().isEmpty()) {
            throw new ServiceException("Please select at least one topic");
        }
        saveSubscriberDetails(subscriber, filteredTopics, subscriberRequest.getName(), subscriberRequest.getEmail(), subscriberRequest);

        SubscriberResponse response = new SubscriberResponse();
        response.setMessage("Subscription is ready.");
        response.setTopics(subscriber.getTopics());

        //TODO set welcome email event here
        return response;
    }

    private void saveSubscriberDetails(Subscriber subscriber, List<String> filteredTopics, String name, String email, SubscriberRequest request) {
        //TODO prevent duplicates
        if (Objects.nonNull(subscriber.getTopics())) {
            subscriber.getTopics().addAll(filteredTopics);
        } else {
            subscriber.setTopics(filteredTopics);
        }
        subscriber.setName(name);
        subscriber.setEmail(email);
        subscriber.setStatus("NEW");
        subscriber.setStatus("SUBSCRIBED");
        subscriberRepository.save(subscriber);
    }

    //TODO is this imlementation correct?
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
