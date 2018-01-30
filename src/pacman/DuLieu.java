package pacman;


public final class DuLieu {

    public static final int BLOCK = 1;
    public static final int TRONG = 0;
    public static final int GRID = 16;
    public static final int TAMANIO_CUADRO_X = 29;
    public static final int TAMANIO_CUADRO_Y = 31;
    public static final int GRID_TRAIL = 2;
    public static final int DIEM_MAGIC = 3;
    public static final int DIEM_NORMAL = 2;

    private static final Object[][] PUNTEROS_DEL_PUNTO = new Object[TAMANIO_CUADRO_X + 1][TAMANIO_CUADRO_Y + 1];
    private static final int[][] LABERINTO_DATOS = new int[TAMANIO_CUADRO_X + 1][TAMANIO_CUADRO_Y + 1];
    private static final int X_OFFSET = GRID * 2;
    private static final int Y_OFFSET = GRID * 2;

    private static int puntoTotal = 0;

    private DuLieu() {
    }

    ;

  private static int hacerRango(int a, char coordenada) {

        if (a < 0) {
            return 0;
        } else if ((coordenada == 'X') && (a > TAMANIO_CUADRO_X)) {
            return TAMANIO_CUADRO_X;
        } else if ((coordenada == 'Y') && (a > TAMANIO_CUADRO_Y)) {
            return TAMANIO_CUADRO_Y;
        }

        return a;
    }

    public static void setBLOCKDatoLaberinto(int x1, int y1, int x2, int y2) {
        x1 = hacerRango(x1, 'X');
        y1 = hacerRango(y1, 'Y');
        x2 = hacerRango(x2, 'X');
        y2 = hacerRango(y2, 'Y');

        for (int i = x1; i <= x2; i++) {
            LABERINTO_DATOS[i][y1] = BLOCK;
            LABERINTO_DATOS[i][y2] = BLOCK;
        }

        for (int i = y1; i <= y2; i++) {
            LABERINTO_DATOS[x1][i] = BLOCK;
            LABERINTO_DATOS[x2][i] = BLOCK;
        }

    }

    public static int calcGridX(int x) {
        return GRID * x + X_OFFSET;
    }

    public static float calcGridXFloat(final float x) {
        return GRID * x + X_OFFSET;
    }

    public static int calcGridY(int y) {
        return GRID * y + Y_OFFSET;
    }

    public static float calcGridYFloat(final float y) {
        return GRID * y + Y_OFFSET;
    }

    public static int getData(int x, int y) {
        return LABERINTO_DATOS[x][y];
    }

    public static void setData(int x, int y, int value) {
        LABERINTO_DATOS[x][y] = value;

        if ((value == DIEM_MAGIC) || (value == DIEM_NORMAL)) {
            puntoTotal++;
        }
    }

    public static Object getPunto(int x, int y) {
        return PUNTEROS_DEL_PUNTO[x][y];
    }

    public static void setDot(int x, int y, Object dot) {
        PUNTEROS_DEL_PUNTO[x][y] = dot;
    }

    public static int getPuntoTotal() {
        return puntoTotal;
    }

    public static void indulieu() {
        for (int i = 0; i <= TAMANIO_CUADRO_Y; i++) {
            for (int j = 0; j <= TAMANIO_CUADRO_X; j++) {
                System.out.print(LABERINTO_DATOS[j][i] + " ");
            }
            System.out.println("");
        }
    }

    public static void indot() {
        for (int i = 0; i <= TAMANIO_CUADRO_Y; i++) {
            for (int j = 0; j <= TAMANIO_CUADRO_X; j++) {
                if (null != PUNTEROS_DEL_PUNTO[j][i]) {
                    System.out.print(((Dot) PUNTEROS_DEL_PUNTO[j][i]).tipoPunto + " ");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println("");
        }
    }

}
