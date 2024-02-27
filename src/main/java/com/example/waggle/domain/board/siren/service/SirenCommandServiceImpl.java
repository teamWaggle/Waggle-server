package com.example.waggle.domain.board.siren.service;

import static com.example.waggle.domain.board.service.BoardType.SIREN;

import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.entity.SirenCategory;
import com.example.waggle.domain.board.siren.repository.SirenRepository;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.SirenHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.siren.SirenRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SirenCommandServiceImpl implements SirenCommandService {

    private final SirenRepository sirenRepository;
    private final RecommendRepository recommendRepository;
    private final MemberQueryService memberQueryService;
    private final BoardService boardService;
    private final CommentCommandService commentCommandService;
    private final MediaCommandService mediaCommandService;

    @Override
    public Long createSiren(SirenRequest createSirenRequest,
                            List<MultipartFile> multipartFiles) {
        Siren build = buildSiren(createSirenRequest);
        sirenRepository.save(build);
        mediaCommandService.createMedia(multipartFiles, build);
        return build.getId();
    }

    @Override
    public Long createSirenByUsername(SirenRequest createSirenRequest, List<MultipartFile> multipartFiles,
                                      String username) {
        Siren build = buildSiren(createSirenRequest, username);
        sirenRepository.save(build);
        mediaCommandService.createMedia(multipartFiles, build);
        return build.getId();
    }


    @Override
    public Long updateSiren(Long boardId,
                            SirenRequest updateSirenRequest,
                            List<MultipartFile> multipartFiles,
                            List<String> deleteFiles) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.changeSiren(updateSirenRequest);

        mediaCommandService.updateMedia(multipartFiles, deleteFiles, siren);
        return siren.getId();
    }

    @Override
    public Long updateSirenV2(Long boardId,
                              SirenRequest updateSirenRequest,
                              MediaUpdateDto updateMediaRequest,
                              List<MultipartFile> multipartFiles) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.changeSiren(updateSirenRequest);

        mediaCommandService.updateMediaV2(updateMediaRequest, multipartFiles, siren);

        return siren.getId();
    }

    @Override
    public Long updateSirenByUsername(Long boardId,
                                      String username,
                                      SirenRequest updateSirenRequest,
                                      MediaUpdateDto updateMediaRequest,
                                      List<MultipartFile> multipartFiles) {
        Member member = memberQueryService.getMemberByUsername(username);
        if (!boardService.validateMemberUseBoard(boardId, SIREN, member)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.changeSiren(updateSirenRequest);

        mediaCommandService.updateMediaV2(updateMediaRequest, multipartFiles, siren);

        return siren.getId();
    }

    @Override
    public void deleteSiren(Long boardId) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
        recommendRepository.deleteAllByBoardId(boardId);

        sirenRepository.delete(siren);
    }

    @Override
    public void deleteSirenByUsername(Long boardId, String username) {
        Member member = memberQueryService.getMemberByUsername(username);

        if (!boardService.validateMemberUseBoard(boardId, SIREN, member)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.getComments().forEach(comment -> commentCommandService.deleteCommentForHardReset(comment.getId()));
        recommendRepository.deleteAllByBoardId(boardId);

        sirenRepository.delete(siren);
    }

    private Siren buildSiren(SirenRequest createSirenRequest) {
        Member signInMember = memberQueryService.getSignInMember();
        Siren build = Siren.builder()
                .title(createSirenRequest.getTitle())
                .contact(createSirenRequest.getContact())
                .content(createSirenRequest.getContent())
                .petBreed(createSirenRequest.getPetBreed())
                .petGender(createSirenRequest.getPetGender())
                .petAge(createSirenRequest.getPetAge())
                .lostDate(createSirenRequest.getLostDate())
                .lostLocate(createSirenRequest.getLostLocate())
                .category(SirenCategory.valueOf(createSirenRequest.getCategory()))
                .member(signInMember)
                .build();
        return build;
    }

    private Siren buildSiren(SirenRequest createSirenRequest, String username) {
        Member signInMember = memberQueryService.getMemberByUsername(username);
        Siren build = Siren.builder()
                .title(createSirenRequest.getTitle())
                .contact(createSirenRequest.getContact())
                .content(createSirenRequest.getContent())
                .petBreed(createSirenRequest.getPetBreed())
                .petGender(createSirenRequest.getPetGender())
                .petAge(createSirenRequest.getPetAge())
                .lostDate(createSirenRequest.getLostDate())
                .lostLocate(createSirenRequest.getLostLocate())
                .category(SirenCategory.valueOf(createSirenRequest.getCategory()))
                .member(signInMember)
                .build();
        return build;
    }
}