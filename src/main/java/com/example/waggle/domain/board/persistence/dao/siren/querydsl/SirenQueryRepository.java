package com.example.waggle.domain.board.persistence.dao.siren.querydsl;

import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.presentation.dto.siren.SirenFilterParam;
import com.example.waggle.domain.board.presentation.dto.siren.SirenSortParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SirenQueryRepository {

    Page<Siren> findSirensByFilterAndSort(SirenFilterParam filterParam, SirenSortParam sortParam, Pageable pageable);

    List<Siren> findRandomUnresolvedSirens();

    Page<Siren> findSirensForSearching(String keyword, Pageable pageable);

}
