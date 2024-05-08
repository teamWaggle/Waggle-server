package com.example.waggle.domain.board.presentation.converter;

import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenSummaryListDto;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenDetailDto;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenPagedSummaryListDto;
import com.example.waggle.domain.board.presentation.dto.siren.SirenResponse.SirenSummaryDto;
import com.example.waggle.global.util.MediaUtil;
import com.example.waggle.domain.member.presentation.converter.MemberConverter;
import com.example.waggle.global.util.PageUtil;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class SirenConverter {

    public static SirenSummaryDto toSirenSummaryDto(Siren siren) {
        return SirenSummaryDto.builder()
                .boardId(siren.getId())
                .thumbnail(MediaUtil.getThumbnail(siren))
                .lostLocate(siren.getLostLocate())
                .createdDate(siren.getCreatedDate())
                .category(siren.getCategory())
                .title(siren.getTitle())
                .status(siren.getStatus())
                .build();
    }

    public static SirenPagedSummaryListDto toSirenPageDto(Page<Siren> pagedSiren) {
        List<SirenSummaryDto> collect = pagedSiren.stream()
                .map(SirenConverter::toSirenSummaryDto).collect(Collectors.toList());
        return SirenPagedSummaryListDto.builder()
                .sirenList(collect)
                .nextPageParam(PageUtil.countNextPage(pagedSiren))
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
                .viewCount(siren.getViewCount())
                .build();
    }

    public static SirenSummaryListDto toSirenSummaryListDto(List<Siren> sirenList) {
        List<SirenSummaryDto> collect = sirenList.stream()
                .map(SirenConverter::toSirenSummaryDto).collect(Collectors.toList());
        return SirenSummaryListDto.builder()
                .sirenList(collect)
                .build();
    }
}
