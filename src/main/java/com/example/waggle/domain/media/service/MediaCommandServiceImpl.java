package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.media.repository.MediaRepository;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.pet.repository.PetRepository;
import com.example.waggle.domain.schedule.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Override
    public void deleteMediaFileInS3() {
        List<Media> dbImageList = mediaRepository.findAll();
        Set<String> dbImageListSet = new HashSet(dbImageList);
        memberRepository.findAll().forEach(member -> dbImageListSet.add(member.getProfileImgUrl()));
        petRepository.findAll().forEach(pet -> dbImageListSet.add(pet.getProfileImgUrl()));
        teamRepository.findAll().forEach(team -> dbImageListSet.add(team.getCoverImageUrl()));
        List<String> imgFileList = awsS3Service.getImgFileList();

        List<String> filesToDelete = imgFileList.stream()
                .filter(file -> !dbImageListSet.contains(file))
                .collect(Collectors.toList());

        filesToDelete
                .forEach(file -> {
                    //log is for check scheduled
                    awsS3Service.deleteFile(file);
                });
    }

}
