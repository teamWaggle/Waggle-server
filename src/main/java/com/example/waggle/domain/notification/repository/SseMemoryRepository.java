package com.example.waggle.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;

@Repository
public class SseMemoryRepository implements SseRepository {
    @Override
    public void put(String key, SseEmitter sseEmitter) {

    }

    @Override
    public Optional<SseEmitter> get(String key) {
        return Optional.empty();
    }

    @Override
    public List<SseEmitter> getListByKeyPrefix(String keyPrefix) {
        return null;
    }

    @Override
    public List<String> getKeyListByKeyPrefix(String keyPrefix) {
        return null;
    }

    @Override
    public void remove(String key) {

    }
}
