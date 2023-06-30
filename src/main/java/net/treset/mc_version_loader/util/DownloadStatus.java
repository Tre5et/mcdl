package net.treset.mc_version_loader.util;

public class DownloadStatus {
    private int currentAmount;
    private int totalAmount;
    private String currentFile;
    private boolean failure;

    public DownloadStatus(int currentAmount, int totalAmount, String currentFile, boolean failure) {
        this.currentAmount = currentAmount;
        this.totalAmount = totalAmount;
        this.currentFile = currentFile;
        this.failure = failure;
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

    public boolean isFailure() {
        return failure;
    }

    public void setFailure(boolean failure) {
        this.failure = failure;
    }
}
