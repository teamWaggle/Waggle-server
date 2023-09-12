package com.example.waggle.dto.board.page;

import com.example.waggle.dto.board.question.QuestionSimpleViewDto;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Pagination {
    //board list size
    private int total;
    //한 페이지 내에서 나타낼 게시글의 개수(행 개수)
    private int colSize;
    //현재 페이지
    private int currentPage;
    //총 페이지 개수
    private int totalPages;
    //시작 페이지(->페이지의 이동에 따라 매번 달라질 수 있음)
    //ex. prev [1][2][3] next ==> prev [4][5][6] next
    private int startPage;
    //이하동문
    private int endPage;
    //페이징할 페이지 개수
    private int pagingCount;
    private List<QuestionSimpleViewDto> questionViewlist = new ArrayList<>();

    @Builder
    public Pagination(int total, int colSize, int currentPage,
                      int pagingCount, List<QuestionSimpleViewDto> questionViewlist) {
        this.total = total;
        this.colSize = colSize;
        this.currentPage = currentPage;
        this.pagingCount = pagingCount;
        this.questionViewlist = questionViewlist;

        //list.size() == 0
        if (total == 0) {
            //page 하나만 띄워준다.
            totalPages = 1;
            startPage = 1;
            endPage = 1;
        }
        else {
            //totalPages set
            //총 페이지 수 = 데이터 개수 / 한 페이지당 나타낼 데이터 개수
            totalPages = total/colSize;
            if (total % colSize > 0) {
                //해당 조건에서는 +1
                totalPages++;
            }

            //startPage set
            //현재 페이지 / 페이지 개수 = 페이징할 페이지 개수 묶음 - 1
            //말이 복잡한데 예를 들어 pagingCount = 5면 5를 간격으로 데이터가 몇 묶음이 나오는지 알 수 있는 것이다.
            //(페이징할 페이지 개수 묶음 - 1) * 페이징 카운트 = 묶음 * 카운트 = 시작점 - 1
            startPage = currentPage / pagingCount * pagingCount + 1;
            if (currentPage % pagingCount == 0) {
                //현재 페이지가 마지막 페이지
                startPage -= pagingCount;
            }

            //endPage set
            endPage = startPage + pagingCount - 1 ;
            if(endPage > totalPages) {
                //총 페이지가 페이징 카운트를 채울 만큼 많지 않으면
                //마지막페이지는 총 페이지가 되는 것이다.
                endPage = totalPages;
            }
        }
    }
}
