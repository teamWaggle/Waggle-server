package com.example.waggle.member.service;

import com.example.waggle.member.dto.MemberSummaryDto;

public interface MemberQueryService {

    MemberSummaryDto getMemberSummaryDto(String username);

}
