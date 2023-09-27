package com.example.waggle.service.board;


import com.example.waggle.domain.board.Media;
import com.example.waggle.domain.board.boardType.Answer;
import com.example.waggle.domain.board.boardType.Question;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.answer.AnswerViewDto;
import com.example.waggle.dto.board.answer.AnswerWriteDto;
import com.example.waggle.dto.board.question.QuestionSimpleViewDto;
import com.example.waggle.dto.board.question.QuestionViewDto;
import com.example.waggle.dto.board.question.QuestionWriteDto;
import com.example.waggle.exception.CustomPageException;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.service.board.util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.waggle.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.example.waggle.exception.ErrorCode.CANNOT_TOUCH_NOT_YOURS;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UtilService utilService;

    /**
     * 조회는 entity -> dto과정을,
     * 저장은 dto -> entity 과정을 거치도록 한다.(기본)
     */

    //===========1. 조회===========

    //1.1.1 전체 조회 -> 후에 시간 순으로 조회
    //P1. 지금은 story -> storySimpleDto로 변경하지만 조회를 dto로 변경하면 query양이 적어질 것이다.
    //P2. paging 필수
    @Transactional(readOnly = true)
    public List<QuestionSimpleViewDto> findAllQuestion() {
        //board setting
        List<Question> allQuestion = questionRepository.findAll();
        List<QuestionSimpleViewDto> simpleQuestions = new ArrayList<>();

        for (Question question : allQuestion) {
            simpleQuestions.add(QuestionSimpleViewDto.toDto(question));
        }
        return simpleQuestions;
    }

    //1.1.2 회원 정보에 따른 전체 조회
    @Transactional(readOnly = true)
    public List<QuestionSimpleViewDto> findAllQuestionByUsername(String username) {
        List<Question> questionsByUsername = questionRepository.findByMemberUsername(username);
        List<QuestionSimpleViewDto> simpleQuestions = new ArrayList<>();

        for (Question question : questionsByUsername) {
            simpleQuestions.add(QuestionSimpleViewDto.toDto(question));
        }

        return simpleQuestions;
    }

    //1.2 낱개 조회
    @Transactional(readOnly = true)
    public QuestionViewDto findQuestionByBoardId(Long boardId) {

        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        //====== answer find & link ======
        List<Answer> byQuestionId = answerRepository.findByQuestionId(boardId);
        List<AnswerViewDto> answerViewDtoList = new ArrayList<>();

        for (Answer answer : byQuestionId) {
            answerViewDtoList.add(AnswerViewDto.toDto(answer));
        }

        QuestionViewDto viewDto = QuestionViewDto.toDto(question);
        viewDto.linkAnswerView(answerViewDtoList);

        return viewDto;
    }


    //2. ===========저장===========
    //2.1 question 저장(media, hashtag 포함)
    public Long saveQuestion(QuestionWriteDto saveQuestionDto) {
        Member signInMember = utilService.getSignInMember();
        Question question = saveQuestionDto.toEntity(signInMember);
        questionRepository.save(question);

        //hashtag 저장
        if(!saveQuestionDto.getHashtags().isEmpty()){
            for (String hashtag : saveQuestionDto.getHashtags()) {
                utilService.saveHashtag(question,hashtag);
            }
        }
        //media 저장
        if (!saveQuestionDto.getMedias().isEmpty()) {
            for (String media : saveQuestionDto.getMedias()) {
                Media.builder().url(media).board(question).build().linkBoard(question);
            }
        }
        return question.getId();
    }

    //2.4 answer 저장(media, hashtag 포함)
    public void saveAnswer(AnswerWriteDto writeDto, Long boardId) {
        Member signInMember = utilService.getSignInMember();
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        Answer answer = writeDto.toEntity(signInMember);
        //hashtag 저장
        if(!writeDto.getHashtags().isEmpty()){
            for (String hashtag : writeDto.getHashtags()) {
                utilService.saveHashtag(answer,hashtag);
            }
        }
        //media 저장
        if (!writeDto.getMedias().isEmpty()) {
            for (String media : writeDto.getMedias()) {
                Media.builder().url(media).board(answer).build().linkBoard(answer);
            }
        }
        //link relation method
        question.addAnswer(answer);
    }

    //3. ===========수정===========
    //3.1 question 수정(media, hashtag 포함)
    public String changeQuestion(QuestionWriteDto questionDto, Long boardId) {
        //find
        Question question = questionRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        //edit
        question.changeQuestion(questionDto.getContent(),questionDto.getTitle());

        //delete(media)
        question.getMedias().clear();

        //newly insert data(media)
        for (String media : questionDto.getMedias()) {
            Media.builder().url(media).board(question).build().linkBoard(question);
        }

        //delete connecting relate (boardHashtag)
        question.getBoardHashtags().clear();

        //newly insert data(hashtag, boardHashtag)
        for (String hashtag : questionDto.getHashtags()) {
            utilService.saveHashtag(question, hashtag);
        }
        return question.getMember().getUsername();
    }
    //3.2 answer 수정(media, hashtag 포함)
    public void changeAnswer(AnswerWriteDto answerDto, Long boardId) {
        //find
        Answer answer = answerRepository.findById(boardId)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));

        //edit
        answer.changeAnswer(answerDto.getContent());

        //delete(media)
        answer.getMedias().clear();

        //newly insert data(media)
        for (String media : answerDto.getMedias()) {
            Media.builder().url(media).board(answer).build();
        }

        //delete connecting relate (boardHashtag)
        answer.getBoardHashtags().clear();

        //newly insert data(hashtag, boardHashtag)
        for (String hashtag : answerDto.getHashtags()) {
            utilService.saveHashtag(answer, hashtag);
        }
    }

    //3.3 수정을 위한 user 확인 절차
    @Transactional(readOnly = true)
    public boolean checkMember(Long boardId, String boardType) {
        Member member = utilService.getSignInMember();
        Boolean isSameUser;
        switch (boardType) {
            case "question" :
                Optional<Question> questionById = questionRepository.findById(boardId);
                if (questionById.isEmpty()) {
                    log.info("not exist question");
                    //error
                    return false;
                }
                isSameUser = questionById.get().getMember().equals(member);
                break;
            case "answer" :
                Optional<Answer> answerById = answerRepository.findById(boardId);
                if (answerById.isEmpty()) {
                    log.info("not exist answer");
                    //error
                    return false;
                }
                isSameUser = answerById.get().getMember().equals(member);
                break;
            default :
                return false;
        }
        return isSameUser;
    }


    //4. ===========삭제(취소)===========

    //4.1 question 삭제(media, hashtag 포함)
    public void removeQuestion(Long id) {
        Member signInMember = utilService.getSignInMember();
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
        //check user
        if (!question.getMember().equals(signInMember)) {
            log.info("only same user can delete board!");
            throw new CustomPageException(CANNOT_TOUCH_NOT_YOURS);
        }
        questionRepository.delete(question);
        log.info("remove completely");
    }

    //4.4 answer 삭제(media, hashtag 포함)
    public void removeAnswer(Long id) {
        Member signInMember = utilService.getSignInMember();
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new CustomPageException(BOARD_NOT_FOUND));
        if (!answer.getMember().equals(signInMember)) {
            log.info("only same user can delete board!");
            throw new CustomPageException(CANNOT_TOUCH_NOT_YOURS);
        }
        answerRepository.delete(answer);
        log.info("remove completely");
    }

}
