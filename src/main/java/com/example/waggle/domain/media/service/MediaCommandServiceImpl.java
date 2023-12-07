package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.Board;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.media.repository.MediaRepository;
import com.example.waggle.global.util.service.BoardType;
import com.example.waggle.global.util.service.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class MediaCommandServiceImpl implements MediaCommandService{

    private final UtilService utilService;
    private final MediaRepository mediaRepository;
    @Override
    public void createMedia(List<String> uploadFiles, Long boardId, BoardType boardType) {
        Board board = utilService.getBoard(boardId, boardType);
        uploadFiles.stream()
                .forEach(f -> mediaRepository.save(Media.builder().board(board).uploadFile(f).build()));
    }

    @Override
    public void updateMedia(List<String> uploadFiles, Long boardId, BoardType boardType) {
        Board board = utilService.getBoard(boardId, boardType);
        //TODO awsS3file how clean?
        board.getMedias().clear();
        uploadFiles.stream()
                .forEach(f -> mediaRepository.save(Media.builder().board(board).uploadFile(f).build()));
    }
}
