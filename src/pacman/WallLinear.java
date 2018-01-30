package pacman;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;


public class WallLinear extends Line {

  public WallLinear(float x1, float y1, float x2, float y2) {
    init(x1, y1, x2, y2);
    setStrokeWidth(DuLieu.GRID_TRAIL + 1);
    setStroke(Color.BLACK);
  }

  public WallLinear(Color lineaDeColor, float x1, float y1, float x2, float y2) {
    init(x1, y1, x2, y2);
    
    setStrokeWidth(DuLieu.GRID_TRAIL);
    setStroke(lineaDeColor);
  }

  private void init(float x1, float y1, float x2, float y2) {
    setCache(true);
    if (x1 == x2) { // LINEA VERTICAL
      setStartX(DuLieu.calcGridXFloat(x1));
      setStartY(DuLieu.calcGridYFloat(y1) + DuLieu.GRID_TRAIL);
      setEndX(DuLieu.calcGridXFloat(x2));
      setEndY(DuLieu.calcGridYFloat(y2) - DuLieu.GRID_TRAIL);
    }
    else { // LINEA HORIZONTAL
      setStartX(DuLieu.calcGridXFloat(x1) + DuLieu.GRID_TRAIL);
      setStartY(DuLieu.calcGridYFloat(y1));
      setEndX(DuLieu.calcGridXFloat(x2) - DuLieu.GRID_TRAIL);
      setEndY(DuLieu.calcGridYFloat(y2));
    }
  }
}
