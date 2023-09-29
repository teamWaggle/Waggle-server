package com.example.waggle.repository.board;

import com.example.waggle.board.story.domain.Story;
import com.example.waggle.hashtag.domain.BoardHashtag;
import com.example.waggle.hashtag.domain.Hashtag;
import com.example.waggle.hashtag.repository.HashtagRepository;
import com.example.waggle.board.story.repository.StoryRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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
    @Rollback
    @Disabled
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
    @Rollback
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

//        List<Story> findStories = storyRepository.findByHashtag("vacation");
//        assertThat(findStories.size()).isEqualTo(1);
    }

}