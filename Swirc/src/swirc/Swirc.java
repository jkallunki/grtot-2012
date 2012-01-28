package swirc;

public class Swirc {
    public static void main(String[] args) {
        SwircModel model = new SwircModel();
        SwircView view = new SwircView(model);
        SwircController controller = new SwircController(model, view);
        
        view.setVisible(true);
    }
}
