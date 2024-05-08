package com.example.waggle.domain.board.presentation.controller;

import static com.example.waggle.global.util.PageUtil.SIREN_SIZE;

import com.example.waggle.domain.board.application.siren.SirenCacheService;
import com.example.waggle.domain.board.application.siren.SirenCommandService;
import com.example.waggle.domain.board.application.siren.SirenQueryService;
import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.persistence.entity.SirenCategory;
import com.example.waggle.domain.board.presentation.converter.SirenConverter;
import com.example.waggle.domain.board.presentation.dto.siren.SirenFilterParam;
import com.example.waggle.domain.board.presentation.dto.siren.SirenRequest;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenSummaryListDto;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenDetailDto;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenPageDto;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenSummaryDto;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.application.query.RecommendQueryService;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.util.MediaUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
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

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/sirens")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Siren API", description = "사이렌 API")
public class SirenApiController {

    private final SirenCacheService sirenCacheService;
    private final SirenCommandService sirenCommandService;
    private final SirenQueryService sirenQueryService;
    private final RecommendQueryService recommendQueryService;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "사이렌 작성 🔑", description = "사용자가 사이렌을 작성합니다. 작성한 사이렌의 정보를 저장하고 사이렌의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createSiren(
            @RequestPart("createSirenRequest") @Validated SirenRequest createSirenRequest,
            @AuthUser Member member) {
        List<String> removedPrefixMedia = createSirenRequest.getMediaList().stream()
                .map(media -> MediaUtil.removePrefix(media)).collect(Collectors.toList());
        createSirenRequest.setMediaList(removedPrefixMedia);
        Long boardId = sirenCommandService.createSiren(createSirenRequest, member);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "사이렌 수정 🔑", description = "사용자가 사이렌을 수정합니다. 수정한 사이렌의 정보를 저장하고 사이렌의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{sirenId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateSiren(@PathVariable("sirenId") Long sirenId,
                                            @RequestPart("updateSirenRequest") @Validated SirenRequest updateSirenRequest,
                                            @AuthUser Member member) {
        List<String> removedPrefixMedia = updateSirenRequest.getMediaList().stream()
                .map(media -> MediaUtil.removePrefix(media)).collect(Collectors.toList());
        updateSirenRequest.setMediaList(removedPrefixMedia);
        sirenCommandService.updateSiren(sirenId, updateSirenRequest, member);
        return ApiResponseDto.onSuccess(sirenId);
    }

    @Operation(summary = "사이렌 상태 변경 🔑", description = "사용자가 사이렌 상태 변경합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{sirenId}/status")
    public ApiResponseDto<Long> convertStatus(@PathVariable("sirenId") Long sirenId,
                                              @AuthUser Member member) {
        sirenCommandService.convertStatus(sirenId, member);
        return ApiResponseDto.onSuccess(sirenId);
    }

    @Operation(summary = "사이렌 삭제 🔑", description = "특정 사이렌을 삭제합니다.게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{sirenId}")
    public ApiResponseDto<Boolean> deleteSiren(@PathVariable("sirenId") Long sirenId,
                                               @AuthUser Member member) {
        sirenCommandService.deleteSiren(sirenId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }


    @Operation(summary = "전체 사이렌 목록 조회", description = "전체 사이렌 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping
    public ApiResponseDto<SirenPageDto> getAllSiren(
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, SIREN_SIZE, latestSorting);
        Page<Siren> pagedSirenList = sirenQueryService.getPagedSirenList(pageable);
        SirenPageDto listDto = SirenConverter.toSirenPageDto(pagedSirenList);
        setRecommendCntInList(listDto.getSirenList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사이렌 필터 조회", description = "필터 옵션에 맞추어 결과를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/filter")
    public ApiResponseDto<SirenPageDto> getSirensByFilter(
            @RequestParam(name = "filterParam") SirenFilterParam filterParam,
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, SIREN_SIZE);
        Page<Siren> pagedSirenList = sirenQueryService.getPagedSirenListByFilter(filterParam, pageable);
        SirenPageDto listDto = SirenConverter.toSirenPageDto(pagedSirenList);
        setRecommendCntInList(listDto.getSirenList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사이렌 카테고리 조회", description = "카테고리 옵션에 맞추어 결과를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/category")
    public ApiResponseDto<SirenPageDto> getSirensByCategory(
            @RequestParam(name = "category") SirenCategory category,
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, SIREN_SIZE, latestSorting);
        Page<Siren> pagedSirenList = sirenQueryService.getPagedSirenListByCategory(category, pageable);
        SirenPageDto listDto = SirenConverter.toSirenPageDto(pagedSirenList);
        setRecommendCntInList(listDto.getSirenList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "대표 사이렌 조회", description = "대표 사이렌을 조회합니다. 미해결 인기순으로 정렬하고, 상단 3개의 사이렌을 반환합니다.")
    @ApiErrorCodeExample({ErrorStatus._INTERNAL_SERVER_ERROR})
    @GetMapping("/representative")
    public ApiResponseDto<SirenSummaryListDto> getRepresentativeSirenList() {
        List<Siren> representativeSirenList = sirenQueryService.getRepresentativeSirenList();
        SirenSummaryListDto listDto = SirenConverter.toSirenSummaryListDto(
                representativeSirenList);
        setRecommendCntInList(listDto.getSirenList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 사이렌 목록 조회", description = "특정 사용자가 작성한 사이렌 목록을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/member/{userUrl}")
    public ApiResponseDto<SirenPageDto> getSirenListByUsername(@PathVariable("userUrl") String userUrl,
                                                               @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, SIREN_SIZE, latestSorting);
        Page<Siren> pagedSirenList = sirenQueryService.getPagedSirenListByUserUrl(userUrl, pageable);
        SirenPageDto listDto = SirenConverter.toSirenPageDto(pagedSirenList);
        setRecommendCntInList(listDto.getSirenList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 사이렌 조회", description = "특정 사이렌의 상세 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{sirenId}")
    public ApiResponseDto<SirenDetailDto> getSirenByBoardId(@PathVariable("sirenId") Long sirenId) {
        Siren siren = sirenQueryService.getSirenByBoardId(sirenId);
        SirenDetailDto detailDto = SirenConverter.toSirenDetailDto(siren);
        detailDto.setViewCount(sirenCacheService.applyViewCountToRedis(sirenId));
        detailDto.setRecommendCount(recommendQueryService.countRecommend(sirenId));
        return ApiResponseDto.onSuccess(detailDto);
    }

    @Operation(summary = "랜덤 사이렌 조회", description = "임의의 사이렌을 조회합니다. 미해결 사이렌 중 임의로 3개의 사이렌을 반환합니다.")
    @ApiErrorCodeExample({ErrorStatus._INTERNAL_SERVER_ERROR})
    @GetMapping("/random")
    public ApiResponseDto<SirenSummaryListDto> getUnsolvedRandomSirenList() {
        List<Siren> representativeSirenList = sirenQueryService.getRandomUnsolvedSirenList();
        SirenSummaryListDto listDto = SirenConverter.toSirenSummaryListDto(
                representativeSirenList);
        setRecommendCntInList(listDto.getSirenList());
        return ApiResponseDto.onSuccess(listDto);
    }


    private void setRecommendCntInList(List<SirenSummaryDto> sirenList) {
        sirenList
                .forEach(siren ->
                        siren.setRecommendCount(
                                recommendQueryService.countRecommend(siren.getBoardId()))
                );
    }
}
