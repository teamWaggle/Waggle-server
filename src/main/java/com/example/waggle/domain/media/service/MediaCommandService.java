package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaCommandService {
    void createMedia(List<MultipartFile> uploadFiles, Board board);
    void updateMedia(List<MultipartFile> uploadFiles, List<String> deleteFile, Board board);
}
