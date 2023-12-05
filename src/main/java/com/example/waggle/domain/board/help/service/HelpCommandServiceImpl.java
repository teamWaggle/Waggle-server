package com.example.waggle.domain.board.help.service;

import static com.example.waggle.global.util.service.BoardType.HELP;

import com.example.waggle.domain.board.help.entity.Help;
import com.example.waggle.domain.board.help.repository.HelpRepository;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.global.component.file.FileStore;
import com.example.waggle.global.component.file.UploadFile;
import com.example.waggle.global.exception.handler.HelpHandler;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.service.UtilService;
import com.example.waggle.web.dto.help.HelpRequest;
import java.io.IOException;
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
public class HelpCommandServiceImpl implements HelpCommandService{
    private final HelpRepository helpRepository;
    private final MemberRepository memberRepository;
    private final UtilService utilService;
    private final FileStore fileStore;

    @Override
    public Long createHelp(HelpRequest.Post helpWriteDto,
                           List<MultipartFile> multipartFiles,
                           MultipartFile thumbnail) throws IOException {
        Member signInMember = utilService.getSignInMember();
        Help build = Help.builder()
                .title(helpWriteDto.getTitle())
                .contact(helpWriteDto.getContact())
                .petKind(helpWriteDto.getPetKind())
                .petGender(helpWriteDto.getPetGender())
                .petAge(helpWriteDto.getPetAge())
                .lostDate(helpWriteDto.getLostDate())
                .petName(helpWriteDto.getPetName())
                .build();
        helpRepository.save(build);
        if(thumbnail != null) changeThumbnail(build, thumbnail);
//        mediaService.createMedias(help.getId(), multipartFiles, help);
        return build.getId();
    }

    @Override
    public Long createHelpTest(HelpRequest.Post helpWriteDto, String username) {
        Member signInMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        log.info("nickname = {}",signInMember.getNickname());

        Help help = Help.builder()
                .title(helpWriteDto.getTitle())
                .contact(helpWriteDto.getContact())
                .petKind(helpWriteDto.getPetKind())
                .petGender(helpWriteDto.getPetGender())
                .petAge(helpWriteDto.getPetAge())
                .lostDate(helpWriteDto.getLostDate())
                .petName(helpWriteDto.getPetName())
                .build();;
        log.info("save help");
        helpRepository.save(help);
        log.info("save help completely");
//        if (!helpWriteDto.getMedias().isEmpty()) {
//            for (String mediaUrl : helpWriteDto.getMedias()) {
////                Media.builder().url(mediaUrl).board(help).build().linkBoard(help);
//            }
//        }
        return help.getId();
    }

    @Override
    public Long updateHelp(Long boardId,
                           HelpRequest.Post helpWriteDto,
                           List<MultipartFile> multipartFiles,
                           MultipartFile thumbnail)throws IOException {
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));

        help.changeHelp(helpWriteDto);
        if(thumbnail != null) changeThumbnail(help, thumbnail);

        help.getMedias().clear();
//        mediaService.createMedias(help.getId(), multipartFiles, help);
        return help.getId();
    }

    @Override
    public void deleteHelp(Long boardId) {
        Help help = helpRepository.findById(boardId)
                .orElseThrow(() -> new HelpHandler(ErrorStatus.BOARD_NOT_FOUND));
        if (!utilService.validateMemberUseBoard(boardId, HELP)) {
            throw new HelpHandler(ErrorStatus.BOARD_CANNOT_EDIT_OTHERS);
        }
        helpRepository.delete(help);
    }

    public void changeThumbnail(Help help, MultipartFile thumbnail) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(thumbnail);
        help.changeThumbnail(uploadFile.getStoreFileName());
    }
}