package com.example.waggle.web.controller;

import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.domain.board.siren.service.SirenCommandService;
import com.example.waggle.domain.board.siren.service.SirenQueryService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.converter.SirenConverter;
import com.example.waggle.web.dto.siren.SirenRequest;
import com.example.waggle.web.dto.siren.SirenResponse;
import com.example.waggle.web.dto.media.MediaRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/helps")
@RestController
@Tag(name = "help API", description = "헬퓨 API")
public class SirenApiController {

    private final SirenCommandService helpCommandService;
    private final SirenQueryService helpQueryService;
    private final RecommendQueryService recommendQueryService;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "사이렌 작성", description = "사용자가 사이렌을 작성합니다. 작성한 사이렌의 정보를 저장하고 사이렌의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 작성 성공. 작성한 사이렌의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 사이렌 작성에 실패했습니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createHelp(@RequestPart SirenRequest.Post helpWriteDto,
                                           @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) throws IOException {
        Long boardId = helpCommandService.createSiren(helpWriteDto, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "사이렌 수정", description = "사용자가 사이렌을 수정합니다. 수정한 사이렌의 정보를 저장하고 사이렌의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 수정 성공. 수정한 사이렌의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 사이렌의 수정에 실패했습니다.")
    @PutMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updateHelp(@PathVariable Long boardId,
                                           @RequestPart SirenRequest.Put helpUpdateDto,
                                           @RequestPart MediaRequest.Put mediaUpdateDto,
                                           @RequestPart(required = false, value = "files") List<MultipartFile> multipartFiles) throws IOException {
        mediaUpdateDto.getMediaList().forEach(media -> media.setImageUrl(MediaUtil.removePrefix(media.getImageUrl())));
        helpCommandService.updateSirenV2(boardId, helpUpdateDto, mediaUpdateDto, multipartFiles);
        return ApiResponseDto.onSuccess(boardId);
    }


    @Operation(summary = "전체 사이렌 목록 조회", description = "전체 사이렌 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 조회 성공. 전체 사이렌 목록을 반환합니다.")
    @GetMapping
    public ApiResponseDto<SirenResponse.ListDto> getAllHelp(@RequestParam(defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Siren> pagedHelpList = helpQueryService.getPagedSirenList(pageable);
        SirenResponse.ListDto listDto = SirenConverter.toListDto(pagedHelpList);
        listDto.getSirenList().stream()
                .forEach(h -> {
                    h.setRecommend(recommendQueryService.checkRecommend(h.getId(), h.getUsername()));
                    h.setRecommendCount(recommendQueryService.countRecommend(h.getId()));
                });
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 사이렌 목록 조회", description = "특정 사용자가 작성한 사이렌 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 조회 성공. 사용자가 작성한 사이렌 목록을 반환합니다.")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다.")
    @GetMapping("/member/{username}")
    public ApiResponseDto<SirenResponse.ListDto> getHelpListByUsername(@RequestParam(defaultValue = "0") int currentPage,
                                                                       @PathVariable String username) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Siren> pagedHelpList = helpQueryService.getPagedSirenListByUsername(username, pageable);
        SirenResponse.ListDto listDto = SirenConverter.toListDto(pagedHelpList);
        listDto.getSirenList().stream()
                .forEach(h -> {
                    h.setRecommend(recommendQueryService.checkRecommend(h.getId(), h.getUsername()));
                    h.setRecommendCount(recommendQueryService.countRecommend(h.getId()));
                });
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 사이렌 조회", description = "특정 사이렌의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사이렌 조회 성공. 특정 사이렌의 상세 정보를 반환합니다.")
    @ApiResponse(responseCode = "404", description = "사이렌을 찾을 수 없음. 지정된 사이렌 ID에 해당하는 사이렌을 찾을 수 없습니다.")
    @GetMapping("/{boardId}")
    public ApiResponseDto<SirenResponse.DetailDto> getHelpByBoardId(@PathVariable Long boardId) {
        Siren help = helpQueryService.getSirenByBoardId(boardId);
        SirenResponse.DetailDto detailDto = SirenConverter.toDetailDto(help);
        detailDto.setRecommend(recommendQueryService.checkRecommend(detailDto.getId(), detailDto.getUsername()));
        detailDto.setRecommendCount(recommendQueryService.countRecommend(detailDto.getId()));
        return ApiResponseDto.onSuccess(detailDto);
    }
}
