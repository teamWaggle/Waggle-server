package com.example.waggle.repository.board;

import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;
import com.example.waggle.domain.board.Story;
import com.example.waggle.repository.board.boardtype.StoryRepository;
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
    @Rollback(value = false)
    void test() {

        Story testStory = Story.builder().thumbnail("@hann").recommend(1).content("Board test Repository").build();

        Hashtag hashtag1 = Hashtag.builder().tag("trip").build();
        Hashtag hashtag2 = Hashtag.builder().tag("vacation").build();

        List<Hashtag> hashtags = new ArrayList<>();
        hashtags.add(hashtag1);
        hashtags.add(hashtag2);

        for (Hashtag hashtag : hashtags) {
            BoardHashtag build = BoardHashtag.builder().board(testStory).hashtag(hashtag).build();
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
    @Rollback(value = false)
    void testFindByHashtag() {

        Story testStory = Story.builder().thumbnail("@hann").recommend(1).content("Board test Repository").build();
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

        List<Story> findStories = storyRepository.findByHashtag("vacation");
        assertThat(findStories.size()).isEqualTo(1);
    }

}