package com.example.waggle.service.board;

import com.example.waggle.dto.board.StoryDto;
import com.example.waggle.dto.board.StorySimpleDto;
import com.example.waggle.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoryServiceTest {

    @Autowired
    StoryService storyService;
    @Autowired
    MemberService memberService;

    //read test

    
}