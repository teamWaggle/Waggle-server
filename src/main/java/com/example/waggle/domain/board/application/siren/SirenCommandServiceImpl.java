package com.example.waggle.domain.board.application.siren;

import com.example.waggle.domain.board.application.board.BoardService;
import com.example.waggle.domain.board.persistence.dao.board.jpa.BoardRepository;
import com.example.waggle.domain.board.persistence.dao.siren.jpa.SirenRepository;
import com.example.waggle.domain.board.persistence.entity.ResolutionStatus;
import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.persistence.entity.SirenCategory;
import com.example.waggle.domain.board.presentation.dto.siren.SirenRequest;
import com.example.waggle.domain.conversation.application.comment.CommentCommandService;
import com.example.waggle.domain.conversation.persistence.dao.comment.jpa.CommentRepository;
import com.example.waggle.domain.media.application.MediaCommandService;
import com.example.waggle.domain.member.persistence.entity.Gender;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.member.persistence.entity.Role;
import com.example.waggle.domain.recommend.persistence.dao.RecommendRepository;
import com.example.waggle.exception.object.handler.QuestionHandler;
import com.example.waggle.exception.object.handler.SirenHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.waggle.domain.board.persistence.entity.BoardType.SIREN;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SirenCommandServiceImpl implements SirenCommandService {

    private final SirenRepository sirenRepository;
    private final RecommendRepository recommendRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final CommentCommandService commentCommandService;
    private final MediaCommandService mediaCommandService;

    @Override
    public Long createSiren(SirenRequest createSirenRequest, Member member) {
        Siren siren = buildSiren(createSirenRequest, member);
        sirenRepository.save(siren);
        mediaCommandService.createMedia(createSirenRequest.getMediaList(), siren);
        return siren.getId();
    }

    @Override
    public Long updateSiren(Long boardId,
                            SirenRequest updateSirenRequest,
                            Member member) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN, member)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.changeSiren(updateSirenRequest);
        mediaCommandService.updateMedia(updateSirenRequest.getMediaList(), siren);

        return siren.getId();
    }


    @Override
    public void convertStatus(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN, member)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new QuestionHandler(ErrorStatus.BOARD_NOT_FOUND));
        switch (siren.getStatus()) {
            case RESOLVED -> siren.changeStatus(ResolutionStatus.UNRESOLVED);
            case UNRESOLVED -> siren.changeStatus(ResolutionStatus.RESOLVED);
            default -> throw new QuestionHandler(ErrorStatus.BOARD_INVALID_TYPE);
        }
    }

    @Override
    public void deleteSiren(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN, member)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
        recommendRepository.deleteAllByBoardId(boardId);

        sirenRepository.delete(siren);
    }

    @Override
    public void deleteSirenWithRelations(Long boardId, Member member) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN, member) || !member.getRole().equals(Role.ADMIN)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        commentRepository.deleteCommentsWithRelationsByBoard(boardId);
        boardRepository.deleteBoardsWithRelations(SIREN, boardId);
    }

    private Siren buildSiren(SirenRequest createSirenRequest, Member member) {
        return Siren.builder()
                .title(createSirenRequest.getTitle())
                .contact(createSirenRequest.getContact())
                .content(createSirenRequest.getContent())
                .petBreed(createSirenRequest.getPetBreed())
                .petGender(Gender.valueOf(createSirenRequest.getPetGender()))
                .petAge(createSirenRequest.getPetAge())
                .lostDate(createSirenRequest.getLostDate())
                .lostLocate(createSirenRequest.getLostLocate())
                .category(SirenCategory.valueOf(createSirenRequest.getCategory()))
                .status(ResolutionStatus.UNRESOLVED)
                .member(member)
                .build();
    }
}