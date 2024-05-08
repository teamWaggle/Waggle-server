package com.example.waggle.domain.hashtag.application;

import com.example.waggle.domain.hashtag.persistence.dao.HashtagRepository;
import com.example.waggle.domain.hashtag.persistence.entity.Hashtag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class HashtagQueryServiceImpl implements HashtagQueryService {

    private final HashtagRepository hashtagRepository;

    @Override
    public List<Hashtag> getAllHashtags() {
        List<Hashtag> all = hashtagRepository.findAll();
//        all.stream().forEach(h -> h.getBoardHashtags().stream().forEach(bh -> log.info("boardHashtag = {}",bh)));
        return all;
    }
}
