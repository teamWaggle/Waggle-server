package com.example.waggle.web.converter;

import com.example.waggle.domain.board.help.entity.Help;
import com.example.waggle.web.dto.help.HelpResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class HelpConverter {

    public static HelpResponse.SummaryDto toSummaryDto(Help help) {
        return HelpResponse.SummaryDto.builder()
                .thumbnail(help.getThumbnail())
                .lostLocate(help.getLostLocate())
                .lostDate(help.getLostDate())
                .category(help.getCategory())
                .username(help.getMember().getUsername())
                .title(help.getTitle())
                .build();
    }

    public static HelpResponse.ListDto toListDto(Page<Help> pagedHelp) {
        List<HelpResponse.SummaryDto> collect = pagedHelp.stream()
                .map(HelpConverter::toSummaryDto).collect(Collectors.toList());
        return HelpResponse.ListDto.builder()
                .helpList(collect)
                .isFirst(pagedHelp.isFirst())
                .isLast(pagedHelp.isLast())
                .totalHelps(pagedHelp.getTotalElements())
                .build();
    }

    public static HelpResponse.DetailDto toDetailDto(Help help) {
        return HelpResponse.DetailDto.builder()
                .id(help.getId())
                .title(help.getTitle())
                .petKind(help.getPetKind())
                .petAge(help.getPetAge())
                .petGender(help.getPetGender())
                .contact(help.getContact())
                .thumbnail(help.getThumbnail())
                .category(help.getCategory())
                .lostLocate(help.getLostLocate())
                .lostDate(help.getLostDate())
                .content(help.getContent())
                .username(help.getMember().getUsername())
                .build();
    }
}
