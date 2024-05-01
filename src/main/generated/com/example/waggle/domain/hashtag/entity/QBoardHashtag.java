package com.example.waggle.domain.hashtag.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardHashtag is a Querydsl query type for BoardHashtag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardHashtag extends EntityPathBase<BoardHashtag> {

    private static final long serialVersionUID = -1253591553L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardHashtag boardHashtag = new QBoardHashtag("boardHashtag");

    public final com.example.waggle.global.component.auditing.QBaseEntity _super = new com.example.waggle.global.component.auditing.QBaseEntity(this);

    public final com.example.waggle.domain.board.QBoard board;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QHashtag hashtag;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public QBoardHashtag(String variable) {
        this(BoardHashtag.class, forVariable(variable), INITS);
    }

    public QBoardHashtag(Path<? extends BoardHashtag> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardHashtag(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardHashtag(PathMetadata metadata, PathInits inits) {
        this(BoardHashtag.class, metadata, inits);
    }

    public QBoardHashtag(Class<? extends BoardHashtag> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.example.waggle.domain.board.QBoard(forProperty("board"), inits.get("board")) : null;
        this.hashtag = inits.isInitialized("hashtag") ? new QHashtag(forProperty("hashtag")) : null;
    }

}

