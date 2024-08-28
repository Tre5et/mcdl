import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.util.FileUtil;

import java.util.List;
import java.util.Map;

public class Web {
    public static void main(String... args) {
        try {
            FileUtil.useWebRequestCache(true);
            byte[] res = FileUtil.getFromHttpGet("https://httpbin.org/anything", List.of(Map.entry("header1", "val1"), Map.entry("header2", "val2")), List.of(Map.entry("arg1", "val1"), Map.entry("arg2", "val2")));

            if(res.length == 0) {
                System.err.println("Web:testWebRequests FAILED: no response");
                return;
            }

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
            if(foundArgs != 2) {
                System.err.println("Web:testWebRequests FAILED: args not found");
                return;
            }
            if(foundHeaders != 2) {
                System.err.println("Web:testWebRequests FAILED: headers not found");
                return;
            }
            System.out.println("Web:testWebRequests PASSED");
        } catch (FileDownloadException e) {
            System.err.println("Web:testWebRequests FAILED: " + e.getMessage());
        }
    }
}
