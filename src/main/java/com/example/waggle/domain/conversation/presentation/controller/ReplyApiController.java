package com.example.waggle.domain.conversation.presentation.controller;

import com.example.waggle.domain.conversation.application.reply.ReplyCommandService;
import com.example.waggle.domain.conversation.application.reply.ReplyQueryService;
import com.example.waggle.domain.conversation.persistence.entity.Reply;
import com.example.waggle.domain.conversation.presentation.converter.ReplyConverter;
import com.example.waggle.domain.conversation.presentation.dto.reply.ReplyRequest;
import com.example.waggle.domain.conversation.presentation.dto.reply.ReplyResponse.ReplyListDto;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/{commentId}")
    public ApiResponseDto<Long> createReply(@PathVariable("commentId") Long commentId,
                                            @RequestBody ReplyRequest createReplyRequest,
                                            @AuthUser Member member) {
        Long replyId = replyCommandService.createReply(commentId, createReplyRequest, member);
        return ApiResponseDto.onSuccess(replyId);
    }

    @Operation(summary = "대댓글 수정 🔑", description = "사용자가 대댓글을 수정합니다. 수정한 대댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping("/{replyId}")
    public ApiResponseDto<Long> updateReply(@PathVariable("replyId") Long replyId,
                                            @RequestBody ReplyRequest updateReplyRequest,
                                            @AuthUser Member member) {
        Long updatedReplyId = replyCommandService.updateReply(replyId, updateReplyRequest, member);
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
        ReplyListDto listDto = ReplyConverter.toReplyListDto(pagedReplies);
        return ApiResponseDto.onSuccess(listDto);
    }


    @Operation(summary = "대댓글 삭제 🔑", description = "특정 대댓글을 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{replyId}")
    public ApiResponseDto<Boolean> deleteReply(@PathVariable("replyId") Long replyId,
                                               @AuthUser Member member) {
        replyCommandService.deleteReply(replyId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "대댓글 강제 삭제 🔑", description = "특정 대댓글이 관리자에 의해 삭제됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{replyId}/admin")
    public ApiResponseDto<Boolean> deleteReplyByAdmin(@PathVariable("replyId") Long replyId,
                                                      @AuthUser Member admin) {
        replyCommandService.deleteReplyByAdmin(replyId, admin);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
