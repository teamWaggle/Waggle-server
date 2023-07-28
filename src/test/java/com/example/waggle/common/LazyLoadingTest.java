package com.example.waggle.common;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Slf4j
public class LazyLoadingTest {

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    public void proxyTest() {
        // data initialize
        Board board = Story.builder()
                .content("content")
                .build();

        Hashtag hashtag = Hashtag.builder()
                .tag("hashtag").build();

        BoardHashtag boardHashtag = BoardHashtag.builder()
                .board(board)
                .hashtag(hashtag).build();


        em.persist(board);
        em.persist(hashtag);
        em.persist(boardHashtag);

        em.flush();
        em.clear();


        // Board 엔티티 조회
        Board findBoard = em.find(Board.class, 1L);

        // boardHashtags 필드 접근
        List<BoardHashtag> boardHashtags = findBoard.getBoardHashtags();
        log.info("boardHashtags = {}", boardHashtags.getClass().getName()); // boardHashtags = org.hibernate.collection.spi.PersistentBag

        // 실제 데이터 로딩
        log.info("boardHashtag = {}", boardHashtags.get(0).getClass().getName());   // boardHashtag = com.example.waggle.domain.board.hashtag.BoardHashtag

        log.info("hashtag = {}", hashtag.getTag());

    }

}
