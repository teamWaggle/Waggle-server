package com.example.waggle.domain.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberSchedule is a Querydsl query type for MemberSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberSchedule extends EntityPathBase<MemberSchedule> {

    private static final long serialVersionUID = 499849717L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberSchedule memberSchedule = new QMemberSchedule("memberSchedule");

    public final com.example.waggle.global.component.auditing.QBaseEntity _super = new com.example.waggle.global.component.auditing.QBaseEntity(this);

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final com.example.waggle.domain.member.entity.QMember member;

    public final QSchedule schedule;

    public QMemberSchedule(String variable) {
        this(MemberSchedule.class, forVariable(variable), INITS);
    }

    public QMemberSchedule(Path<? extends MemberSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberSchedule(PathMetadata metadata, PathInits inits) {
        this(MemberSchedule.class, metadata, inits);
    }

    public QMemberSchedule(Class<? extends MemberSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.example.waggle.domain.member.entity.QMember(forProperty("member")) : null;
        this.schedule = inits.isInitialized("schedule") ? new QSchedule(forProperty("schedule"), inits.get("schedule")) : null;
    }

}

