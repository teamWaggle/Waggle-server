package com.example.waggle.repository.board.boardtype;

import com.example.waggle.domain.board.qna.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    //BoardId를 통해 question 하나를 조회
    @Query(value = "select * from question join board on board.board_id = :boardId", nativeQuery = true)
    Question findByBoardId(@Param("boardId")Long id);

    //hashtag를 통해 board를 모두 가져온다.(question에 한해서)
    @Query(value = "SELECT * FROM question AS q" +
            " JOIN board AS b ON q.board_id = b.board_id" +
            " JOIN board_hashtag AS bh ON b.board_id = bh.board_id" +
            " JOIN hashtag AS h ON bh.hashtag_id = h.hashtag_id" +
            " WHERE h.h_content = :content", nativeQuery = true)
    List<Question> findByHashtag(@Param("content") String content);
}