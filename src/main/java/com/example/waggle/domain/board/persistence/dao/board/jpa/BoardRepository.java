package com.example.waggle.domain.board.persistence.dao.board.jpa;

import com.example.waggle.domain.board.persistence.dao.board.querydsl.BoardQueryRepository;
import com.example.waggle.domain.board.persistence.entity.Board;
import com.example.waggle.domain.member.persistence.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryRepository {

    List<Board> findAllByMember(Member member);

    List<Board> findAllByMemberIn(List<Member> memberList);

}
