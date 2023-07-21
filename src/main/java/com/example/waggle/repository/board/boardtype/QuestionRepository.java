package com.example.waggle.repository.board.boardtype;

import com.example.waggle.domain.board.boardType.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT q.title, b.board_id, b.content, b.created_date ,m.member_id, m.username FROM question AS q" +
            " JOIN board AS b on b.board_id = q.board_id" +
            " JOIN member AS m on m.member_id = b.member_id" +
            " WHERE m.username = :username", nativeQuery = true)
    List<Question> findByUsername(@Param("username") String username);

    //hashtag를 통해 board를 모두 가져온다.(question에 한해서)
    @Query(value = "SELECT * FROM question AS q" +
            " JOIN board AS b ON q.board_id = b.board_id" +
            " JOIN board_hashtag AS bh ON b.board_id = bh.board_id" +
            " JOIN hashtag AS h ON bh.hashtag_id = h.hashtag_id" +
            " WHERE h.tag = :tag", nativeQuery = true)
    List<Question> findByHashtag(@Param("tag") String content);

}