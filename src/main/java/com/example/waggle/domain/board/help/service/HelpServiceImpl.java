package com.example.waggle.domain.board.help.service;

import com.example.waggle.domain.board.help.entity.Help;
import com.example.waggle.domain.board.help.repository.HelpRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.global.component.file.FileStore;
import com.example.waggle.global.component.file.UploadFile;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.help.HelpDetailDto;
import com.example.waggle.web.dto.help.HelpSummaryDto;
import com.example.waggle.web.dto.help.HelpWriteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.global.exception.ErrorCode.*;
import static com.example.waggle.global.util.service.BoardType.HELP;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HelpServiceImpl implements HelpService {
    private final HelpRepository helpRepository;
    private final MemberRepository memberRepository;
    private final UtilService utilService;
//    private final MediaService mediaService;
    private final FileStore fileStore;

    @Override
    public List<HelpSummaryDto> getAllHelp() {
        List<Help> all = helpRepository.findAll();
        log.info("all size is ={}",all.size());
        return all.stream().map(HelpSummaryDto::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<HelpSummaryDto> getPagedHelpList(Pageable pageable) {
        Page<Help> all = helpRepository.findAll(pageable);
        return all.map(HelpSummaryDto::toDto);
    }

    @Override
    public Page<HelpSummaryDto> getPagedHelpListByUsername(String username, Pageable pageable) {
        Page<Help> pagehelpByUsername = helpRepository.findByMemberUsername(username, pageable);
        return pagehelpByUsername.map(HelpSummaryDto::toDto);
    }

    @Override
    public HelpDetailDto getHelpByBoardId(Long boardId) {
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(BOARD_NOT_FOUND));

        return HelpDetailDto.toDto(help);
    }

    @Transactional
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


    @Transactional
    @Override
    public Long createHelpTest(HelpWriteDto helpWriteDto, String username) {
        Member signInMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(MEMBER_NOT_FOUND));
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


    @Transactional
    @Override
    public Long updateHelp(Long boardId,
                            HelpWriteDto helpWriteDto,
                            List<MultipartFile> multipartFiles,
                            MultipartFile thumbnail)throws IOException {
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(BOARD_NOT_FOUND));

        help.changeHelp(helpWriteDto);
        if(thumbnail != null) changeThumbnail(help, thumbnail);

        help.getMedias().clear();
//        mediaService.createMedias(help.getId(), multipartFiles, help);
        return help.getId();
    }

    @Transactional
    @Override
    public void deleteHelp(Long boardId) {
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(BOARD_NOT_FOUND));
        if (!utilService.validateMemberUseBoard(boardId, HELP)) {
            throw new GeneralException(CANNOT_TOUCH_NOT_YOURS);
        }
        helpRepository.delete(help);
    }

    public void changeThumbnail(Help help, MultipartFile thumbnail) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(thumbnail);
        help.changeThumbnail(uploadFile.getStoreFileName());
    }
}
