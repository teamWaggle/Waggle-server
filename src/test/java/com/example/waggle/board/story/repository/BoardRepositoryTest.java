package com.example.waggle.board.story.repository;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.hashtag.entity.BoardHashtag;
import com.example.waggle.domain.hashtag.entity.Hashtag;
import com.example.waggle.domain.hashtag.repository.HashtagRepository;
import com.example.waggle.domain.board.story.repository.StoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    HashtagRepository hashtagRepository;
    @Autowired
    StoryRepository storyRepository;


    @Test
    @Transactional
    void test() {

        Story testStory = Story.builder().thumbnail("@hann").content("Board test Repository").build();

        Hashtag hashtag1 = Hashtag.builder().tag("trip").build();
        Hashtag hashtag2 = Hashtag.builder().tag("vacation").build();

        List<Hashtag> hashtags = new ArrayList<>();
        hashtags.add(hashtag1);
        hashtags.add(hashtag2);

        for (Hashtag hashtag : hashtags) {
            BoardHashtag.builder().board(testStory).hashtag(hashtag).build().link(testStory,hashtag);
            //build.addHashtag(testStory,hashtag);
        }

        storyRepository.save(testStory);
        for (Hashtag hashtag : hashtags) {
            hashtagRepository.save(hashtag);
        }


        List<Story> all = storyRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getBoardHashtags().size()).isEqualTo(2);
    }
    @Test
    @Transactional
    void testFindByHashtag() {

        Story testStory = Story.builder().thumbnail("@hann").content("Board test Repository").build();
        storyRepository.save(testStory);


        Hashtag hashtag1 = Hashtag.builder().tag("trip").build();
        Hashtag hashtag2 = Hashtag.builder().tag("vacation").build();


        List<Hashtag> hashtags = new ArrayList<>();
        hashtags.add(hashtag1);
        hashtags.add(hashtag2);
        for (Hashtag hashtag : hashtags) {
            hashtagRepository.save(hashtag);
        }

        for (Hashtag hashtag : hashtags) {
            BoardHashtag build = BoardHashtag.builder().board(testStory).hashtag(hashtag).build();
        }


    }

}