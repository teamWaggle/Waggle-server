package com.example.waggle.service.board;

import com.example.waggle.repository.board.HashtagRepository;
import com.example.waggle.repository.board.MediaRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.board.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class QuestionService {

    private final StoryRepository storyRepository;
    private final HashtagRepository hashtagRepository;
    private final MediaRepository mediaRepository;
    private final CommentRepository commentRepository;

    /**
     * 조회는 entity -> dto과정을,
     * 저장은 dto -> entity 과정을 거치도록 한다.(기본)
     */

    //===========1. 조회===========

    //1.1 그룹 조회

    //1.2 낱개 조회

    //2. ===========저장===========

    //2.1 question 저장(media, hashtag 포함)

    //2.2 question_comment 저장

    //2.3 question_comment_reply 저장

    //2.4 answer 저장(media, hashtag 포함)

    //2.5 answer_comment 저장

    //2.6 answer_comment_reply 저장

    //3. ===========수정===========

    //3.1 question 수정(media, hashtag 포함)

    //3.2 question_comment 수정

    //3.3 question_comment_reply 수정

    //3.4 answer 수정(media, hashtag 포함)

    //3.5 answer_comment 수정

    //3.6 answer_comment_reply 수정

    //4. ===========삭제(취소)===========

    //4.1 question 수정(media, hashtag 포함)

    //4.2 question_comment 수정

    //4.3 question_comment_reply 수정

    //4.4 answer 수정(media, hashtag 포함)

    //4.5 answer_comment 수정

    //4.6 answer_comment_reply 수정
}
