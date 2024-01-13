package com.example.waggle.domain.board.siren.service;

import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.board.siren.repository.SirenRepository;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.service.comment.CommentCommandService;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.HelpHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.SecurityUtil;
import com.example.waggle.web.dto.siren.SirenRequest;
import com.example.waggle.web.dto.media.MediaRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.waggle.domain.board.service.BoardType.SIREN;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SirenCommandServiceImpl implements SirenCommandService {
    private final SirenRepository sirenRepository;
    private final RecommendRepository recommendRepository;
    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final MemberQueryService memberQueryService;
    private final CommentCommandService commentCommandService;
    private final MediaCommandService mediaCommandService;

    @Override
    public Long createSiren(SirenRequest.Post helpWriteDto,
                            List<MultipartFile> multipartFiles) throws IOException {
        Siren build = buildSiren(helpWriteDto);
        sirenRepository.save(build);
        mediaCommandService.createMedia(multipartFiles, build);
        return build.getId();
    }


    @Override
    public Long updateSiren(Long boardId,
                           SirenRequest.Put helpWriteDto,
                           List<MultipartFile> multipartFiles,
                           List<String> deleteFiles) throws IOException {
        if (!boardService.validateMemberUseBoard(boardId, SIREN)) {
            throw new HelpHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren help = sirenRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        help.changeSiren(helpWriteDto);

        mediaCommandService.updateMedia(multipartFiles, deleteFiles, help);
        return help.getId();
    }

    @Override
    public Long updateSirenV2(Long boardId,
                             SirenRequest.Put helpUpdateDto,
                             MediaRequest.Put mediaUpdateDto,
                             List<MultipartFile> multipartFiles) throws IOException {
        if (!SecurityUtil.getCurrentUsername().equals(helpUpdateDto.getUsername())) {
            throw new HelpHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren help = sirenRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        help.changeSiren(helpUpdateDto);

        mediaCommandService.updateMediaV2(mediaUpdateDto, multipartFiles, help);

        return help.getId();
    }

    @Override
    public void deleteSiren(Long boardId) {
        if (!boardService.validateMemberUseBoard(boardId, SIREN)) {
            throw new HelpHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Siren help = sirenRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        List<Comment> comments = commentRepository.findByBoardId(help.getId());
        comments.stream().forEach(c -> commentCommandService.deleteComment(c.getId()));

        List<Recommend> recommends = recommendRepository.findByBoardId(help.getId());
        recommends.stream().forEach(r -> recommendRepository.delete(r));
        sirenRepository.delete(help);
    }

    private Siren buildSiren(SirenRequest.Post sirenWriteDto) {
        Member signInMember = memberQueryService.getSignInMember();
        Siren build = Siren.builder()
                .title(sirenWriteDto.getTitle())
                .contact(sirenWriteDto.getContact())
                .content(sirenWriteDto.getContent())
                .petKind(sirenWriteDto.getPetKind())
                .petGender(sirenWriteDto.getPetGender())
                .petAge(sirenWriteDto.getPetAge())
                .lostDate(sirenWriteDto.getLostDate())
                .lostLocate(sirenWriteDto.getLostLocate())
                .category(sirenWriteDto.getCategory())
                .member(signInMember)
                .build();
        return build;
    }
}