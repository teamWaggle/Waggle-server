package com.example.waggle.domain.board.helpU.service;

import static com.example.waggle.commons.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.example.waggle.commons.exception.ErrorCode.CANNOT_TOUCH_NOT_YOURS;
import static com.example.waggle.commons.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.example.waggle.commons.util.service.BoardType.HELPU;

import com.example.waggle.domain.board.helpU.domain.HelpU;
import com.example.waggle.domain.board.helpU.dto.HelpUDetailDto;
import com.example.waggle.domain.board.helpU.dto.HelpUSummaryDto;
import com.example.waggle.domain.board.helpU.dto.HelpUWriteDto;
import com.example.waggle.domain.board.helpU.repository.HelpURepository;
import com.example.waggle.commons.component.file.FileStore;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.commons.exception.CustomApiException;
import com.example.waggle.commons.util.service.UtilService;
import com.example.waggle.domain.member.domain.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HelpUServiceImpl implements HelpUService{
    private final HelpURepository helpURepository;
    private final MemberRepository memberRepository;
    private final UtilService utilService;
//    private final MediaService mediaService;
    private final FileStore fileStore;

    @Override
    public List<HelpUSummaryDto> getAllHelpU() {
        List<HelpU> all = helpURepository.findAll();
        log.info("all size is ={}",all.size());
        return all.stream().map(HelpUSummaryDto::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<HelpUSummaryDto> getPagedHelpUs(Pageable pageable) {
        Page<HelpU> all = helpURepository.findAll(pageable);
        return all.map(HelpUSummaryDto::toDto);
    }

    @Override
    public Page<HelpUSummaryDto> getPagedHelpUsByUsername(String username, Pageable pageable) {
        Page<HelpU> pageHelpUByUsername = helpURepository.findByMemberUsername(username, pageable);
        return pageHelpUByUsername.map(HelpUSummaryDto::toDto);
    }

    @Override
    public HelpUDetailDto getHelpUByBoardId(Long boardId) {
        HelpU helpU = helpURepository.findById(boardId)
                .orElseThrow(() -> new CustomApiException(BOARD_NOT_FOUND));

        return HelpUDetailDto.toDto(helpU);
    }

    @Transactional
    @Override
    public Long createHelpU(HelpUWriteDto helpUWriteDto,
                            List<MultipartFile> multipartFiles,
                            MultipartFile thumbnail) throws IOException {
        Member signInMember = utilService.getSignInMember();
        HelpU helpU = helpUWriteDto.toEntity(signInMember);
        helpURepository.save(helpU);
        if(thumbnail != null) changeThumbnail(helpU, thumbnail);
//        mediaService.createMedias(helpU.getId(), multipartFiles, HELPU);
        return helpU.getId();
    }


    @Transactional
    @Override
    public Long createHelpUTest(HelpUWriteDto helpUWriteDto, String username) {
        Member signInMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomApiException(MEMBER_NOT_FOUND));
        log.info("nickname = {}",signInMember.getNickname());

        HelpU helpU = helpUWriteDto.toEntity(signInMember);
        log.info("save HelpU");
        helpURepository.save(helpU);
        log.info("save HelpU completely");
        if (!helpUWriteDto.getMedias().isEmpty()) {
            for (String mediaUrl : helpUWriteDto.getMedias()) {
//                Media.builder().url(mediaUrl).board(helpU).build().linkBoard(helpU);
            }
        }
        return helpU.getId();
    }


    @Transactional
    @Override
    public Long updateHelpU(Long boardId,
                            HelpUWriteDto helpUWriteDto,
                            List<MultipartFile> multipartFiles,
                            MultipartFile thumbnail)throws IOException {
        HelpU helpU = helpURepository.findById(boardId)
                .orElseThrow(() -> new CustomApiException(BOARD_NOT_FOUND));

        helpU.changeHelpU(helpUWriteDto);
        if(thumbnail != null) changeThumbnail(helpU, thumbnail);

        helpU.getMedias().clear();
//        mediaService.createMedias(helpU.getId(), multipartFiles, HELPU);
        return helpU.getId();
    }

    @Transactional
    @Override
    public void deleteHelpU(Long boardId) {
        HelpU helpU = helpURepository.findById(boardId)
                .orElseThrow(() -> new CustomApiException(BOARD_NOT_FOUND));
        if (!utilService.validateMemberUseBoard(boardId, HELPU)) {
            throw new CustomApiException(CANNOT_TOUCH_NOT_YOURS);
        }
        helpURepository.delete(helpU);
    }

    public void changeThumbnail(HelpU helpU, MultipartFile thumbnail) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(thumbnail);
        helpU.changeThumbnail(uploadFile.getStoreFileName());
    }
}
