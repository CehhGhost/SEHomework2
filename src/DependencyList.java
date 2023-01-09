import java.io.*;
import java.util.*;

public final class DependencyList {
    public DependencyList(File mainDir) {
        _mainDir = mainDir;
        _resultMap = new HashMap<>();
        _resultMap.put(NILL, new ArrayList<>());
        SearchThroughDirs(_mainDir);
        /*for (var key : _resultMap.keySet()) {
            if (key.get_path() != null) {
                System.out.print(new File(key.get_path()).getName());
            }
            System.out.print(": ");
            for (var value : _resultMap.get(key)) {
                System.out.print(new File(value.get_path()).getName() + " ");
            }
            System.out.print("\r\n");
        }*/
        boolean stop = true;
        for (var node : _resultMap.get(NILL)) {
            stop = TopologicalSort(node);
            if (!stop) {
                break;
            }
        }
        if (stop) {
            Print();
        } else {
            System.out.println("Found circles, check your files!");
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
                    fileDir.insert(0, _mainDir.getAbsolutePath() + "\\");
                    boolean flag = false;
                    Node parent = NILL;
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
                if (GetNode(thisNode.get_path()) == null) {
                    _resultMap.get(NILL).add(thisNode);
                } else {
                    _resultMap.get(NILL).add(GetNode(thisNode.get_path()));
                }
            }
        } catch (IOException e) {
            return;
        }
    }

    private boolean TopologicalSort(Node node) {
        if (node.get_color() == Node.Color.GREY) {
            return false;
        } else if (node.get_color() == Node.Color.WHITE) {
            node.set_color(Node.Color.GREY);
            var children = _resultMap.get(node);
            if (children != null) {
                for (Node child : children) {
                    boolean flag = TopologicalSort(child);
                    if (!flag) {
                        return false;
                    }
                }
            }
            node.set_color(Node.Color.BLACK);
            _resultList.add(0, node);
        }
        return true;
    }

    private void Print() {
        for (var item : _resultList) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(item.get_path()))) {
                String line;
                do {
                    line = fileReader.readLine();
                    if (line != null) {
                        System.out.println(line);
                    }
                } while (line != null);
            } catch (IOException e) {
                return;
            }
        }
    }
    private Node GetNode(String path) {
        for (var node : _resultMap.keySet()) {
            if (node != null && Objects.equals(node.get_path(), path)) {
                return node;
            }
        }
        return null;
    }
    private File _mainDir;
    private Map<Node, List<Node>> _resultMap;
    private List<Node> _resultList = new LinkedList<Node>();
    private final Node NILL = new Node(null);
}
