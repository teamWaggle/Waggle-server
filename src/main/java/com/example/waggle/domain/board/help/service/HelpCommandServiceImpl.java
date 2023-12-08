package com.example.waggle.domain.board.help.service;

import com.example.waggle.domain.board.help.entity.Help;
import com.example.waggle.domain.board.help.repository.HelpRepository;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.repository.CommentRepository;
import com.example.waggle.domain.comment.service.comment.CommentCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.recommend.entity.Recommend;
import com.example.waggle.domain.recommend.repository.RecommendRepository;
import com.example.waggle.global.exception.handler.HelpHandler;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.help.HelpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.example.waggle.global.util.service.BoardType.HELP;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HelpCommandServiceImpl implements HelpCommandService{
    private final HelpRepository helpRepository;
    private final RecommendRepository recommendRepository;
    private final CommentRepository commentRepository;
    private final UtilService utilService;
    private final CommentCommandService commentCommandService;

    @Override
    public Long createHelp(HelpRequest.Post helpWriteDto) throws IOException {
        Member signInMember = utilService.getSignInMember();
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
                .thumbnail(helpWriteDto.getThumbnail())
                .member(signInMember)
                .build();
        helpRepository.save(build);
//        mediaService.createMedias(help.getId(), multipartFiles, help);
        return build.getId();
    }


    @Override
    public Long updateHelp(Long boardId,
                           HelpRequest.Post helpWriteDto)throws IOException {
        if (!utilService.validateMemberUseBoard(boardId, HELP)) {
            throw new MemberHandler(ErrorStatus.CANNOT_TOUCH_NOT_YOURS);
        }
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        help.changeHelp(helpWriteDto);
//        mediaService.createMedias(help.getId(), multipartFiles, help);
        return help.getId();
    }

    @Override
    public void deleteHelp(Long boardId) {
        if (!utilService.validateMemberUseBoard(boardId, HELP)) {
            throw new MemberHandler(ErrorStatus.CANNOT_TOUCH_NOT_YOURS);
        }
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        List<Comment> comments = commentRepository.findByBoardId(help.getId());
        comments.stream().forEach(c -> commentCommandService.deleteComment(c.getId()));

        List<Recommend> recommends = recommendRepository.findByBoardId(help.getId());
        recommends.stream().forEach(r -> recommendRepository.delete(r));
        helpRepository.delete(help);
    }

}