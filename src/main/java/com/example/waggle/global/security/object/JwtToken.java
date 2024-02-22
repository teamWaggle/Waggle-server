package com.example.waggle.global.security.object;

import com.example.waggle.web.dto.member.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    private String grantType;   // JWT에 대한 인증 타입. Bearer 사용. 이후 HTTP 헤더에 prefix로 붙여줌
    private String accessToken;
    private String refreshToken;

    private MemberResponse.SummaryDto member;
}
