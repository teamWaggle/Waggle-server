package com.example.waggle.board.helpU.service;

import com.example.waggle.board.helpU.repository.HelpURepository;
import com.example.waggle.board.helpU.dto.HelpUDetailDto;
import com.example.waggle.board.helpU.dto.HelpUSummaryDto;
import com.example.waggle.board.helpU.dto.HelpUWriteDto;
import com.example.waggle.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class HelpUServiceImpl implements HelpUService{
    private final HelpURepository helpURepository;
    private final MemberRepository memberRepository;

    @Override
    public List<HelpUSummaryDto> getAllHelpU() {
        return null;
    }

    @Override
    public Page<HelpUSummaryDto> getAllHelpUByPaging(Pageable pageable) {
        return null;
    }

    @Override
    public Page<HelpUSummaryDto> getHelpUsByUsername(String username, Pageable pageable) {
        return null;
    }

    @Override
    public HelpUDetailDto getHelpUById(Long helpUId) {
        return null;
    }

    @Override
    public Long createHelpU(HelpUWriteDto helpUWriteDto) {
        return null;
    }

    @Override
    public Long updateHelpU(HelpUWriteDto helpUWriteDto) {
        return null;
    }

    @Override
    public void deleteHelpU(HelpUDetailDto helpUDetailDto) {

    }
}
