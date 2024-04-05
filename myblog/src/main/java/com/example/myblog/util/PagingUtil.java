package com.example.myblog.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class PagingUtil {
    private int dataCount; // 총 게시물 개수
    private int numPerPage; // 페이지당 보여줄 데이터의 개수
    private int totalPage; // 페이지의 전체 개수
    private int currentPage = 1; // 현재 페이지(디폴트는 첫번째 페이지를 보여주므로 1)
    private int start; // rownum의 시작값
    private int end; // rownum의 끝값

    public void resetPaging(int dataCount, int numPerPage) {
        this.dataCount = dataCount;
        this.numPerPage = numPerPage;
        this.totalPage = getPageCount();

        // 현재 페이지가 totalPage보다 큰 경우는 있을 수 없으므로
        if (this.currentPage > this.totalPage) this.currentPage = this.totalPage;

        this.start = (this.currentPage - 1) * numPerPage + 1; // 1 6 11 16 ...
        this.end = this.currentPage * numPerPage; // 5 10 15 20 ...
    }

    // 전체 페이지 개수 구하는 메소드
    public int getPageCount() {
        int pageCount = 0;
        pageCount = dataCount / numPerPage;

        if (dataCount % numPerPage != 0) pageCount++;

        return pageCount; // 전체 페이지 개수
    }

    // 페이징 버튼을 만들어주는 메소드
    public String pageIndexList(String listUrl) {
        // 문자열 데이터를 자주 추가해주거나 삭제할 때 메모리 낭비 방지를 위해 사용
        StringBuffer sb = new StringBuffer();

        int numPerBlock = 5; // ◀이전과 다음▶ 사이의 숫자를 몇 개로 표시할 것인지
        int currentPageSetup; // ◀이전 버튼에 들어갈 pageNum 값
        int page; // 그냥 페이지 숫자를 클릭했을 때 들어갈 pageNum 값

        if (currentPage == 0 || totalPage == 0) return ""; // 데이터가 없을 때

        if (listUrl.indexOf("?") != -1) { // 쿼리스트링이 있을 때(검색어가 있을 때)
            listUrl += "&"; // /list?searchKey=subject&searchValue=네번째&
        } else { // 쿼리스트링이 없을 때(검색어가 없을 때)
            listUrl += "?"; // /list?
        }
        // 1. ◀이전 버튼 만들기
        // currentPage = 1,2,3,4,5 -> currentPageSetup = 0
        // currentPage = 6 -> currentPageSetup = 5
        // currentPage = 11 -> currentPageSetup = 10
        currentPageSetup = (currentPage / numPerBlock) * numPerBlock;

        if (currentPage % numPerBlock == 0) {
            currentPageSetup = currentPageSetup - numPerBlock;
        }

        // 검색어가 있을 때: /list?searchKey=subject&searchValue=네번째&pageNum=1
        // 검색어가 없을 때: /list?pageNum=1
        if (totalPage > numPerBlock && currentPageSetup > 0) {
            sb.append("<li class=\"page-item\">" +
                    "       <a class=\"page-link\" href=\"" + listUrl + "pageNum=" + currentPageSetup + "\" " +
                    "aria-label=\"Previous\">" +
                    "            <span aria-hidden=\"true\">이전</span>" +
                    "       </a>" +
                    " </li>");
        }

        // 2. 그냥 페이지(6 7 8 9 10) 이동 버튼 만들기
        // currentPage = 1,2,3,4,5 -> currentPageSetup = 0, page = 1
        // currentPage = 6 -> currentPageSetup = 5, page = 6
        // currentPage = 11 -> currentPageSetup = 10, page = 11
        page = currentPageSetup + 1;

        while (page <= totalPage && page <= (currentPageSetup + numPerBlock)) {
            if (page == currentPage) {
                // 현재 내가 선택한 페이지일 때
                sb.append("<li class=\"page-item active\">" +
                        "<a class=\"page-link\" href=\"" + listUrl + "pageNum=" + page + "\">" + page + "</a>" +
                        "</li>");
            } else {
                // 현재 내가 선택한 페이지가 아닐 때
                sb.append("<li class=\"page-item\">" +
                        "<a class=\"page-link\" href=\"" + listUrl + "pageNum=" + page + "\">" + page + "</a>" +
                        "</li>");
            }

            page++; // 페이지 번호 1씩 증가
        }

        // 3. 다음▶ 버튼 만들기
        if (totalPage - currentPageSetup > numPerBlock) {
            // pageNum: 6, 11, 16, 21, ...
            sb.append("<li class=\"page-item\">" +
                    "     <a class=\"page-link\" href=\"" + listUrl + "pageNum=" + (currentPageSetup + numPerBlock + 1) + "\" aria-label=\"Next\">" +
                    "         <span aria-hidden=\"true\">다음</span>" +
                    "     </a>" +
                    " </li>");
        }

        // 4. 버튼 합쳐서 문자열로 리턴
        return sb.toString();
    }
}
