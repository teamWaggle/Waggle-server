package com.example.waggle.board.helpU.service;

import com.example.waggle.board.helpU.domain.HelpU;
import com.example.waggle.board.helpU.repository.HelpURepository;
import com.example.waggle.board.helpU.dto.HelpUDetailDto;
import com.example.waggle.board.helpU.dto.HelpUSummaryDto;
import com.example.waggle.board.helpU.dto.HelpUWriteDto;
import com.example.waggle.board.story.domain.Story;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.commons.exception.CustomApiException;
import com.example.waggle.commons.exception.ErrorCode;
import com.example.waggle.commons.util.service.BoardType;
import com.example.waggle.commons.util.service.UtilService;
import com.example.waggle.media.domain.Media;
import com.example.waggle.member.domain.Member;
import com.example.waggle.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.waggle.commons.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HelpUServiceImpl implements HelpUService{
    private final HelpURepository helpURepository;
    private final MemberRepository memberRepository;
    private final UtilService utilService;

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
    public Long createHelpU(HelpUWriteDto helpUWriteDto) {
        Member signInMember = utilService.getSignInMember();
        HelpU helpU = helpUWriteDto.toEntity(signInMember);
        helpURepository.save(helpU);
        if (!helpUWriteDto.getMedias().isEmpty()) {
            for (String mediaUrl : helpUWriteDto.getMedias()) {
                Media.builder().url(mediaUrl).board(helpU).build().linkBoard(helpU);
            }
        }
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
                Media.builder().url(mediaUrl).board(helpU).build().linkBoard(helpU);
            }
        }
        return helpU.getId();
    }

    @Transactional
    @Override
    public Long createHelpUWithThumbnail(HelpUWriteDto helpUDto, String thumbnail) {
        Member signInMember = utilService.getSignInMember();
        helpUDto.changeThumbnail(thumbnail);

        HelpU saveHelpU = helpUDto.toEntity(signInMember);
        helpURepository.save(saveHelpU);

        if (!helpUDto.getMedias().isEmpty()) {
            for (String mediaURL : helpUDto.getMedias()) {
                Media.builder().url(mediaURL).board(saveHelpU).build().linkBoard(saveHelpU);
            }
        }

        return saveHelpU.getId();
    }

    @Transactional
    @Override
    public Long updateHelpU(Long boardId, HelpUWriteDto helpUWriteDto) {
        HelpU helpU = helpURepository.findById(boardId)
                .orElseThrow(() -> new CustomApiException(BOARD_NOT_FOUND));
        helpU.changeHelpU(helpUWriteDto);

        helpU.getMedias().clear();
        if (!helpUWriteDto.getMedias().isEmpty()) {
            for (String mediaUrl : helpUWriteDto.getMedias()) {
                Media.builder().url(mediaUrl).board(helpU).build().linkBoard(helpU);
            }
        }
        return helpU.getId();
    }

    @Transactional
    @Override
    public void deleteHelpU(Long boardId) {
        HelpU helpU = helpURepository.findById(boardId)
                .orElseThrow(() -> new CustomApiException(BOARD_NOT_FOUND));
        if (!utilService.validateMemberUseBoard(boardId, BoardType.HELPU)) {
            throw new CustomApiException(CANNOT_TOUCH_NOT_YOURS);
        }
        helpURepository.delete(helpU);
    }
}
