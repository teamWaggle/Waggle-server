package com.example.waggle.global;

import com.example.waggle.domain.board.story.dto.StorySummaryDto;
import com.example.waggle.global.dto.page.Pagination;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PagingTest {
    @Test
    void 총데이터_개수_페이징개수보다_부족() {
        List<StorySummaryDto> stories = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            stories.add(new StorySummaryDto());
        }
        Pagination pagination = new Pagination(1,stories);

        assertThat(pagination.getStartPage()).isEqualTo(1);
        assertThat(pagination.getEndPage()).isEqualTo(4);
        assertThat(pagination.getTotalPages()).isEqualTo(4);

        Pagination pagination1 = new Pagination(3, stories);

        assertThat(pagination1.getStartPage()).isEqualTo(1);
        assertThat(pagination1.getEndPage()).isEqualTo(4);
        assertThat(pagination1.getTotalPages()).isEqualTo(4);
    }
    @Test
    void 총데이터_개수_페이징개수보다_초과() {
        List<StorySummaryDto> stories = new ArrayList<>();
        for (int i = 0; i < 123; i++) {
            stories.add(new StorySummaryDto());
        }
        Pagination pagination = new Pagination(1, stories);

        assertThat(pagination.getStartPage()).isEqualTo(1);
        assertThat(pagination.getEndPage()).isEqualTo(5);
        assertThat(pagination.getTotalPages()).isEqualTo(13);

        Pagination pagination1 = new Pagination(4, stories);

        assertThat(pagination1.getStartPage()).isEqualTo(1);
        assertThat(pagination1.getEndPage()).isEqualTo(5);
        assertThat(pagination1.getTotalPages()).isEqualTo(13);

        Pagination pagination2 = new Pagination(7, stories);

        assertThat(pagination2.getStartPage()).isEqualTo(6);
        assertThat(pagination2.getEndPage()).isEqualTo(10);
        assertThat(pagination2.getTotalPages()).isEqualTo(13);

        Pagination pagination3 = new Pagination(11, stories);

        assertThat(pagination3.getStartPage()).isEqualTo(11);
        assertThat(pagination3.getEndPage()).isEqualTo(13);
        assertThat(pagination3.getTotalPages()).isEqualTo(13);

    }
    @Test
    void 총데이터_개수_한개() {
        List<StorySummaryDto> stories = new ArrayList<>();
        stories.add(new StorySummaryDto());
        Pagination pagination = new Pagination(1, stories);

        assertThat(pagination.getStartPage()).isEqualTo(1);
        assertThat(pagination.getEndPage()).isEqualTo(1);
        assertThat(pagination.getTotalPages()).isEqualTo(1);
    }


}
