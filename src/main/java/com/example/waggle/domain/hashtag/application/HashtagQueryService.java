package com.example.waggle.domain.hashtag.application;

import com.example.waggle.domain.hashtag.persistence.entity.Hashtag;

import java.util.List;

public interface HashtagQueryService {
    List<Hashtag> getAllHashtags();
}
