package com.example.waggle.domain.conversation.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class ConversationRequest {

    @NotEmpty(message = "내용을 작성해주세요.")
    @Size(max = 200, message = "내용은 최대 200자까지 작성할 수 있습니다.")
    private String content;

}
