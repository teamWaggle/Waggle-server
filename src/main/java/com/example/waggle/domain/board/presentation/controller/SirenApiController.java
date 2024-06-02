package com.example.waggle.domain.board.presentation.controller;

import com.example.waggle.domain.board.application.siren.SirenCacheService;
import com.example.waggle.domain.board.application.siren.SirenCommandService;
import com.example.waggle.domain.board.application.siren.SirenQueryService;
import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.presentation.converter.SirenConverter;
import com.example.waggle.domain.board.presentation.dto.siren.SirenFilterParam;
import com.example.waggle.domain.board.presentation.dto.siren.SirenRequest;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenDetailDto;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenPagedSummaryListDto;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenSummaryDto;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenSummaryListDto;
import com.example.waggle.domain.board.presentation.dto.siren.SirenSortParam;
import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.recommend.application.query.RecommendQueryService;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.*;
import static com.example.waggle.global.util.PageUtil.LATEST_SORTING;
import static com.example.waggle.global.util.PageUtil.SIREN_SIZE;

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

    @Operation(summary = "사이렌 작성 🔑", description = "사용자가 사이렌을 작성합니다. 작성한 사이렌의 정보를 저장하고 사이렌의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.MEDIA_PREFIX_IS_WRONG
    }, status = AUTH)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createSiren(@RequestPart("createSirenRequest") @Validated SirenRequest createSirenRequest,
                                            @AuthUser Member member) {
        List<String> removedPrefixMedia = createSirenRequest.getMediaList().stream()
                .map(media -> MediaUtil.removePrefix(media)).collect(Collectors.toList());
        createSirenRequest.setMediaList(removedPrefixMedia);
        Long boardId = sirenCommandService.createSiren(createSirenRequest, member);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "사이렌 수정 🔑", description = "사용자가 사이렌을 수정합니다. 수정한 사이렌의 정보를 저장하고 사이렌의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample(status = BOARD_DATA_CHANGE)
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
    @ApiErrorCodeExample(value = {
            ErrorStatus.BOARD_INVALID_TYPE
    }, status = BOARD_DATA_CHANGE)
    @PutMapping(value = "/{sirenId}/status")
    public ApiResponseDto<Long> convertStatus(@PathVariable("sirenId") Long sirenId,
                                              @AuthUser Member member) {
        sirenCommandService.convertStatus(sirenId, member);
        return ApiResponseDto.onSuccess(sirenId);
    }

    @Operation(summary = "사이렌 삭제 🔑", description = "특정 사이렌을 삭제합니다.게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiErrorCodeExample(status = BOARD_DATA_CHANGE)
    @DeleteMapping("/{sirenId}")
    public ApiResponseDto<Boolean> deleteSiren(@PathVariable("sirenId") Long sirenId,
                                               @AuthUser Member member) {
        sirenCommandService.deleteSirenWithRelations(sirenId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "사이렌 강제 삭제 🔑", description = "특정 사이렌이 관리자에 의해 삭제됩니다. 게시글과 관련된 댓글, 대댓글, 미디어 등을 모두 삭제합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.MEMBER_ACCESS_DENIED_BY_AUTHORIZATION,
            ErrorStatus.BOARD_NOT_FOUND
    }, status = ADMIN)
    @DeleteMapping("/{sirenId}/admin")
    public ApiResponseDto<Boolean> deleteSirenByAdmin(@PathVariable("sirenId") Long sirenId,
                                                      @AuthUser Member admin) {
        sirenCommandService.deleteSirenByAdmin(sirenId, admin);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }


    @Operation(summary = "대표 사이렌 조회", description = "대표 사이렌을 조회합니다. 미해결 인기순으로 정렬하고, 상단 3개의 사이렌을 반환합니다.")
    @ApiErrorCodeExample
    @GetMapping("/representative")
    public ApiResponseDto<SirenSummaryListDto> getRepresentativeSirenList() {
        List<Siren> representativeSirenList = sirenQueryService.getRepresentativeSirenList();
        SirenSummaryListDto listDto = SirenConverter.toSirenSummaryListDto(
                representativeSirenList);
        setRecommendCntInList(listDto.getSirenList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 사이렌 목록 조회", description = "특정 사용자가 작성한 사이렌 목록을 조회합니다.")
    @ApiErrorCodeExample
    @GetMapping("/member/{userUrl}")
    public ApiResponseDto<SirenPagedSummaryListDto> getSirenListByUsername(@PathVariable("userUrl") String userUrl,
                                                                           @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, SIREN_SIZE, LATEST_SORTING);
        Page<Siren> pagedSirenList = sirenQueryService.getPagedSirenListByUserUrl(userUrl, pageable);
        SirenPagedSummaryListDto listDto = SirenConverter.toSirenPageDto(pagedSirenList);
        setRecommendCntInList(listDto.getSirenList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 사이렌 조회", description = "특정 사이렌의 상세 정보를 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.BOARD_NOT_FOUND
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
    @ApiErrorCodeExample
    @GetMapping("/random")
    public ApiResponseDto<SirenSummaryListDto> getUnresolvedRandomSirenList() {
        List<Siren> randomUnresolvedSirenList = sirenQueryService.getRandomUnresolvedSirenList();
        SirenSummaryListDto listDto = SirenConverter.toSirenSummaryListDto(
                randomUnresolvedSirenList);
        setRecommendCntInList(listDto.getSirenList());
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사이렌 검색 및 정렬", description = "키워드를 포함하고 있는 타이틀을 지닌 사이렌을 조회합니다." +
            "이때 정렬 파라미터와 필터 파라미터를 통해 검색 결과를 정렬합니다.")
    @ApiErrorCodeExample({
            ErrorStatus.BOARD_SEARCHING_KEYWORD_IS_TOO_SHORT
    })
    @GetMapping("/search")
    public ApiResponseDto<SirenPagedSummaryListDto> searchSirenListBySorting(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "sortParam") SirenSortParam sortParam,
            @RequestParam(name = "filterParam") SirenFilterParam filterParam,
            @RequestParam(name = "currentPage", defaultValue = "0") int currentPage) {
        ObjectUtil.validateKeywordLength(keyword);
        Pageable pageable = PageRequest.of(currentPage, SIREN_SIZE, LATEST_SORTING);
        Page<Siren> pagedSirenList = sirenQueryService
                .getPagedSirenListBySearchingAndSorting(keyword, sortParam, filterParam, pageable);
        SirenPagedSummaryListDto sirenPageDto = SirenConverter.toSirenPageDto(
                pagedSirenList);
        setRecommendCntInList(sirenPageDto.getSirenList());
        return ApiResponseDto.onSuccess(sirenPageDto);
    }


    private void setRecommendCntInList(List<SirenSummaryDto> sirenList) {
        sirenList
                .forEach(siren ->
                        siren.setRecommendCount(
                                recommendQueryService.countRecommend(siren.getBoardId()))
                );
    }
}
