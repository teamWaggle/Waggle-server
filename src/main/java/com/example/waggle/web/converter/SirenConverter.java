package com.example.waggle.web.converter;

import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.web.dto.siren.SirenResponse.SirenDetailDto;
import com.example.waggle.web.dto.siren.SirenResponse.SirenListDto;
import com.example.waggle.web.dto.siren.SirenResponse.SirenSummaryDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class SirenConverter {

    public static SirenSummaryDto toSirenSummaryDto(Siren siren) {
        return SirenSummaryDto.builder()
                .boardId(siren.getId())
                .thumbnail(MediaUtil.getThumbnail(siren))
                .lostLocate(siren.getLostLocate())
                .lostDate(siren.getLostDate())
                .createdDate(siren.getCreatedDate())
                .category(siren.getCategory())
                .title(siren.getTitle())
                .status(siren.getStatus())
                .member(MemberConverter.toMemberSummaryDto(siren.getMember()))
                .build();
    }

    public static SirenListDto toSirenListDto(Page<Siren> pagedSiren) {
        List<SirenSummaryDto> collect = pagedSiren.stream()
                .map(SirenConverter::toSirenSummaryDto).collect(Collectors.toList());
        return SirenListDto.builder()
                .sirenList(collect)
                .isFirst(pagedSiren.isFirst())
                .isLast(pagedSiren.isLast())
                .sirenCount(pagedSiren.getTotalElements())
                .build();
    }

    public static SirenDetailDto toSirenDetailDto(Siren siren) {
        return SirenDetailDto.builder()
                .boardId(siren.getId())
                .title(siren.getTitle())
                .petBreed(siren.getPetBreed())
                .petAge(siren.getPetAge())
                .petGender(siren.getPetGender())
                .contact(siren.getContact())
                .category(siren.getCategory())
                .lostLocate(siren.getLostLocate())
                .lostDate(siren.getLostDate())
                .createdDate(siren.getCreatedDate())
                .content(siren.getContent())
                .mediaList(MediaUtil.getBoardMedias(siren))
                .status(siren.getStatus())
                .member(MemberConverter.toMemberSummaryDto(siren.getMember()))
                .build();
    }
}
