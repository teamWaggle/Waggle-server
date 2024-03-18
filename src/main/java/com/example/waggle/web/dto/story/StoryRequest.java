package com.example.waggle.web.dto.story;

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
    private List<String> mediaList;
}
