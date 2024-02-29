package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.RecommendHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.answer.AnswerResponse.AnswerListDto;
import com.example.waggle.web.dto.question.QuestionResponse;
import com.example.waggle.web.dto.question.QuestionResponse.QuestionSummaryListDto;
import com.example.waggle.web.dto.siren.SirenResponse.SirenDetailDto;
import com.example.waggle.web.dto.siren.SirenResponse.SirenListDto;
import com.example.waggle.web.dto.story.StoryResponse.StoryDetailDto;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecommendQueryServiceImpl implements RecommendQueryService {

    private final RecommendRepository recommendRepository;
    private final MemberQueryService memberQueryService;

    @Override
    public boolean checkRecommend(Long boardId, Long memberId) {
        if (!memberQueryService.isAuthenticated()) {
            return false;
        }
        Member signInMember = memberQueryService.getMemberByUsername(SecurityUtil.getCurrentUsername());

        boolean recommendIt = false;
        //(login user == board writer) checking
        if (signInMember.getId() != memberId) {
            recommendIt = recommendRepository
                    .existsByMemberIdAndBoardId(signInMember.getId(), boardId);
        }
        return recommendIt;
    }

    @Override
    public int countRecommend(Long boardId) {
        return recommendRepository.countByBoardId(boardId);
    }

    @Override
    public List<Member> getRecommendingMembers(Long boardId) {
        List<Recommend> byBoardId = recommendRepository.findByBoardId(boardId);
        return byBoardId.stream().map(r -> r.getMember()).collect(Collectors.toList());
    }

    @Override
    public void getRecommendValues(Object dto) {
        if (dto == null) {
            throw new RecommendHandler(ErrorStatus.BOARD_OBJECT_CANNOT_BE_NULL_WHEN_CHECK_RECOMMEND);
        }
        switch (dto.getClass().getSimpleName()) {
            case "ListDto":
                handleListDto(dto);
                break;
            case "DetailDto":
                handleDetailDto(dto);
                break;
            default:
                throw new RecommendHandler(ErrorStatus.BOARD_TYPE_CANNOT_BE_FOUND_WHEN_CHECK_RECOMMEND);
        }
    }

    private void handleDetailDto(Object dto) {
//        if (dto instanceof QuestionResponse.QuestionDetailDto) {
//            ((QuestionResponse.QuestionDetailDto) dto).setIsRecommend(
//                    checkRecommend(((QuestionResponse.QuestionDetailDto) dto).getBoardId(),
//                            ((QuestionResponse.QuestionDetailDto) dto).getMember().getMemberId()));
//            ((QuestionResponse.QuestionDetailDto) dto).setRecommendCount(
//                    countRecommend(((QuestionResponse.QuestionDetailDto) dto).getBoardId()));
//        } else if (dto instanceof StoryDetailDto) {
//            ((StoryDetailDto) dto).setIsRecommend(checkRecommend(((StoryDetailDto) dto).getBoardId(),
//                    ((StoryDetailDto) dto).getMember().getMemberId()));
//            ((StoryDetailDto) dto).setRecommendCount(countRecommend(((StoryDetailDto) dto).getBoardId()));
//        } else if (dto instanceof SirenDetailDto) {
//            ((SirenDetailDto) dto).setIsRecommend(checkRecommend(((SirenDetailDto) dto).getBoardId(),
//                    ((SirenDetailDto) dto).getMember().getMemberId()));
//            ((SirenDetailDto) dto).setRecommendCount(countRecommend(((SirenDetailDto) dto).getBoardId()));
//        }
    }

    private void handleListDto(Object dto) {
//        if (dto instanceof AnswerListDto) {
//            ((AnswerListDto) dto).getAnswerList()
//                    .forEach(board -> {
//                        board.setIsRecommend(checkRecommend(board.getBoardId(), board.getMember().getMemberId()));
//                        board.setRecommendCount(countRecommend(board.getBoardId()));
//                    });
//        } else if (dto instanceof QuestionSummaryListDto) {
//            ((QuestionSummaryListDto) dto).getQuestionList()
//                    .forEach(board -> {
//                        board.setIsRecommend(checkRecommend(board.getBoardId(), board.getMember().getMemberId()));
//                        board.setRecommendCount(countRecommend(board.getBoardId()));
//                    });
//        } else if (dto instanceof SirenListDto) {
//            ((SirenListDto) dto).getSirenList()
//                    .forEach(board -> {
//                        board.setIsRecommend(checkRecommend(board.getBoardId(), board.getMember().getMemberId()));
//                        board.setRecommendCount(countRecommend(board.getBoardId()));
//                    });
//        }
    }


}
