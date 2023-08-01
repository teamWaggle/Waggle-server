package com.example.waggle.service.board;

import com.example.waggle.component.jwt.SecurityUtil;
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
    public List<StorySimpleViewDto> findAllStory() {

        log.info(SecurityUtil.getCurrentUsername());
        Member signInMember = getMember(SecurityUtil.getCurrentUsername());

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
    public List<StorySimpleViewDto> findAllStoryByMember() {
        Optional<Member> MemberByUsername = memberRepository.findByUsername(SecurityUtil.getCurrentUsername());
        if (MemberByUsername.isEmpty()) {
            log.info("can't find user!");
            // error message 출력
        }
        List<Story> storyByUsername = storyRepository.findByMemberUsername(SecurityUtil.getCurrentUsername());

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
    public StoryViewDto findStoryViewByBoardId( Long id) {
        //member setting
        Member signInMember = getMember(SecurityUtil.getCurrentUsername());

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

    // StoryWriteDto 조회 -> edit에서 사용. (저장된 story 정보 불러오기)
    @Transactional(readOnly = true)
    public StoryWriteDto findStoryWriteByBoardId(Long id) {
        //board setting
        Optional<Story> storyById = storyRepository.findById(id);
        if (storyById.isEmpty()) {
            //error and return null
        }

        return StoryWriteDto.toDto(storyById.get());
    }

    //2. ===========저장===========

    //2.1 story 저장(media, hashtag 포함)
    //***중요!
    public Long saveStory(StoryWriteDto saveStoryDto) {
        //member setting
        Member signInMember = getMember(SecurityUtil.getCurrentUsername());

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
                Media.builder().url(mediaURL).board(saveStory).build().linkBoard(saveStory);
            }
        }
        log.info("tag's size is {}", saveStory.getBoardHashtags().size());
        return saveStory.getId();
    }


    //3. ===========수정===========

    //3.1 story 수정(media, hashtag 포함)
    public String changeStory(StoryWriteDto storyDto) {

        Optional<Story> storyByBoardId = storyRepository.findById(storyDto.getId());

        if (storyByBoardId.isPresent()) {
            Story story = storyByBoardId.get();
            story.changeStory(storyDto.getContent(),storyDto.getThumbnail());

            //delete(media)
            story.getMedias().clear();

            //newly insert data(media)
            for (String media : storyDto.getMedias()) {
                Media.builder().url(media).board(story).build().linkBoard(story);
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

    @Transactional(readOnly = true)
    public boolean checkMember(Long boardId) {
        Member member = getMember(SecurityUtil.getCurrentUsername());
        Optional<Story> storyById = storyRepository.findById(boardId);
        if (storyById.isEmpty()) {
            log.info("not exist story");
            //error
            return false;
        }
        boolean isSameUser = storyById.get().getMember().equals(member);
        return isSameUser;
    }


    //4. ===========삭제(취소)===========
    //4.1 story 삭제
    // (media, hashtag 포함)
    public void removeStory(StoryViewDto storyDto) {
        Member member = getMember(SecurityUtil.getCurrentUsername());
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
            log.info("not exist hashtag, so newly making");
            Hashtag buildHashtag = Hashtag.builder().tag(hashtag).build();
            hashtagRepository.save(buildHashtag);
            BoardHashtag.builder()
                    .hashtag(buildHashtag)
                    .board(story)
                    .build()
                    .link(story, buildHashtag);
        }//아래 else가 좀 반복되는 것 같다...
        else{
            log.info("already exist hashtag");
            BoardHashtag.builder()
                    .hashtag(hashtagByContent.get())
                    .board(story)
                    .build()
                    .link(story, hashtagByContent.get());
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
        log.info("signInMember user name is {}", signInMember.getUsername());
        return signInMember;
    }
}
