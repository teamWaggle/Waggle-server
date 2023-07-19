package com.example.waggle.component;


import com.example.waggle.domain.board.Media;
import com.example.waggle.domain.board.Story;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;
import com.example.waggle.domain.board.qna.Answer;
import com.example.waggle.domain.board.qna.Question;
import com.example.waggle.domain.member.Member;
import com.example.waggle.repository.board.HashtagRepository;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
import com.example.waggle.repository.board.comment.CommentRepository;
import com.example.waggle.repository.board.comment.ReplyRepository;
import com.example.waggle.repository.member.MemberRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
//        initStoryService.initStory();
//        initQuestionService.initQuestion();
//        initQuestionService.initAnswer();
    }

    @Transactional
    @Component
    @RequiredArgsConstructor
    static class InitStoryService {

        private final StoryRepository storyRepository;
        private final MemberRepository memberRepository;
        private final HashtagRepository hashtagRepository;
        private final CommentRepository commentRepository;
        private final ReplyRepository replyRepository;

        public void initStory() {
            //member save
            Member newMember1 = Member.builder()
                    .address("paju")
                    .nickname("hann")
                    .password("wjdgks2972")
                    .phone("1111.2222")
                    .profileImg("img")
                    .username("hann123").build();
            memberRepository.save(newMember1);

            //board save(not media comments and hashtag)
            Story newStory1 = Story.builder()
                    .content("hi first story. choco is my bro.")
                    .member(newMember1)
                    .thumbnail("choco.img").build();
            storyRepository.save(newStory1);
            Story newStory2 = Story.builder()
                    .content("hi second story. choco is my son.")
                    .member(newMember1)
                    .thumbnail("choco1.img").build();
            storyRepository.save(newStory2);

            //hashtag save and link board
            Hashtag poodle = Hashtag.builder().tag("poodle").build();
            Hashtag cute = Hashtag.builder().tag("cute").build();
            Hashtag init = Hashtag.builder().tag("init").build();

            List<Hashtag> hashtags = new ArrayList<>();
            hashtags.add(poodle);
            hashtags.add(cute);
            hashtags.add(init);

            for (Hashtag hashtag : hashtags) {
                hashtagRepository.save(hashtag);
                if (hashtag.getTag().equals("init")) {
                    BoardHashtag.builder().hashtag(hashtag).board(newStory1).build();
                }else{
                    BoardHashtag.builder().hashtag(hashtag).board(newStory2).build();
                }
            }

            //media save
            Media media1 = Media.builder().url("choco1/img").board(newStory1).build();
            Media media2 = Media.builder().url("choco2/img").board(newStory1).build();
        }
    }
    @Transactional
    @Component
    @RequiredArgsConstructor
    static class InitQuestionService{

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
                    .profileImg("img")
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
            List<Question> hann111 = questionRepository.findByUsername("hann111");
            Answer buildAnswer = Answer.builder()
                    .question(hann111.stream().findFirst().get())
                    .content("i know that")
                    .member(memberRepository.findByUsername("hann123").get())
                    .build();
            answerRepository.save(buildAnswer);
        }

    }
}
