package com.example.waggle.domain.recommend.service;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.RecommendHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.answer.AnswerResponse;
import com.example.waggle.web.dto.question.QuestionResponse;
import com.example.waggle.web.dto.siren.SirenResponse;
import com.example.waggle.web.dto.story.StoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        if (dto instanceof QuestionResponse.DetailDto) {
            ((QuestionResponse.DetailDto) dto).setIsRecommend(checkRecommend(((QuestionResponse.DetailDto) dto).getId(), ((QuestionResponse.DetailDto) dto).getMember().getId()));
            ((QuestionResponse.DetailDto) dto).setRecommendCount(countRecommend(((QuestionResponse.DetailDto) dto).getId()));
        } else if (dto instanceof StoryResponse.DetailDto) {
            ((StoryResponse.DetailDto) dto).setIsRecommend(checkRecommend(((StoryResponse.DetailDto) dto).getId(), ((StoryResponse.DetailDto) dto).getMember().getId()));
            ((StoryResponse.DetailDto) dto).setRecommendCount(countRecommend(((StoryResponse.DetailDto) dto).getId()));
        } else if (dto instanceof SirenResponse.DetailDto) {
            ((SirenResponse.DetailDto) dto).setIsRecommend(checkRecommend(((SirenResponse.DetailDto) dto).getId(), ((SirenResponse.DetailDto) dto).getMember().getId()));
            ((SirenResponse.DetailDto) dto).setRecommendCount(countRecommend(((SirenResponse.DetailDto) dto).getId()));
        }
    }

    private void handleListDto(Object dto) {
        if (dto instanceof AnswerResponse.ListDto) {
            ((AnswerResponse.ListDto) dto).getAnswerList()
                    .forEach(board -> {
                        board.setIsRecommend(checkRecommend(board.getId(), board.getMember().getId()));
                        board.setRecommendCount(countRecommend(board.getId()));
                    });
        } else if (dto instanceof QuestionResponse.ListDto) {
            ((QuestionResponse.ListDto) dto).getQuestionsList()
                    .forEach(board -> {
                        board.setIsRecommend(checkRecommend(board.getId(), board.getMember().getId()));
                        board.setRecommendCount(countRecommend(board.getId()));
                    });
        } else if (dto instanceof SirenResponse.ListDto) {
            ((SirenResponse.ListDto) dto).getSirenList()
                    .forEach(board -> {
                        board.setIsRecommend(checkRecommend(board.getId(), board.getMember().getId()));
                        board.setRecommendCount(countRecommend(board.getId()));
                    });
        } else if (dto instanceof StoryResponse.ListDto) {
            ((StoryResponse.ListDto) dto).getStoryList()
                    .forEach(board -> {
                        board.setIsRecommend(checkRecommend(board.getId(), board.getMember().getId()));
                        board.setRecommendCount(countRecommend(board.getId()));
                    });
        }
    }


}
