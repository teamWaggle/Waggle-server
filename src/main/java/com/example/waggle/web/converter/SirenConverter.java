package com.example.waggle.web.converter;

import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.global.util.SecurityUtil;
import com.example.waggle.web.dto.siren.SirenResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class SirenConverter {

    public static SirenResponse.SummaryDto toSummaryDto(Siren siren) {
        return SirenResponse.SummaryDto.builder()
                .id(siren.getId())
                .thumbnail(MediaUtil.getThumbnail(siren))
                .lostLocate(siren.getLostLocate())
                .lostDate(siren.getLostDate())
                .createdDate(siren.getCreatedDate())
                .category(siren.getCategory())
                .title(siren.getTitle())
                .status(siren.getStatus())
                .member(MemberConverter.toMemberSummaryDto(siren.getMember()))
                .isMine(siren.getMember().getUsername().equals(SecurityUtil.getCurrentUsername()))
                .build();
    }

    public static SirenResponse.ListDto toListDto(Page<Siren> pagedSiren) {
        List<SirenResponse.SummaryDto> collect = pagedSiren.stream()
                .map(SirenConverter::toSummaryDto).collect(Collectors.toList());
        return SirenResponse.ListDto.builder()
                .sirenList(collect)
                .isFirst(pagedSiren.isFirst())
                .isLast(pagedSiren.isLast())
                .totalSirens(pagedSiren.getTotalElements())
                .build();
    }

    public static SirenResponse.DetailDto toDetailDto(Siren siren) {
        return SirenResponse.DetailDto.builder()
                .id(siren.getId())
                .title(siren.getTitle())
                .petKind(siren.getPetKind())
                .petAge(siren.getPetAge())
                .petGender(siren.getPetGender())
                .contact(siren.getContact())
                .category(siren.getCategory())
                .lostLocate(siren.getLostLocate())
                .lostDate(siren.getLostDate())
                .createdDate(siren.getCreatedDate())
                .content(siren.getContent())
                .medias(MediaUtil.getBoardMedias(siren))
                .status(siren.getStatus())
                .member(MemberConverter.toMemberSummaryDto(siren.getMember()))
                .isMine(siren.getMember().getUsername().equals(SecurityUtil.getCurrentUsername()))
                .build();
    }
}
