package com.example.waggle.domain.board.presentation.dto.story;

import com.example.waggle.global.annotation.validation.hashtag.ValidHashtag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryRequest {

    @NotEmpty(message = "내용을 작성해주세요.")
    @Size(max = 500, message = "내용은 최대 500자까지 작성할 수 있습니다.")
    private String content;

    @Size(max = 5, message = "해시태그는 최대 5개까지 작성할 수 있습니다.")
    @ValidHashtag
    private List<String> hashtagList;

    @NotNull(message = "미디어 리스트는 비어 있을 수 없습니다.")
    @Size(max = 10, message = "미디어는 최대 10개까지 추가할 수 있습니다.")
    private List<String> mediaList;

}