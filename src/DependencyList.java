import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DependencyList {
    public DependencyList(File mainDir) {
        _mainDir = mainDir;
        _resultMap = new HashMap<String, List<String>>();
        _resultMap.put(null, new ArrayList<String>());
        SearchThroughDirs(_mainDir);
        for (var key : _resultMap.keySet()) {
            if (key != null) {
                System.out.print(new File(key).getName());
            }
            System.out.print(": ");
            for (var value : _resultMap.get(key)) {
                System.out.print(new File(value).getName() + " ");
            }
            System.out.print("\r\n");
        }
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
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            boolean noParents = true;
            String line;
            do {
                line = fileReader.readLine();
                if (line != null && line.contains("require")) {
                    int index = line.indexOf('‘');
                    StringBuilder fileDir = new StringBuilder();
                    for (int i = line.indexOf('‘') + 1; line.charAt(i) != '’'; ++i) {
                        fileDir.append(line.charAt(i));
                    }
                    fileDir.insert(0, _mainDir.getParent() + "\\");
                    if (!_resultMap.containsKey(fileDir.toString())) {
                        _resultMap.put(fileDir.toString(), new ArrayList<String>());
                    }
                    _resultMap.get(fileDir.toString()).add(file.getAbsolutePath());
                    noParents = false;
                }
            } while (line != null);
            if (noParents) {
                _resultMap.get(null).add(file.getAbsolutePath());
            }
        } catch (IOException e) {
            return;
        }
    }
    private final File _mainDir;
    private final Map<String, List<String>> _resultMap;
}
