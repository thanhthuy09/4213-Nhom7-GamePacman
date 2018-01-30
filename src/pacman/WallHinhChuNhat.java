package pacman;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class WallHinhChuNhat extends Parent {

  public WallHinhChuNhat(float x1, float y1, float x2, float y2) {
    Rectangle rectangulo = new Rectangle();
    rectangulo.setX(DuLieu.calcGridXFloat(x1));
    rectangulo.setY(DuLieu.calcGridYFloat(y1));
    rectangulo.setWidth(DuLieu.calcGridXFloat(x2) - DuLieu.calcGridXFloat(x1));
    rectangulo.setHeight(DuLieu.calcGridYFloat(y2) - DuLieu.calcGridYFloat(y1));
    rectangulo.setStrokeWidth(DuLieu.GRID_TRAIL);
    rectangulo.setStroke(Color.BLUE);
    rectangulo.setArcWidth(12);
    rectangulo.setArcHeight(12);
    rectangulo.setCache(true);

    getChildren().add(rectangulo);
    DuLieu.setBLOCKDatoLaberinto(Math.round(x1), Math.round(y1), Math.round(x2), Math.round(y2));
  }

}
