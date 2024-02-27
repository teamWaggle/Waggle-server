package com.example.waggle.web.controller;

import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.service.SirenCommandService;
import com.example.waggle.domain.board.siren.service.SirenQueryService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.converter.SirenConverter;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.siren.SirenRequest;
import com.example.waggle.web.dto.siren.SirenResponse.SirenDetailDto;
import com.example.waggle.web.dto.siren.SirenResponse.SirenListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sirens")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Siren API", description = "사이렌 API")
public class SirenApiController {

    private final SirenCommandService sirenCommandService;
    private final SirenQueryService sirenQueryService;
    private final RecommendQueryService recommendQueryService;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "사이렌 작성 🔑", description = "사용자가 사이렌을 작성합니다. 작성한 사이렌의 정보를 저장하고 사이렌의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createSiren(@RequestPart("request") @Validated SirenRequest request,
                                            @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) {
        Long boardId = sirenCommandService.createSiren(request, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "사이렌 수정 🔑", description = "사용자가 사이렌을 수정합니다. 수정한 사이렌의 정보를 저장하고 사이렌의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{sirenId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateSiren(@PathVariable("sirenId") Long sirenId,
                                            @RequestPart("updateSirenRequest") @Validated SirenRequest updateSirenRequest,
                                            @RequestPart("updateMediaRequest") MediaUpdateDto updateMediaRequest,
                                            @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) {
        updateMediaRequest.getMediaList()
                .forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        updateMediaRequest.getDeleteMediaList()
                .forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));

        sirenCommandService.updateSirenV2(sirenId, updateSirenRequest, updateMediaRequest, multipartFiles);
        return ApiResponseDto.onSuccess(sirenId);
    }


    @Operation(summary = "전체 사이렌 목록 조회", description = "전체 사이렌 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping
    public ApiResponseDto<SirenListDto> getAllSiren(
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Siren> pagedSirenList = sirenQueryService.getPagedSirenList(pageable);
        SirenListDto listDto = SirenConverter.toListDto(pagedSirenList);
        recommendQueryService.getRecommendValues(listDto);
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 사이렌 목록 조회", description = "특정 사용자가 작성한 사이렌 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/member/{memberId}")
    public ApiResponseDto<SirenListDto> getSirenListByUsername(@PathVariable("memberId") Long memberId,
                                                               @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Siren> pagedSirenList = sirenQueryService.getPagedSirenListByMemberId(memberId, pageable);
        SirenListDto listDto = SirenConverter.toListDto(pagedSirenList);
        recommendQueryService.getRecommendValues(listDto);
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 사이렌 조회", description = "특정 사이렌의 상세 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{sirenId}")
    public ApiResponseDto<SirenDetailDto> getSirenByBoardId(@PathVariable("sirenId") Long sirenId) {
        Siren siren = sirenQueryService.getSirenByBoardId(sirenId);
        SirenDetailDto detailDto = SirenConverter.toDetailDto(siren);
        recommendQueryService.getRecommendValues(detailDto);
        return ApiResponseDto.onSuccess(detailDto);
    }

    @Operation(summary = "사이렌 삭제 🔑", description = "특정 사이렌을 삭제합니다.게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{sirenId}")
    public ApiResponseDto<Boolean> deleteSiren(@PathVariable("sirenId") Long sirenId) {
        sirenCommandService.deleteSiren(sirenId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
