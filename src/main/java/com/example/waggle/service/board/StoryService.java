package com.example.waggle.service.board;

import com.example.waggle.dto.board.StoryDto;
import com.example.waggle.repository.board.HashtagRepository;
import com.example.waggle.repository.board.MediaRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.board.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class StoryService {

    private final StoryRepository storyRepository;
    private final HashtagRepository hashtagRepository;
    private final MediaRepository mediaRepository;
    private final CommentRepository commentRepository;

    /**
     * 조회는 entity -> dto과정을,
     * 저장은 dto -> entity 과정을 거치도록 한다.(기본)
     */

    //1. ===========조회===========

    //1.1 그룹 조회
//    public List<StoryDto> findAllStory() {
//
//    }

    //1.2 낱개 조회

    //2. ===========저장===========

    //2.1 story 저장(media, hashtag 포함)

    //2.2 story_comment 저장

    //2.3 story_comment_reply 저장

    //3. ===========수정===========

    //3.1 story 수정(media, hashtag 포함)

    //3.2 story_comment 수정

    //3.3 story_comment_reply 수정

    //4. ===========삭제(취소)===========

    //4.1 story 저장(media, hashtag 포함)

    //4.2 story_comment 저장

    //4.3 story_comment_reply 저장
}
