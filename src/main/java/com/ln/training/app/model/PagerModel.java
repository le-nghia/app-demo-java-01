package com.ln.training.app.model;

import java.util.ArrayList;
import java.util.List;

public class PagerModel {
    public static final int DISABLE_PAGING_FLAG = 0;
    public static final int NUM_OF_MAX_DISPLAY_PAGE = 5;

    private int currentPage;
    private int totalPage;
    private int nextPage;
    private int previousPage;
    private int firstPage = 1;
    private int lastPage;
    private List<Integer> pageNumberList;

    public PagerModel(int currentPage, int totalPage) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;

        if (currentPage == this.firstPage){
            this.previousPage = DISABLE_PAGING_FLAG;
            this.firstPage = DISABLE_PAGING_FLAG;
        }else {
            this.previousPage = currentPage - 1;
        }
        if (currentPage == totalPage){
            this.lastPage = DISABLE_PAGING_FLAG;
            this.nextPage = DISABLE_PAGING_FLAG;
        }else if (totalPage > 0){
            this.lastPage = totalPage;
            this.nextPage = currentPage + 1;
        }
        this.pageNumberList = getPageNumberList(currentPage, totalPage);
    }


    private List<Integer> getPageNumberList(int currentPage, int totalPage) {
        List<Integer> pageNumberList = new ArrayList<>();

        int pageMin, pageMax;
        pageMin = currentPage - (PagerModel.NUM_OF_MAX_DISPLAY_PAGE - 1 ) / 2;
        pageMax = currentPage + (PagerModel.NUM_OF_MAX_DISPLAY_PAGE - 1 ) / 2;

        if (pageMin <= 0 ){
            pageMin = 1;
            pageMax = PagerModel.NUM_OF_MAX_DISPLAY_PAGE;
        }

        if (pageMax > totalPage){
            pageMax = totalPage;
            pageMin = totalPage - PagerModel.NUM_OF_MAX_DISPLAY_PAGE + 1;
        }

        for (int i = pageMin; i <= pageMax; i++) {
            if (i>0){
                pageNumberList.add(i);
            }
        }
        return pageNumberList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<Integer> getPageNumberList() {
        return pageNumberList;
    }

    public void setPageNumberList(List<Integer> pageNumberList) {
        this.pageNumberList = pageNumberList;
    }
}
