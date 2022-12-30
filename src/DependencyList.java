import java.io.*;
import java.util.*;

public final class DependencyList {
    public DependencyList(File mainDir) {
        _mainDir = mainDir;
        _resultMap = new HashMap<>();
        _resultMap.put(null, new ArrayList<>());
        SearchThroughDirs(_mainDir);
        for (var key : _resultMap.keySet()) {
            if (key != null) {
                System.out.print(new File(key.get_path()).getName());
            }
            System.out.print(": ");
            for (var value : _resultMap.get(key)) {
                System.out.print(new File(value.get_path()).getName() + " ");
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
            Node thisNode = new Node(file.getAbsolutePath());
            do {
                line = fileReader.readLine();
                if (line != null && line.contains("require")) {
                    StringBuilder fileDir = new StringBuilder();
                    for (int i = line.indexOf('‘') + 1; line.charAt(i) != '’'; ++i) {
                        fileDir.append(line.charAt(i));
                    }
                    fileDir.insert(0, _mainDir.getParent() + "\\");
                    boolean flag = false;
                    Node parent = null;
                    for (var key : _resultMap.keySet()) {
                        if (key != null && Objects.equals(key.get_path(), fileDir.toString())) {
                            flag = true;
                            parent = key;
                            break;
                        }
                    }
                    if (!flag) {
                        parent = new Node(fileDir.toString());
                        _resultMap.put(parent, new ArrayList<>());
                    }
                    _resultMap.get(parent).add(thisNode);
                    noParents = false;
                }
            } while (line != null);
            if (noParents) {
                _resultMap.get(null).add(thisNode);
            }
        } catch (IOException e) {
            return;
        }
    }

    private boolean topologicalSort(Node node) {
        if (node.get_color() == Node.Color.GREY) {
            return false;
        } else if (node.get_color() == Node.Color.WHITE) {
            node.set_color(Node.Color.GREY);
            var children = _resultMap.get(node);
            for (Node child : children) {
                boolean flag = topologicalSort(child);
                if (!flag) {
                    return false;
                }
            }
            node.set_color(Node.Color.BLACK);
        }
        return true;
    }

    private final File _mainDir;
    private final Map<Node, List<Node>> _resultMap;
}
