package com.example.waggle.common;

import com.example.waggle.commons.dto.page.Pagination;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PagingTest {
    @Test
    void 총데이터_개수_페이징개수보다_부족() {
        Pagination pagination = new Pagination(33, 1);

        assertThat(pagination.getStartPage()).isEqualTo(1);
        assertThat(pagination.getEndPage()).isEqualTo(4);
        assertThat(pagination.getTotalPages()).isEqualTo(4);

        Pagination pagination1 = new Pagination(33, 3);

        assertThat(pagination1.getStartPage()).isEqualTo(1);
        assertThat(pagination1.getEndPage()).isEqualTo(4);
        assertThat(pagination1.getTotalPages()).isEqualTo(4);
    }
    @Test
    void 총데이터_개수_페이징개수보다_초과() {
        Pagination pagination = new Pagination(123, 1);

        assertThat(pagination.getStartPage()).isEqualTo(1);
        assertThat(pagination.getEndPage()).isEqualTo(5);
        assertThat(pagination.getTotalPages()).isEqualTo(13);

        Pagination pagination1 = new Pagination(123, 4);

        assertThat(pagination1.getStartPage()).isEqualTo(1);
        assertThat(pagination1.getEndPage()).isEqualTo(5);
        assertThat(pagination1.getTotalPages()).isEqualTo(13);

        Pagination pagination2 = new Pagination(123, 7);

        assertThat(pagination2.getStartPage()).isEqualTo(6);
        assertThat(pagination2.getEndPage()).isEqualTo(10);
        assertThat(pagination2.getTotalPages()).isEqualTo(13);

        Pagination pagination3 = new Pagination(123, 11);

        assertThat(pagination3.getStartPage()).isEqualTo(11);
        assertThat(pagination3.getEndPage()).isEqualTo(13);
        assertThat(pagination3.getTotalPages()).isEqualTo(13);

    }
    @Test
    void 총데이터_개수_한개() {
        Pagination pagination = new Pagination(1, 1);

        assertThat(pagination.getStartPage()).isEqualTo(1);
        assertThat(pagination.getEndPage()).isEqualTo(1);
        assertThat(pagination.getTotalPages()).isEqualTo(1);
    }


}
