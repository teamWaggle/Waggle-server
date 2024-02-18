package com.example.waggle.member.service;

import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.entity.Reply;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.conversation.service.comment.CommentQueryService;
import com.example.waggle.domain.conversation.service.reply.ReplyCommandService;
import com.example.waggle.domain.conversation.service.reply.ReplyQueryService;
import com.example.waggle.domain.follow.entity.Follow;
import com.example.waggle.domain.follow.service.FollowCommandService;
import com.example.waggle.domain.follow.service.FollowQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.domain.member.repository.MemberRepository;
import com.example.waggle.domain.member.service.MemberCommandService;
import com.example.waggle.domain.member.service.MemberQueryService;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.pet.service.PetCommandService;
import com.example.waggle.domain.pet.service.PetQueryService;
import com.example.waggle.global.component.DatabaseCleanUp;
import com.example.waggle.web.dto.comment.CommentRequest;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.pet.PetRequest;
import com.example.waggle.web.dto.reply.ReplyRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class MemberServiceImplTest {
    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberCommandService memberService;
    @Autowired
    MemberQueryService memberQueryService;
    @Autowired
    StoryCommandService storyCommandService;
    @Autowired
    StoryQueryService storyQueryService;
    @Autowired
    CommentCommandService commentCommandService;
    @Autowired
    CommentQueryService commentQueryService;
    @Autowired
    ReplyCommandService replyCommandService;
    @Autowired
    ReplyQueryService replyQueryService;
    @Autowired
    FollowCommandService followCommandService;
    @Autowired
    FollowQueryService followQueryService;
    @Autowired
    PetCommandService petService;
    @Autowired
    PetQueryService petQueryService;
    @Autowired
    TestRestTemplate testRestTemplate;
    @LocalServerPort
    int randomServerPort;
    @Autowired
    EntityManager entityManager;


    private MemberRequest.AccessDto signUpDto1;
    private MemberRequest.AccessDto signUpDto2;
    private MemberRequest.AccessDto signUpDto3;
    private PetRequest.Post petRequest;

    private StoryRequest.Post storyRequest;
    private CommentRequest.Post commentRequest;
    private CommentRequest.Post commentRequest2;
    private ReplyRequest.Post replyRequest;

    private MemberRequest.Put updateDto;


    @BeforeEach
    void beforeEach() {
        // Member 회원가입
        signUpDto1 = MemberRequest.AccessDto.builder()
                .password("12345678")
                .email("asdklfj")
                .build();


        signUpDto2 = MemberRequest.AccessDto.builder()
                .password("12345678")
                .email("sdlfkjsalkfj")
                .build();

        signUpDto3 = MemberRequest.AccessDto.builder()
                .password("12345678")
                .email("asdklfj")
                .build();

        petRequest = PetRequest.Post.builder().name("chococococo").build();

        updateDto = MemberRequest.Put.builder()
                .password("34567898765")
                .nickname("hann.o_i")
                .build();

        storyRequest = StoryRequest.Post.builder()
                .content("hi")
                .build();
        commentRequest = CommentRequest.Post.builder()
                .content("hi this is comment")
                .build();
        commentRequest2 = CommentRequest.Post.builder()
                .content("member2 write comment")
                .build();

        replyRequest = ReplyRequest.Post.builder()
                .content("hi this is reply")
                .build();
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    public void signUp() {
        Long memberId = memberService.signUp(signUpDto1);

        Member member = memberQueryService.getMemberById(memberId);

        assertThat(member.getEmail()).isEqualTo(signUpDto1.getEmail());
    }

//    @Test
//    public void 중복_회원_예외() {
//        // savedMemberDto1와 savedMemberDto3의 username 중복 ➡︎ IllegalArgumentException 발생해야 함.
//        memberService.signUp(signUpDto1);
//        memberService.signUp(signUpDto2);
//        Assertions.assertThrows(MemberHandler.class, () -> memberService.signUp(signUpDto3));
//    }

    @Test
    @Disabled
    public void signIn() {
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        MemberRequest.AccessDto signInDto = MemberRequest.AccessDto.builder()
                .email("member1")
                .password("12345678").build();

        // 로그인 요청

        // HttpHeaders 객체 생성 및 토큰 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        // API 요청 설정
        String url = "http://localhost:" + randomServerPort + "/member/test";
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(httpHeaders), String.class);
        log.info("responseEntity = {}", responseEntity);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(signInDto.getEmail());
    }

    @Test
    void member_Information_update() {
        //given
        Long aLong = memberService.signUp(signUpDto1);
        Member member1 = memberQueryService.getMemberById(aLong);
        //when
        Long aLong1 = memberService.updateMemberInfo(member1.getUsername(), updateDto);
        Member member = memberQueryService.getMemberById(aLong);
        Member memberByUsername = memberQueryService.getMemberByUsername(member.getUsername());
        //then
        assertThat(memberByUsername.getNickname()).isEqualTo(updateDto.getNickname());
    }

    @Test
    // pass
    @Disabled
    void member_delete_hardReset() {
        //given
        Long member1 = memberService.signUp(signUpDto1);
        Long member2 = memberService.signUp(signUpDto2);
        Member A = memberQueryService.getMemberById(member1);
        Member B = memberQueryService.getMemberById(member2);

        Long story = storyCommandService.createStoryByUsername(storyRequest, null, A.getUsername());
        Long comment = commentCommandService.createCommentByUsername(story, commentRequest, A.getUsername());
        replyCommandService.createReplyByUsername(comment, replyRequest, A.getUsername());
        commentCommandService.createCommentByUsername(story, commentRequest2, A.getUsername());
        followCommandService.follow(A.getUsername(), B.getUsername());
        Long pet = petService.createPetByUsername(petRequest, A.getUsername());
        Member member = memberQueryService.getMemberByUsername(A.getUsername());

        //when
        memberService.deleteMember(member1);

        //then
        List<Story> stories = storyQueryService.getStories();
        List<Comment> comments = commentQueryService.getComments(story);
        List<Reply> replies = replyQueryService.getReplies(comment);
        List<Follow> follows = followQueryService.getFollowings(A.getId());
        List<Pet> pets = petQueryService.getPetsByUsername(A.getUsername());

        assertThat(stories.size()).isEqualTo(0);
        assertThat(comments.size()).isEqualTo(0);
        assertThat(replies.size()).isEqualTo(0);
        assertThat(follows.size()).isEqualTo(0);
        assertThat(pets.size()).isEqualTo(0);

    }
}