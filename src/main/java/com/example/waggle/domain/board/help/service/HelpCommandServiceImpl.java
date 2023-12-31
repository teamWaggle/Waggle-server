package com.example.waggle.domain.board.help.service;

import com.example.waggle.domain.board.help.entity.Help;
import com.example.waggle.domain.board.help.repository.HelpRepository;
import com.example.waggle.domain.board.service.BoardService;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.service.comment.CommentCommandService;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.HelpHandler;
import com.example.waggle.global.exception.handler.StoryHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.security.SecurityUtil;
import com.example.waggle.web.dto.help.HelpRequest;
import com.example.waggle.web.dto.media.MediaRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.waggle.domain.board.service.BoardType.HELP;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HelpCommandServiceImpl implements HelpCommandService{
    private final HelpRepository helpRepository;
    private final RecommendRepository recommendRepository;
    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final MemberQueryService memberQueryService;
    private final CommentCommandService commentCommandService;
    private final MediaCommandService mediaCommandService;

    @Override
    public Long createHelp(HelpRequest.Post helpWriteDto,
                           List<MultipartFile> multipartFiles) throws IOException {
        Help build = buildHelp(helpWriteDto);
        helpRepository.save(build);
        mediaCommandService.createMedia(multipartFiles,build);
        return build.getId();
    }


    @Override
    public Long updateHelp(Long boardId,
                           HelpRequest.Put helpWriteDto,
                           List<MultipartFile> multipartFiles,
                           List<String> deleteFiles)throws IOException {
        if (!boardService.validateMemberUseBoard(boardId, HELP)) {
            throw new HelpHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        help.changeHelp(helpWriteDto);
        mediaCommandService.updateMedia(multipartFiles,deleteFiles,help);
        return help.getId();
    }

    @Override
    public Long updateHelpV2(Long boardId,
                             HelpRequest.Put helpUpdateDto,
                             MediaRequest.Put mediaUpdateDto,
                             List<MultipartFile> multipartFiles) throws IOException {
        if (!SecurityUtil.getCurrentUsername().equals(helpUpdateDto.getUsername())) {
            throw new StoryHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        help.changeHelp(helpUpdateDto);

        mediaCommandService.updateMediaV2(mediaUpdateDto, multipartFiles, help);

        return help.getId();
    }

    @Override
    public void deleteHelp(Long boardId) {
        if (!boardService.validateMemberUseBoard(boardId, HELP)) {
            throw new HelpHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        List<Comment> comments = commentRepository.findByBoardId(help.getId());
        comments.stream().forEach(c -> commentCommandService.deleteComment(c.getId()));

        List<Recommend> recommends = recommendRepository.findByBoardId(help.getId());
        recommends.stream().forEach(r -> recommendRepository.delete(r));
        helpRepository.delete(help);
    }

    private Help buildHelp(HelpRequest.Post helpWriteDto) {
        Member signInMember = memberQueryService.getSignInMember();
        Help build = Help.builder()
                .title(helpWriteDto.getTitle())
                .contact(helpWriteDto.getContact())
                .content(helpWriteDto.getContent())
                .petKind(helpWriteDto.getPetKind())
                .petGender(helpWriteDto.getPetGender())
                .petAge(helpWriteDto.getPetAge())
                .lostDate(helpWriteDto.getLostDate())
                .lostLocate(helpWriteDto.getLostLocate())
                .category(helpWriteDto.getCategory())
                .member(signInMember)
                .build();
        return build;
    }

}