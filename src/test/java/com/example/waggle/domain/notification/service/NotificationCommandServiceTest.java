package com.example.waggle.domain.notification.service;

import com.example.waggle.domain.conversation.application.comment.CommentCommandService;
import com.example.waggle.domain.follow.application.FollowCommandService;
import com.example.waggle.domain.member.persistence.dao.MemberRepository;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.notification.application.NotificationCommandService;
import com.example.waggle.domain.notification.persistence.dao.NotificationRepository;
import com.example.waggle.domain.notification.persistence.entity.Notification;
import com.example.waggle.domain.schedule.application.team.TeamCommandService;
import com.example.waggle.domain.schedule.persistence.entity.TeamColor;
import com.example.waggle.domain.schedule.presentation.dto.team.TeamRequest;
import com.example.waggle.global.service.test.DatabaseCleanUp;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.waggle.domain.notification.persistence.entity.NotificationType.FOLLOWED;
import static com.example.waggle.domain.notification.persistence.entity.NotificationType.PARTICIPATION_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class NotificationCommandServiceTest {
    //service
    @Autowired
    NotificationCommandService notificationCommandService;
    @Autowired
    FollowCommandService followCommandService;
    @Autowired
    TeamCommandService teamCommandService;
    @Autowired
    CommentCommandService commentCommandService;
    //repository
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    NotificationRepository notificationRepository;
    //bean
    @Autowired
    DatabaseCleanUp databaseCleanUp;
    //entity & dto
    Member sender;
    Member receiver;


    @BeforeEach
    void setUp() {
        sender = Member.builder()
                .username("sender")
                .userUrl("sender")
                .password("sender")
                .nickname("sender")
                .email("sender")
                .build();
        receiver = Member.builder()
                .username("receiver")
                .userUrl("receiver")
                .password("receiver")
                .nickname("receiver")
                .email("receiver")
                .build();
        memberRepository.saveAll(List.of(sender, receiver));

    }

    @AfterEach
    void cleanUp() {
        databaseCleanUp.truncateAllEntity();
    }


    @Test
    @DisplayName("발신자가 follow를 하면서 알림을 생성합니다.")
    void createNotificationWhenFollow() {
        //given & when
        Long followId = followCommandService.follow(sender, receiver.getId());
        //then
        List<Notification> all = notificationRepository.findAll();
        assertThat(all).hasSize(1)
                .extracting("type", "targetId")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(FOLLOWED, followId)
                );
    }

    @Test
    @DisplayName("발신자가 team에 참가 요청을 하면서 알림을 생성합니다.")
    void createNotificationWhenParticipation() {
        //given
        TeamRequest teamRequest = TeamRequest.builder()
                .name("team")
                .teamColor(String.valueOf(TeamColor.team_1))
                .description("description")
                .build();
        Long teamId = teamCommandService.createTeam(teamRequest, receiver);

        //when
        Long participationId = teamCommandService.requestParticipation(teamId, sender);

        //then
        List<Notification> all = notificationRepository.findAll();
        assertThat(all).hasSize(1)
                .extracting("type", "targetId")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(PARTICIPATION_REQUEST, participationId)
                );
    }

    @Test
    @DisplayName("수신한 알림을 읽었을 때, 상태를 변경합니다.")
    void testMethodName() {
        //given
        TeamRequest teamRequest = TeamRequest.builder()
                .name("team")
                .teamColor(String.valueOf(TeamColor.team_1))
                .description("description")
                .build();
        Long teamId = teamCommandService.createTeam(teamRequest, receiver);
        Long participationId = teamCommandService.requestParticipation(teamId, sender);
        //when
        notificationCommandService.convertIsRead(receiver, participationId, PARTICIPATION_REQUEST);
        //then
        List<Notification> all = notificationRepository.findAll();
        assertThat(all).hasSize(1)
                .extracting("isRead")
                .containsExactlyInAnyOrder(true);
    }


}