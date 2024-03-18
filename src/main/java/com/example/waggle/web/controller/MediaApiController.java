package com.example.waggle.web.controller;

import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.web.converter.MediaConverter;
import com.example.waggle.web.dto.media.MediaResponse.MediaListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
}
