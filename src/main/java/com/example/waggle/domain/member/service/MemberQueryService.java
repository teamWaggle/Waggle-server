package com.example.waggle.domain.member.service;

import com.example.waggle.domain.member.dto.MemberSummaryDto;

public interface MemberQueryService {

    MemberSummaryDto getMemberSummaryDto(String username);

}
