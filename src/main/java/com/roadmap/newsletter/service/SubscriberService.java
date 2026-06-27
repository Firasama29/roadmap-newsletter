package com.roadmap.newsletter.service;

import com.roadmap.newsletter.entity.Subscriber;
import com.roadmap.newsletter.entity.Topic;
import com.roadmap.newsletter.model.SubscriptionObj;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubscriberService {

    private SubscriberRepository subscriberRepository;
    private TopicService topicService;
    private ApplicationEventPublisher publisher;
    private JavaMailSender mailSender;

    public SubscriberResponse signup(SubscriberRequest subscriberRequest) {
        // will add simple logic
        Optional<Subscriber> subscriber = subscriberRepository.findByEmail(subscriberRequest.getEmail());
        if (subscriber.isPresent()) {
            throw new ServiceException("User already exists");
        }
        saveSubscriberDetails(subscriberRequest);
        SubscriberResponse response = new SubscriberResponse();
        response.setMessage("Signup successful");
        publisher.publishEvent(new UserSignupEvent(subscriberRequest.getEmail()));
        return response;
    }

    @EventListener
    public void sendWelcomeEmail(UserSignupEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "UTF-8");
        String html = "<h1>Welcome to Roadmap Newsletter</h1>";
        mimeMessageHelper.setTo("firasdev29@gmail.com");
        message.setText(html);
        mailSender.send(message);
    }

    public SubscriberResponse addNewTopic(SubscriberRequest subscriberRequest) {
        Subscriber subscriber = subscriberRepository.findByEmail(subscriberRequest.getEmail()).orElseThrow(() -> new ServiceException("User does not exist"));

        // subscriber can see topics
        List<TopicData> topics = topicService.getTopics().getData();
        List<TopicData> selectedTopics = new ArrayList<>();
        topics.forEach(topic -> {
            // select a topic and add to empty list
            if (subscriberRequest.getSubscriptions().stream()
              .anyMatch(sub -> StringUtils.equals(sub.getTopic(), topic.getName()))) {
                selectedTopics.add(topic);
                log.info("subscriber selected: {}", topic.getName());
            }
          });
        //TODO subscriber selects day and time
        // get list of topic names
        List<String> filteredTopics = !selectedTopics.isEmpty() ? selectedTopics.stream().map(TopicData::getName).toList() : new ArrayList<>();
        //store subscriber details in subscriptions
        if(Objects.isNull(subscriber.getTopics()) || subscriber.getTopics().isEmpty()) {
            subscriber.setTopics(mapNewTopics(subscriberRequest.getSubscriptions()));
        } else {
            subscriber.getTopics().addAll(filteredTopics);
        }
        subscriber.setStatus("SUBSCRIBED");
        subscriberRepository.save(subscriber);

        SubscriberResponse response = new SubscriberResponse();
        response.setMessage("Subscription is ready.");
        response.setTopics(filteredTopics);

        //TODO set 'subscribed' email event here
        return response;
    }

    private List<String> mapNewTopics(List<SubscriptionObj> subs) {
        return subs.stream()
          .map(SubscriptionObj::getTopic)
          .toList();
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
