package com.example.waggle.domain.comment.controller;

import com.example.waggle.domain.comment.dto.ReplyWriteDto;
import com.example.waggle.domain.comment.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/replies")
@RestController
@Tag(name = "Reply API", description = "대댓글 API")
public class ReplyApiController {

    private final ReplyService replyService;

    @Operation(summary = "대댓글 작성", description = "사용자가 대댓글을 작성합니다. 작성한 대댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "대댓글 작성 성공. 작성한 대댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다.")
    @PostMapping
    public ResponseEntity<Long> createReply(@RequestBody ReplyWriteDto replyWriteDto) {
        Long replyId = replyService.createReply(replyWriteDto.getCommentId(), replyWriteDto);
        return ResponseEntity.ok(replyId);
    }

    @Operation(summary = "대댓글 수정", description = "사용자가 대댓글을 수정합니다. 수정한 대댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "대댓글 수정 성공. 수정한 대댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다.")
    @PutMapping("/{replyId}")
    public ResponseEntity<Long> updateReply(@PathVariable Long replyId, @RequestBody ReplyWriteDto replyWriteDto) {
        Long updatedReplyId = replyService.updateReply(replyId, replyWriteDto);
        return ResponseEntity.ok(updatedReplyId);
    }
}
