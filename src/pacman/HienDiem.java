package pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class HienDiem extends Parent {
  private static final Font CO_CHU = new Font(11);
  private static final Color MAU_CHU = Color.YELLOW;
  private static final int HIEN_THI_TG = 2;
  private final Text texto;

  private Timeline cronograma;

  public HienDiem(String s, boolean esVisible) { 
    texto = new Text(s);
    texto.setFont(CO_CHU);
    texto.setFill(MAU_CHU);
    crearCronograma();
    getChildren().add(texto);
    setVisible(esVisible);
  }

  private void crearCronograma() {
    cronograma = new Timeline();
    cronograma.setCycleCount(1);
    KeyFrame kf = new KeyFrame(Duration.seconds(HIEN_THI_TG), new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        setVisible(false);
      }
    });
    cronograma.getKeyFrames().add(kf);
  }

  public void mostrarTexto() {
    setVisible(true);
    cronograma.playFromStart();
  }

  public void setX(int x) {
    texto.setX(x);
  }

  public void setY(int y) {
    texto.setY(y);
  }
}
