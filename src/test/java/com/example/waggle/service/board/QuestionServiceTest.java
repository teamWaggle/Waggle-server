package com.example.waggle.service.board;

import com.example.waggle.dto.board.QuestionDto;
import com.example.waggle.dto.board.QuestionSimpleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionServiceTest {

    @Autowired
    QuestionService questionService;

    //read test
    @Test
    public void findQuestions() {
        List<QuestionSimpleDto> allQuestion =
                questionService.findAllQuestion();
        for (QuestionSimpleDto questionSimpleDto : allQuestion) {
            System.out.println("questionSimpleDto.getTitle() = " + questionSimpleDto.getTitle());
        }
    }

    @Test
    public void findQuestionsByUsername() {
        List<QuestionSimpleDto> hann111 = questionService.findAllQuestionByMember("hann111");
        for (QuestionSimpleDto questionSimpleDto : hann111) {
            System.out.println("questionSimpleDto.getTitle() = " + questionSimpleDto.getTitle());
        }
    }

    @Test
    public void findQuestionById() {
        QuestionDto questionByBoardId = questionService.findQuestionByBoardId(3L);
        assertThat(questionByBoardId.getAnswers().size()).isEqualTo(1);
    }

}