package com.example.waggle.domain.media.service;

import com.example.waggle.domain.board.entity.Board;
import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.media.repository.MediaRepository;
import com.example.waggle.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class MediaCommandServiceImpl implements MediaCommandService{

    private final BoardService utilService;
    private final MediaRepository mediaRepository;
    @Override
    public void createMedia(List<String> uploadFiles, Board board) {
        uploadFiles.stream()
                .forEach(f -> mediaRepository.save(Media.builder().board(board).uploadFile(f).build()));
    }

    @Override
    public void updateMedia(List<String> uploadFiles, Board board) {
        //TODO awsS3file how clean?
        board.getMedias().clear();
        uploadFiles.stream()
                .forEach(f -> mediaRepository.save(Media.builder().board(board).uploadFile(f).build()));
    }
}
