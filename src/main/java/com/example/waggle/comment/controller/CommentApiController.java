package com.example.waggle.comment.controller;

import com.example.waggle.board.story.dto.StoryWriteDto;
import com.example.waggle.comment.dto.CommentWriteDto;
import com.example.waggle.comment.service.CommentService;
import com.example.waggle.commons.component.file.UploadFile;
import com.example.waggle.commons.util.service.BoardType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
@Tag(name = "Comment API", description = "댓글 API")
public class CommentApiController {
    private final CommentService commentService;

    @Operation(
            summary = "스토리 댓글 작성",
            description = "사용자가 댓글을 작성합니다. 작성한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스토리 댓글 작성 성공. 작성한 댓글의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다."
    )
    @PostMapping("/story/{boardId}/write")
    public ResponseEntity<?> StoryCommentWrite(@RequestPart CommentWriteDto commentWriteDto,
                                               @PathVariable Long boardId){
        Long commentId = commentService.createComment(boardId, commentWriteDto, BoardType.STORY);
        return ResponseEntity.ok(commentId);
    }
    @Operation(
            summary = "스토리 댓글 수정",
            description = "사용자가 댓글을 수정합니다. 수정한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "스토리 댓글 수정 성공. 수정한 댓글의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다."
    )
    @PostMapping("/story/{boardId}/edit")
    public ResponseEntity<?> StoryCommentEdit(@RequestPart CommentWriteDto commentWriteDto,
                                               @PathVariable Long boardId){
        Long commentId = commentService.updateComment(boardId, commentWriteDto);
        return ResponseEntity.ok(commentId);
    }
    @Operation(
            summary = "질문 댓글 작성",
            description = "사용자가 댓글을 작성합니다. 작성한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "질문 댓글 작성 성공. 작성한 댓글의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다."
    )
    @PostMapping("/question/{boardId}/write")
    public ResponseEntity<?> QuestionCommentWrite(@RequestPart CommentWriteDto commentWriteDto,
                                                  @PathVariable Long boardId){
        Long commentId = commentService.createComment(boardId, commentWriteDto, BoardType.QUESTION);
        return ResponseEntity.ok(commentId);
    }
    @Operation(
            summary = "질문 댓글 수정",
            description = "사용자가 댓글을 수정합니다. 수정한 댓글의 정보를 저장하고 댓글의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "질문 댓글 수정 성공. 수정한 댓글의 고유 ID를 반환합니다."
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 댓글 작성에 실패했습니다."
    )
    @PostMapping("/story/{boardId}/edit")
    public ResponseEntity<?> QuestionCommentEdit(@RequestPart CommentWriteDto commentWriteDto,
                                              @PathVariable Long boardId){
        Long commentId = commentService.updateComment(boardId, commentWriteDto);
        return ResponseEntity.ok(commentId);
    }
}
