package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.media.repository.MediaRepository;
import com.example.waggle.global.exception.handler.MediaHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.media.MediaRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${app.server.uri}")
    private String SERVER_URI;
    @Override
    public boolean createMedia(List<MultipartFile> multipartFiles, Board board) {
        if (multipartFiles == null) {
            return false;
        }else{
            List<String> uploadedFiles = awsS3Service.uploadFiles(multipartFiles);
            StringBuffer stringBuffer = new StringBuffer(SERVER_URI);
            uploadedFiles.stream().forEach(file -> {
                StringBuffer appendURI = stringBuffer.append("/").append(file);
                mediaRepository.save(
                        Media.builder().board(board).uploadFile(String.valueOf(appendURI)).build()
                );
            });
            return true;
        }
    }

    @Override
    public void updateMedia(List<MultipartFile> uploadFiles,
                            List<String> deleteFiles,
                            Board board) {
        //TODO awsS3file how clean?
        //first step. delete
        //delete file check
        if (deleteFiles != null) {
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

    @Override
    public void updateMediaV2(MediaRequest.Put request, List<MultipartFile> uploadFiles, Board board) {
        board.getMedias().clear();

        request.getMediaList().forEach(media -> {
            if (media.allowUpload) {
                if (uploadFiles.isEmpty()) {
                    throw new MediaHandler(ErrorStatus.MEDIA_REQUEST_IS_EMPTY);
                }
                String url = awsS3Service.uploadFile(uploadFiles.get(0));
                media.setImageUrl(url);
                uploadFiles.remove(0);
            }
            mediaRepository.save(Media.builder()
                    .uploadFile(media.getImageUrl())
                    .board(board)
                    .build());
        });
        if (!uploadFiles.isEmpty()) {
            throw new MediaHandler(ErrorStatus.MEDIA_REQUEST_STILL_EXIST);
        }

        request.getDeleteMediaList().forEach(dto -> mediaRepository.deleteById(dto.getId()));

    }
}
