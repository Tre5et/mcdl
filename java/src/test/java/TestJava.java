import net.treset.mcdl.java.JavaFile;
import net.treset.mcdl.java.JavaRuntimeRelease;
import net.treset.mcdl.java.JavaRuntimes;
import net.treset.mcdl.java.JavaDL;
import dev.treset.mcdl.util.FileUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestJava {
    @ParameterizedTest
    @ValueSource(strings = {"java-runtime-gamma", "jre-legacy"})
    public void testJavaVersion(String id) {
        if(new File("download/"+id).exists()) {
            assertDoesNotThrow(() -> FileUtil.delete(new File("download/"+id)));
        }

        JavaRuntimes runtimes = assertDoesNotThrow(JavaDL::getJavaRuntimes);
        Map<String, List<JavaRuntimeRelease>> release = runtimes.get("windows-x64");
        assertNotNull(release, "Windows java runtimes not found");
        List<JavaRuntimeRelease> versions = release.get(id);
        assertNotNull(versions, "Java runtime not found: " + id);
        for(JavaRuntimeRelease version : versions) {
            assertNotNull(version.getAvailability(), "Availability not found: " + id);
            assertNotNull(version.getVersion(), "Version not found: " + id);
            assertNotNull(version.getManifest(), "Manifest not found: " + id);
            assertNotNull(version.getManifest().getUrl(), "Manifest url not found: " + id);

            List<JavaFile> files = assertDoesNotThrow(() -> JavaDL.getJavaFiles(version.getManifest().getUrl()));
            assertNotNull(files, "Java files not found: " + id);
            assertFalse(files.isEmpty(), "No java files found: " + id);

            File dir = new File("download/"+id+"/"+version.getManifest().getSha1());
            dir.mkdirs();

            assertDoesNotThrow(() -> JavaDL.downloadJavaFiles(files, dir));
            assertTrue(dir.isDirectory(), "Downloaded file not found: " + id);
            assertTrue(dir.listFiles().length > 0, "No files downloaded: " + id);
            assertTrue(new File(dir, "bin/java.exe").isFile(), "Java executable not found: " + id);
        }
    }
}
