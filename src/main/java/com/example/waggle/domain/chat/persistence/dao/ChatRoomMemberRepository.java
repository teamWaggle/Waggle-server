package com.example.waggle.domain.chat.persistence.dao;

import com.example.waggle.domain.chat.persistence.entity.ChatRoomMember;
import com.example.waggle.domain.member.persistence.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    List<ChatRoomMember> findAllByMemberId(Long memberId);

    void deleteAllByMember(Member member);

}