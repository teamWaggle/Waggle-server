package com.example.waggle.domain.member.service;

import com.example.waggle.web.dto.member.MemberSummaryDto;

public interface MemberQueryService {

    MemberSummaryDto getMemberSummaryDto(String username);

}
