package com.example.waggle.domain.notification.repository;

import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.notification.entity.alarm.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByCalledMember(Member member);
}
