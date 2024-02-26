package com.example.waggle.web.controller;

import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.service.SirenCommandService;
import com.example.waggle.domain.board.siren.service.SirenQueryService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.converter.SirenConverter;
import com.example.waggle.web.dto.media.MediaRequest.MediaUpdateDto;
import com.example.waggle.web.dto.siren.SirenRequest.SirenCreateDto;
import com.example.waggle.web.dto.siren.SirenResponse.SirenDetailDto;
import com.example.waggle.web.dto.siren.SirenResponse.SirenListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sirens")
@RestController
@Tag(name = "Siren API", description = "사이렌 API")
public class SirenApiController {

    private final SirenCommandService sirenCommandService;
    private final SirenQueryService sirenQueryService;
    private final RecommendQueryService recommendQueryService;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "사이렌 작성", description = "사용자가 사이렌을 작성합니다. 작성한 사이렌의 정보를 저장하고 사이렌의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 작성 성공. 작성한 사이렌의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 사이렌 작성에 실패했습니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createSiren(@RequestPart @Validated SirenCreateDto request,
                                            @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) {
        Long boardId = sirenCommandService.createSiren(request, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "사이렌 수정", description = "사용자가 사이렌을 수정합니다. 수정한 사이렌의 정보를 저장하고 사이렌의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 수정 성공. 수정한 사이렌의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 사이렌의 수정에 실패했습니다.")
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateSiren(@PathVariable Long boardId,
                                            @RequestPart @Validated SirenCreateDto request,
                                            @RequestPart MediaUpdateDto mediaUpdateDto,
                                            @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) {
        mediaUpdateDto.getMediaList().forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        mediaUpdateDto.getDeleteMediaList()
                .forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));

        sirenCommandService.updateSirenV2(boardId, request, mediaUpdateDto, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }


    @Operation(summary = "전체 사이렌 목록 조회", description = "전체 사이렌 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 조회 성공. 전체 사이렌 목록을 반환합니다.")
    @GetMapping
    public ApiResponseDto<SirenListDto> getAllSiren(@RequestParam(defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Siren> pagedSirenList = sirenQueryService.getPagedSirenList(pageable);
        SirenListDto listDto = SirenConverter.toListDto(pagedSirenList);
        recommendQueryService.getRecommendValues(listDto);
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 사이렌 목록 조회", description = "특정 사용자가 작성한 사이렌 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 조회 성공. 사용자가 작성한 사이렌 목록을 반환합니다.")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다.")
    @GetMapping("/member/{memberId}")
    public ApiResponseDto<SirenListDto> getSirenListByUsername(
            @RequestParam(defaultValue = "0") int currentPage,
            @PathVariable Long memberId) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Siren> pagedSirenList = sirenQueryService.getPagedSirenListByMemberId(memberId, pageable);
        SirenListDto listDto = SirenConverter.toListDto(pagedSirenList);
        recommendQueryService.getRecommendValues(listDto);
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 사이렌 조회", description = "특정 사이렌의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 조회 성공. 특정 사이렌의 상세 정보를 반환합니다.")
    @ApiResponse(responseCode = "404", description = "사이렌을 찾을 수 없음. 지정된 사이렌 ID에 해당하는 사이렌을 찾을 수 없습니다.")
    @GetMapping("/{boardId}")
    public ApiResponseDto<SirenDetailDto> getSirenByBoardId(@PathVariable Long boardId) {
        Siren siren = sirenQueryService.getSirenByBoardId(boardId);
        SirenDetailDto detailDto = SirenConverter.toDetailDto(siren);
        recommendQueryService.getRecommendValues(detailDto);
        return ApiResponseDto.onSuccess(detailDto);
    }

    @Operation(summary = "사이렌 삭제", description = "특정 사이렌을 삭제합니다.게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 삭제 성공.")
    @ApiResponse(responseCode = "404", description = "사이렌을 찾을 수 없거나 인증 정보가 사이렌을 작성한 유저와 일치하지 않습니다.")
    @DeleteMapping
    public ApiResponseDto<Boolean> deleteSiren(@PathParam("boardId") Long boardId) {
        sirenCommandService.deleteSiren(boardId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
