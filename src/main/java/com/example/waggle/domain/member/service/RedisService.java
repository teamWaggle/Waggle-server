package com.example.waggle.domain.member.service;

import com.example.waggle.domain.recommend.entity.RecommendationHashKey;
import com.example.waggle.domain.recommend.entity.RecommendationSetKey;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;
    private static String recommendSetKeyPrefix = "set:recommend:board:";
    private static String hashKeyPrefix = "hash:board:";

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
        values.set(String.valueOf(memberId), "initRecommend");
    }

    public boolean existInitRecommend(Long memberId) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        if (values.get(String.valueOf(memberId)) == null) {
            return false;
        }
        return true;
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

    //for newly insert recommend
    public void setIsRecommend(Long boardId, Long memberId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        RecommendationSetKey setKey = buildSetKey(boardId, memberId);
        setOperations.add(setKey.getKey(), setKey.getValue());
    }

    public Set<String> getIsRecommend(Long boardId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.members(recommendSetKeyPrefix + boardId);
    }

    public Set<String> getKeysByPattern(String pattern) {
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

    public boolean existIsRecommend(Long boardId, Long memberId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        RecommendationSetKey setKey = buildSetKey(boardId, memberId);
        return setOperations.isMember(setKey.getKey(), setKey.getValue());
    }

    public void deleteIsRecommend(Long boardId, Long memberId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        RecommendationSetKey setKey = buildSetKey(boardId, memberId);
        setOperations.remove(setKey.getKey(), setKey.getValue());
    }

    private RecommendationHashKey buildHashKey(Long boardId) {
        String key = hashKeyPrefix + boardId;
        String hashKey = "recommendation";
        return RecommendationHashKey.builder()
                .key(key)
                .hashKey(hashKey)
                .build();
    }

    private RecommendationSetKey buildSetKey(Long boardId, Long memberId) {
        String key = recommendSetKeyPrefix + boardId;
        String value = String.valueOf(memberId);
        return RecommendationSetKey.builder()
                .key(key)
                .value(value)
                .build();
    }

}