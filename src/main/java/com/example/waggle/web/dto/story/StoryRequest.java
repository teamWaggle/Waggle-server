package com.example.waggle.web.dto.story;

import jakarta.validation.constraints.Max;
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
    @Max(500)
    private String content;
    private List<String> hashtagList;
    private List<String> mediaList;
}
