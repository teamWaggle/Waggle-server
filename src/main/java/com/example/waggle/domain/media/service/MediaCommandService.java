package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.web.dto.media.MediaRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaCommandService {

    boolean createMedia(List<MultipartFile> uploadFiles, Board board);

    void updateMedia(List<MultipartFile> uploadFiles, List<String> deleteFile, Board board);

    void updateMediaV2(MediaRequest.Put request, List<MultipartFile> uploadFiles, Board board);

    void deleteMedia(Board board);
}
