package com.example.waggle.service.board;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.board.Recommend;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.answer.AnswerViewDto;
import com.example.waggle.dto.board.question.QuestionSimpleViewDto;
import com.example.waggle.dto.board.question.QuestionViewDto;
import com.example.waggle.dto.board.story.StorySimpleViewDto;
import com.example.waggle.dto.board.story.StoryViewDto;
import com.example.waggle.exception.CustomPageException;
import com.example.waggle.repository.board.RecommendRepository;
import com.example.waggle.service.board.util.BoardType;
import com.example.waggle.service.board.util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.waggle.exception.ErrorCode.CANNOT_RECOMMEND_MYSELF;
import static com.example.waggle.exception.ErrorCode.RECOMMEND_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final UtilService utilService;


    public void clickRecommend(Long boardId, BoardType boardType) {

        Board board = utilService.getBoard(boardId, boardType);
        Member member = utilService.getSignInMember();

        //check recommend board
        boolean check = recommendRepository
                .existsByMemberIdAndBoardId(member.getId(), board.getId());
        log.info("i recommended this board {}", check);

        //change state
        if (check) {
            //cancel recommend
            Recommend recommend = recommendRepository
                    .findRecommendByMemberIdAndBoardId(member.getId(), boardId)
                    .orElseThrow(() -> new CustomPageException(RECOMMEND_NOT_FOUND));
            recommendRepository.delete(recommend);
        }
        else{
            //recommend story as member
            if (board.getMember().equals(member)) {
                log.info("can't recommend");
                log.info("board Member {}", board.getMember().getUsername());
                throw new CustomPageException(CANNOT_RECOMMEND_MYSELF);
            }
            Recommend recommendBoard = Recommend.builder().board(board).member(member).build();
            recommendRepository.save(recommendBoard);
        }
    }

    @Transactional(readOnly = true)
    public void checkRecommend(QuestionViewDto questionViewDto) {
        Member signInMember = utilService.getSignInMember();
        boolean recommendIt = false;
        //(login user == board writer) checking
        if (!signInMember.getUsername()
                .equals(questionViewDto.getUsername())) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), questionViewDto.getId());
        }

        int count = recommendRepository.countByBoardId(questionViewDto.getId());
        questionViewDto.linkRecommend(count, recommendIt);
    }

    @Transactional(readOnly = true)
    public void checkRecommend(AnswerViewDto answerViewDto) {
        Member signInMember = utilService.getSignInMember();
        boolean recommendIt = false;
        //(login user == board writer) checking
        if (!signInMember.getUsername()
                .equals(answerViewDto.getUsername())) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), answerViewDto.getId());
        }

        int count = recommendRepository.countByBoardId(answerViewDto.getId());
        answerViewDto.linkRecommend(count, recommendIt);
    }

    @Transactional(readOnly = true)
    public void checkRecommend(StoryViewDto storyViewDto) {
        Member signInMember = utilService.getSignInMember();
        boolean recommendIt = false;
        //(login user == board writer) checking
        if (!signInMember.getUsername()
                .equals(storyViewDto.getUsername())) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), storyViewDto.getId());
        }

        int count = recommendRepository.countByBoardId(storyViewDto.getId());
        log.info("count {}",count);
        storyViewDto.linkRecommend(count, recommendIt);
    }


    //========== 단체 조회 시 =========
    // 단체 recommend 확인은 쿼리량을 줄이기 위함이다.
    @Transactional(readOnly = true)
    public void checkRecommendQuestions(List<QuestionSimpleViewDto> questionViewDtoList) {
        for (QuestionSimpleViewDto questionViewDto : questionViewDtoList) {
            Member signInMember = utilService.getSignInMember();
            boolean recommendIt = false;
            //(login user == board writer) checking
            if (!signInMember.getUsername()
                    .equals(questionViewDto.getUsername())) {
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
            Member signInMember = utilService.getSignInMember();
            boolean recommendIt = false;
            //(login user == board writer) checking
            if (!signInMember.getUsername()
                    .equals(storyViewDto.getUsername())) {
                recommendIt = recommendRepository
                        .existsByMemberIdAndBoardId(signInMember.getId(), storyViewDto.getId());
            }
            int count = recommendRepository.countByBoardId(storyViewDto.getId());
            storyViewDto.linkRecommend(count, recommendIt);
        }
    }

}
