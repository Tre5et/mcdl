package dev.treset.mcdl.auth;

import com.microsoft.aad.msal4j.DeviceCode;

public class InteractiveData {
    private String url;
    private String userCode;
    private long expiresIn;
    private String message;
    private long interval;
    private String deviceCode;

    public InteractiveData(String url, String userCode, long expiresIn, String message, long interval, String deviceCode) {
        this.url = url;
        this.userCode = userCode;
        this.expiresIn = expiresIn;
        this.message = message;
        this.interval = interval;
        this.deviceCode = deviceCode;
    }

    public static InteractiveData fromDeviceCode(DeviceCode deviceCode) {
        return new InteractiveData(
                deviceCode.verificationUri(),
                deviceCode.userCode(),
                deviceCode.expiresIn(),
                deviceCode.message(),
                deviceCode.interval(),
                deviceCode.deviceCode()
        );
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
