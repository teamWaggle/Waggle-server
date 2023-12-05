package com.example.waggle.web.controller;

import com.example.waggle.domain.board.help.entity.Help;
import com.example.waggle.domain.board.help.service.HelpCommandService;
import com.example.waggle.domain.board.help.service.HelpQueryService;
import com.example.waggle.domain.recommend.service.RecommendQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.converter.HelpConverter;
import com.example.waggle.web.dto.help.HelpRequest;
import com.example.waggle.web.dto.help.HelpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/help-us")
@RestController
@Tag(name = "help API", description = "헬퓨 API")
public class HelpApiController {

    private final HelpCommandService helpCommandService;
    private final HelpQueryService helpQueryService;
    private final RecommendQueryService recommendQueryService;
    private Sort latestSorting = Sort.by("createdDate").descending();

    @Operation(summary = "헬퓨 작성", description = "사용자가 헬퓨를 작성합니다. 작성한 헬퓨의 정보를 저장하고 헬퓨의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "헬퓨 작성 성공. 작성한 헬퓨의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 헬퓨 작성에 실패했습니다.")
    @PostMapping
    public ApiResponseDto<Long> createHelp(@RequestPart HelpRequest.Post helpWriteDto,
                                     @RequestPart List<MultipartFile> multipartFiles,
                                     @RequestPart MultipartFile thumbnail) throws IOException {
        Long boardId = helpCommandService.createHelp(helpWriteDto, multipartFiles, thumbnail);
        return ApiResponseDto.onSuccess(boardId);
    }

    @Operation(summary = "헬퓨 수정", description = "사용자가 헬퓨를 수정합니다. 수정한 헬퓨의 정보를 저장하고 헬퓨의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "헬퓨 수정 성공. 수정한 헬퓨의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "400", description = "잘못된 요청. 입력 데이터 유효성 검사 실패 등의 이유로 헬퓨의 수정에 실패했습니다.")
    @PutMapping("/{boardId}")
    public ApiResponseDto<Long> updateHelp(@PathVariable Long boardId,
                                            @ModelAttribute HelpRequest.Post helpWriteDto,
                                            @RequestPart List<MultipartFile> multipartFiles,
                                            @RequestPart MultipartFile thumbnail) throws IOException {
        helpCommandService.updateHelp(boardId, helpWriteDto, multipartFiles, thumbnail);
        return ApiResponseDto.onSuccess(boardId);
    }


    @Operation(summary = "전체 헬퓨 목록 조회", description = "전체 헬퓨 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "헬퓨 조회 성공. 전체 헬퓨 목록을 반환합니다.")
    @GetMapping
    public ApiResponseDto<HelpResponse.ListDto> getAllHelp(@RequestParam(defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Help> pagedHelpList = helpQueryService.getPagedHelpList(pageable);
        HelpResponse.ListDto listDto = HelpConverter.toListDto(pagedHelpList);
        listDto.getHelpList().stream()
                .forEach(h->{
                    h.setRecommendIt(recommendQueryService.checkRecommend(h.getId(),h.getUsername()));
                    h.setRecommendCount(recommendQueryService.countRecommend(h.getId()));
                });
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "사용자의 헬퓨 목록 조회", description = "특정 사용자가 작성한 헬퓨 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "헬퓨 조회 성공. 사용자가 작성한 헬퓨 목록을 반환합니다.")
    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음. 지정된 사용자 이름에 해당하는 사용자를 찾을 수 없습니다.")
    @GetMapping("/member/{username}")
    public ApiResponseDto<HelpResponse.ListDto> getHelpListByUsername(@RequestParam(defaultValue = "1") int currentPage,
                                                                    @PathVariable String username) {
        Pageable pageable = PageRequest.of(currentPage, 10, latestSorting);
        Page<Help> pagedHelpList = helpQueryService.getPagedHelpListByUsername(username, pageable);
        HelpResponse.ListDto listDto = HelpConverter.toListDto(pagedHelpList);
        listDto.getHelpList().stream()
                .forEach(h->{
                    h.setRecommendIt(recommendQueryService.checkRecommend(h.getId(),h.getUsername()));
                    h.setRecommendCount(recommendQueryService.countRecommend(h.getId()));
                });
        return ApiResponseDto.onSuccess(listDto);
    }

    @Operation(summary = "특정 헬퓨 조회", description = "특정 헬퓨의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "헬퓨 조회 성공. 특정 헬퓨의 상세 정보를 반환합니다.")
    @ApiResponse(responseCode = "404", description = "헬퓨를 찾을 수 없음. 지정된 헬퓨 ID에 해당하는 헬퓨를 찾을 수 없습니다.")
    @GetMapping("/{boardId}")
    public ApiResponseDto<HelpResponse.DetailDto> getHelpByBoardId(@PathVariable Long boardId) {
        Help help = helpQueryService.getHelpByBoardId(boardId);
        HelpResponse.DetailDto detailDto = HelpConverter.toDetailDto(help);
        detailDto.setRecommendIt(recommendQueryService.checkRecommend(detailDto.getId(), detailDto.getUsername()));
        detailDto.setRecommendCount(recommendQueryService.countRecommend(detailDto.getId()));
        return ApiResponseDto.onSuccess(detailDto);
    }
}
