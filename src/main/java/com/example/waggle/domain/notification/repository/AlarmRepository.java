package com.example.waggle.domain.notification.repository;

import com.example.waggle.domain.notification.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
