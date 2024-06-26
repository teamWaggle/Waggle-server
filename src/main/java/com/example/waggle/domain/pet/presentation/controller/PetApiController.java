package com.example.waggle.domain.pet.presentation.controller;

import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.ADMIN;
import static com.example.waggle.global.annotation.api.PredefinedErrorStatus.AUTH;

import com.example.waggle.domain.member.persistence.entity.Member;
import com.example.waggle.domain.pet.application.PetCommandService;
import com.example.waggle.domain.pet.application.PetQueryService;
import com.example.waggle.domain.pet.persistence.entity.Pet;
import com.example.waggle.domain.pet.presentation.converter.PetConverter;
import com.example.waggle.domain.pet.presentation.dto.PetRequest;
import com.example.waggle.domain.pet.presentation.dto.PetResponse;
import com.example.waggle.exception.payload.code.ErrorStatus;
import com.example.waggle.exception.payload.dto.ApiResponseDto;
import com.example.waggle.global.annotation.api.ApiErrorCodeExample;
import com.example.waggle.global.annotation.auth.AuthUser;
import com.example.waggle.global.util.MediaUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiErrorCodeExample(status = AUTH)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createPet(@RequestPart("createPetRequest") @Valid PetRequest createPetRequest,
                                          @AuthUser Member member) {
        createPetRequest.setPetProfileImg(MediaUtil.removePrefix(createPetRequest.getPetProfileImg()));
        Long petId = petCommandService.createPet(createPetRequest, member);
        return ApiResponseDto.onSuccess(petId);
    }


    @Operation(summary = "반려견 정보 수정 🔑", description = "반려견 정보를 수정합니다. 입력한 반려견의 고유 ID를 반환합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.PET_NOT_FOUND,
            ErrorStatus.MEDIA_REQUEST_IS_EMPTY
    }, status = AUTH)
    @PutMapping(value = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updatePet(@PathVariable("petId") Long petId,
                                          @RequestPart("updatePetRequest") @Valid PetRequest updatePetRequest,
                                          @AuthUser Member member) {
        updatePetRequest.setPetProfileImg(MediaUtil.removePrefix(updatePetRequest.getPetProfileImg()));
        Long result = petCommandService.updatePet(petId, updatePetRequest, member);
        return ApiResponseDto.onSuccess(result);
    }

    @Operation(summary = "반려견 정보 조회", description = "회원 ID를 통해 반려견의 정보를 목록으로 조회합니다.")
    @ApiErrorCodeExample
    @GetMapping("/{userUrl}")
    public ApiResponseDto<List<PetResponse.PetDetailDto>> findPets(@PathVariable("userUrl") String userUrl) {
        List<Pet> petsByUsername = petQueryService.getPetsByUserUrl(userUrl);
        return ApiResponseDto.onSuccess(PetConverter.toPetDetailListDto(petsByUsername));
    }

    @Operation(summary = "반려견 삭제 🔑", description = "회원의 특정 반려견의 정보를 삭제합니다.")
    @ApiErrorCodeExample(value = {
            ErrorStatus.PET_NOT_FOUND,
            ErrorStatus.MEDIA_REQUEST_IS_EMPTY
    }, status = AUTH)
    @DeleteMapping("/{petId}")
    public ApiResponseDto<Boolean> deletePet(@PathVariable("petId") Long petId,
                                             @AuthUser Member member) {
        petCommandService.deletePet(petId, member);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }

    @Operation(summary = "반려견 강제 삭제 🔑", description = "회원의 특정 반려견의 정보가 관리자에 의해 삭제됩니다.")
    @ApiErrorCodeExample(status = ADMIN)
    @DeleteMapping("/{petId}/admin")
    public ApiResponseDto<Boolean> deletePetByAdmin(@PathVariable("petId") Long petId,
                                                    @AuthUser Member admin) {
        petCommandService.deletePetByAdmin(petId, admin);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
