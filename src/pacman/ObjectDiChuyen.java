package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.util.Duration;


public abstract class ObjectDiChuyen extends Parent {
  protected static final int BUOC_TAO_ANIMATION = 4;
  protected static final int TOC_DO_CHUYEN_DONG = DuLieu.GRID / BUOC_TAO_ANIMATION;

  protected static final int DI_CHUYEN = 1;
  protected static final int DUNG = 0;

  protected static final int DI_CHUYEN_TRAI = 0;
  protected static final int DI_CHUYEN_LEN = 1;
  protected static final int DI_CHUYEN_QUYEN = 2;
  protected static final int DI_CHUYEN_XUONG = 3;

  protected States laberinto;
  protected int estado;

  protected IntegerProperty imagenActual;
  protected Image[] imagenes;
  protected ObjectBinding imagenBindear;
  protected int contadorMovimientos;

  protected int x;
  protected int y;

  public IntegerProperty imagenX;
  public IntegerProperty imagenY;

  protected int direccionY;
  protected int direccionX;

  protected Timeline tiempo;

  public ObjectDiChuyen() {
    imagenActual = new SimpleIntegerProperty(0);

    imagenBindear = new ObjectBinding() {

      {
        super.bind(imagenActual);
      }

      @Override
      protected Image computeValue() {
        return imagenes[imagenActual.get()];
      }
    };

    contadorMovimientos = 0;
    direccionY = 0;
    direccionX = 0;
    tiempo = crearCronograma();
  }

  public abstract void caminarEnElPaso();
  private Timeline crearCronograma() {
    tiempo = new Timeline();
    tiempo.setCycleCount(Timeline.INDEFINITE);
    KeyFrame kf = new KeyFrame(Duration.millis(45), new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        caminarEnElPaso();
      }

    });
    tiempo.getKeyFrames().add(kf);

    return tiempo;
  }

  public void parar() {
    tiempo.stop();
  }

  public void pausear() {
    tiempo.pause();
  }

  public void iniciar() {
    tiempo.play();
  }

  public boolean estaCorriendo() {
    return tiempo.getStatus() == Animation.Status.RUNNING;
  }

  public boolean estaPauseado() {
    return tiempo.getStatus() == Animation.Status.PAUSED;
  }

}
