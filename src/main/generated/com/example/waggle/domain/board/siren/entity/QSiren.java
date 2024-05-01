package com.example.waggle.domain.board.siren.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSiren is a Querydsl query type for Siren
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSiren extends EntityPathBase<Siren> {

    private static final long serialVersionUID = 76278427L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSiren siren = new QSiren("siren");

    public final com.example.waggle.domain.board.QBoard _super;

    //inherited
    public final ListPath<com.example.waggle.domain.hashtag.entity.BoardHashtag, com.example.waggle.domain.hashtag.entity.QBoardHashtag> boardHashtags;

    public final EnumPath<SirenCategory> category = createEnum("category", SirenCategory.class);

    //inherited
    public final ListPath<com.example.waggle.domain.conversation.entity.Comment, com.example.waggle.domain.conversation.entity.QComment> comments;

    public final StringPath contact = createString("contact");

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

    public final DatePath<java.time.LocalDate> lostDate = createDate("lostDate", java.time.LocalDate.class);

    public final StringPath lostLocate = createString("lostLocate");

    //inherited
    public final ListPath<com.example.waggle.domain.media.entity.Media, com.example.waggle.domain.media.entity.QMedia> medias;

    // inherited
    public final com.example.waggle.domain.member.entity.QMember member;

    public final StringPath petAge = createString("petAge");

    public final StringPath petBreed = createString("petBreed");

    public final EnumPath<com.example.waggle.domain.member.entity.Gender> petGender = createEnum("petGender", com.example.waggle.domain.member.entity.Gender.class);

    public final EnumPath<com.example.waggle.domain.board.ResolutionStatus> status = createEnum("status", com.example.waggle.domain.board.ResolutionStatus.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QSiren(String variable) {
        this(Siren.class, forVariable(variable), INITS);
    }

    public QSiren(Path<? extends Siren> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSiren(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSiren(PathMetadata metadata, PathInits inits) {
        this(Siren.class, metadata, inits);
    }

    public QSiren(Class<? extends Siren> type, PathMetadata metadata, PathInits inits) {
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

