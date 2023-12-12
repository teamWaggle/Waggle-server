package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.media.repository.MediaRepository;
import com.example.waggle.global.exception.handler.MediaHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class MediaCommandServiceImpl implements MediaCommandService{

    private final MediaRepository mediaRepository;
    private final AwsS3Service awsS3Service;
    @Override
    public void createMedia(List<MultipartFile> multipartFiles, Board board) {
        if (multipartFiles == null) {
            return;
        }
        List<String> uploadedFiles = awsS3Service.uploadFiles(multipartFiles);
        uploadedFiles.stream()
                .forEach(f -> mediaRepository.save(Media.builder().board(board).uploadFile(f).build()));
    }

    @Override
    public void updateMedia(List<MultipartFile> uploadFiles,
                            List<String> deleteFiles,
                            Board board) {
        //TODO awsS3file how clean?
        //first step. delete
        //delete file check
        if (uploadFiles != null) {
            List<Media> collect = deleteFiles.stream()
                    .map(f -> mediaRepository.findByUploadFile(f)
                            .orElseThrow(() -> new MediaHandler(ErrorStatus.MEDIA_NOT_FOUND)))
                    .collect(Collectors.toList());
            collect.stream().forEach(m -> {
                if (m.getBoard().equals(board)) {
                    mediaRepository.delete(m);
                }
            });
        }
        //second step. add file
        createMedia(uploadFiles,board);
    }
}
