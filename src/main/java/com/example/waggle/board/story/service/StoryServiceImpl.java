package com.example.waggle.board.story.service;

import com.example.waggle.board.story.domain.Story;
import com.example.waggle.board.story.dto.StorySimpleViewDto;
import com.example.waggle.board.story.dto.StoryViewDto;
import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.board.story.repository.StoryRepository;
import com.example.waggle.commons.exception.CustomPageException;
import com.example.waggle.commons.exception.ErrorCode;
import com.example.waggle.commons.util.service.UtilService;
import com.example.waggle.media.domain.Media;
import com.example.waggle.member.domain.Member;
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
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final UtilService utilService;


    /**
     * 조회는 entity -> dto과정을,
     * 저장은 dto -> entity 과정을 거치도록 한다.(기본)
     */

    //1. ===========조회===========

    //1.1 그룹 조회

    // 1.1.1 전체 조회 -> 후에 시간 순으로 조회
    // P1. 지금은 story -> storySimpleDto로 변경하지만 조회를 dto로 변경하면 query양이 적어질 것이다.
    // P2. paging 필수
    @Transactional(readOnly = true)
    @Override
    public List<StorySimpleViewDto> findAllStory() {
        //board setting
        List<Story> allStory = storyRepository.findAll();
        List<StorySimpleViewDto> simpleStories = new ArrayList<>();

        for (Story story : allStory) {
            log.info("board Id is {}", story.getId());
            simpleStories.add(StorySimpleViewDto.toDto(story));
        }
        return simpleStories;
    }


    // 1.1.2 개인 story 모두 가져오기
    // 특징 : 개인 story를 가져오는 것이기 때문에 recommend는 누를 수 없다.
    @Transactional(readOnly = true)
    @Override
    public List<StorySimpleViewDto> findAllStoryByUsername(String username) {
        List<Story> storyByUsername = storyRepository.findByMemberUsername(username);
        List<StorySimpleViewDto> simpleStories = new ArrayList<>();

        for (Story story : storyByUsername) {
            simpleStories.add(StorySimpleViewDto.toDto(story));
        }

        return simpleStories;
    }

    // 1.2 낱개 조회
    @Transactional(readOnly = true)
    @Override
    public StoryViewDto findStoryViewByBoardId(Long id) {
        //board setting
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new CustomPageException(ErrorCode.BOARD_NOT_FOUND));
        return StoryViewDto.toDto(story);
    }


    //2. ===========저장===========

    //2.1 story 저장(media, hashtag 포함)
    //***중요!
    @Override
    public Long saveStory(StoryWriteDto saveStoryDto) {
        //member setting
        Member signInMember = utilService.getSignInMember();

        //board setting
        Story saveStory = saveStoryDto.toEntity(signInMember);
        storyRepository.save(saveStory);

        //hashtag 저장
        if (!saveStoryDto.getHashtags().isEmpty()) {
            for (String hashtagContent : saveStoryDto.getHashtags()) {
                utilService.saveHashtag(saveStory, hashtagContent);
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

    @Override
    public Long saveStoryWithThumbnail(StoryWriteDto saveStoryDto, String thumbnail) {
        //member setting
        Member signInMember = utilService.getSignInMember();
        saveStoryDto.changeThumbnail(thumbnail);
        //board setting
        Story saveStory = saveStoryDto.toEntity(signInMember);
        storyRepository.save(saveStory);

        //hashtag 저장
        if (!saveStoryDto.getHashtags().isEmpty()) {
            for (String hashtagContent : saveStoryDto.getHashtags()) {
                utilService.saveHashtag(saveStory, hashtagContent);
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
    @Override
    public String changeStory(StoryWriteDto storyDto) {

        Optional<Story> storyByBoardId = storyRepository.findById(storyDto.getId());

        if (storyByBoardId.isPresent()) {
            Story story = storyByBoardId.get();
            story.changeStory(storyDto.getContent(), storyDto.getThumbnail());

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
                utilService.saveHashtag(story, hashtag);
            }
            return story.getMember().getUsername();
        }
        return null;
    }

    // StoryWriteDto 조회 -> edit에서 사용. (저장된 story 정보 불러오기)
    @Transactional(readOnly = true)
    @Override
    public StoryWriteDto findStoryWriteByBoardId(Long id) {
        //board setting
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new CustomPageException(ErrorCode.BOARD_NOT_FOUND));
        return StoryWriteDto.toDto(story);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean checkMember(Long boardId) {
        Member signInMember = utilService.getSignInMember();
        Optional<Story> storyById = storyRepository.findById(boardId);
        if (storyById.isEmpty()) {
            log.info("not exist story");
            return false;
        }
        boolean isSameUser = storyById.get().getMember().equals(signInMember);
        return isSameUser;
    }


    //4. ===========삭제(취소)===========
    //4.1 story 삭제
    // (media, hashtag 포함)
    @Override
    public void removeStory(StoryViewDto storyDto) {
        Member signInMember = utilService.getSignInMember();
        Optional<Story> storyByBoardId = storyRepository.findById(storyDto.getId());
        if (storyByBoardId.isPresent()) {
            Story story = storyByBoardId.get();
            if (!story.getMember().equals(signInMember)) {
                log.info("only same user can delete board!");
                //error
                throw new CustomPageException(ErrorCode.CANNOT_TOUCH_NOT_YOURS);
            }
            storyRepository.delete(story);
            log.info("remove!");
        }
    }


}