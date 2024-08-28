package net.treset.mcdl.auth.data;

import com.google.gson.annotations.SerializedName;
import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.SerializationException;

import java.util.Set;

public class XstsError extends GenericJsonParsable {
    @SerializedName("Identity")
    private String identity;
    @SerializedName("XErr")
    private Long xErr;
    @SerializedName("Message")
    private String message;
    @SerializedName("Redirect")
    private String redirect;

    public enum Error {
        NO_XBOX_ACCOUNT(Set.of(2148916233L), "No Xbox account found with that username"),
        UNAVAILABLE_REGION(Set.of(2148916235L), "The service is unavailable in the account region"),
        ADULT_VERIFICATION(Set.of(2148916236L, 2148916237L), "Adult verification required"),
        CHILD_ACCOUNT(Set.of(2148916238L), "The account is under 18 years old and not part of a family."),
        UNKNOWN(Set.of(0L), "Unknown error");

        private final Set<Long> codes;
        private final String message;

        Error(Set<Long> codes, String message) {
            this.codes = codes;
            this.message = message;
        }

        public Set<Long> getCodes() {
            return codes;
        }

        public String getMessage() {
            return message;
        }
    }

    public XstsError(String identity, Long xErr, String message, String redirect) {
        this.identity = identity;
        this.xErr = xErr;
        this.message = message;
        this.redirect = redirect;
    }

    public static XstsError fromJson(String json) throws SerializationException {
        return fromJson(json, XstsError.class);
    }

    public Error getError() {
        for (Error error : Error.values()) {
            if (error.getCodes().contains(xErr)) {
                return error;
            }
        }
        return Error.UNKNOWN;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Long getxErr() {
        return xErr;
    }

    public void setxErr(Long xErr) {
        this.xErr = xErr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
