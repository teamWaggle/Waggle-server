package com.example.waggle.media.service;

import com.example.waggle.board.Board;
import com.example.waggle.commons.component.file.FileStore;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.commons.util.service.BoardType;
import com.example.waggle.commons.util.service.UtilService;
import com.example.waggle.media.domain.Media;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MediaServiceImpl implements MediaService {

    private final FileStore fileStore;
    private final UtilService utilService;

    @Transactional
    @Override
    public List<UploadFile> createMedias(Long boardId, List<MultipartFile> multipartFiles, BoardType boardType) throws IOException {
        List<UploadFile> uploadFiles = fileStore.storeFiles(multipartFiles);
        Board board = utilService.getBoard(boardId, boardType);

        for (UploadFile uploadFile : uploadFiles) {
            Media media = Media.builder()
                    .board(board)
                    .uploadFile(uploadFile).build();
            media.linkBoard(board);
        }

        return uploadFiles;
    }

}
