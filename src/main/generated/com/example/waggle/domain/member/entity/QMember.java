package com.example.waggle.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -1947466751L;

    public static final QMember member = new QMember("member1");

    public final com.example.waggle.global.component.auditing.QBaseTimeEntity _super = new com.example.waggle.global.component.auditing.QBaseTimeEntity(this);

    public final EnumPath<com.example.waggle.global.security.oauth2.OAuth2UserInfoFactory.AuthProvider> authProvider = createEnum("authProvider", com.example.waggle.global.security.oauth2.OAuth2UserInfoFactory.AuthProvider.class);

    public final DatePath<java.time.LocalDate> birthday = createDate("birthday", java.time.LocalDate.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<com.example.waggle.domain.pet.entity.Pet, com.example.waggle.domain.pet.entity.QPet> pets = this.<com.example.waggle.domain.pet.entity.Pet, com.example.waggle.domain.pet.entity.QPet>createList("pets", com.example.waggle.domain.pet.entity.Pet.class, com.example.waggle.domain.pet.entity.QPet.class, PathInits.DIRECT2);

    public final StringPath profileImgUrl = createString("profileImgUrl");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final ListPath<com.example.waggle.domain.schedule.entity.TeamMember, com.example.waggle.domain.schedule.entity.QTeamMember> teamMembers = this.<com.example.waggle.domain.schedule.entity.TeamMember, com.example.waggle.domain.schedule.entity.QTeamMember>createList("teamMembers", com.example.waggle.domain.schedule.entity.TeamMember.class, com.example.waggle.domain.schedule.entity.QTeamMember.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public final StringPath userUrl = createString("userUrl");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

