package com.example.waggle.domain.board.presentation.dto.story;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryRequest {
    @Size(max = 500)
    private String content;
    private List<String> hashtagList;

    @Size(min = 1, message = "미디어는 최소 1개 이상 필요합니다.")
    private List<String> mediaList;
}