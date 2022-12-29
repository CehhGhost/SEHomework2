import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DependencyList {
    public DependencyList(File mainDir) {
        _mainDir = mainDir;
        SearchThroughDirs(_mainDir);
    }
    private void SearchThroughDirs(File dir) {
        var files = dir.listFiles();
        if (files != null) {
            for (var f : files) {
                if (f.isDirectory()) {
                    SearchThroughDirs(f);
                } else if (f.isFile()) {
                    GetFilesParents(f);
                }
            }
        }
    }
    private void GetFilesParents(File file) {
    }
    private final File _mainDir;
    private Map<Node, List<File>> _resultMap;
}
