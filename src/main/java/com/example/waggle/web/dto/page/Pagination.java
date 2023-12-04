package com.example.waggle.web.dto.page;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class Pagination {
    //===already set value===

    private int total;
    //한 페이지 내에서 나타낼 게시글의 개수(행 개수)
    private int colSize;
    private int currentPage;
    //페이징할 페이지 개수
    private int pagingCount;

    //===require to set value===

    private int totalPages;
    //시작 페이지(->페이지의 이동에 따라 매번 달라질 수 있음)
    //ex. prev [1][2][3] next ==> prev [4][5][6] next
    private int startPage;
    //이하동문
    private int endPage;
    private List<?> boardList;

    @Builder
    public Pagination(int currentPage, List<?> boardList) {
        colSize = 10;
        pagingCount = 5;

        this.boardList = boardList;
        this.total = boardList.size();
        this.currentPage = currentPage;

        if (total == 0) {
            //page 하나만 띄워준다.
            totalPages = 1;
            startPage = 1;
            endPage = 1;
        }
        else {
            totalPages = total/colSize;
            if (total % colSize > 0) {
                totalPages++;
            }

            startPage = currentPage / pagingCount * pagingCount + 1;
            if (currentPage % pagingCount == 0) {
                //현재 페이지가 마지막 페이지
                startPage -= pagingCount;
            }

            endPage = startPage + pagingCount - 1 ;
            if(endPage > totalPages) {
                //총 페이지가 페이징 카운트를 채울 만큼 많지 않으면
                //마지막페이지는 총 페이지가 되는 것이다.
                endPage = totalPages;
            }
        }
    }
}
