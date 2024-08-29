package net.treset.mcdl.util;

public class DownloadStatus {
    private int currentAmount;
    private int totalAmount;
    private String currentFile;

    public DownloadStatus(int currentAmount, int totalAmount, String currentFile) {
        this.currentAmount = currentAmount;
        this.totalAmount = totalAmount;
        this.currentFile = currentFile;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(String currentFile) {
        this.currentFile = currentFile;
    }
}
