package pacman;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class WallBlack extends Parent {

  public WallBlack(float x1, float y1, float x2, float y2) {

    Rectangle rectangulo = new Rectangle();
    rectangulo.setX(DuLieu.calcGridXFloat(x1) + DuLieu.GRID_TRAIL);
    rectangulo.setY(DuLieu.calcGridYFloat(y1) + DuLieu.GRID_TRAIL);
    rectangulo.setWidth(DuLieu.GRID * (x2 - x1) - DuLieu.GRID_TRAIL * 2);
    rectangulo.setHeight(DuLieu.GRID * (y2 - y1) - DuLieu.GRID_TRAIL * 2);
    rectangulo.setStrokeWidth(DuLieu.GRID_TRAIL);
    rectangulo.setStroke(Color.BLACK);
    rectangulo.setArcWidth(3);
    rectangulo.setArcHeight(3);
    rectangulo.setCache(true);
    getChildren().add(rectangulo); 
  }

}
