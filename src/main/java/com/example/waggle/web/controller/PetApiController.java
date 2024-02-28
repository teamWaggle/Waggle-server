package com.example.waggle.web.controller;

import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.pet.service.PetCommandService;
import com.example.waggle.domain.pet.service.PetQueryService;
import com.example.waggle.global.annotation.ApiErrorCodeExample;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.global.payload.code.ErrorStatus;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.converter.PetConverter;
import com.example.waggle.web.dto.pet.PetRequest;
import com.example.waggle.web.dto.pet.PetResponse.PetSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/pets")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Pet API", description = "반려견 API")
public class PetApiController {

    private final PetCommandService petCommandService;
    private final PetQueryService petQueryService;
    private final AwsS3Service awsS3Service;

    @Operation(summary = "반려견 정보 입력 🔑", description = "반려견 정보를 입력합니다. 입력한 반려견의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createPet(@RequestPart @Validated PetRequest createPetRequest,
                                          @RequestPart(value = "file", required = false) MultipartFile petProfileImg) {
        createPetRequest.setProfileImgUrl(MediaUtil.saveProfileImg(petProfileImg, awsS3Service));
        Long petId = petCommandService.createPet(createPetRequest);
        return ApiResponseDto.onSuccess(petId);
    }


    @Operation(summary = "반려견 정보 수정 🔑", description = "반려견 정보를 수정합니다. 입력한 반려견의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updatePet(@PathVariable("petId") Long petId,
                                          @RequestPart @Validated PetRequest updatePetRequest,
                                          @RequestPart(value = "file", required = false) MultipartFile petProfileImg,
                                          @RequestParam("allowUpload") boolean allowUpload) {
        String removePrefixProfileUrl = MediaUtil.removePrefix(updatePetRequest.getProfileImgUrl());
        if (allowUpload) {
            awsS3Service.deleteFile(removePrefixProfileUrl);
            updatePetRequest.setProfileImgUrl(MediaUtil.saveProfileImg(petProfileImg, awsS3Service));
        } else {
            updatePetRequest.setProfileImgUrl(removePrefixProfileUrl);
        }

        Long result = petCommandService.updatePet(petId, updatePetRequest);
        return ApiResponseDto.onSuccess(result);
    }

    @Operation(summary = "반려견 정보 조회", description = "회원 ID를 통해 반려견의 정보를 목록으로 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{memberId}")
    public ApiResponseDto<List<PetSummaryDto>> findPets(@PathVariable("memberId") Long memberId) {
        List<Pet> petsByUsername = petQueryService.getPetsByMemberId(memberId);
        return ApiResponseDto.onSuccess(PetConverter.toListDto(petsByUsername));
    }

    @Operation(summary = "반려견 삭제 🔑", description = "회원의 특정 반려견의 정보를 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{petId}")
    public ApiResponseDto<Boolean> deletePet(@PathVariable("petId") Long petId) {
        petCommandService.deletePet(petId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
