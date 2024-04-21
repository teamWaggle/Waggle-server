package com.example.waggle.domain.notification.service;

import com.example.waggle.domain.follow.service.FollowCommandService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.notification.entity.Notification;
import com.example.waggle.domain.notification.repository.NotificationRepository;
import com.example.waggle.domain.schedule.entity.TeamColor;
import com.example.waggle.domain.schedule.service.team.TeamCommandService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.notification.NotificationRequest;
import com.example.waggle.web.dto.schedule.TeamRequest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.waggle.domain.notification.entity.NotificationType.FOLLOWED;
import static com.example.waggle.domain.notification.entity.NotificationType.PARTICIPATION_REQUEST;
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
    @DisplayName("발신자가 알림을 생성하여 데이터베이스에서 해당 내용을 확인합니다.")
    void createNotification() {
        //given
        NotificationRequest notice = NotificationRequest.builder()
                .type(FOLLOWED)
                .targetId(1L)
                .receiverId(receiver.getId())
                .build();
        //when
        notificationCommandService.sendNotification(sender, notice);

        //then
        List<Notification> all = notificationRepository.findAll();
        assertThat(all).hasSize(1)
                .extracting("type", "targetId")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(FOLLOWED, 1L)
                );
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

}