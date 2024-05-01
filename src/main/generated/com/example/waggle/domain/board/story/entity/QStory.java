package com.example.waggle.domain.board.story.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStory is a Querydsl query type for Story
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStory extends EntityPathBase<Story> {

    private static final long serialVersionUID = 466870427L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStory story = new QStory("story");

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

    public QStory(String variable) {
        this(Story.class, forVariable(variable), INITS);
    }

    public QStory(Path<? extends Story> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStory(PathMetadata metadata, PathInits inits) {
        this(Story.class, metadata, inits);
    }

    public QStory(Class<? extends Story> type, PathMetadata metadata, PathInits inits) {
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

