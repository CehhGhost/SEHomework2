import java.io.*;
import java.util.*;

/**
 * Публичный класс для создания и работы со списком смежности
 */
public final class DependencyList {
    /**
     * Публичный конструктор, который создает список и с помощью топологической сортировки выводит текст
     *
     * @param mainDir Абсолютный путь к корневой папке
     */
    public DependencyList(File mainDir) {
        _mainDir = mainDir;
        _resultMap = new HashMap<>();
        _resultMap.put(NILL, new ArrayList<>());
        SearchThroughDirs(_mainDir);
        boolean stop = true;
        for (var node : _resultMap.get(NILL)) {
            stop = TopologicalSort(node);
            if (!stop) {
                break;
            }
        }
        if (stop && _resultMap.get(NILL).size() != 0) {
            Print();
        } else {
            System.out.println("Found circles, check your files!");
        }
    }

    /**
     * Приватный метод для прохода по всем директориям
     *
     * @param dir Директория из которой выполняется проход
     */
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

    /**
     * Приватный метод для записи файла в список смежности на основе его содержимого
     *
     * @param file Сам файл
     */
    private void GetFilesParents(File file) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            boolean noParents = true;
            String line;
            Node thisNode = GetNode(file.getAbsolutePath());
            if (thisNode == null) {
                thisNode = new Node(file.getAbsolutePath());
            }
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
                        parent = GetNode(fileDir.toString());
                        if (parent == null) {
                            parent = new Node(fileDir.toString());
                        }
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

    /**
     * Приватный метод для топологической сортировки
     *
     * @param node node относительно которого начинается сортировка
     * @return true, если нет циклов и false, если он нашелся(тогда в конструкторе выходит сообщение об этом)
     */
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

    /**
     * Приватный метод для печати списка смежности
     */
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

    /**
     * Приватный метод для доступа по ссылке с элементами списка смежности(для осуществления прохода по ним)
     *
     * @param path Путь, по которому ищется node
     * @return node, если была найдена такая, null иначе
     */
    private Node GetNode(String path) {
        for (var node : _resultMap.keySet()) {
            if (node != null && Objects.equals(node.get_path(), path)) {
                return node;
            }
            for (var value : _resultMap.get(node)) {
                if (value != null && Objects.equals(value.get_path(), path)) {
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * Абсолютной путь корневой папки
     */
    private File _mainDir;
    /**
     * Сам список смежности
     */
    private Map<Node, List<Node>> _resultMap;
    /**
     * Итоговый список node, которые необходимо вывести(в порядке индексации)
     */
    private List<Node> _resultList = new LinkedList<>();
    /**
     * "Нулевая" node(если нет родителей, то добавляется к детям NILL => если у NILL нет детей, то есть цикл)
     */
    private final Node NILL = new Node(null);
}
