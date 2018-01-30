package pacman;

import javafx.animation.Animation;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class PacMan extends ObjectDiChuyen {

    public int contarPuntoComido;
    public SimpleIntegerProperty puntaje;
    private static final int[] GRADO_DE_ROTACION = new int[]{0, 90, 180, 270};
    private int bufferDelTeclado;
    private final SimpleIntegerProperty actualDireccion;
    private States laberinto;
    public PacMan(States laberinto, int x, int y) {

        this.laberinto = laberinto;
        this.x = x;
        this.y = y;

        Image imagenPorDefecto = new Image(getClass().getResourceAsStream("images/izq1.png"));
        imagenes = new Image[]{imagenPorDefecto,new Image(getClass().getResourceAsStream("images/izq2.png")),
        imagenPorDefecto,new Image(getClass().getResourceAsStream("images/round.png"))
        };

        contarPuntoComido = 0;
        puntaje = new SimpleIntegerProperty(0);
        actualDireccion = new SimpleIntegerProperty(DI_CHUYEN_TRAI);

        imagenX = new SimpleIntegerProperty(DuLieu.calcGridX(x));
        imagenY = new SimpleIntegerProperty(DuLieu.calcGridX(y));

        direccionY = -1;
        direccionX = 0;

        ImageView imagenPacman = new ImageView(imagenPorDefecto);
        imagenPacman.xProperty().bind(imagenX.add(-13));
        imagenPacman.yProperty().bind(imagenY.add(-13));
        imagenPacman.imageProperty().bind(imagenBindear);
        IntegerBinding rotacion = new IntegerBinding() {

            {
                super.bind(actualDireccion);
            }

            @Override
            protected int computeValue() {
                return GRADO_DE_ROTACION[actualDireccion.get()];
            }
        };
        imagenPacman.rotateProperty().bind(rotacion);

        bufferDelTeclado = -1;

        getChildren().add(imagenPacman);
    }

    private void moverHorizontalmente() {

        contadorMovimientos++;

        if (contadorMovimientos < BUOC_TAO_ANIMATION) {
            imagenX.set(imagenX.get() + (direccionY * TOC_DO_CHUYEN_DONG));
        } else {
            contadorMovimientos = 0;
            x += direccionY;

            imagenX.set(DuLieu.calcGridX(x));

            int siguienteX = direccionY + x;

            if ((y == 14) && (siguienteX <= 1 || siguienteX >= 28)) {
                if ((siguienteX < -1) && (direccionY < 0)) {
                    x = DuLieu.TAMANIO_CUADRO_X;
                    imagenX.set(DuLieu.calcGridX(x));
                } else {
                    if ((siguienteX > 30) && (direccionY > 0)) {
                        x = 0;
                        imagenX.set(DuLieu.calcGridX(x));
                    }
                }
            } else if (DuLieu.getData(siguienteX, y) == DuLieu.BLOCK) {
                estado = DUNG;
            }
        }
    }

    private void moverVerticalmente() {

        contadorMovimientos++;

        if (contadorMovimientos < BUOC_TAO_ANIMATION) {
            imagenY.set(imagenY.get() + (direccionX * TOC_DO_CHUYEN_DONG));
        } else {
            contadorMovimientos = 0;
            y += direccionX;
            imagenY.set(DuLieu.calcGridX(y));
            int sigienteY = direccionX + y;
            if (DuLieu.getData(x, sigienteY) == DuLieu.BLOCK) {
                estado = DUNG;
            }
        }
    }

    private void moverDerecha() {

        if (actualDireccion.get() == DI_CHUYEN_QUYEN) {
            return;
        }

        int siguienteX = x + 1;

        if (siguienteX >= DuLieu.TAMANIO_CUADRO_X) {
            return;
        }

        if (DuLieu.getData(siguienteX, y) == DuLieu.BLOCK) {
            return;
        }

        direccionY = 1;
        direccionX = 0;

        bufferDelTeclado = -1;
        actualDireccion.set(DI_CHUYEN_QUYEN);
        estado = DI_CHUYEN;
    }

    private void moverIzquierda() {

        if (actualDireccion.get() == DI_CHUYEN_TRAI) {
            return;
        }

        int siguienteX = x - 1;

        if (siguienteX <= 1) {
            return;
        }

        if (DuLieu.getData(siguienteX, y) == DuLieu.BLOCK) {
            return;
        }

        direccionY = -1;
        direccionX = 0;

        bufferDelTeclado = -1;
        actualDireccion.set(DI_CHUYEN_TRAI);
        estado = DI_CHUYEN;
    }

    private void moverArriba() {

        if (actualDireccion.get() == DI_CHUYEN_LEN) {
            return;
        }

        int siguienteY = y - 1;

        if (siguienteY <= 1) {
            return;
        }

        if (DuLieu.getData(x, siguienteY) == DuLieu.BLOCK) {
            return;
        }

        direccionY = 0;
        direccionX = -1;

        bufferDelTeclado = -1;
        actualDireccion.set(DI_CHUYEN_LEN);
        estado = DI_CHUYEN;
    }

    private void moverAbajo() {

        if (actualDireccion.get() == DI_CHUYEN_XUONG) {
            return;
        }

        int siguienteY = y + 1;

        if (siguienteY >= DuLieu.TAMANIO_CUADRO_Y) {
            return;
        }

        if (DuLieu.getData(x, siguienteY) == DuLieu.BLOCK) {
            return;
        }

        direccionY = 0;
        direccionX = 1;

        bufferDelTeclado = -1;
        actualDireccion.set(DI_CHUYEN_XUONG);

        estado = DI_CHUYEN;
    }

    private void manejadorDeTecladoDeEntrada() {

        if (bufferDelTeclado < 0) {
            return;
        }
        if (bufferDelTeclado == DI_CHUYEN_TRAI) {
            moverIzquierda();
        } else if (bufferDelTeclado == DI_CHUYEN_QUYEN) {
            moverDerecha();
        } else if (bufferDelTeclado == DI_CHUYEN_LEN) {
            moverArriba();
        } else if (bufferDelTeclado == DI_CHUYEN_XUONG) {
            moverAbajo();
        }

    }

    public void setBufferDeTeclado(int k) {
        bufferDelTeclado = k;
    }
//+diem
    private void actualizarPuntaje() {
        if (y != 14 || (x > 0 && x < DuLieu.TAMANIO_CUADRO_X)) {
            Dot punto = (Dot) DuLieu.getPunto(x, y);

            if (punto != null && punto.isVisible()) {
                puntaje.set(puntaje.get() + 100);
                punto.setVisible(false);
                contarPuntoComido++;

                if (puntaje.get() >= 10000) {
                    laberinto.agregarVida();// thêm 1 mạng
                }
               

                if (punto.tipoPunto == DuLieu.DIEM_MAGIC) {
                    laberinto.hacerFantasmaHueco();
                    // ăn cục to làm ăn đc ghost 
                }

                if (contarPuntoComido >= DuLieu.getPuntoTotal()) {
                    laberinto.iniciarNuevoNivel();
                }
            }
        }
    }

    public void ocultar() {
        setVisible(false);
        tiempo.stop();
    }

    @Override
    public void caminarEnElPaso() {
        if (laberinto.juegoPauseado.get()) {

            if (tiempo.getStatus() != Animation.Status.PAUSED) {
                tiempo.pause();
            }
            return;
        }
        if (imagenActual.get() == 0) {
            manejadorDeTecladoDeEntrada();
        }

        if (estado == DI_CHUYEN) {

            if (direccionY != 0) {
                moverHorizontalmente();
            }

            if (direccionX != 0) {
                moverVerticalmente();
            }

            if (imagenActual.get() < BUOC_TAO_ANIMATION - 1) {
                imagenActual.set(imagenActual.get() + 1);
            } else {
                imagenActual.set(0);
                actualizarPuntaje();
            }
        }
        laberinto.pacmanEncuentraAFantasma();
    }
//refresh
    public void resetearEstado() {
        estado = DI_CHUYEN;
        actualDireccion.set(DI_CHUYEN_TRAI);
        direccionY = -1;
        direccionX = 0;

        bufferDelTeclado = -1;
        imagenActual.set(0);
        contadorMovimientos = 0;

        x = 15;
        y = 24;

        imagenX.set(DuLieu.calcGridX(x));
        imagenY.set(DuLieu.calcGridY(y));

        setVisible(true);
        iniciar();
    }

}
