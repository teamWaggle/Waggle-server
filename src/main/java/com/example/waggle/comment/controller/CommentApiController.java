package com.example.waggle.comment.controller;

import com.example.waggle.comment.dto.CommentWriteDto;
import com.example.waggle.comment.service.CommentService;
import com.example.waggle.commons.util.service.BoardType;
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
@RequestMapping("/api/comments")
@RestController
@Tag(name = "Comment API", description = "댓글 API")
public class CommentApiController {

    private final CommentService commentService;

    @Operation(summary = "스토리 댓글 작성", description = "사용자가 댓글을 작성합니다. 작성한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 댓글 작성 성공. 작성한 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다.")
    @PostMapping("/story")
    public ResponseEntity<Long> createStoryComment(@RequestBody CommentWriteDto commentWriteDto) {
        Long commentId = commentService.createComment(commentWriteDto.getBoardId(), commentWriteDto, BoardType.STORY);
        return ResponseEntity.ok(commentId);
    }

    @Operation(summary = "스토리 댓글 수정", description = "사용자가 댓글을 수정합니다. 수정한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "스토리 댓글 수정 성공. 수정한 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다.")
    @PutMapping("/story/{commentId}")
    public ResponseEntity<Long> updateStoryComment(@PathVariable Long commentId, @RequestBody CommentWriteDto commentWriteDto) {
        Long updatedCommentId = commentService.updateComment(commentId, commentWriteDto);
        return ResponseEntity.ok(updatedCommentId);
    }

    @Operation(summary = "질문 댓글 작성", description = "사용자가 댓글을 작성합니다. 작성한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "질문 댓글 작성 성공. 작성한 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다.")
    @PostMapping("/question")
    public ResponseEntity<Long> createQuestionComment(@RequestBody CommentWriteDto commentWriteDto) {
        Long commentId = commentService.createComment(commentWriteDto.getBoardId(), commentWriteDto, BoardType.QUESTION);
        return ResponseEntity.ok(commentId);
    }

    @Operation(summary = "질문 댓글 수정", description = "사용자가 댓글을 수정합니다. 수정한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "질문 댓글 수정 성공. 수정한 댓글의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다.")
    @PutMapping("/question/{commentId}")
    public ResponseEntity<Long> updateQuestionComment(@PathVariable Long commentId, @RequestBody CommentWriteDto commentWriteDto) {
        Long updatedCommentId = commentService.updateComment(commentId, commentWriteDto);
        return ResponseEntity.ok(updatedCommentId);
    }
}
