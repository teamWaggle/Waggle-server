package com.example.waggle.service.board;

import com.example.waggle.domain.board.Media;
import com.example.waggle.domain.board.boardType.Story;
import com.example.waggle.domain.board.hashtag.BoardHashtag;
import com.example.waggle.domain.board.hashtag.Hashtag;
import com.example.waggle.domain.member.Member;
import com.example.waggle.dto.board.story.StoryViewDto;
import com.example.waggle.dto.board.story.StorySimpleViewDto;

import com.example.waggle.dto.board.story.StoryWriteDto;
import com.example.waggle.repository.board.HashtagRepository;
import com.example.waggle.repository.board.RecommendRepository;
import com.example.waggle.repository.board.boardtype.StoryRepository;
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
    private final RecommendRepository recommendRepository;

//    private final CommentRepository commentRepository;
//    private final ReplyRepository replyRepository;


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
    public List<StorySimpleViewDto> findAllStory(String username) {
        Member signInMember = getMember(username);

        //board setting
        List<Story> allStory = storyRepository.findAll();
        List<StorySimpleViewDto> simpleStories = new ArrayList<>();

        //entity -> dto
        for (Story story : allStory) {
            boolean likeIt = recommendRepository.existsByMemberIdAndBoardId(signInMember.getId(), story.getId());
            int count = recommendRepository.countByBoardId(story.getId());
            simpleStories.add(StorySimpleViewDto.toDto(story, count, likeIt));
        }
        return  simpleStories;
    }


    //1.1.2 회원 정보에 따른 전체 조회
    //특징 : 개인 story를 가져오는 것이기 때문에 recommend는 누를 수 없다.
    @Transactional(readOnly = true)
    public List<StorySimpleViewDto> findAllStoryByMember(String username) {
        Optional<Member> MemberByUsername = memberRepository.findByUsername(username);
        if (MemberByUsername.isEmpty()) {
            log.info("can't find user!");
            // error message 출력
        }
        List<Story> storyByUsername = storyRepository.findByMemberUsername(username);

        List<StorySimpleViewDto> simpleStories = new ArrayList<>();
        for (Story story : storyByUsername) {
            boolean cantLike = false;
            int count = recommendRepository.countByBoardId(story.getId());
            simpleStories.add(StorySimpleViewDto.toDto(story, count, cantLike));
        }

        return simpleStories;
    }

    //1.2 낱개 조회
    @Transactional(readOnly = true)
    public StoryViewDto findStoryByBoardId(String username, Long id) {
        //member setting
        Member signInMember = getMember(username);

        //board setting
        Optional<Story> storyById = storyRepository.findById(id);
        if (storyById.isEmpty()) {
            //error and return null
        }

        //check recommend
        boolean recommendIt = recommendRepository.existsByMemberIdAndBoardId(signInMember.getId(), id);
        int count = recommendRepository.countByBoardId(id);
        return StoryViewDto.toDto(storyById.get(), count, recommendIt);
    }

    //2. ===========저장===========

    //2.1 story 저장(media, hashtag 포함)
    //***중요!
    public Long saveStory(String username, StoryWriteDto saveStoryDto) {
        //member setting
        Member signInMember = getMember(username);

        //board setting
        Story saveStory = saveStoryDto.toEntity(signInMember);
        storyRepository.save(saveStory);

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
        return saveStory.getId();
    }

//    //2.2 story_comment 저장
//    public void saveComment(CommentDto commentDto, StoryDto storyDto, MemberDto memberDto) {
//        Optional<Story> storyByBoardId = storyRepository.findById(storyDto.getId());
//        Optional<Member> memberByUsername = memberRepository.findByUsername(memberDto.getUsername());
//
//        int lastOrder = commentRepository.findLastOrderByBoardId(storyDto.getId());
//
//        if (storyByBoardId.isPresent() && memberByUsername.isPresent()) {
//            Comment buildComment = Comment.builder()
//                    .orders(++lastOrder)
//                    .content(commentDto.getContent())
//                    .board(storyByBoardId.get())
//                    .member(memberByUsername.get())
//                    .build();
//            commentRepository.save(buildComment);
//        }
//
//    }

//    //2.3 story_comment_reply 저장
//    public void saveReply(ReplyDto replyDto, CommentDto commentDto, MemberDto memberDto) {
//        Optional<Comment> commentById = commentRepository.findById(commentDto.getId());
//        Optional<Member> memberByUsername = memberRepository.findByUsername(memberDto.getUsername());
//        //reply order set
//        int lastOrder = replyRepository.findLastOrderByCommentId(commentDto.getId());
//        //mention member set
//        List<MemberMention> memberMentions = new ArrayList<>();
//        for (String mentionMember : replyDto.getMentionMembers()) {
//            memberMentions.add(MemberMention.builder().username(mentionMember).build());
//        }
//
//        if (commentById.isPresent() && memberByUsername.isPresent()) {
//            Reply buildReply = Reply.builder()
//                    .orders(++lastOrder)
//                    .content(replyDto.getContent())
//                    .comment(commentById.get())
//                    .member(memberByUsername.get())
//                    .mentionedMembers(memberMentions)
//                    .build();
//
//            replyRepository.save(buildReply);
//        }
//    }

    //3. ===========수정===========

    //3.1 story 수정(media, hashtag 포함)
    public String changeStory(String username, StoryWriteDto storyDto, Long boardId) {
        Member member = getMember(username);
        Optional<Story> storyByBoardId = storyRepository.findById(boardId);

        if (storyByBoardId.isPresent()) {
            //check Board user
            Story story = storyByBoardId.get();
            if (!story.getMember().equals(member)) {
                log.info("only same user can edit board!");
                //error
            }
            story.changeStory(storyDto.getContent(),storyDto.getThumbnail());

            //delete(media)
            story.getMedias().clear();

            //newly insert data(media)
            for (String media : storyDto.getMedias()) {
                Media board = Media.builder().url(media).board(story).build();
            }

            //delete connecting relate (boardHashtag)
            story.getBoardHashtags().clear();

            //newly insert data(hashtag, boardHashtag)
            for (String hashtag : storyDto.getHashtags()) {
                saveHashtag(story, hashtag);
            }
            return story.getMember().getUsername();
        }
        return null;
    }


//    //3.2 story_comment 수정
//    public void changeComment(CommentDto commentDto) {
//        Optional<Comment> commentById = commentRepository.findById(commentDto.getId());
//        if (commentById.isPresent()) {
//            commentById.get().changeContent(commentDto.getContent());
//        }
//    }
//
//    //3.3 story_comment_reply 수정
//    public void changeReply(ReplyDto replyDto) {
//        Optional<Reply> replyById = replyRepository.findById(replyDto.getId());
//        if (replyById.isPresent()) {
//            //change content
//            replyById.get().changeContent(replyDto.getContent());
//            //mention member setting
//            //delete older data
//            replyById.get().getMemberMentions().clear();
//            //save mention entity
//            List<MemberMention> memberMentions = new ArrayList<>();
//            for (String mentionMember : replyDto.getMentionMembers()) {
//                memberMentions.add(MemberMention.builder().username(mentionMember).build());
//            }
//            //link relation -> entity save(cascade)
//            for (MemberMention memberMention : memberMentions) {
//                replyById.get().addMemberMention(memberMention);
//            }
//        }
//    }

    //4. ===========삭제(취소)===========
    //4.1 story 삭제
    // (media, hashtag 포함)
    public void removeStory(String username, StoryViewDto storyDto) {
        Member member = getMember(username);
        Optional<Story> storyByBoardId = storyRepository.findById(storyDto.getId());
        if (storyByBoardId.isPresent()) {
            if (!storyByBoardId.get().equals(member)) {
                log.info("only same user can delete board!");
                //error
                return;
            }
            storyRepository.delete(storyByBoardId.get());
        }
    }

//    //4.2 story_comment 저장
//    public void removeComment(CommentDto commentDto) {
//        Optional<Comment> commentById = commentRepository.findById(commentDto.getId());
//        if (commentById.isPresent()) {
//            commentRepository.delete(commentById.get());
//        }
//    }
//
//    //4.3 story_comment_reply 저장
//    public void removeReply(ReplyDto replyDto) {
//        Optional<Reply> replyById = replyRepository.findById(replyDto.getId());
//        if (replyById.isPresent()) {
//            replyRepository.delete(replyById.get());
//        }
//    }

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

    /**
     * use at :
     * @param username
     * @return member
     */
    private Member getMember(String username) {
        //member setting
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        if (byUsername.isEmpty()) {
            //error
            //여기서 return null 이 아니라 위 로직이 아예 멈출 수 있도록 한다.
            return null;
        }
        Member signInMember = byUsername.get();
        return signInMember;
    }
}
