package com.example.waggle.domain.notification.service;


import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.RedisService;
import com.example.waggle.domain.notification.entity.alarm.Alarm;
import com.example.waggle.domain.notification.entity.alarm.AlarmType;
import com.example.waggle.domain.notification.entity.alarm.alarmArgs.AlarmArgs;
import com.example.waggle.domain.notification.entity.sse.SseEventName;
import com.example.waggle.domain.notification.entity.sse.SseRepositoryKeyRule;
import com.example.waggle.domain.notification.repository.AlarmRepository;
import com.example.waggle.domain.notification.repository.SseMemoryRepository;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.global.payload.code.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlarmService {
    private static final Long sseTimeout = 1000 * 60 * 3L;
    private static final String UNDER_SCORE = "_";
    private static final String CONNECTED = "CONNECTED";
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final SseMemoryRepository sseRepository;
    private final RedisService redisService;


    @Transactional
    public List<Alarm> sendAlarmSliceAndIsReadToTrue(Pageable pageable, Member member) {
        return alarmRepository.findByCalledMember(member);
    }

    @Transactional
    public void createAlarm(Long alarmReceiverId, AlarmType alarmType, AlarmArgs alarmArgs) {
        Member alarmReceiver = memberRepository.findById(alarmReceiverId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Alarm alarm = Alarm.of(alarmType, alarmArgs, alarmReceiver);
        alarmRepository.save(alarm);
    }

    public void send(Long alarmReceiverId, SseEventName sseEventName) {
        redisService.publishMessage(alarmReceiverId, sseEventName);
    }

    public SseEmitter subscribe(Member member, LocalDateTime now) {
        Long userId = member.getId();
        SseEmitter sse = new SseEmitter(sseTimeout);
        String key = new SseRepositoryKeyRule(userId, SseEventName.ALARM_LIST, now)
                .toCompleteKeyWhichSpecifyOnlyOneValue();

        sse.onCompletion(() -> {
            log.info("onCompletion callback");
            sseRepository.remove(key);
        });
        sse.onTimeout(() -> {
            log.info("onTimeout callback");
            //만료 시 Repository에서 삭제 되어야함.
            sse.complete();
        });

        sseRepository.put(key, sse);
        try {
            sse.send(SseEmitter.event()
                    .name(CONNECTED)
                    .id(getEventId(userId, now, SseEventName.ALARM_LIST))
                    .data("subscribe"));
        } catch (IOException exception) {
            sseRepository.remove(key);
            log.info("SSE Exception: {}", exception.getMessage());
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }

        // 중간에 연결이 끊겨서 다시 연결할 때, lastEventId를 통해 기존의 받지못한 이벤트를 받을 수 있도록 할 수 있음.
        // 한번의 알림이나 새로고침을 받으면 알림을 slice로 가져오기 때문에
        // 수신 못한 응답을 다시 보내는 로직을 구현하지 않음.
        return sse;
    }

    /**
     * 특정 유저의 특정 sse 이벤트에 대한 id를 생성한다.
     * 위 조건으로 여러개 정의 될 경우 now 로 구분한다.
     *
     * @param userId
     * @param now
     * @param eventName
     * @return
     */
    private String getEventId(Long userId, LocalDateTime now, SseEventName eventName) {
        return userId + UNDER_SCORE + eventName.getValue() + UNDER_SCORE + now;
    }
}