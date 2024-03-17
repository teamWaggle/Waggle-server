package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaCommandService {

    boolean createMedia(List<MultipartFile> uploadFiles, Board board);

    void updateMedia(MediaUpdateDto updateMediaRequest, List<MultipartFile> uploadFiles, Board board);

    void updateMedia(List<String> imgUrlList, Board board);

    void deleteMedia(Board board);

    void deleteMediaFileInS3();

    List<String> checkDeleteFileInS3();
}
