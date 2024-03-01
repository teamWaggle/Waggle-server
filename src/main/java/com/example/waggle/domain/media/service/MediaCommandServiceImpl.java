package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.media.repository.MediaRepository;
import com.example.waggle.global.exception.handler.MediaHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.dto.media.MediaRequest.MediaCreateDto;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

        if (validateUpdateMedia(uploadFiles, updateMediaRequest)) {
            updateMediaRequest.getMediaList().forEach(media -> forEachMediaUpdate(uploadFiles, board, media));
        }
        if (updateMediaRequest != null) {
            Optional.ofNullable(updateMediaRequest.getDeleteMediaList())
                    .map(Collection::stream)
                    .orElseGet(Stream::empty)
                    .forEach(deleteFileDto -> {
                        awsS3Service.deleteFile(deleteFileDto.getImageUrl());
//                    mediaRepository.deleteById(deleteFileDto.getId());
                    });
        }
    }

    private void forEachMediaUpdate(List<MultipartFile> uploadFiles, Board board, MediaCreateDto media) {
        if (media.allowUpload) {
            String url = awsS3Service.uploadFile(uploadFiles.get(0));
            media.setImageUrl(url);
            uploadFiles.remove(0);
        }
        mediaRepository.save(Media.builder()
                .uploadFile(media.getImageUrl())
                .board(board)
                .build());
    }

    @Override
    public void deleteMedia(Board board) {
        board.getMedias().forEach(media -> awsS3Service.deleteFile(media.getUploadFile()));
        mediaRepository.deleteMediaByBoardId(board.getId());
    }

    private boolean validateUpdateMedia(List<MultipartFile> multipartFiles, MediaUpdateDto request) {
        long requestCount =
                (request != null && request.getMediaList() != null) ? request.getMediaList().stream()
                        .filter(media -> media.allowUpload).count() : 0;
        long mediaCount = (multipartFiles != null) ? multipartFiles.size() : 0;

        if (requestCount != mediaCount) {
            throw new MediaHandler(ErrorStatus.MEDIA_COUNT_IS_DIFFERENT);
        }

        return requestCount > 0;
    }

}
