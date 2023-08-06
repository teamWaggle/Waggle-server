package com.example.waggle.service.board;

import com.example.waggle.component.jwt.SecurityUtil;
import com.example.waggle.domain.board.Recommend;
import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.board.boardType.Answer;
import com.example.waggle.domain.board.boardType.Question;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.answer.AnswerViewDto;
import com.example.waggle.dto.board.question.QuestionSimpleViewDto;
import com.example.waggle.dto.board.question.QuestionViewDto;
import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.dto.board.story.StoryViewDto;
import com.example.waggle.repository.board.RecommendRepository;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecommendService {

    private final MemberRepository memberRepository;
    private final StoryRepository storyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final RecommendRepository recommendRepository;


    public void clickRecommend(Long boardId, String boardType) {

        Board board = getBoard(boardId, boardType);
        if (board == null) return;
        Member member = getMember(SecurityUtil.getCurrentUsername());

        //check recommend board
        boolean check = recommendRepository
                .existsByMemberIdAndBoardId(member.getId(), board.getId());

        //change state
        if (check) {
            Optional<Recommend> recommendBoard = recommendRepository
                    .findRecommendByMemberIdAndBoardId(member.getId(), boardId);
            if (recommendBoard.isEmpty()) {
                //error
            }
            recommendRepository.delete(recommendBoard.get());
            return;
        }
        else{
            //recommend story as member
            Recommend recommendBoard = Recommend.builder().board(board).member(member).build();
            recommendRepository.save(recommendBoard);
        }
    }

    @Transactional(readOnly = true)
    public void checkRecommend(QuestionViewDto questionViewDto) {
        Member signInMember = getSignInMember();
        boolean recommendIt = false;
        if (login()) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), questionViewDto.getId());
        }
        int count = recommendRepository.countByBoardId(questionViewDto.getId());
        questionViewDto.linkRecommend(count, recommendIt);
    }

    @Transactional(readOnly = true)
    public void checkRecommend(AnswerViewDto answerViewDto) {
        Member signInMember = getSignInMember();
        boolean recommendIt = false;
        if (login()) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), answerViewDto.getId());
        }
        int count = recommendRepository.countByBoardId(answerViewDto.getId());
        answerViewDto.linkRecommend(count, recommendIt);
    }

    @Transactional(readOnly = true)
    public void checkRecommend(StoryViewDto storyViewDto) {
        Member signInMember = getSignInMember();
        boolean recommendIt = false;
        if (login()) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), storyViewDto.getId());
        }
        int count = recommendRepository.countByBoardId(storyViewDto.getId());
        storyViewDto.linkRecommend(count, recommendIt);
    }


    //========== 단체 조회 시 =========
    // 단체 recommend 확인은 쿼리량을 줄이기 위함이다.
    @Transactional(readOnly = true)
    public void checkRecommendQuestions(List<QuestionSimpleViewDto> questionViewDtoList) {
        for (QuestionSimpleViewDto questionViewDto : questionViewDtoList) {
            Member signInMember = getSignInMember();
            boolean recommendIt = false;
            if (login()) {
                recommendIt = recommendRepository
                        .existsByMemberIdAndBoardId(signInMember.getId(), questionViewDto.getId());
            }
            int count = recommendRepository.countByBoardId(questionViewDto.getId());
            questionViewDto.linkRecommend(count, recommendIt);
        }
    }
    @Transactional(readOnly = true)
    public void checkRecommendAnswers(List<AnswerViewDto> answerViewDtoList) {
        for (AnswerViewDto answerViewDto : answerViewDtoList) {
            checkRecommend(answerViewDto);
        }
    }
    @Transactional(readOnly = true)
    public void checkRecommendStories(List<StorySimpleViewDto> storyViewDtoList) {
        for (StorySimpleViewDto storyViewDto : storyViewDtoList) {
            Member signInMember = getSignInMember();
            boolean recommendIt = false;
            if (login()) {
                recommendIt = recommendRepository
                        .existsByMemberIdAndBoardId(signInMember.getId(), storyViewDto.getId());
            }
            int count = recommendRepository.countByBoardId(storyViewDto.getId());
            storyViewDto.linkRecommend(count, recommendIt);
        }

    }

    //====== else =====

    private Member getMember(String username) {
        //member get
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            //error
            return null;
        }
        Member member = byUsername.get();
        return member;
    }

    private boolean login() {
        if (SecurityUtil.getCurrentUsername().equals("anonymousUser")) {
            return false;
        }
        return true;
    }

    private Member getSignInMember() {
        Member signInMember = null;

        //check login
        if (login()) {
            //check exist user
            signInMember = getMember(SecurityUtil.getCurrentUsername());
        }
        return signInMember;
    }

    private Board getBoard(Long boardId, String boardType) {
        //board get
        Board board;

        switch (boardType) {
            case "story":
                Optional<Story> storyById = storyRepository.findById(boardId);
                if (storyById.isEmpty()) {
                    //error
                }
                board = storyById.get();
                break;
            case "question":
                Optional<Question> questionById = questionRepository.findById(boardId);
                if (questionById.isEmpty()) {
                    //error
                }
                board = questionById.get();
                break;
            case "answer":
                Optional<Answer> answerById = answerRepository.findById(boardId);
                if (answerById.isEmpty()) {
                    //error
                }
                board = answerById.get();
                break;
            default:
                // error: Invalid dtype
                return null;
        }
        return board;
    }

}
