package com.example.waggle.domain.board.persistence.dao.board.querydsl;

import com.example.waggle.domain.board.persistence.entity.BoardType;
import com.example.waggle.exception.object.general.GeneralException;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.waggle.domain.board.persistence.entity.QQuestion.question;
import static com.example.waggle.domain.board.persistence.entity.QSiren.siren;
import static com.example.waggle.domain.board.persistence.entity.QStory.story;
import static com.example.waggle.domain.hashtag.persistence.entity.QBoardHashtag.boardHashtag;
import static com.example.waggle.domain.media.persistence.entity.QMedia.media;
import static com.example.waggle.domain.recommend.persistence.entity.QRecommend.recommend;
import static com.example.waggle.domain.schedule.persistence.entity.QMemberSchedule.memberSchedule;
import static com.example.waggle.domain.schedule.persistence.entity.QSchedule.schedule;

@RequiredArgsConstructor
@Repository
public class BoardQueryRepositoryImpl implements BoardQueryRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public void deleteBoardsWithRelations(BoardType boardType, Long boardId) {
        queryFactory.delete(media).where(media.board.id.eq(boardId)).execute();
        queryFactory.delete(boardHashtag).where(boardHashtag.board.id.eq(boardId)).execute();
        queryFactory.delete(recommend).where(recommend.board.id.eq(boardId)).execute();
        deleteByBoardType(boardType, boardId);
    }

    private void deleteByBoardType(BoardType boardType, Long boardId) {
        switch (boardType) {
            case STORY -> {
                queryFactory.delete(story).where(story.id.eq(boardId)).execute();
            }
            case QUESTION -> {
                queryFactory.delete(question).where(question.id.eq(boardId)).execute();
            }
            case SIREN -> {
                queryFactory.delete(siren).where(siren.id.eq(boardId)).execute();
            }
            case SCHEDULE -> {
                queryFactory.delete(memberSchedule).where(memberSchedule.schedule.id.eq(boardId)).execute();
                queryFactory.delete(schedule).where(schedule.id.eq(boardId)).execute();
            }
            default -> {
                throw new GeneralException(ErrorStatus.BOARD_INVALID_TYPE);
            }
        }
    }
}