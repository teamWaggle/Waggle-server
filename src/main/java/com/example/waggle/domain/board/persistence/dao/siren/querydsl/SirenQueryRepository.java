package com.example.waggle.domain.board.persistence.dao.siren.querydsl;

import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.presentation.dto.siren.SirenFilterParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SirenQueryRepository {

    Page<Siren> findSirensByFilter(SirenFilterParam filterParam, Pageable pageable);
}
