package com.example.waggle.domain.media.application;

import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.media.persistence.dao.MediaRepository;
import com.example.waggle.domain.media.persistence.entity.Media;
import com.example.waggle.exception.object.handler.MediaHandler;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.service.aws.AwsS3Service;
import com.example.waggle.global.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MediaCommandServiceImpl implements MediaCommandService {

    private final MediaRepository mediaRepository;
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
        board.getMedias().stream()
                .map(Media::getUploadFile)
                .filter(img -> ObjectUtil.doesNotContain(imgUrlList, img))
                .forEach(awsS3Service::deleteFile);
        board.getMedias().clear();
        createMedia(imgUrlList, board);
    }

    @Override
    public void deleteMedia(Board board) {
        board.getMedias().forEach(media -> awsS3Service.deleteFile(media.getUploadFile()));
        mediaRepository.deleteMediaByBoardId(board.getId());
    }

}
