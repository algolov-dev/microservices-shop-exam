package com.techie.microservices.cat.service;

import com.techie.microservices.cat.model.Tag;
import com.techie.microservices.cat.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> listAll() {
        return tagRepository.findAll();
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

    public Tag findByName(String name) {
        return tagRepository.findByName(name).orElse(null);
    }

    public List<Tag> getTopTags() {
        List<Tag> tags = tagRepository.findTopTags();
        return tags.size() > 10 ? tags.subList(0, 10) : tags;
    }

}