package pacman;


public class DiChuyen {

  public int x;
  public int y;

  public int puntaje;

  public void evaluar(PacMan pacMan, boolean eshueco) {
    if (x < 1 || y < 1 || (y >= DuLieu.TAMANIO_CUADRO_Y) || (x >= DuLieu.TAMANIO_CUADRO_X)) {
      puntaje = -1;
      return;
    }

    int estado = DuLieu.getData(x, y);
    if (estado == DuLieu.BLOCK) {
      puntaje = -1;
      return;
    }

    int distancia = Math.abs(x - pacMan.x) + Math.abs(y - pacMan.y);

    if (eshueco) {
      puntaje = 500 + distancia; // 
    }
    else {
      puntaje = 500 - distancia; //
    }

  }

}
