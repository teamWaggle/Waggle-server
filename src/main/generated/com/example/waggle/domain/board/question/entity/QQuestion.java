package com.example.waggle.domain.board.question.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = 1641789153L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestion question = new QQuestion("question");

    public final com.example.waggle.domain.board.QBoard _super;

    //inherited
    public final ListPath<com.example.waggle.domain.hashtag.entity.BoardHashtag, com.example.waggle.domain.hashtag.entity.QBoardHashtag> boardHashtags;

    //inherited
    public final ListPath<com.example.waggle.domain.conversation.entity.Comment, com.example.waggle.domain.conversation.entity.QComment> comments;

    //inherited
    public final StringPath content;

    //inherited
    public final StringPath createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    //inherited
    public final NumberPath<Long> id;

    //inherited
    public final StringPath lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    //inherited
    public final ListPath<com.example.waggle.domain.media.entity.Media, com.example.waggle.domain.media.entity.QMedia> medias;

    // inherited
    public final com.example.waggle.domain.member.entity.QMember member;

    public final EnumPath<com.example.waggle.domain.board.ResolutionStatus> status = createEnum("status", com.example.waggle.domain.board.ResolutionStatus.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QQuestion(String variable) {
        this(Question.class, forVariable(variable), INITS);
    }

    public QQuestion(Path<? extends Question> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestion(PathMetadata metadata, PathInits inits) {
        this(Question.class, metadata, inits);
    }

    public QQuestion(Class<? extends Question> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.example.waggle.domain.board.QBoard(type, metadata, inits);
        this.boardHashtags = _super.boardHashtags;
        this.comments = _super.comments;
        this.content = _super.content;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.id = _super.id;
        this.lastModifiedBy = _super.lastModifiedBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.medias = _super.medias;
        this.member = _super.member;
    }

}

