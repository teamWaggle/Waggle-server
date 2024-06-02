package com.example.waggle.domain.board.application.siren;

import com.example.waggle.domain.board.persistence.entity.Siren;
import com.example.waggle.domain.board.presentation.dto.siren.SirenFilterParam;
import com.example.waggle.domain.board.presentation.dto.siren.SirenSortParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SirenQueryService {

    List<Siren> getRepresentativeSirenList();

    Page<Siren> getPagedSirenListByUserUrl(String userUrl, Pageable pageable);

    Siren getSirenByBoardId(Long boardId);

    List<Siren> getRandomUnresolvedSirenList();

    Page<Siren> getPagedSirenListBySearchingAndSorting(String keyword,
                                                       SirenSortParam sortParam,
                                                       SirenFilterParam filterParam,
                                                       Pageable pageable);

}