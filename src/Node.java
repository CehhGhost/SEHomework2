import java.io.File;

public class Node {
    public Node(File file) {
        _file = file;
    }
    private File _file;

    public File get_file() {
        return _file;
    }
}
