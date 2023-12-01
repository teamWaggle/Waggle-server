package com.example.waggle.domain.board.help.service;

import com.example.waggle.domain.board.help.domain.Help;
import com.example.waggle.domain.board.help.repository.HelpRepository;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.global.component.file.FileStore;
import com.example.waggle.global.component.file.UploadFile;
import com.example.waggle.global.exception.CustomApiException;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.help.HelpWriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.waggle.global.exception.ErrorCode.*;
import static com.example.waggle.global.util.service.BoardType.HELP;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HelpCommandServiceImpl implements HelpCommandService{
    private final HelpRepository helpRepository;
    private final MemberRepository memberRepository;
    private final UtilService utilService;
    private final FileStore fileStore;

    @Override
    public Long createHelp(HelpWriteDto helpWriteDto,
                           List<MultipartFile> multipartFiles,
                           MultipartFile thumbnail) throws IOException {
        Member signInMember = utilService.getSignInMember();
        Help help = helpWriteDto.toEntity(signInMember);
        helpRepository.save(help);
        if(thumbnail != null) changeThumbnail(help, thumbnail);
//        mediaService.createMedias(help.getId(), multipartFiles, help);
        return help.getId();
    }

    @Override
    public Long createHelpTest(HelpWriteDto helpWriteDto, String username) {
        Member signInMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomApiException(MEMBER_NOT_FOUND));
        log.info("nickname = {}",signInMember.getNickname());

        Help help = helpWriteDto.toEntity(signInMember);
        log.info("save help");
        helpRepository.save(help);
        log.info("save help completely");
        if (!helpWriteDto.getMedias().isEmpty()) {
            for (String mediaUrl : helpWriteDto.getMedias()) {
//                Media.builder().url(mediaUrl).board(help).build().linkBoard(help);
            }
        }
        return help.getId();
    }

    @Override
    public Long updateHelp(Long boardId,
                           HelpWriteDto helpWriteDto,
                           List<MultipartFile> multipartFiles,
                           MultipartFile thumbnail)throws IOException {
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new CustomApiException(BOARD_NOT_FOUND));

        help.changeHelp(helpWriteDto);
        if(thumbnail != null) changeThumbnail(help, thumbnail);

        help.getMedias().clear();
//        mediaService.createMedias(help.getId(), multipartFiles, help);
        return help.getId();
    }

    @Override
    public void deleteHelp(Long boardId) {
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new CustomApiException(BOARD_NOT_FOUND));
        if (!utilService.validateMemberUseBoard(boardId, HELP)) {
            throw new CustomApiException(CANNOT_TOUCH_NOT_YOURS);
        }
        helpRepository.delete(help);
    }

    public void changeThumbnail(Help help, MultipartFile thumbnail) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(thumbnail);
        help.changeThumbnail(uploadFile.getStoreFileName());
    }
}
