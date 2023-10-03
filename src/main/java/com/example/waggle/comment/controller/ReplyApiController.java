package com.example.waggle.comment.controller;

import com.example.waggle.comment.dto.CommentWriteDto;
import com.example.waggle.comment.dto.ReplyWriteDto;
import com.example.waggle.comment.service.ReplyService;
import com.example.waggle.commons.util.service.BoardType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
@Tag(name = "Reply API", description = "대댓글 API")
public class ReplyApiController {
    private final ReplyService replyService;

    @Operation(
            summary = "대댓글 작성",
            description = "사용자가 대댓글을 작성합니다. 작성한 대댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "대댓글 작성 성공. 작성한 대댓글의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다."
    )
    @PostMapping("/{commentId}/write")
    public ResponseEntity<?> StoryCommentWrite(@RequestPart ReplyWriteDto replyWriteDto,
                                               @PathVariable Long commentId){
        Long replyId = replyService.createReply(commentId, replyWriteDto);
        return ResponseEntity.ok(replyId);
    }
}
