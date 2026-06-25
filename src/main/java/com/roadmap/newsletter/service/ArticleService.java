package com.roadmap.newsletter.service;

import com.roadmap.newsletter.entity.Article;
import com.roadmap.newsletter.entity.Topic;
import com.roadmap.newsletter.model.ArticleData;
import com.roadmap.newsletter.model.ArticleRequest;
import com.roadmap.newsletter.model.ArticleResponse;
import com.roadmap.newsletter.repository.ArticleRepository;
import com.roadmap.newsletter.repository.TopicRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleService {

    private TopicRepository topicRepository;
    private ArticleRepository articleRepository;

    // add article
    public ArticleResponse addArticle(ArticleRequest request) {
        Optional<Topic> topicEntity = topicRepository.findByNameContaining(request.getTopic());
        Optional<Article> article = articleRepository.findByTitleContaining(request.getTitle());
        if (article.isPresent()) {
            throw new ServiceException("Article already exist");
        }
        Topic newTopic = new Topic();
        Article newArticle = new Article();
        if (topicEntity.isPresent()) {
            newArticle.setTopic(topicEntity.get());
        } else {
            newTopic.setName(request.getTopic());
            newTopic.setCategory(request.getCategory());
            newArticle.setTopic(newTopic);
            // saving the transient instance `Topic` before saving the article
            topicRepository.save(newTopic);
        }
        newArticle.setTitle(request.getTitle());
        newArticle.setDescription(request.getDescription());
        newArticle.setLink(request.getLink());
        // now saving the article
        articleRepository.save(newArticle);

        ArticleResponse response = new ArticleResponse();
        response.setMessage("Article added successfully.");
        return response;
    }

    // get all articles
    public ArticleResponse getArticles() {
        List<Article> articles = articleRepository.findAll();
        ArticleResponse response = new ArticleResponse();
        if (articles.isEmpty()) {
            response.setMessage("No records found.");
            return response;
        }
        List<ArticleData> articleDataList = new ArrayList<>();
        response.setMessage("Records retrieved successfully.");
        articles.forEach(article -> {
            ArticleData articleData = new ArticleData();
            articleData.setTitle(article.getTitle());
            articleData.setTopic(article.getTopic().getName());
            articleData.setCategory(article.getTopic().getCategory());
            articleDataList.add(articleData);
        });
        response.setData(articleDataList);
        return response;
    }

    // get articles by category
    public ArticleResponse filterTopicsByCategory(String category) {
        List<Article> articles = articleRepository.findByTopicCategory(category);
        ArticleResponse response = new ArticleResponse();
        List<ArticleData> dataList = new ArrayList<>();
        if (articles.isEmpty()) {
            response.setMessage("No records found for category");
            return response;
        }
        articles.forEach(article -> {
            ArticleData data = new ArticleData();
            data.setTitle(article.getTitle());
            data.setTopic(article.getTopic().getName());
            data.setCategory(article.getTopic().getCategory());
            data.setLink(article.getLink());
            dataList.add(data);
        });
        response.setMessage("Records retrieved successfully");
        response.setData(dataList);
        return response;
    }

}
