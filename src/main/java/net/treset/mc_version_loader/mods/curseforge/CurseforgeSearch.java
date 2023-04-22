package net.treset.mc_version_loader.mods.curseforge;

import java.util.List;

public class CurseforgeSearch {
    private List<CurseforgeSearchData> data;
    private int paginationIndex;
    private int pageSize;
    private int resultsCount;
    private int totalResults;

    public CurseforgeSearch(List<CurseforgeSearchData> data, int paginationIndex, int pageSize, int resultsCount, int totalResults) {
        this.data = data;
        this.paginationIndex = paginationIndex;
        this.pageSize = pageSize;
        this.resultsCount = resultsCount;
        this.totalResults = totalResults;
    }

    public List<CurseforgeSearchData> getData() {
        return data;
    }

    public void setData(List<CurseforgeSearchData> data) {
        this.data = data;
    }

    public int getPaginationIndex() {
        return paginationIndex;
    }

    public void setPaginationIndex(int paginationIndex) {
        this.paginationIndex = paginationIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getResultsCount() {
        return resultsCount;
    }

    public void setResultsCount(int resultsCount) {
        this.resultsCount = resultsCount;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
