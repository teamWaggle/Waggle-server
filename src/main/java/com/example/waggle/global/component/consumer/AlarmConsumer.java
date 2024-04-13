//package com.example.waggle.global.component.consumer;
//
//import com.example.waggle.domain.notification.entity.alarm.AlarmEvent;
//import com.example.waggle.domain.notification.service.AlarmService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class AlarmConsumer {
//    private final AlarmService alarmService;
//
//    /**
//     * offset을 최신으로 설정.*
//     *
//     * @param alarmEvent
//     * @param ack
//     */
//    @KafkaListener(topics = "${kafka.topic.alarm.name}", groupId = "${kafka.consumer.alarm.rdb-group-id}",
//            containerFactory = "kafkaListenerContainerFactoryRDB")
//    public void createAlarmInRDBConsumerGroup(@Payload AlarmEvent alarmEvent, Acknowledgment ack) {
//        log.debug("createAlarmInRDBConsumerGroup");
//        alarmService.createAlarm(alarmEvent.getMemberId(), alarmEvent.getType(), alarmEvent.getArgs());
//        ack.acknowledge();
//    }
//
//    @KafkaListener(topics = "${kafka.topic.alarm.name}", groupId = "${kafka.consumer.alarm.redis-group-id}",
//            containerFactory = "kafkaListenerContainerFactoryRedis")
//    public void redisPublishConsumerGroup(@Payload AlarmEvent alarmEvent, Acknowledgment ack) {
//        log.debug("redisPublishConsumerGroup");
//        alarmService.send(alarmEvent);
//        ack.acknowledge();
//    }
//}
