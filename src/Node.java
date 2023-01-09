/**
 * Публичный класс для удобного построения списка смежности
 */
public class Node {
    public Node(String path) {
        _color = Color.WHITE;
        _path = path;
    }

    /**
     * Возможные цвета node
     */
    public enum Color {
        BLACK,
        GREY,
        WHITE
    }

    /**
     * Геттер для получения цвета node
     *
     * @return цвет node
     */
    public Color get_color() {
        return _color;
    }

    /**
     * Сеттер для записи цвета node
     *
     * @param _color цвет node
     */
    public void set_color(Color _color) {
        this._color = _color;
    }

    /**
     * Цвет node
     */
    private Color _color;

    /**
     * Геттер для получения глобального пути node
     *
     * @return глобальный путь node
     */
    public String get_path() {
        return _path;
    }

    /**
     * Глобальный путь node
     */
    private final String _path;
}
