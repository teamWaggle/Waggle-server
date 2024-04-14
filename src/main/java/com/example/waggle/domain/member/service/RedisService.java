package com.example.waggle.domain.member.service;

import com.example.waggle.domain.notification.entity.alarm.AlarmEvent;
import com.example.waggle.domain.recommend.entity.RecommendationHashKey;
import com.example.waggle.domain.recommend.entity.RecommendationSetKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;
    private static String recommendMemberKeyPrefix = "set:recommend:member:";
    //    private static String recommendBoardKeyPrefix = "set:recommend:board:";
    private static String hashKeyPrefix = "hash:board:";
    private static String initKeyPrefix = "init:member:";
    private static final String UNDER_SCORE = "_";

    // key-value 설정
    public void setValue(String token, String username) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, username, Duration.ofDays(7));
    }

    // key 값으로 value 가져오기
    public String getValue(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(token);
    }

    public void deleteValue(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            // "Bearer " 접두사 제거
            token = token.substring(7);
        }
        redisTemplate.delete(token);
    }

    public void setValueWithExpiration(String token, String value, long expireInMillis) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, value, Duration.ofMillis(expireInMillis));
    }

    public void initRecommend(Long memberId) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        String key = initKeyPrefix + memberId;
        values.set(key, "initRecommend");
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String memberKey = recommendMemberKeyPrefix + memberId;
        setOperations.add(memberKey, String.valueOf(0L));
    }

    public boolean existInitRecommend(Long memberId) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        if (values.get(initKeyPrefix + memberId) == null) {
            return false;
        }
        return true;
    }

    public void clearInitRecommend() {
        Set<String> initRecommendKey = getKeysByPattern(initKeyPrefix + "*");
        redisTemplate.delete(initRecommendKey);
    }

    //for recommend count & view count
    public void setRecommendCnt(Long boardId, Long value) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        RecommendationHashKey recommendationHashKey = buildHashKey(boardId);
        hashOperations.put(recommendationHashKey.getKey(), recommendationHashKey.getHashKey(), value);
    }

    public void incrementRecommendCnt(Long boardId) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        RecommendationHashKey recommendationHashKey = buildHashKey(boardId);
        hashOperations.increment(recommendationHashKey.getKey(), recommendationHashKey.getHashKey(), 1L);
    }

    public void decrementRecommendCnt(Long boardId) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        RecommendationHashKey recommendationHashKey = buildHashKey(boardId);
        hashOperations.increment(recommendationHashKey.getKey(), recommendationHashKey.getHashKey(), -1L);
    }

    public Long getRecommendCnt(Long boardId) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        RecommendationHashKey recommendationHashKey = buildHashKey(boardId);
        log.info("class = {}", hashOperations.get(recommendationHashKey.getKey(), recommendationHashKey.getHashKey()).getClass());
        return hashOperations.get(recommendationHashKey.getKey(), recommendationHashKey.getHashKey());
    }

    public boolean existRecommendCnt(Long boardId) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        RecommendationHashKey recommendationHashKey = buildHashKey(boardId);
        return hashOperations.hasKey(recommendationHashKey.getKey(), recommendationHashKey.getHashKey());
    }

    public void deleteRecommendCnt(Long boardId) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        RecommendationHashKey recommendationHashKey = buildHashKey(boardId);
        hashOperations.delete(recommendationHashKey.getKey(), recommendationHashKey.getHashKey());
    }

    public void clearRecommendCnt() {
        Set<String> recommendCntKey = getKeysByPattern(hashKeyPrefix + "*");
        redisTemplate.delete(recommendCntKey);
    }

    //for newly insert recommend
    public void setRecommend(Long memberId, Long boardId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        RecommendationSetKey memberSetKey = buildMemberSetKey(memberId, boardId);
//        RecommendationSetKey boardSetKey = buildBoardSetKey(boardId, memberId);
        setOperations.add(memberSetKey.getKey(), memberSetKey.getValue());
//        setOperations.add(boardSetKey.getKey(), boardSetKey.getValue());
    }

    private Set<String> getKeysByPattern(String pattern) {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).build();
        Cursor<byte[]> cursor = connection.scan(scanOptions);

        Set<String> keys = new HashSet<>();
        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }

        try {
            cursor.close();
        } catch (Exception e) {
            // Handle exception
        }

        return keys;
    }

    public List<Long> getRecommendedBoardList(Long memberId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String key = recommendMemberKeyPrefix + memberId;
        Set<String> boards = setOperations.members(key);
        return boards.stream()
                .map(board -> Long.valueOf(board)).collect(Collectors.toList());
    }

//    public List<Long> getRecommendingMemberList(Long boardId) {
//            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
//            String key = recommendBoardKeyPrefix + boardId;
//            Set<String> members = setOperations.members(key);
//        return members.stream()
//                .map(member -> Long.valueOf(member)).collect(Collectors.toList());
//    }

    public List<Long> getAllRecommendingMemberList() {
        Set<String> keysByPattern = getKeysByPattern(recommendMemberKeyPrefix + "*");
        return keysByPattern.stream()
                .map(key -> Long.valueOf(key.substring(recommendMemberKeyPrefix.length())))
                .collect(Collectors.toList());
    }

    public boolean existRecommend(Long memberId, Long boardId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        RecommendationSetKey setKey = buildMemberSetKey(memberId, boardId);
        return setOperations.isMember(setKey.getKey(), setKey.getValue());
    }

    public void deleteRecommend(Long memberId, Long boardId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        RecommendationSetKey memberSetKey = buildMemberSetKey(memberId, boardId);
//        RecommendationSetKey boardSetKey = buildBoardSetKey(boardId, memberId);
        setOperations.remove(memberSetKey.getKey(), memberSetKey.getValue());
//        setOperations.remove(boardSetKey.getKey(), boardSetKey.getValue());
    }

    public void clearRecommend() {
        Set<String> memberKeys = getKeysByPattern(recommendMemberKeyPrefix + "*");
//        Set<String> boardKeys = getKeysByPattern(recommendBoardKeyPrefix + "*");
        redisTemplate.delete(memberKeys);
//        redisTemplate.delete(boardKeys);
    }

    private RecommendationHashKey buildHashKey(Long boardId) {
        String key = hashKeyPrefix + boardId;
        String hashKey = "recommendation";
        return RecommendationHashKey.builder()
                .key(key)
                .hashKey(hashKey)
                .build();
    }

    private RecommendationSetKey buildMemberSetKey(Long memberId, Long boardId) {
        String key = recommendMemberKeyPrefix + memberId;
        String value = String.valueOf(boardId);
        return RecommendationSetKey.builder()
                .key(key)
                .value(value)
                .build();
    }

//    private RecommendationSetKey buildBoardSetKey(Long boardId, Long memberId) {
//        String key = recommendBoardKeyPrefix + memberId;
//        String value = String.valueOf(memberId);
//        return RecommendationSetKey.builder()
//                .key(key)
//                .value(value)
//                .build();
//    }

    public void publishMessage(AlarmEvent alarmEvent) {
        redisTemplate.convertAndSend(alarmEvent.getEventName().getValue(), getRedisPubMessage(alarmEvent));
    }

    private String getRedisPubMessage(AlarmEvent alarmEvent) {
        return alarmEvent.getMemberId()
                + UNDER_SCORE + alarmEvent.getEventName().getValue()
                + UNDER_SCORE + alarmEvent.getArgs().getCallingMemberUserUrl()
                + UNDER_SCORE + alarmEvent.getType().getAlarmContent();
    }

}