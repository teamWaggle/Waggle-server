package com.example.waggle.domain.board.siren.service;

import com.example.waggle.domain.board.ResolutionStatus;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.entity.SirenCategory;
import com.example.waggle.domain.board.siren.repository.SirenRepository;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Gender;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.SirenHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.media.MediaRequest;
import com.example.waggle.web.dto.siren.SirenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.waggle.domain.board.service.BoardType.SIREN;

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
    public Long createSiren(SirenRequest.Post sirenWriteDto,
                            List<MultipartFile> multipartFiles) {
        Siren build = buildSiren(sirenWriteDto);
        sirenRepository.save(build);
        mediaCommandService.createMedia(multipartFiles, build);
        return build.getId();
    }

    @Override
    public Long createSiren(Member member,
                            SirenRequest.Post sirenWriteDto,
                            List<MultipartFile> multipartFiles) {
        Siren build = buildSiren(sirenWriteDto, member);
        sirenRepository.save(build);
        mediaCommandService.createMedia(multipartFiles, build);
        return build.getId();
    }

    @Override
    public Long updateSiren(Long boardId,
                            SirenRequest.Post sirenUpdateDto,
                            List<MultipartFile> multipartFiles,
                            List<String> deleteFiles) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.changeSiren(sirenUpdateDto);

        mediaCommandService.updateMedia(multipartFiles, deleteFiles, siren);
        return siren.getId();
    }

    @Override
    public Long updateSirenV2(Long boardId,
                              SirenRequest.Post request,
                              MediaRequest.Put mediaUpdateDto,
                              List<MultipartFile> multipartFiles) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.changeSiren(request);

        mediaCommandService.updateMediaV2(mediaUpdateDto, multipartFiles, siren);

        return siren.getId();
    }

    @Override
    public Long updateSiren(Long boardId,
                            Member member,
                            SirenRequest.Post request,
                            MediaRequest.Put mediaUpdateDto,
                            List<MultipartFile> multipartFiles) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN, member)) {
            throw new SirenHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren siren = sirenRepository.findById(boardId)
                .orElseThrow(() -> new SirenHandler(ErrorStatus.BOARD_NOT_FOUND));

        siren.changeSiren(request);
        mediaCommandService.updateMediaV2(mediaUpdateDto, multipartFiles, siren);

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

    private Siren buildSiren(SirenRequest.Post sirenWriteDto) {
        Member signInMember = memberQueryService.getSignInMember();
        Siren build = Siren.builder()
                .title(sirenWriteDto.getTitle())
                .contact(sirenWriteDto.getContact())
                .content(sirenWriteDto.getContent())
                .petKind(sirenWriteDto.getPetKind())
                .petGender(Gender.valueOf(sirenWriteDto.getPetGender()))
                .petAge(sirenWriteDto.getPetAge())
                .lostDate(sirenWriteDto.getLostDate())
                .lostLocate(sirenWriteDto.getLostLocate())
                .category(SirenCategory.valueOf(sirenWriteDto.getCategory()))
                .member(signInMember)
                .build();
        return build;
    }

    private Siren buildSiren(SirenRequest.Post sirenWriteDto, Member member) {
        Siren build = Siren.builder()
                .title(sirenWriteDto.getTitle())
                .contact(sirenWriteDto.getContact())
                .content(sirenWriteDto.getContent())
                .petKind(sirenWriteDto.getPetKind())
                .petGender(Gender.valueOf(sirenWriteDto.getPetGender()))
                .petAge(sirenWriteDto.getPetAge())
                .lostDate(sirenWriteDto.getLostDate())
                .lostLocate(sirenWriteDto.getLostLocate())
                .category(SirenCategory.valueOf(sirenWriteDto.getCategory()))
                .status(ResolutionStatus.valueOf(sirenWriteDto.getStatus()))
                .member(member)
                .build();
        return build;
    }
}