package com.example.waggle.member.service;

import com.example.waggle.domain.board.service.BoardType;
import com.example.waggle.domain.board.story.entity.Story;
import com.example.waggle.domain.board.story.service.StoryCommandService;
import com.example.waggle.domain.board.story.service.StoryQueryService;
import com.example.waggle.domain.comment.entity.Comment;
import com.example.waggle.domain.comment.entity.Reply;
import com.example.waggle.domain.comment.service.comment.CommentCommandService;
import com.example.waggle.domain.comment.service.comment.CommentQueryService;
import com.example.waggle.domain.comment.service.reply.ReplyCommandService;
import com.example.waggle.domain.comment.service.reply.ReplyQueryService;
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
import com.example.waggle.global.exception.handler.MemberHandler;
import com.example.waggle.web.dto.comment.CommentRequest;
import com.example.waggle.web.dto.global.annotation.withMockUser.WithMockCustomUser;
import com.example.waggle.web.dto.member.MemberRequest;
import com.example.waggle.web.dto.pet.PetRequest;
import com.example.waggle.web.dto.reply.ReplyRequest;
import com.example.waggle.web.dto.story.StoryRequest;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.io.IOException;
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


    private MemberRequest.RegisterDto signUpDto1;
    private MemberRequest.RegisterDto signUpDto2;
    private MemberRequest.RegisterDto signUpDto3;
    private PetRequest.Post petRequest;

    private StoryRequest.Post storyRequest;
    private CommentRequest.Post commentRequest;
    private CommentRequest.Post commentRequest2;
    private ReplyRequest.Post replyRequest;

    private MemberRequest.Put updateDto;


    @BeforeEach
    void beforeEach() {
        // Member 회원가입
        signUpDto1 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임1")
                .address("서울시 광진구")
                .email("asdklfj")
                .phone("010-1234-5678")
                .build();


        signUpDto2 = MemberRequest.RegisterDto.builder()
                .username("member2")
                .password("12345678")
                .nickname("닉네임2")
                .address("서울시 광진구")
                .email("sdlfkjsalkfj")
                .phone("010-1234-5678")
                .build();

        signUpDto3 = MemberRequest.RegisterDto.builder()
                .username("member1")
                .password("12345678")
                .nickname("닉네임3")
                .address("서울시 광진구")
                .email("wjdgks")
                .phone("010-1234-5678")
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
        memberService.signUp(signUpDto1);

        Member member = memberQueryService.getMemberByUsername(signUpDto1.getUsername());

        assertThat(member.getUsername()).isEqualTo(signUpDto1.getUsername());
    }

    @Test
    public void 중복_회원_예외() {
        // savedMemberDto1와 savedMemberDto3의 username 중복 ➡︎ IllegalArgumentException 발생해야 함.
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);
        Assertions.assertThrows(MemberHandler.class, () -> memberService.signUp(signUpDto3));
    }

    @Test
    @Disabled
    public void signIn() {
        memberService.signUp(signUpDto1);
        memberService.signUp(signUpDto2);

        MemberRequest.LoginDto signInDto = MemberRequest.LoginDto.builder()
                .username("member1")
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
        assertThat(responseEntity.getBody()).isEqualTo(signInDto.getUsername());
    }

    @Test
    @WithMockCustomUser
    void member_Information_update() {
        //given
        Long aLong = memberService.signUp(signUpDto1);
        //when
        Long aLong1 = memberService.updateMemberInfo(updateDto);
        Member memberByUsername = memberQueryService.getMemberByUsername("member1");
        //then
        assertThat(memberByUsername.getNickname()).isEqualTo(updateDto.getNickname());
    }

    @Test
    @WithMockCustomUser
    void member_delete_hardReset() throws IOException {
        //given
        Long member1 = memberService.signUp(signUpDto1);
        Long member2 = memberService.signUp(signUpDto2);
        Long story = storyCommandService.createStory(storyRequest, null);
        Long comment = commentCommandService.createComment(story, commentRequest, BoardType.STORY);
        replyCommandService.createReply(comment, replyRequest);
        commentCommandService.createCommentByUsername(story, commentRequest2, "member2", BoardType.STORY);
        followCommandService.follow("member2");
        Long pet = petService.createPet(petRequest);

        //when
        memberService.deleteMember();

        //then
        List<Story> stories = storyQueryService.getStories();
        List<Comment> comments = commentQueryService.getComments(story);
        List<Reply> replies = replyQueryService.getReplies(comment);
        List<Follow> follows = followQueryService.getFollowings("member1");
        List<Pet> pets = petQueryService.getPetsByUsername("member1");

        assertThat(stories.size()).isEqualTo(0);
        assertThat(comments.size()).isEqualTo(0);
        assertThat(replies.size()).isEqualTo(0);
        assertThat(follows.size()).isEqualTo(0);
        assertThat(pets.size()).isEqualTo(0);

    }
}