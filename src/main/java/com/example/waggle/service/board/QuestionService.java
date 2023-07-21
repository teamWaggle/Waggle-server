package com.example.waggle.service.board;

import com.example.waggle.domain.board.Media;
import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.domain.board.comment.MemberMention;
import com.example.waggle.domain.board.comment.Reply;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;
import com.example.waggle.domain.board.qna.Answer;
import com.example.waggle.domain.board.qna.Question;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.*;
import com.example.waggle.dto.member.MemberDto;
import com.example.waggle.repository.board.HashtagRepository;
import com.example.waggle.repository.board.boardtype.AnswerRepository;
import com.example.waggle.repository.board.boardtype.QuestionRepository;
import com.example.waggle.repository.board.comment.CommentRepository;
import com.example.waggle.repository.board.comment.ReplyRepository;
import com.example.waggle.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final HashtagRepository hashtagRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;

    /**
     * 조회는 entity -> dto과정을,
     * 저장은 dto -> entity 과정을 거치도록 한다.(기본)
     */

    //===========1. 조회===========

    //1.1.1 전체 조회 -> 후에 시간 순으로 조회
    //P1. 지금은 story -> storySimpleDto로 변경하지만 조회를 dto로 변경하면 query양이 적어질 것이다.
    //P2. paging 필수
    @Transactional(readOnly = true)
    public List<QuestionSimpleDto> findAllQuestion() {
        List<Question> allQuestion = questionRepository.findAll();
        List<QuestionSimpleDto> simpleQuestions = new ArrayList<>();
        for (Question question : allQuestion) {
            simpleQuestions.add(QuestionSimpleDto.toDto(question));
        }
        return simpleQuestions;
    }

    //1.1.2 회원 정보에 따른 전체 조회
    @Transactional(readOnly = true)
    public List<QuestionSimpleDto> findAllQuestionByMember(String username) {
        Optional<Member> MemberByUsername = memberRepository.findByUsername(username);
        if (MemberByUsername.isEmpty()) {
            log.info("can't find user!");
            // error message 출력
        }
        List<Question> questionsByUsername = questionRepository.findByMemberUsername(username);
        List<QuestionSimpleDto> simpleQuestions = new ArrayList<>();
        for (Question question : questionsByUsername) {
            simpleQuestions.add(QuestionSimpleDto.toDto(question));
        }

        return simpleQuestions;
    }

    //1.2 낱개 조회
    @Transactional(readOnly = true)
    public QuestionDto findQuestionByBoardId(Long id) {
        Optional<Question> questionById = questionRepository.findById(id);

        if (questionById.isEmpty()) {
            //error and return null
        }
        return QuestionDto.toDto(questionById.get());
    }

//    @Transactional(readOnly = true)
//    public QuestionDto findQuestionByTitleAndUsername(String title, String username) {
//        Optional<Member> MemberByUsername = memberRepository.findByUsername(username);
//        if (MemberByUsername.isEmpty()) {
//            log.info("can't find user!");
//            // error message 출력
//        }
//        List<Question> questionsByUsername = questionRepository.findByUsername(username);
//        if (questionsByUsername.isEmpty()) {
//
//        }
//        List<Question> collect = questionsByUsername.stream()
//                .filter(q -> q.getTitle().equals(title)).collect(Collectors.toList());
//
//        if (collect.stream().findFirst().isPresent()) {
//            QuestionDto questionDto = QuestionDto.toDto(collect.stream().findFirst().get());
//            return questionDto;
//        }
//        return null;
//    }

    //2. ===========저장===========

    //2.1 question 저장(media, hashtag 포함)

    public void saveQuestion(QuestionDto saveQuestionDto) {
        Question question = saveQuestionDto.toEntity();
        Optional<Member> byUsername = memberRepository.findByUsername(saveQuestionDto.getUsername());
        if (byUsername.isEmpty()) {
            //error message
        }
        //hashtag 저장
        if(!saveQuestionDto.getHashtags().isEmpty()){
            for (String hashtag : saveQuestionDto.getHashtags()) {
                saveHashtagInQuestion(question,hashtag);
            }
        }
        //media 저장
        if (!saveQuestionDto.getMedias().isEmpty()) {
            for (String media : saveQuestionDto.getMedias()) {
                Media buildMedia = Media.builder().url(media).board(question).build();
            }
        }
    }

    //2.2 question_comment 저장
    public void saveCommentInQuestion(CommentDto commentDto, QuestionDto questionDto, MemberDto memberDto) {
        Optional<Question> questionById = questionRepository.findById(questionDto.getId());
        Optional<Member> memberByUsername = memberRepository.findByUsername(memberDto.getUsername());

        int lastOrder = commentRepository.findLastOrderByBoardId(questionDto.getId());

        if (questionById.isPresent() && memberByUsername.isPresent()) {
            Comment buildComment = Comment.builder()
                    .orders(++lastOrder)
                    .content(commentDto.getContent())
                    .board(questionById.get())
                    .member(memberByUsername.get())
                    .build();
            commentRepository.save(buildComment);
        }
    }
    //2.3 question_comment_reply 저장
    public void saveReplyInQuestion(ReplyDto replyDto, CommentDto commentDto, MemberDto memberDto) {
        Optional<Comment> commentById = commentRepository.findById(commentDto.getId());
        Optional<Member> memberByUsername = memberRepository.findByUsername(memberDto.getUsername());
        //order set
        int lastOrder = replyRepository.findLastOrderByCommentId(commentDto.getId());
        //mention set
        List<MemberMention> memberMentions = new ArrayList<>();
        for (String mentionMember : replyDto.getMentionMembers()) {
            memberMentions.add(MemberMention.builder().username(mentionMember).build());
        }

        if (commentById.isPresent() && memberByUsername.isPresent()) {
            Reply buildReply = Reply.builder()
                    .orders(++lastOrder)
                    .content(replyDto.getContent())
                    .comment(commentById.get())
                    .member(memberByUsername.get())
                    .mentionedMembers(memberMentions)
                    .build();

            replyRepository.save(buildReply);
        }
    }
    //2.4 answer 저장(media, hashtag 포함)
    public void saveAnswer(AnswerDto saveAnswerDto, QuestionDto questionDto) {
        Optional<Question> questionById = questionRepository.findById(questionDto.getId());

        if (questionById.isPresent()) {
            Answer answer = saveAnswerDto.toEntity();

            Optional<Member> byUsername = memberRepository.findByUsername(saveAnswerDto.getUsername());
            if (byUsername.isEmpty()) {
                //error message
            }
            //hashtag 저장
            if(!saveAnswerDto.getHashtags().isEmpty()){
                for (String hashtag : saveAnswerDto.getHashtags()) {
                    saveHashtagInAnswer(answer,hashtag);
                }
            }
            //media 저장
            if (!saveAnswerDto.getMedias().isEmpty()) {
                for (String media : saveAnswerDto.getMedias()) {
                    Media.builder().url(media).board(answer).build();
                }
            }
            //link relation method
            questionById.get().addAnswer(answer);
        }
    }

    //2.5 answer_comment 저장
    public void saveCommentInAnswer(CommentDto commentDto, AnswerDto answerDto, MemberDto memberDto) {
        Optional<Answer> answerById = answerRepository.findById(answerDto.getId());
        Optional<Member> memberByUsername = memberRepository.findByUsername(memberDto.getUsername());

        int lastOrder = commentRepository.findLastOrderByBoardId(answerDto.getId());

        if (answerById.isPresent() && memberByUsername.isPresent()) {
            Comment buildComment = Comment.builder()
                    .orders(++lastOrder)
                    .content(commentDto.getContent())
                    .board(answerById.get())
                    .member(memberByUsername.get())
                    .build();
            commentRepository.save(buildComment);
        }
    }

    //2.6 answer_comment_reply 저장
    public void saveReplyInAnswer(ReplyDto replyDto, CommentDto commentDto, MemberDto memberDto) {
        Optional<Comment> commentById = commentRepository.findById(commentDto.getId());
        Optional<Member> memberByUsername = memberRepository.findByUsername(memberDto.getUsername());

        int lastOrder = replyRepository.findLastOrderByCommentId(commentDto.getId());
        //mention set
        List<MemberMention> memberMentions = new ArrayList<>();
        for (String mentionMember : replyDto.getMentionMembers()) {
            memberMentions.add(MemberMention.builder().username(mentionMember).build());
        }

        if (commentById.isPresent() && memberByUsername.isPresent()) {
            Reply buildReply = Reply.builder()
                    .orders(++lastOrder)
                    .content(replyDto.getContent())
                    .comment(commentById.get())
                    .member(memberByUsername.get())
                    .mentionedMembers(memberMentions)
                    .build();

            replyRepository.save(buildReply);
        }
    }

    //3. ===========수정===========

    //3.1 question 수정(media, hashtag 포함)
    public void changeQuestion(QuestionDto questionDto) {
        Optional<Question> questionById = questionRepository.findById(questionDto.getId());
        if (questionById.isPresent()) {
            questionById.get().changeQuestion(questionDto.getContent(),questionDto.getTitle());

            //delete(media)
            questionById.get().getMedias().clear();

            //newly insert data(media)
            for (String media : questionDto.getMedias()) {
                Media changeMedia = Media.builder().url(media).board(questionById.get()).build();
            }

            //delete connecting relate (boardHashtag)
            questionById.get().getBoardHashtags().clear();

            //newly insert data(hashtag, boardHashtag)
            for (String hashtag : questionDto.getHashtags()) {
                saveHashtagInQuestion(questionById.get(), hashtag);
            }
        }
    }
    //3.2 answer 수정(media, hashtag 포함)
    public void changeAnswer(AnswerDto answerDto) {
        Optional<Answer> answerById = answerRepository.findById(answerDto.getId());
        if (answerById.isPresent()) {
            answerById.get().changeAnswer(answerDto.getContent());

            //delete(media)
            answerById.get().getMedias().clear();

            //newly insert data(media)
            for (String media : answerDto.getMedias()) {
                Media.builder().url(media).board(answerById.get()).build();
            }

            //delete connecting relate (boardHashtag)
            answerById.get().getBoardHashtags().clear();

            //newly insert data(hashtag, boardHashtag)
            for (String hashtag : answerDto.getHashtags()) {
                saveHashtagInAnswer(answerById.get(), hashtag);
            }
        }
    }

    //3.3 question(answer)_comment 수정
    public void changeComment(CommentDto commentDto) {
        Optional<Comment> commentById = commentRepository.findById(commentDto.getId());
        if (commentById.isPresent()) {
            commentById.get().changeContent(commentDto.getContent());
        }
    }

    //3.4 question(answer)_comment_reply 수정
    public void changeReply(ReplyDto replyDto) {
        Optional<Reply> replyById = replyRepository.findById(replyDto.getId());
        if (replyById.isPresent()) {
            replyById.get().changeContent(replyDto.getContent());
            //mention member setting
            //delete older data
            replyById.get().getMemberMentions().clear();
            //save mention entity
            List<MemberMention> memberMentions = new ArrayList<>();
            for (String mentionMember : replyDto.getMentionMembers()) {
                memberMentions.add(MemberMention.builder().username(mentionMember).build());
            }
            //link relation -> entity save(cascade)
            for (MemberMention memberMention : memberMentions) {
                replyById.get().addMemberMention(memberMention);
            }
        }
    }


    //4. ===========삭제(취소)===========

    //4.1 question 삭제(media, hashtag 포함)
    public void deleteQuestion(Long id) {
        Optional<Question> questionById = questionRepository.findById(id);
        if (questionById.isPresent()) {
            questionRepository.delete(questionById.get());
        }
    }

    //4.2 question_comment 삭제
    public void deleteComment(Long id) {
        Optional<Comment> commentById = commentRepository.findById(id);
        if (commentById.isPresent()) {
            commentRepository.delete(commentById.get());
        }
    }

    //4.3 question_comment_reply 삭제
    public void deleteReply(Long id) {
        Optional<Reply> replyById = replyRepository.findById(id);
        if (replyById.isPresent()) {
            replyRepository.delete(replyById.get());
        }
    }

    //4.4 answer 삭제(media, hashtag 포함)
    public void deleteAnswer(Long id) {
        Optional<Answer> answerById = answerRepository.findById(id);
        if (answerById.isPresent()) {
            answerRepository.delete(answerById.get());
        }
    }

    //5. ============= else ============
    private void saveHashtagInQuestion(Question question, String hashtag) {
        Optional<Hashtag> hashtagByContent = hashtagRepository.findByTag(hashtag);
        if (hashtagByContent.isEmpty()) {
            Hashtag buildHashtag = Hashtag.builder().tag(hashtag).build();
            hashtagRepository.save(buildHashtag);
            BoardHashtag buildBoardHashtag = BoardHashtag.builder()
                    .hashtag(buildHashtag).board(question).build();
        }//아래 else가 좀 반복되는 것 같다...
        else{
            BoardHashtag buildBoardHashtag = BoardHashtag.builder()
                    .hashtag(hashtagByContent.get()).board(question).build();
        }
    }
    private void saveHashtagInAnswer(Answer answer, String hashtag) {
        Optional<Hashtag> hashtagByContent = hashtagRepository.findByTag(hashtag);
        if (hashtagByContent.isEmpty()) {
            Hashtag buildHashtag = Hashtag.builder().tag(hashtag).build();
            hashtagRepository.save(buildHashtag);
            BoardHashtag buildBoardHashtag = BoardHashtag.builder()
                    .hashtag(buildHashtag).board(answer).build();
        }//아래 else가 좀 반복되는 것 같다...
        else{
            BoardHashtag buildBoardHashtag = BoardHashtag.builder()
                    .hashtag(hashtagByContent.get()).board(answer).build();
        }
    }

}
