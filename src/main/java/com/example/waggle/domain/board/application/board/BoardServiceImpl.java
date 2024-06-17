package com.example.waggle.domain.board.application.board;

import com.example.waggle.domain.board.persistence.dao.board.jpa.BoardRepository;
import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.board.persistence.entity.BoardType;
import com.example.waggle.domain.hashtag.persistence.dao.HashtagRepository;
import com.example.waggle.domain.hashtag.persistence.entity.BoardHashtag;
import com.example.waggle.domain.hashtag.persistence.entity.Hashtag;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.ErrorStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public boolean validateMemberUseBoard(Long boardId, BoardType boardType, Member member) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));
        return board.getMember().equals(member);
    }

    @Override
    public void saveHashtag(Board board, String tag) {
        Hashtag hashtag = getHashtag(tag);
        BoardHashtag.builder()
                .hashtag(hashtag)
                .board(board)
                .build()
                .link(board, hashtag);
    }

    @Override
    public Hashtag getHashtag(String tag) {
        Optional<Hashtag> byContent = hashtagRepository.findByContent(tag);
        if (byContent.isEmpty()) {
            Hashtag build = Hashtag.builder().content(tag).build();
            hashtagRepository.save(build);
            return build;
        }
        return byContent.get();
    }
}
