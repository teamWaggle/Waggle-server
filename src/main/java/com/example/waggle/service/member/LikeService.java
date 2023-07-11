package com.example.waggle.service.member;

import com.example.waggle.dto.board.QuestionDto;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.member.LikeRepository;
import com.example.waggle.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {

    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final StoryRepository storyRepository;
    private final LikeRepository likeRepository;

    /**
     * add like
     */
    public void addLike(MemberDto memberDto, Long boardId) {

    }


    /**
     * cacnel like
     */
    public void cancelLike(MemberDto memberDto, Long boardId) {

    }

}
