package dev.treset.mcdl.mods.curseforge;

public class CurseforgePagination {
    private int index;
    private int pageSize;
    private int resultCount;
    private int totalCount;

    public CurseforgePagination(int index, int pageSize, int resultCount, int totalCount) {
        this.index = index;
        this.pageSize = pageSize;
        this.resultCount = resultCount;
        this.totalCount = totalCount;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
