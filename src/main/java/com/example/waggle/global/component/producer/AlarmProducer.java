//package com.example.waggle.global.component.producer;
//
//import com.example.waggle.domain.notification.entity.alarm.AlarmEvent;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class AlarmProducer {
//    private final KafkaTemplate<String, AlarmEvent> kafkaTemplate;
//
//    @Value("${kafka.topic.alarm.name}")
//    private String topicName;
//
//    public void send(AlarmEvent alarmEvent) {
//        kafkaTemplate.send(topicName, alarmEvent);
//        log.info("alarm kafka produce");
//    }
//}
