package com.example.waggle.web.controller;

import com.example.waggle.domain.conversation.entity.Comment;
import com.example.waggle.domain.conversation.service.comment.CommentCommandService;
import com.example.waggle.domain.conversation.service.comment.CommentQueryService;
import com.example.waggle.domain.member.entity.Member;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.PageUtil;
import com.example.waggle.web.converter.CommentConverter;
import com.example.waggle.web.dto.comment.CommentRequest;
import com.example.waggle.web.dto.comment.CommentResponse.CommentListDto;
import com.example.waggle.web.dto.comment.CommentResponse.QuestionCommentListDto;
import com.example.waggle.web.dto.comment.CommentResponse.QuestionCommentViewDto;
import com.example.waggle.web.dto.comment.CommentResponse.SirenCommentListDto;
import com.example.waggle.web.dto.comment.CommentResponse.SirenCommentViewDto;
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
@RequestMapping("/api/comments")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Comment API", description = "댓글 API")
public class CommentApiController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "특정 게시글(로그, 질답, 사이렌) 댓글 페이징 조회", description = "게시글의 댓글 목록을 페이징 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{boardId}/paged")
    public ApiResponseDto<CommentListDto> getCommentsByPage(@PathVariable("boardId") Long boardId,
                                                            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, PageUtil.COMMENT_SIZE, latestSorting);
        Page<Comment> pagedComments = commentQueryService.getPagedComments(boardId, pageable);
        CommentListDto listDto = CommentConverter.toCommentListDto(pagedComments);
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "마이페이지 Question 댓글 페이징 조회", description = "게시글의 댓글 목록을 페이징 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/members/{userUrl}/question/paged")
    public ApiResponseDto<QuestionCommentListDto> getPagedQuestionCommentsByUserUrl(
            @PathVariable("userUrl") String userUrl,
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, PageUtil.COMMENT_SIZE, latestSorting);
        Page<QuestionCommentViewDto> pagedQuestionComments = commentQueryService.getPagedQuestionCommentsByUserUrl(
                userUrl, pageable);
        return ApiResponseDto.onSuccess(CommentConverter.toQuestionCommentListDto(pagedQuestionComments));
    }

    @Operation(summary = "마이페이지 Siren 댓글 페이징 조회", description = "게시글의 댓글 목록을 페이징 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/members/{userUrl}/siren/paged")
    public ApiResponseDto<SirenCommentListDto> getPagedSirenCommentsByUserUrl(@PathVariable("userUrl") String userUrl,
                                                                              @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, PageUtil.COMMENT_SIZE, latestSorting);
        Page<SirenCommentViewDto> pagedSirenComments = commentQueryService.getPagedSirenCommentsByUserUrl(
                userUrl, pageable);
        return ApiResponseDto.onSuccess(CommentConverter.toSirenCommentListDto(pagedSirenComments));
    }


    @Operation(summary = "댓글 작성 🔑", description = "사용자가 댓글을 작성합니다. 작성한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping("/{boardId}")
    public ApiResponseDto<Long> createComment(@PathVariable("boardId") Long boardId,
                                              @RequestBody CommentRequest createCommentRequest,
                                              @AuthUser Member member) {
        Long commentId = commentCommandService.createComment(boardId, createCommentRequest, member);
        return ApiResponseDto.onSuccess(commentId);
    }

    @Operation(summary = "댓글 수정 🔑", description = "사용자가 댓글을 수정합니다. 수정한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping("/{commentId}")
    public ApiResponseDto<Long> updateComment(@PathVariable("commentId") Long commentId,
                                              @RequestBody CommentRequest updateCommentRequest,
                                              @AuthUser Member member) {
        Long updatedCommentId = commentCommandService.updateComment(commentId, updateCommentRequest, member);
        return ApiResponseDto.onSuccess(updatedCommentId);
    }

    @Operation(summary = "댓글 삭제 🔑", description = "특정 댓글을 삭제합니다. 하위 대댓글들도 모두 삭제됩니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{commentId}")
    public ApiResponseDto<Boolean> deleteComment(@PathVariable("commentId") Long commentId,
                                                 @AuthUser Member member) {
        commentCommandService.deleteComment(commentId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
