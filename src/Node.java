public class Node {
    public Node(String path) {
        _color = Color.WHITE;
        _path = path;
    }
    public enum Color {
        BLACK,
        GREY,
        WHITE
    }

    public Color get_color() {
        return _color;
    }

    public void set_color(Color _color) {
        this._color = _color;
    }

    private Color _color;

    public String get_path() {
        return _path;
    }

    public void set_path(String _path) {
        this._path = _path;
    }

    private String _path;
}
