package com.example.waggle.service.board;

import com.example.waggle.domain.board.Media;
import com.example.waggle.domain.board.Story;
import com.example.waggle.domain.board.comment.Comment;
import com.example.waggle.domain.board.comment.MemberMention;
import com.example.waggle.domain.board.comment.Reply;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.*;
import com.example.waggle.dto.member.MemberDto;

import com.example.waggle.repository.board.HashtagRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
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

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StoryService {

    private final StoryRepository storyRepository;
    private final MemberRepository memberRepository;
    private final HashtagRepository hashtagRepository;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    /**
     * 조회는 entity -> dto과정을,
     * 저장은 dto -> entity 과정을 거치도록 한다.(기본)
     */

    //1. ===========조회===========

    //1.1 그룹 조회

    //1.1.1 전체 조회 -> 후에 시간 순으로 조회
    //P1. 지금은 story -> storySimpleDto로 변경하지만 조회를 dto로 변경하면 query양이 적어질 것이다.
    //P2. paging 필수
    @Transactional(readOnly = true)
    public List<StorySimpleDto> findAllStory() {
        List<Story> allStory = storyRepository.findAll();
        List<StorySimpleDto> simpleStories = new ArrayList<>();
        for (Story story : allStory) {
            simpleStories.add(StorySimpleDto.toDto(story));
        }

        return  simpleStories;
    }

    //1.1.2 회원 정보에 따른 전체 조회
    @Transactional(readOnly = true)
    public List<StorySimpleDto> findAllStoryByMember(String username) {
        Optional<Member> MemberByUsername = memberRepository.findByUsername(username);
        if (MemberByUsername.isEmpty()) {
            log.info("can't find user!");
            // error message 출력
        }
        List<Story> storyByUsername = storyRepository.findByUsername(username);
        List<StorySimpleDto> simpleStories = new ArrayList<>();
        for (Story story : storyByUsername) {
            simpleStories.add(StorySimpleDto.toDto(story));
        }

        return simpleStories;
    }

    //1.2 낱개 조회
    @Transactional(readOnly = true)
    public StoryDto findStoryByBoardId(Long id) {
        Optional<Story> storyById = storyRepository.findById(id);

        if (storyById.isEmpty()) {
            //error and return null
        }
        return StoryDto.toDto(storyById.get());
    }

    //2. ===========저장===========

    //2.1 story 저장(media, hashtag 포함)
    //***중요!
    public void saveStory(StoryDto saveStoryDto) {
        Story saveStory = saveStoryDto.toEntity();
        storyRepository.save(saveStory);
        Optional<Member> byUsername = memberRepository.findByUsername(saveStoryDto.getUsername());
        if (byUsername.isEmpty()) {
            //error message
        }
        //hashtag 저장
        if(!saveStoryDto.getHashtags().isEmpty()){
            for (String hashtagContent : saveStoryDto.getHashtags()) {
                saveHashtag(saveStory, hashtagContent);
            }
        }
        //media 저장
        if (!saveStoryDto.getMedias().isEmpty()) {
            for (String mediaURL : saveStoryDto.getMedias()) {
                Media buildMedia = Media.builder().url(mediaURL).board(saveStory).build();
            }
        }
    }

    //2.2 story_comment 저장
    public void saveComment(CommentDto commentDto, StoryDto storyDto, MemberDto memberDto) {
        Optional<Story> storyByBoardId = storyRepository.findById(storyDto.getId());
        Optional<Member> memberByUsername = memberRepository.findByUsername(memberDto.getUsername());

        int lastOrder = commentRepository.findLastOrderByBoardId(storyDto.getId());

        if (storyByBoardId.isPresent() && memberByUsername.isPresent()) {
            Comment buildComment = Comment.builder()
                    .orders(++lastOrder)
                    .content(commentDto.getContent())
                    .board(storyByBoardId.get())
                    .member(memberByUsername.get())
                    .build();
            commentRepository.save(buildComment);
        }

    }

    //2.3 story_comment_reply 저장
    public void saveReply(ReplyDto replyDto, CommentDto commentDto, MemberDto memberDto) {
        Optional<Comment> commentById = commentRepository.findById(commentDto.getId());
        Optional<Member> memberByUsername = memberRepository.findByUsername(memberDto.getUsername());
        //reply order set
        int lastOrder = replyRepository.findLastOrderByCommentId(commentDto.getId());
        //mention member set
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

    //3.1 story 수정(media, hashtag 포함)
    public void changeStory(StoryDto storyDto) {
        Optional<Story> storyByBoardId = storyRepository.findById(storyDto.getId());
        if (storyByBoardId.isPresent()) {
            storyByBoardId.get().changeStory(storyDto.getContent(),storyDto.getThumbnail());

            //delete(media)
            storyByBoardId.get().getMedias().clear();


            //newly insert data(media)
            for (String media : storyDto.getMedias()) {
                Media board = Media.builder().url(media).board(storyByBoardId.get()).build();
            }

            //delete connecting relate (boardHashtag)
            storyByBoardId.get().getBoardHashtags().clear();

            //newly insert data(hashtag, boardHashtag)
            for (String hashtag : storyDto.getHashtags()) {
                saveHashtag(storyByBoardId.get(), hashtag);
            }
        }

    }


    //3.2 story_comment 수정
    public void changeComment(CommentDto commentDto) {
        Optional<Comment> commentById = commentRepository.findById(commentDto.getId());
        if (commentById.isPresent()) {
            commentById.get().changeContent(commentDto.getContent());
        }
    }

    //3.3 story_comment_reply 수정
    public void changeReply(ReplyDto replyDto) {
        Optional<Reply> replyById = replyRepository.findById(replyDto.getId());
        if (replyById.isPresent()) {
            //change content
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

    //4.1 story 삭제
    // (media, hashtag 포함)
    public void removeStory(StoryDto storyDto) {
        Optional<Story> storyByBoardId = storyRepository.findById(storyDto.getId());
        if (storyByBoardId.isPresent()) {
            storyRepository.delete(storyByBoardId.get());
        }
    }

    //4.2 story_comment 저장
    public void removeComment(CommentDto commentDto) {
        Optional<Comment> commentById = commentRepository.findById(commentDto.getId());
        if (commentById.isPresent()) {
            commentRepository.delete(commentById.get());
        }
    }

    //4.3 story_comment_reply 저장
    public void removeReply(ReplyDto replyDto) {
        Optional<Reply> replyById = replyRepository.findById(replyDto.getId());
        if (replyById.isPresent()) {
            replyRepository.delete(replyById.get());
        }
    }

    //5. ===============else==============
    /**
     * private method
     * use at : 2.1, 3.1
     * @param story
     * @param hashtag
     */
    private void saveHashtag(Story story, String hashtag) {
        Optional<Hashtag> hashtagByContent = hashtagRepository.findByTag(hashtag);
        if (hashtagByContent.isEmpty()) {
            Hashtag buildHashtag = Hashtag.builder().tag(hashtag).build();
            hashtagRepository.save(buildHashtag);
            BoardHashtag buildBoardHashtag = BoardHashtag.builder()
                    .hashtag(buildHashtag).board(story).build();
        }//아래 else가 좀 반복되는 것 같다...
        else{
            BoardHashtag buildBoardHashtag = BoardHashtag.builder()
                    .hashtag(hashtagByContent.get()).board(story).build();
        }
    }
}
