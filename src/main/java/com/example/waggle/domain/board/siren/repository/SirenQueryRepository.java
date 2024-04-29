package com.example.waggle.domain.board.siren.repository;

import com.example.waggle.domain.board.siren.entity.Siren;
import com.example.waggle.web.dto.siren.SirenFilterParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SirenQueryRepository {

    Page<Siren> findSirensByFilter(SirenFilterParam filterParam, Pageable pageable);
}
