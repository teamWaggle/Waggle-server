package com.example.waggle.web.controller;

import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.conversation.service.comment.CommentQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.converter.CommentConverter;
import com.example.waggle.web.dto.comment.CommentRequest;
import com.example.waggle.web.dto.comment.CommentResponse;
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
@RequestMapping("/api/comments")
@RestController
@Tag(name = "Comment API", description = "댓글 API")
public class CommentApiController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;
    private Sort oldestSorting = Sort.by("createdDate").ascending();

    @Operation(summary = "특정 게시글(로그, 질답, 사이렌) 댓글 페이징 조회", description = "게시글의 댓글 목록을 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 조회 성공. 게시글 댓글 목록을 반환합니다.")
    @GetMapping("/page/{boardId}")
    public ApiResponseDto<CommentResponse.ListDto> getCommentsByPage(@RequestParam(defaultValue = "0") int currentPage,
                                                                     @PathVariable Long boardId) {
        Pageable pageable = PageRequest.of(currentPage, 10, oldestSorting);
        Page<Comment> pagedComments = commentQueryService.getPagedComments(boardId, pageable);
        CommentResponse.ListDto listDto = CommentConverter.toListDto(pagedComments);
        return ApiResponseDto.onSuccess(listDto);
    }


    @Operation(summary = "댓글 작성", description = "사용자가 댓글을 작성합니다. 작성한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 작성 성공. 작성한 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다.")
    @PostMapping("/{boardId}")
    public ApiResponseDto<Long> createScheduleComment(@PathVariable Long boardId,
                                                      @RequestBody CommentRequest.Post request,
                                                      @AuthUser Member member) {
        Long commentId = commentCommandService.createComment(boardId, member, request);
        return ApiResponseDto.onSuccess(commentId);
    }

    @Operation(summary = "댓글 수정", description = "사용자가 댓글을 수정합니다. 수정한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공. 수정한 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다.")
    @PutMapping("/{commentId}")
    public ApiResponseDto<Long> updateComment(@PathVariable Long commentId,
                                              @RequestBody CommentRequest.Post request,
                                              @AuthUser Member member) {
        Long updatedCommentId = commentCommandService.updateComment(commentId, member, request);
        return ApiResponseDto.onSuccess(updatedCommentId);
    }

    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다. 하위 대댓글들도 모두 삭제됩니다.")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공.")
    @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없거나 인증 정보가 댓글을 작성한 유저와 일치하지 않습니다.")
    @DeleteMapping
    public ApiResponseDto<Boolean> deleteComment(@RequestParam("commentId") Long commentId,
                                                 @AuthUser Member member) {
        commentCommandService.deleteComment(commentId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
