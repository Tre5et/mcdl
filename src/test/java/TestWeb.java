import dev.treset.mcdl.util.HttpUtil;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestWeb {
    @Test
    public void testRequest() {
        byte[] res = assertDoesNotThrow(() -> HttpUtil.get(new URL("https://httpbin.org/anything"), Map.of("header1", "val1", "header2", "val2"), Map.of("arg1", "val1", "arg2", "val2")).body());
        assertTrue(res.length > 0, "no response");
        String response = new String(res);

        boolean inArgs = false;
        boolean inHeaders = false;
        int foundArgs = 0;
        int foundHeaders = 0;
        for(String l: response.split("\n")) {
            if(l.contains("args")) {
                inArgs = true;
            } else if(l.contains("headers")) {
                inHeaders = true;
            } else if(l.contains("}")) {
                inArgs = false;
                inHeaders = false;
            } else if(inArgs) {
                if(l.contains("arg1") && l.contains("val1")) {
                    foundArgs++;
                } else if(l.contains("arg2") && l.contains("val2")) {
                    foundArgs++;
                }
            } else if(inHeaders) {
                if(l.contains("Header1") && l.contains("val1")) {
                    foundHeaders++;
                } else if(l.contains("Header2") && l.contains("val2")) {
                    foundHeaders++;
                }
            }
        }
        assertEquals(2, foundArgs, "args not found");
        assertEquals(2, foundHeaders, "headers not found");
    }
}
