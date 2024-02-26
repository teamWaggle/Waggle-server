package com.example.waggle.web.controller;

import com.example.waggle.domain.conversation.entity.Reply;
import com.example.waggle.domain.conversation.service.reply.ReplyCommandService;
import com.example.waggle.domain.conversation.service.reply.ReplyQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.converter.ReplyConverter;
import com.example.waggle.web.dto.reply.ReplyRequest.ReplyCreateDto;
import com.example.waggle.web.dto.reply.ReplyResponse.ReplyListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/replies")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Reply API", description = "대댓글 API")
public class ReplyApiController {

    private final ReplyCommandService replyCommandService;
    private final ReplyQueryService replyQueryService;

    private Sort oldestSorting = Sort.by("createdDate").ascending();

    @Operation(summary = "대댓글 작성 🔑", description = "사용자가 대댓글을 작성합니다. 작성한 대댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping
    public ApiResponseDto<Long> createReply(@RequestBody ReplyCreateDto request) {
        Long replyId = replyCommandService.createReply(request.getCommentId(), request);
        return ApiResponseDto.onSuccess(replyId);
    }

    @Operation(summary = "대댓글 수정 🔑", description = "사용자가 대댓글을 수정합니다. 수정한 대댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping("/{replyId}")
    public ApiResponseDto<Long> updateReply(@PathVariable("replyId") Long replyId,
                                            @RequestBody ReplyCreateDto request) {
        Long updatedReplyId = replyCommandService.updateReply(replyId, request);
        return ApiResponseDto.onSuccess(updatedReplyId);
    }

    @Operation(summary = "대댓글 목록 조회", description = "댓글의 대댓글 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{commentId}")
    public ApiResponseDto<ReplyListDto> getReplies(@PathVariable("commentId") Long commentId,
                                                   @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, oldestSorting);
        Page<Reply> pagedReplies = replyQueryService.getPagedReplies(commentId, pageable);
        ReplyListDto listDto = ReplyConverter.toListDto(pagedReplies);
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "대댓글 삭제 🔑", description = "특정 대댓글을 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping
    public ApiResponseDto<Boolean> deleteReply(@RequestParam("replyId") Long replyId) {
        replyCommandService.deleteReply(replyId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
