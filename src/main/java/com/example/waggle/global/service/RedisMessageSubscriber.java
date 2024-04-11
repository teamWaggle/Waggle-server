package com.example.waggle.global.service;

import com.example.waggle.domain.notification.entity.sse.SseEventName;
import com.example.waggle.domain.notification.entity.sse.SseRepositoryKeyRule;
import com.example.waggle.domain.notification.repository.SseRepository;
import com.example.waggle.global.exception.GeneralException;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisMessageSubscriber implements MessageListener {
    private static final String UNDER_SCORE = "_";
    private final SseRepository sseRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Redis Pub/Sub message received: {}", message.toString());
        String[] strings = message.toString().split(UNDER_SCORE);
        Long userId = Long.parseLong(strings[0]);
        SseEventName eventName = SseEventName.getEnumFromValue(strings[1]);
        String keyPrefix = new SseRepositoryKeyRule(userId, eventName, null)
                .toCompleteKeyWhichSpecifyOnlyOneValue();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(strings[2]);
        stringBuilder.append(UNDER_SCORE);
        stringBuilder.append(strings[3]);

        LocalDateTime now = TimeUtil.nowWithoutNano();

        sseRepository.getKeyListByKeyPrefix(keyPrefix).forEach(key -> {
            SseEmitter emitter = sseRepository.get(key).get();
            try {
                emitter.send(SseEmitter.event()
                        .id(getEventId(userId, now, eventName))
                        .name(eventName.getValue())
                        .data(stringBuilder.toString()));
            } catch (IOException e) {
                sseRepository.remove(key);
                log.error("SSE send error", e);
                throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
            }
        });
    }

    private String getEventId(Long userId, LocalDateTime now, SseEventName eventName) {
        return userId + UNDER_SCORE + eventName.getValue() + UNDER_SCORE + now;
    }
}
