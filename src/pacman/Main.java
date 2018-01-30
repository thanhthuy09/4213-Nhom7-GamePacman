package pacman;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  /**
   * @param args 
   */
  public static void main(String[] args) {
    Application.launch(Main.class, args);
  }

  @Override
  public void start(Stage escenarioPrimario) {
    escenarioPrimario.setTitle("Trò Chơi PacMan");
    escenarioPrimario.setWidth(DuLieu.calcGridX(DuLieu.TAMANIO_CUADRO_X + 2));
    escenarioPrimario.setHeight(DuLieu.calcGridY(DuLieu.TAMANIO_CUADRO_Y + 5));

    final Group raiz = new Group();
    final Scene escena = new Scene(raiz);
    raiz.getChildren().add(new States());
    escenarioPrimario.setScene(escena);
    escenarioPrimario.show();
  }
}
