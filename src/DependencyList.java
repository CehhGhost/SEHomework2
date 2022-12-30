import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyList {
    public DependencyList(File mainDir) {
        _mainDir = mainDir;
        _resultMap = new HashMap<String, List<String>>();
        _resultMap.put(null, new ArrayList<String>());
        SearchThroughDirs(_mainDir);
    }
    private void SearchThroughDirs(File dir){
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
        _resultMap.get(null).add(file.getAbsolutePath());
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String line;
            do {
                line = fileReader.readLine();
                if (line.contains("require")) {
                    int index = line.indexOf('‘');
                    StringBuilder fileDir = new StringBuilder();
                    for (int i = line.indexOf('‘') + 1; line.charAt(i) != '’'; ++i) {
                        fileDir.append(line.charAt(i));
                    }
                    fileDir.insert(0, _mainDir.getParent());
                    if (_resultMap.containsKey(file.getAbsolutePath())) {
                        _resultMap.get(file.getAbsolutePath()).add(fileDir.toString());
                    } else {
                        _resultMap.put(file.getAbsolutePath(), new ArrayList<String>());
                    }
                    /*String
                    for ()*/
                }
            } while (line != null);
        } catch (IOException e) {
            return;
        }

    }
    private final File _mainDir;
    private Map<String, List<String>> _resultMap;
}
