package com.example.waggle.domain.notification.entity.alarm;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
public class AlarmArgs implements Serializable {

    private static final long serialVersionUID = 300L;
    // 알람 발생 시킨 멤버
    private String callingMemberNickname;
    // 알람 발생 시킨 글id
    private String boardId;
    // 알람 발생 시킨 댓글id
    private String commentId;
    //링크 url
    private String linkUrl;
}
