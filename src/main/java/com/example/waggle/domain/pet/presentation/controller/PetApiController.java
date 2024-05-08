package com.example.waggle.domain.pet.presentation.controller;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.pet.application.PetCommandService;
import com.example.waggle.domain.pet.application.PetQueryService;
import com.example.waggle.domain.pet.persistence.entity.Pet;
import com.example.waggle.domain.pet.presentation.converter.PetConverter;
import com.example.waggle.domain.pet.presentation.dto.PetRequest;
import com.example.waggle.domain.pet.presentation.dto.PetResponse;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.global.util.MediaUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/pets")
@RestController
@ApiResponse(responseCode = "2000", description = "성공")
@Tag(name = "Pet API", description = "반려견 API")
public class PetApiController {

    private final PetCommandService petCommandService;
    private final PetQueryService petQueryService;

    @Operation(summary = "반려견 정보 입력 🔑", description = "반려견 정보를 입력합니다. 입력한 반려견의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createPet(@RequestPart("createPetRequest") @Validated PetRequest createPetRequest,
                                          @AuthUser Member member) {
        createPetRequest.setPetProfileImg(MediaUtil.removePrefix(createPetRequest.getPetProfileImg()));
        Long petId = petCommandService.createPet(createPetRequest, member);
        return ApiResponseDto.onSuccess(petId);
    }


    @Operation(summary = "반려견 정보 수정 🔑", description = "반려견 정보를 수정합니다. 입력한 반려견의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @PutMapping(value = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updatePet(@PathVariable("petId") Long petId,
                                          @RequestPart("updatePetRequest") @Validated PetRequest updatePetRequest,
                                          @AuthUser Member member) {
        updatePetRequest.setPetProfileImg(MediaUtil.removePrefix(updatePetRequest.getPetProfileImg()));
        Long result = petCommandService.updatePet(petId, updatePetRequest, member);
        return ApiResponseDto.onSuccess(result);
    }

    @Operation(summary = "반려견 정보 조회", description = "회원 ID를 통해 반려견의 정보를 목록으로 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @GetMapping("/{userUrl}")
    public ApiResponseDto<List<PetResponse.PetDetailDto>> findPets(@PathVariable("userUrl") String userUrl) {
        List<Pet> petsByUsername = petQueryService.getPetsByUserUrl(userUrl);
        return ApiResponseDto.onSuccess(PetConverter.toPetDetailListDto(petsByUsername));
    }

    @Operation(summary = "반려견 삭제 🔑", description = "회원의 특정 반려견의 정보를 삭제합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR
    })
    @DeleteMapping("/{petId}")
    public ApiResponseDto<Boolean> deletePet(@PathVariable("petId") Long petId,
                                             @AuthUser Member member) {
        petCommandService.deletePet(petId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
