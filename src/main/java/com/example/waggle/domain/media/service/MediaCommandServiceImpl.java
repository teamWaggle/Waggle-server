package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.media.repository.MediaRepository;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.pet.repository.PetRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import com.example.waggle.global.exception.handler.MediaHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MediaCommandServiceImpl implements MediaCommandService {

    private final MediaRepository mediaRepository;
    private final MemberRepository memberRepository;
    private final PetRepository petRepository;
    private final TeamRepository teamRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public void createMedia(List<String> imgUrlList, Board board) {
        if (imgUrlList.contains(null)) {
            throw new MediaHandler(ErrorStatus.MEDIA_PREFIX_IS_WRONG);
        }
        List<Media> mediaList = imgUrlList.stream().map(img -> Media.builder()
                .uploadFile(img)
                .board(board)
                .build()).collect(Collectors.toList());
        mediaRepository.saveAll(mediaList);
    }

    @Override
    public void updateMedia(List<String> imgUrlList, Board board) {
        board.getMedias().clear();
        createMedia(imgUrlList, board);
    }

    @Override
    public void deleteMedia(Board board) {
        board.getMedias().forEach(media -> awsS3Service.deleteFile(media.getUploadFile()));
        mediaRepository.deleteMediaByBoardId(board.getId());
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Profile({"real1", "real2"})
    @Override
    public void deleteMediaFileInS3() {
        log.info("start sync img file");
        List<String> dbImageList = mediaRepository.findAll().stream()
                .map(media -> media.getUploadFile()).collect(Collectors.toList());
        Set<String> dbImageListSet = new HashSet(dbImageList);
        memberRepository.findAll().forEach(member -> dbImageListSet.add(member.getProfileImgUrl()));
        petRepository.findAll().forEach(pet -> dbImageListSet.add(pet.getProfileImgUrl()));
        teamRepository.findAll().forEach(team -> dbImageListSet.add(team.getCoverImageUrl()));
        List<String> imgFileList = awsS3Service.getImgFileList();
        imgFileList.forEach(imgInDB -> log.info("imgInDB : {}", imgInDB));

        List<String> filesToDelete = imgFileList.stream()
                .filter(file -> !dbImageListSet.contains(file))
                .collect(Collectors.toList());
        filesToDelete.remove("Waggle/Waggle.zip");

        filesToDelete
                .forEach(file -> {
                    log.info("file = {}", file);
                    awsS3Service.deleteFile(file);
                });
    }

}
