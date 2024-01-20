package com.example.waggle.web.controller;

import com.example.waggle.domain.media.service.AwsS3Service;
import com.example.waggle.domain.pet.entity.Pet;
import com.example.waggle.domain.pet.service.PetCommandService;
import com.example.waggle.domain.pet.service.PetQueryService;
import com.example.waggle.global.payload.ApiResponseDto;
import com.example.waggle.web.converter.PetConverter;
import com.example.waggle.web.dto.pet.PetRequest;
import com.example.waggle.web.dto.pet.PetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/pets")
@RestController
@Tag(name = "Pet API", description = "반려견 API")
public class PetApiController {

    private final PetCommandService petCommandService;
    private final PetQueryService petQueryService;
    private final AwsS3Service awsS3Service;

    @Operation(summary = "반려견 정보 입력", description = "반려견 정보를 입력합니다. 입력한 반려견의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "반려견 정보 입력 성공. 입력한 반려견 고유의 ID를 반환.")
    @ApiResponse(responseCode = "400", description = "정보 입력 실패. 잘못된 요청 또는 파일 저장 실패.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> createPet(@RequestPart PetRequest.Post request,
                                          @RequestPart MultipartFile profileImg) throws IOException {
        String profileImgUrl = null;
        if (profileImg != null) {
            profileImgUrl = awsS3Service.uploadFile(profileImg);
        }
        Long petId = petCommandService.createPet(request);
        return ApiResponseDto.onSuccess(petId);
    }

    @Operation(summary = "반려견 정보 다수 입력", description = "회원가입 시 반려견 정보를 목록으로 입력합니다. 입력한 반려견들의 주인 이름을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "반려견 정보 입력 성공. 입력한 반려견들의 주인 이름 반환.")
    @ApiResponse(responseCode = "400", description = "정보 입력 실패. 잘못된 요청 또는 파일 저장 실패.")
    @PostMapping("/{username}")
    public ApiResponseDto<String> createPets(@RequestPart List<PetRequest.Post> requests,
                                             @PathVariable String username) throws IOException {
        //TODO list profile save
        petCommandService.createPets(requests, username);
        return ApiResponseDto.onSuccess(username);
    }

    @Operation(summary = "반려견 정보 수정", description = "반려견 정보를 수정합니다. 입력한 반려견의 고유 ID를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "반려견 정보 수정 성공. 입력한 반려견 고유의 ID를 반환.")
    @ApiResponse(responseCode = "400", description = "정보 수정 실패. 잘못된 요청 또는 파일 저장 실패.")
    @PutMapping(value = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponseDto<Long> updatePet(@RequestPart PetRequest.Post request,
                                          @RequestPart MultipartFile profileImg,
                                          @PathVariable Long petId) throws IOException {
        String profileImgUrl = null;
        if (profileImg != null) {
            profileImgUrl = awsS3Service.uploadFile(profileImg);
        }
        Long result = petCommandService.updatePet(petId, request);
        return ApiResponseDto.onSuccess(result);
    }

    @Operation(summary = "반려견 정보 조회", description = "사용자의 정보를 통해 반려견 정보를 목록으로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "반려견 정보 조회 성공. 반려견 정보들을 목록으로 반환.")
    @ApiResponse(responseCode = "400", description = "정보 조회 실패. 잘못된 요청 혹은 존재하지 않는 유저")
    @GetMapping("/{username}")
    public ApiResponseDto<List<PetResponse.SummaryDto>> findPets(@PathVariable String username) {
        List<Pet> petsByUsername = petQueryService.getPetsByUsername(username);
        return ApiResponseDto.onSuccess(PetConverter.toListDto(petsByUsername));
    }

    @Operation(summary = "펫 삭제", description = "회원의 특정 펫 정보를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "펫 삭제 성공.")
    @ApiResponse(responseCode = "404", description = "펫정보를 찾을 수 없거나 인증 정보가 펫을 소유한 유저와 일치하지 않습니다.")
    @DeleteMapping
    public ApiResponseDto<Boolean> deletePet(@PathParam("petId") Long petId) {
        petCommandService.deletePet(petId);
        return ApiResponseDto.onSuccess(Boolean.TRUE);
    }
}
