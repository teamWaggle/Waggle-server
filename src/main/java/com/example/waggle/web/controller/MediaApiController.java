package com.example.waggle.web.controller;

import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.media.service.MediaCommandService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.converter.MediaConverter;
import com.example.waggle.web.dto.media.MediaResponse.MediaListDto;
import com.example.waggle.web.dto.media.MediaResponse.MediaViewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/media")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Media API", description = "미디어 API")
public class MediaApiController {
    private final AwsS3Service awsS3Service;
    private final MediaCommandService mediaCommandService;

    @Operation(summary = "media file 변환", description = "로컬 파일을 서버에 업로드하여 url로 변환 요청을 합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<MediaViewDto> convertMedia(@RequestPart(value = "uploadImgFile") MultipartFile uploadImgFile) {
        String imgUrl = awsS3Service.uploadFile(uploadImgFile);
        return ApiResponseDto.onSuccess(MediaConverter.toMediaViewDto(imgUrl));
    }

    @Operation(summary = "media file list 변환", description = "로컬 파일 list를 서버에 업로드하여 url로 변환 요청을 합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(value = "/list", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<MediaListDto> convertMediaList(
            @RequestPart(value = "uploadImgFileList") List<MultipartFile> uploadImgFileList) {
        List<String> imgUrlList = awsS3Service.uploadFiles(uploadImgFileList);
        return ApiResponseDto.onSuccess(MediaConverter.toMediaListDto(imgUrlList));
    }

    @Operation(summary = "delete file check", description = "db와 비교했을 때 존재하지 않는 이미지 파일을 s3d에서 지울 파일들을 리스트로 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping(value = "/check")
    public ApiResponseDto<List<String>> checkDeleteFileInS3() {
        return ApiResponseDto.onSuccess(mediaCommandService.checkDeleteFileInS3());
    }
}
