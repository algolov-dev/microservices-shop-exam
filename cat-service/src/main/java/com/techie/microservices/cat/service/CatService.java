package com.techie.microservices.cat.service;

import com.techie.microservices.cat.dto.CatRequest;
import com.techie.microservices.cat.event.CatPlacedEvent;
import com.techie.microservices.cat.model.Cat;
import com.techie.microservices.cat.model.User;
import com.techie.microservices.cat.repository.CatRepository;
import com.techie.microservices.cat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatService {
    private final CatRepository catRepository;

    private final UserRepository userRepository;
    private final KafkaTemplate<String, CatPlacedEvent> kafkaTemplate;
    public void placeCat(CatRequest catRequest) {

        User user = userRepository.findByEmail(catRequest.userEmail());


        log.info("Start - Processing cat request: {}", catRequest);
        Cat cat = new Cat();
        cat.setCatNumber(UUID.randomUUID().toString());
        cat.setPrice(catRequest.price());
        cat.setTitle(catRequest.title());
        cat.setEmail(catRequest.ownerEmail());
        cat.setUserEmail(catRequest.userEmail());
        catRepository.save(cat);

        log.info("Cat saved to database: {}", cat);

        // Send the message to Kafka Topic
        CatPlacedEvent catPlacedEvent = new CatPlacedEvent();
        catPlacedEvent.setCatNumber(cat.getCatNumber());
        catPlacedEvent.setTitle(cat.getTitle());
        catPlacedEvent.setPrice(cat.getPrice().toString());
        catPlacedEvent.setOwnerEmail(cat.getEmail());
        catPlacedEvent.setUserEmail(cat.getUserEmail());
        log.info("Start - Sending CatPlacedEvent {} to Kafka topic cat-placed", catPlacedEvent);
        kafkaTemplate.send("cat-placed", catPlacedEvent);
        log.info("End - Sending CatPlacedEvent {} to Kafka topic cat-placed", catPlacedEvent);
    }
}
