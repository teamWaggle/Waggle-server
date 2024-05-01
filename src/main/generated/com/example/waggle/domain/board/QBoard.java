package com.example.waggle.domain.board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = 1537887886L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final com.example.waggle.global.component.auditing.QBaseEntity _super = new com.example.waggle.global.component.auditing.QBaseEntity(this);

    public final ListPath<com.example.waggle.domain.hashtag.entity.BoardHashtag, com.example.waggle.domain.hashtag.entity.QBoardHashtag> boardHashtags = this.<com.example.waggle.domain.hashtag.entity.BoardHashtag, com.example.waggle.domain.hashtag.entity.QBoardHashtag>createList("boardHashtags", com.example.waggle.domain.hashtag.entity.BoardHashtag.class, com.example.waggle.domain.hashtag.entity.QBoardHashtag.class, PathInits.DIRECT2);

    public final ListPath<com.example.waggle.domain.conversation.entity.Comment, com.example.waggle.domain.conversation.entity.QComment> comments = this.<com.example.waggle.domain.conversation.entity.Comment, com.example.waggle.domain.conversation.entity.QComment>createList("comments", com.example.waggle.domain.conversation.entity.Comment.class, com.example.waggle.domain.conversation.entity.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<com.example.waggle.domain.media.entity.Media, com.example.waggle.domain.media.entity.QMedia> medias = this.<com.example.waggle.domain.media.entity.Media, com.example.waggle.domain.media.entity.QMedia>createList("medias", com.example.waggle.domain.media.entity.Media.class, com.example.waggle.domain.media.entity.QMedia.class, PathInits.DIRECT2);

    public final com.example.waggle.domain.member.entity.QMember member;

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.waggle.domain.member.entity.QMember(forProperty("member")) : null;
    }

}

