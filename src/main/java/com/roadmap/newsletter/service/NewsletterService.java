package com.roadmap.newsletter.service;

import com.roadmap.newsletter.entity.Article;
import com.roadmap.newsletter.entity.Subscriber;
import com.roadmap.newsletter.repository.ArticleRepository;
import com.roadmap.newsletter.repository.SubscriberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class NewsletterService {

    private JavaMailSender javaMailSender;
    private SubscriberRepository subscriberRepository;
    private ArticleRepository articleRepository;

    public String sendNewsletter(String to, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom("ferasama@gmailc.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        javaMailSender.send(simpleMailMessage);

        return "email sent successfully";
    }

    @Scheduled(fixedRate = 10000)
    public void sendScheduledNewsletter() {
//        sendNewsletter("firasdev29@gmail.com", "scheduled newsletter test 1", "this is a test for scheduled newsletter");
        log.info("test at : {}", LocalTime.now());

        // find subscribers by day
        String currentDay = LocalDate.now().getDayOfWeek().name();
        List<Subscriber> subscriberDetails = subscriberRepository.findBySubscriptionDay(currentDay);

        // send email by day and topic and email
        if (Objects.nonNull(subscriberDetails)) {
            subscriberDetails.forEach(subscriber -> {
                // find articles by topics for each subscriber and group them by topic
                Map<String, List<Article>> topicGroup = articleRepository.findByTopicNameAndDate(subscriber.getTopics(), currentDay).stream()
                  .collect(Collectors.groupingBy(a -> a.getTopic().getName()));
                log.info("topic: {}", topicGroup.values());

                // main body
                StringBuilder body = new StringBuilder();
                body.append("Hi ").append(subscriber.getName()).append(",\n\n");
                body.append("Here are your articles for for this week:\n\n");

                // format the string body
               topicGroup.forEach((topic, articles) -> {
                   body.append(topic.toUpperCase()).append(": \n");
                   articles.forEach(article -> {
                       body.append(article.getTitle()).append(": \n");
                       body.append(article.getLink()).append("\n\n");
                   });
               });
               body.append("Happy reading!");
               sendNewsletter(subscriber.getEmail(), subscriber.getSubscription().getSubject(), body.toString());
            });
        }
    }
}
