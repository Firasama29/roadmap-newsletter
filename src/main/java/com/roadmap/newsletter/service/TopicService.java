package com.roadmap.newsletter.service;

import com.roadmap.newsletter.entity.Topic;
import com.roadmap.newsletter.model.TopicData;
import com.roadmap.newsletter.model.TopicRequest;
import com.roadmap.newsletter.model.TopicResponse;
import com.roadmap.newsletter.repository.TopicRepository;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TopicService {

    private TopicRepository topicRepository;

    // add topic
    public TopicResponse addTopic(TopicRequest topicRequest) {
        TopicResponse topicResponse = new TopicResponse();
        List<Topic> topics = topicRepository.findByNameContaining(topicRequest.getName());
        if (topics.isEmpty()) {
            Topic topic = new Topic();
            topic.setName(topicRequest.getName());
            topic.setCategory(topicRequest.getCategory());
            topicRepository.save(topic);
            topicResponse.setMessage("New topic saved successfully");
            return topicResponse;
        } else {
            throw new ServiceException("Topic already exists!");
        }
    }

    // list all topics
    public TopicResponse getTopics() {
        TopicResponse topicResponse = new TopicResponse();
        List<TopicData> topicDataList = new ArrayList<>();
        List<Topic> topics = topicRepository.findAll();
        if (topics.isEmpty()) {
            topicResponse.setMessage("No records found");
            return topicResponse;
        }
        topics.forEach(topic -> {
            TopicData topicData = new TopicData();
            topicData.setName(topic.getName());
            topicData.setCategory(topic.getCategory());
            topicDataList.add(topicData);
          });
        topicResponse.setData(topicDataList);

        return topicResponse;
    }

    // list filtered topics by name
    public TopicResponse filterTopicsByName(String name) {
        List<Topic> filteredTopics = topicRepository.findByNameContaining(name);
        return mapTopicResponse(filteredTopics);
    }

    public TopicResponse filterTopicsByCategory(String category) {
        List<Topic> filteredTopics = topicRepository.findByCategoryContaining(category);
        return mapTopicResponse(filteredTopics);
    }

    private TopicResponse mapTopicResponse(List<Topic> filteredTopics) {
        TopicResponse response = new TopicResponse();
        List<TopicData> topicDataList = new ArrayList<>();
        if (filteredTopics.isEmpty()) {
            response.setMessage("No records found");
            return response;
        } else {
            filteredTopics.forEach(topic -> {
                TopicData data = new TopicData();
                data.setName(topic.getName());
                data.setCategory(topic.getCategory());
                topicDataList.add(data);
            });
            response.setData(topicDataList);
        }
        return response;
    }

    // update topic details
    public TopicResponse modifyTopic(Long id, TopicRequest request) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new ServiceException("No Topic found"));
        topic.setName(Objects.nonNull(request.getName()) ? request.getName() : topic.getName());
        topic.setCategory(Objects.nonNull(request.getCategory()) ? request.getCategory() : topic.getCategory());
        topicRepository.save(topic);

        TopicResponse response = new TopicResponse();
        response.setMessage("Topic updated successfully.");
        List<TopicData> dataList = new ArrayList<>();
        TopicData topicData = new TopicData();
        topicData.setName(topic.getName());
        topicData.setCategory(topic.getCategory());

        dataList.add(topicData);
        response.setData(dataList);
        return response;
    }

    public TopicResponse deleteTopic(Long id) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new ServiceException("No Topic found"));
        topicRepository.delete(topic);
        TopicResponse response = new TopicResponse();

        response.setMessage("Topic deleted successfully.");
        return  response;
    }
}
