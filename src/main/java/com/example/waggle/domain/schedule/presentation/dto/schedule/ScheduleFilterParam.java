package com.example.waggle.domain.board.presentation.dto.siren;

import com.example.waggle.domain.board.persistence.entity.SirenCategory;
import lombok.Getter;

@Getter
public enum SirenFilterParam {
    FIND_PET(SirenCategory.FIND_PET),
    FIND_OWNER(SirenCategory.FIND_OWNER),
    PROTECT(SirenCategory.PROTECT),
    ETC(SirenCategory.ETC),
    ALL(null);

    private final SirenCategory category;

    SirenFilterParam(SirenCategory category) {
        this.category = category;
    }

}
