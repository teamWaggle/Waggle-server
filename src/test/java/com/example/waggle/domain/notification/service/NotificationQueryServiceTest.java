//package com.example.waggle.domain.notification.service;
//
//import com.example.waggle.domain.follow.application.FollowCommandService;
//import com.example.waggle.domain.member.persistence.dao.jpa.MemberRepository;
//import com.example.waggle.domain.member.persistence.entity.Member;
//import com.example.waggle.domain.notification.application.NotificationCommandService;
//import com.example.waggle.domain.notification.application.NotificationQueryService;
//import com.example.waggle.domain.notification.persistence.entity.Notification;
//import com.example.waggle.domain.schedule.application.TeamCommandService;
//import com.example.waggle.global.service.test.DatabaseCleanUp;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.groups.Tuple;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static com.example.waggle.domain.notification.persistence.entity.NotificationType.FOLLOWED;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@Slf4j
//@SpringBootTest
//class NotificationQueryServiceTest {
//    //service
//    @Autowired
//    NotificationQueryService notificationQueryService;
//    @Autowired
//    NotificationCommandService notificationCommandService;
//    @Autowired
//    TeamCommandService teamCommandService;
//    @Autowired
//    FollowCommandService followCommandService;
//    //repository
//    @Autowired
//    MemberRepository memberRepository;
//    //Bean
//    @Autowired
//    DatabaseCleanUp databaseCleanUp;
//
//    //entity & dto
//    Member sender1;
//    Member sender2;
//    Member sender3;
//    Member sender4;
//    Member sender5;
//    Member receiver;
//
//    @BeforeEach
//    void setUp() {
//        sender1 = Member.builder()
//                .username("sender1")
//                .userUrl("sender1")
//                .password("sender1")
//                .nickname("sender1")
//                .email("sender1")
//                .build();
//        sender2 = Member.builder()
//                .username("sender2")
//                .userUrl("sender2")
//                .password("sender2")
//                .nickname("sender2")
//                .email("sender2")
//                .build();
//        sender3 = Member.builder()
//                .username("sender3")
//                .userUrl("sender3")
//                .password("sender3")
//                .nickname("sender3")
//                .email("sender3")
//                .build();
//        sender4 = Member.builder()
//                .username("sender4")
//                .userUrl("sender4")
//                .password("sender4")
//                .nickname("sender4")
//                .email("sender4")
//                .build();
//        sender5 = Member.builder()
//                .username("sender5")
//                .userUrl("sender5")
//                .password("sender5")
//                .nickname("sender5")
//                .email("sender5")
//                .build();
//        receiver = Member.builder()
//                .username("receiver")
//                .userUrl("receiver")
//                .password("receiver")
//                .nickname("receiver")
//                .email("receiver")
//                .build();
//        memberRepository.saveAll(List.of(sender1, sender2, sender3, sender4, sender5, receiver));
//    }
//
//    @AfterEach
//    void cleanUp() {
//        databaseCleanUp.truncateAllEntity();
//    }
//
//    @Transactional
//    @Test
//    @DisplayName("발신자가 팔로우를 신청했을 때, 수신자의 알림 내역에 해당 요청이 발신됐는지 확인합니다.")
//    void readAllNotification() {
//        //given
//        followCommandService.follow(sender1, receiver.getId());
//        followCommandService.follow(sender2, receiver.getId());
//        followCommandService.follow(sender3, receiver.getId());
//        followCommandService.follow(sender4, receiver.getId());
//        followCommandService.follow(sender5, receiver.getId());
//        Sort latestSorting = Sort.by("createdDate").descending();
//        Pageable pageable = PageRequest.of(0, 3, latestSorting);
//        //when
//        Page<Notification> notificationList = notificationQueryService.getNotificationList(receiver, pageable);
//
//        //then
//        assertThat(notificationList.getContent()).hasSize(3)
//                .extracting("type", "sender")
//                .containsExactly(
//                        Tuple.tuple(FOLLOWED, sender5),
//                        Tuple.tuple(FOLLOWED, sender4),
//                        Tuple.tuple(FOLLOWED, sender3)
//                );
//
//    }
//
//    @Test
//    @DisplayName("발신자가 팔로우를 신청했을 때, 수신자의 알림 내역을 일부 읽어 안읽은 알림의 개수를 확인합니다.")
//    void readNotificationAndCountIsRead() {
//        //given
//        Long follow_id_1 = followCommandService.follow(sender1, receiver.getId());
//        followCommandService.follow(sender2, receiver.getId());
//        followCommandService.follow(sender3, receiver.getId());
//        followCommandService.follow(sender4, receiver.getId());
//        Long follow_id_5 = followCommandService.follow(sender5, receiver.getId());
//
//        //when
//        notificationCommandService.convertIsRead(receiver, follow_id_1, FOLLOWED);
//        notificationCommandService.convertIsRead(receiver, follow_id_5, FOLLOWED);
//        //then
//        int countNotReadNotification = notificationQueryService.countNotReadNotification(receiver);
//        assertThat(countNotReadNotification).isEqualTo(3);
//    }
//
//    @Transactional
//    @Test
//    @DisplayName("발신자가 팔로우를 신청했을 때, 수신자의 알림 내역을 page로 가져와 sorting을 최신순, 안읽은 순으로 배치했는지 확인합니다.")
//    void readNotificationAndCheckIsRead() {
//        //given
//        Long follow_id_1 = followCommandService.follow(sender1, receiver.getId());
//        followCommandService.follow(sender2, receiver.getId());
//        followCommandService.follow(sender3, receiver.getId());
//        followCommandService.follow(sender4, receiver.getId());
//        Long follow_id_5 = followCommandService.follow(sender5, receiver.getId());
//        Sort latestSorting = Sort.by("createdDate").descending();
//        Sort readSorting = Sort.by("isRead").ascending().and(latestSorting);
//
//        Pageable pageable = PageRequest.of(0, 5, readSorting);
//
//        //when
//        notificationCommandService.convertIsRead(receiver, follow_id_1, FOLLOWED);
//        notificationCommandService.convertIsRead(receiver, follow_id_5, FOLLOWED);
//
//
//        //then
//        Page<Notification> notificationList = notificationQueryService.getNotificationList(receiver, pageable);
//        assertThat(notificationList.getContent()).hasSize(5)
//                .extracting("sender", "isRead")
//                .containsExactly(
//                        Tuple.tuple(sender4, false),
//                        Tuple.tuple(sender3, false),
//                        Tuple.tuple(sender2, false),
//                        Tuple.tuple(sender5, true),
//                        Tuple.tuple(sender1, true)
//                );
//    }
//
//}