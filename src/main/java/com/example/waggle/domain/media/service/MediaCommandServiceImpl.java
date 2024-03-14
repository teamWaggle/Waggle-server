package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.media.repository.MediaRepository;
import com.example.waggle.global.exception.handler.MediaHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.ObjectUtil;
import com.example.waggle.web.dto.media.MediaRequest.MediaCreateDto;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MediaCommandServiceImpl implements MediaCommandService {

    private final MediaRepository mediaRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public boolean createMedia(List<MultipartFile> multipartFiles, Board board) {
        if (multipartFiles == null) {
            return false;
        } else {
            List<String> uploadedFiles = awsS3Service.uploadFiles(multipartFiles);
            uploadedFiles.forEach(file -> mediaRepository.save(
                    Media.builder()
                            .uploadFile(file)
                            .board(board)
                            .build()
            ));
            return true;
        }
    }

    @Override
    public void updateMedia(MediaUpdateDto updateMediaRequest, List<MultipartFile> uploadFiles, Board board) {
        board.getMedias().clear();

        validateUpdateMedia(uploadFiles, updateMediaRequest);
        uploadAndUpdateMedia(uploadFiles, board, updateMediaRequest);
        deleteExistMedia(updateMediaRequest);
    }

    @Override
    public void updateMedia(List<String> imgUrlList, Board board) {
        board.getMedias().clear();
        imgUrlList.forEach(imgUrl -> {
            Media build = Media.builder().uploadFile(imgUrl).board(board).build();
            mediaRepository.save(build);
        });
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
        List<String> imgFileList = awsS3Service.getImgFileList();

        List<String> filesToDelete = imgFileList.stream()
                .filter(file -> !dbImageListSet.contains(file))
                .collect(Collectors.toList());

        filesToDelete
                .forEach(file -> {
                    //log is for check scheduled
                    log.info("delete File is = {}", file);
                    awsS3Service.deleteFile(file);
                });
    }

    private void validateUpdateMedia(List<MultipartFile> multipartFiles, MediaUpdateDto request) {
        long uploadRequestCount = Optional.ofNullable(request)
                .map(MediaUpdateDto::getMediaList)
                .orElse(Collections.emptyList())
                .stream()
                .filter(MediaCreateDto::isAllowUpload)
                .count();

        long uploadFileCount = Optional.ofNullable(multipartFiles)
                .orElse(Collections.emptyList())
                .size();

        if (uploadRequestCount != uploadFileCount) {
            throw new MediaHandler(ErrorStatus.MEDIA_COUNT_IS_DIFFERENT);
        }
    }

    private void uploadAndUpdateMedia(List<MultipartFile> uploadFiles, Board board, MediaUpdateDto updateMediaRequest) {
        if (ObjectUtil.isPresent(updateMediaRequest) && ObjectUtil.isPresent(updateMediaRequest.getMediaList())) {
            updateMediaRequest.getMediaList().forEach(media -> {
                if (media.allowUpload) {
                    String url = awsS3Service.uploadFile(uploadFiles.remove(0));
                    media.setImageUrl(url);
                }
                mediaRepository.save(Media.builder().uploadFile(media.getImageUrl()).board(board).build());
            });
        }
    }

    private void deleteExistMedia(MediaUpdateDto updateMediaRequest) {
        Optional.ofNullable(updateMediaRequest.getDeleteMediaList())
                .orElse(Collections.emptyList())
                .forEach(deleteFileDto -> awsS3Service.deleteFile(deleteFileDto.getImageUrl()));
    }

}
