package com.example.waggle.domain.board.answer.repository;

import com.example.waggle.domain.board.answer.entity.Answer;
import com.example.waggle.domain.board.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Page<Answer> findByQuestionId(Long questionId, Pageable pageable);

    List<Answer> findListByMemberUsername(String username);

    Page<Answer> findAll(Pageable pageable);

    List<Answer> findAnswerByQuestionId(Long questionId);

    Page<Answer> findPagedAnswerByMemberUsername(String username, Pageable pageable);

    Page<Answer> findPagedAnswerByMemberUserUrl(String userUrl, Pageable pageable);

    Page<Answer> findPagedAnswerByMemberId(Long memberId, Pageable pageable);

    void deleteAllByMemberUsername(String username);

    void deleteAllByQuestion(Question question);

}