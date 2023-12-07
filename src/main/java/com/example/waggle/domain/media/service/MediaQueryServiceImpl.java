package com.example.waggle.domain.media.service;

import com.example.waggle.domain.media.entity.Media;
import com.example.waggle.domain.media.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MediaQueryServiceImpl implements MediaQueryService{

    private final MediaRepository mediaRepository;
    @Override
    public List<Media> findMediaList(Long boardId) {
        return mediaRepository.findByBoardId(boardId);
    }
}
