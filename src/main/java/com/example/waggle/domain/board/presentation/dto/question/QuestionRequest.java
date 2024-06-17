package com.example.waggle.domain.board.presentation.dto.question;

import com.example.waggle.global.annotation.validation.hashtag.ValidHashtag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
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
@Schema
public class QuestionRequest {

    @NotEmpty(message = "내용을 작성해주세요.")
    @Size(max = 1500, message = "내용은 최대 1500자까지 작성할 수 있습니다.")
    private String content;

    @Size(min = 2, max = 20, message = "제목을 2자 이상 20자 이하로 작성해주세요.")
    private String title;

    @Size(max = 5, message = "해시태그는 최대 5개까지 작성할 수 있습니다.")
    @ValidHashtag
    private List<String> hashtagList;

    @Size(max = 10, message = "미디어는 최대 10개까지 추가할 수 있습니다.")
    private List<String> mediaList;

}