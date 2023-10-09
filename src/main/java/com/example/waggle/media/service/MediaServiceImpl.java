package com.example.waggle.media.service;

import com.example.waggle.board.Board;
import com.example.waggle.board.question.domain.Answer;
import com.example.waggle.board.question.domain.Question;
import com.example.waggle.board.question.repository.AnswerRepository;
import com.example.waggle.board.question.repository.QuestionRepository;
import com.example.waggle.board.story.domain.Story;
import com.example.waggle.board.story.repository.StoryRepository;
import com.example.waggle.commons.component.file.FileStore;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.commons.exception.CustomAlertException;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.media.domain.Media;
import com.example.waggle.media.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.waggle.commons.exception.ErrorCode.BOARD_NOT_FOUND;
import static com.example.waggle.commons.exception.ErrorCode.INVALID_BOARD_TYPE;

@Slf4j
@RequiredArgsConstructor
@Service
public class MediaServiceImpl implements MediaService {

    private final FileStore fileStore;
    private final MediaRepository mediaRepository;
    private final StoryRepository storyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    @Override
    public List<UploadFile> createMedias(Long boardId, List<MultipartFile> multipartFiles, String boardType) throws IOException {
        List<UploadFile> uploadFiles = fileStore.storeFiles(multipartFiles);
        Board board = getBoardById(boardId, boardType);

        for (UploadFile uploadFile : uploadFiles) {
            Media media = Media.builder()
                    .board(board)
                    .uploadFile(uploadFile).build();
            mediaRepository.save(media);
            media.linkBoard(board);
        }

        return uploadFiles;
    }

    public Board getBoardById(Long boardId, String boardType) {
        switch (boardType) {
            case "story":
                return storyRepository.findById(boardId)
                        .orElseThrow(() -> new CustomAlertException(BOARD_NOT_FOUND));
            case "question":
                return questionRepository.findById(boardId)
                        .orElseThrow(() -> new CustomAlertException(BOARD_NOT_FOUND));
            case "answer":
                return answerRepository.findById(boardId)
                        .orElseThrow(() -> new CustomAlertException(BOARD_NOT_FOUND));
            default:
                throw new CustomPageException(INVALID_BOARD_TYPE);
        }
    }


}
