package com.example.waggle.repository.board;

import com.example.waggle.domain.board.Recommend;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.member.Member;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RecommendRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RecommendRepository recommendRepository;
    @Autowired
    private StoryRepository storyRepository;

    Story story1;
    //who write board
    Member member;
    //who recommend board x 2
    Member member1;
    Member member2;

    // member made(2) -> story made -> recommend -> counting

    @BeforeEach
    void setting() {
        member = Member.builder()
                .username("username")
                .password("password").build();
        member1 = Member.builder()
                .username("username1")
                .password("password1").build();
        member2 = Member.builder()
                .username("username2")
                .password("password2").build();
        memberRepository.save(member);
        memberRepository.save(member1);
        memberRepository.save(member2);

        story1 = Story.builder().content("chochoco").member(member).build();
        storyRepository.save(story1);

        Recommend recommend1 = Recommend.builder().board(story1).member(member1).build();
        Recommend recommend2 = Recommend.builder().board(story1).member(member2).build();

        recommendRepository.save(recommend1);
        recommendRepository.save(recommend2);
    }
    @Test
    @Transactional
    void countByBoardId() {

        int countRecommend = recommendRepository.countByBoardId(story1.getId());
        assertThat(countRecommend).isEqualTo(2);
    }

    @Test
    @Transactional
    void existsByMemberIdAndBoardId() {
        Long member1Id = member1.getId();
        Long memberId = member.getId();
        Long storyId = story1.getId();

        boolean expectFalse = recommendRepository.existsByMemberIdAndBoardId(memberId, storyId);
        boolean expectTrue = recommendRepository.existsByMemberIdAndBoardId(member1Id, storyId);

        assertThat(expectFalse).isFalse();
        assertThat(expectTrue).isTrue();


    }

    @Test
    @Transactional
    void findRecommendByMemberIdAndBoardId() {
        Long member1Id = member1.getId();
        Long storyId = story1.getId();

        Optional<Recommend> recommendByMemberIdAndBoardId = recommendRepository.findRecommendByMemberIdAndBoardId(member1Id, storyId);

        assertThat(recommendByMemberIdAndBoardId.isPresent()).isTrue();
        assertThat(recommendByMemberIdAndBoardId.get().getMember().getUsername()).isEqualTo("username1");
    }
}