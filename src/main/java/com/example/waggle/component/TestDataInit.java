package com.example.waggle.component;


import com.example.waggle.domain.board.Media;
import com.example.waggle.domain.board.boardType.Answer;
import com.example.waggle.domain.board.boardType.Question;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;

import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.dto.member.SignUpDto;
import com.example.waggle.repository.board.HashtagRepository;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.board.comment.CommentRepository;
import com.example.waggle.repository.board.comment.ReplyRepository;
import com.example.waggle.repository.member.MemberRepository;

import com.example.waggle.service.member.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInit {

    @Autowired
    private final InitStoryService initStoryService;
    @Autowired
    private final InitQuestionService initQuestionService;

    @PostConstruct
    public void init() {
        initStoryService.initStory();
//        initQuestionService.initQuestion();
//        initQuestionService.initAnswer();
    }

    @Transactional
    @Component
    @RequiredArgsConstructor
    static class InitStoryService {

        private final MemberService memberService;
        private final StoryRepository storyRepository;
        private final MemberRepository memberRepository;
        private final HashtagRepository hashtagRepository;
        private final CommentRepository commentRepository;
        private final ReplyRepository replyRepository;

        public void initStory() {

            List<Member> members = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                SignUpDto signUpDto = SignUpDto.builder()
                        .nickname("nickname" + i)
                        .username("user" + i)
                        .password("12345678")
                        .build();
                MemberDto memberDto = memberService.signUp(signUpDto);
                Member member = memberRepository.findByUsername(memberDto.getUsername()).get();
                members.add(member);
            }

           for(int i=1;i<=5;i++) {
               Story story = Story.builder()
                       .member(members.get(i - 1))
                       .content("content" + i)
                       .thumbnail("https://github.com/suddiyo/suddiyo/assets/88311377/438795ab-0623-438e-bf99-e89c92ff4b4f")
                       .build();
               storyRepository.save(story);
           }
//
//            //hashtag save and link board
//            Hashtag poodle = Hashtag.builder().tag("poodle").build();
//            Hashtag cute = Hashtag.builder().tag("cute").build();
//            Hashtag init = Hashtag.builder().tag("init").build();
//
//            List<Hashtag> hashtags = new ArrayList<>();
//            hashtags.add(poodle);
//            hashtags.add(cute);
//            hashtags.add(init);
//
//            for (Hashtag hashtag : hashtags) {
//                hashtagRepository.save(hashtag);
//                if (hashtag.getTag().equals("init")) {
//                    BoardHashtag.builder().hashtag(hashtag).board(newStory1).build();
//                } else {
//                    BoardHashtag.builder().hashtag(hashtag).board(newStory2).build();
//                }
//            }
//
//            //media save
//            Media media1 = Media.builder().url("choco1/img").board(newStory1).build();
//            Media media2 = Media.builder().url("choco2/img").board(newStory1).build();
        }
    }

    @Transactional
    @Component
    @RequiredArgsConstructor
    static class InitQuestionService {

        private final QuestionRepository questionRepository;
        private final AnswerRepository answerRepository;
        private final HashtagRepository hashtagRepository;
        private final CommentRepository commentRepository;
        private final ReplyRepository replyRepository;
        private final MemberRepository memberRepository;

        public void initQuestion() {

            Member newMember1 = Member.builder()
                    .address("paju")
                    .nickname("jeonghan")
                    .password("wjdgks2972")
                    .phone("1111.2223")
//                    .profileImg("img")
                    .username("hann111").build();
            memberRepository.save(newMember1);

            Question first_question = Question.builder()
                    .content("hello i have a question.")
                    .title("first question")
                    .member(newMember1)
                    .build();
            questionRepository.save(first_question);

            Media.builder().url("www.choco.com").board(first_question).build();

        }

        public void initAnswer() {
            List<Question> hann111 = questionRepository.findByMemberUsername("hann111");
            Answer buildAnswer = Answer.builder()
                    .question(hann111.stream().findFirst().get())
                    .content("i know that")
                    .member(memberRepository.findByUsername("hann123").get())
                    .build();
            answerRepository.save(buildAnswer);
        }

    }
}
