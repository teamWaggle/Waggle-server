package com.example.waggle.domain.schedule.presentation.dto.schedule;

import com.example.waggle.global.annotation.validation.localTime.ValidTimeForm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class ScheduleRequest {

    @NotEmpty(message = "제목을 입력해주세요.")
    @Size(max = 30, message = "제목은 최대 30자까지 입력할 수 있습니다.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요.")
    @Size(max = 30, message = "내용은 최대 30자까지 입력할 수 있습니다.")
    private String content;

    @NotNull(message = "시작 날짜를 입력해주세요.")
    private LocalDate startDate;

    @NotNull(message = "종료 날짜를 입력해주세요.")
    private LocalDate endDate;

    @NotNull(message = "시작 시간을 입력해주세요.")
    @ValidTimeForm
    @Schema(description = "시작 시간 (HH:mm 형식)")
    private String startTime;

    @NotNull(message = "종료 시간을 입력해주세요.")
    @ValidTimeForm
    @Schema(description = "종료 시간 (HH:mm 형식)")
    private String endTime;
}
