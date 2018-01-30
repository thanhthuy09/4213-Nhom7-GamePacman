package pacman;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Ghost extends ObjectDiChuyen {

    private static final int ATRAPADO = 10;
    private final PacMan pacMan;
    private static final Image HUECO_IMAGEN1 = new Image(Ghost.class.getResourceAsStream("images/fantasmahueco2.png"));
    private static final Image HUECO_IMAGEN2 = new Image(Ghost.class.getResourceAsStream("images/fantasmahueco3.png"));
    private static final Image HUECO_IMAGEN3 = new Image(Ghost.class.getResourceAsStream("images/fantasmahueco1.png"));
    private static final Image[] HUECO_IMG = new Image[]{
        HUECO_IMAGEN1,
        HUECO_IMAGEN2,
        HUECO_IMAGEN1,
        HUECO_IMAGEN2
    };
    private static final Image[] IMAGEN_HUECO_FLASH = new Image[]{
        HUECO_IMAGEN1,
        HUECO_IMAGEN3,
        HUECO_IMAGEN1,
        HUECO_IMAGEN3
    };

    private static final int TIEMPO_MAX_HUECO = 80;

    private int contadorHueco;

    private final Image[] imagenDefecto;

    private final int inicialLocalizacionX;
    private final int inicialLocalizacionY;
    private final int inicialDireccionX;
    private final int inicialDireccionY;

    private final int tiempoDeLaTrampa;
    public int contadorDeLaTrampa; // = 0;

    private static final double FACTOR_CAMBIO = 0.75;
    private static final double FACTOR_CAPTURA = 0.5;
    private int contadorCaptura; // = 0;
    private States laberinto;
    public boolean esHueco; // = false;

    public Ghost(Image imagenPorDefecto1,
            Image imagenPorDefecto2,
            States laberinto,
            PacMan pacMan,
            int x,
            int y,
            int direccionX,
            int direccionY,
            int tiempoDeLaTrampa) {

        this.laberinto = laberinto;
        this.pacMan = pacMan;
        this.x = x;
        this.y = y;
        this.direccionY = direccionX;
        this.direccionX = direccionY;
        this.tiempoDeLaTrampa = tiempoDeLaTrampa;

        imagenDefecto = new Image[]{
            imagenPorDefecto1,
            imagenPorDefecto2,
            imagenPorDefecto1,
            imagenPorDefecto2
        };
        imagenes = imagenDefecto;
        contadorCaptura = 0;
        esHueco = false;

        contadorDeLaTrampa = 0;

        inicialLocalizacionX = x;
        inicialLocalizacionY = y;
        inicialDireccionX = direccionX;
        inicialDireccionY = direccionY;
        imagenX = new SimpleIntegerProperty(DuLieu.calcGridX(x));
        imagenY = new SimpleIntegerProperty(DuLieu.calcGridY(y));
        ImageView nodoFantasma = new ImageView(imagenPorDefecto1);
        nodoFantasma.xProperty().bind(imagenX.add(-13));
        nodoFantasma.yProperty().bind(imagenY.add(-13));
        nodoFantasma.imageProperty().bind(imagenBindear);
        nodoFantasma.setCache(true);

        getChildren().add(nodoFantasma);
    }

    public void resetearEstado() {
        x = inicialLocalizacionX;
        y = inicialLocalizacionY;

        direccionY = inicialDireccionX;
        direccionX = inicialDireccionY;

        esHueco = false;

        contadorMovimientos = 0;
        contadorDeLaTrampa = 0;
        imagenActual.set(0);

        imagenX.set(DuLieu.calcGridX(x));
        imagenY.set(DuLieu.calcGridY(y));

        imagenes = imagenDefecto;
        estado = ATRAPADO;

        tiempo.setRate(1.0);

        setVisible(true);
        iniciar();
    }

    public void cambiarAHuecoFantasma() {
        contadorHueco = 0;
        esHueco = true;

        imagenes = HUECO_IMG;

        tiempo.stop();
        tiempo.setRate(0.35);
        tiempo.play();
    }

    private void cambiarDirectorioDeXaY(boolean debeCambiar) {
        if (!debeCambiar && (Math.random() > FACTOR_CAMBIO)) {
            return; // no cambia la direccion.
        }

        DiChuyen subir = new DiChuyen();
        subir.x = this.x;
        subir.y = this.y - 1;

        DiChuyen bajar = new DiChuyen();
        bajar.x = this.x;
        bajar.y = this.y + 1;

        subir.evaluar(pacMan, esHueco);
        bajar.evaluar(pacMan, esHueco);

        if (subir.puntaje < 0 && bajar.puntaje < 0) {
            return; // no cambia la direccion.
        }

        if (Math.random() < FACTOR_CAPTURA && contadorCaptura == 0) {
            contadorCaptura += (int) (Math.random() * 10 + 3);
        }

        DiChuyen seguirAdelante = new DiChuyen();
        seguirAdelante.x = this.x + direccionY;
        seguirAdelante.y = this.y;
        seguirAdelante.evaluar(pacMan, esHueco);

        if ((seguirAdelante.puntaje > 0) && (seguirAdelante.puntaje > subir.puntaje)
                && (seguirAdelante.puntaje > bajar.puntaje) && (contadorCaptura > 0)) {
            contadorCaptura--;
            return;
        }

        int decision = -1;
        if (subir.puntaje < 0) {
            decision = 1;
        } else {
            if (bajar.puntaje > 0) {
                if (contadorCaptura > 0) {
                    if (bajar.puntaje > subir.puntaje) {
                        decision = 1;
                        contadorCaptura--;
                    }
                } else {

                    if (Math.random() > 0.5) {
                        decision = 1;
                    }
                }
            }
        }

        direccionX = decision;
        direccionY = 0;
    }

    private void cambiarDeDirectorioDeYaX(boolean debeCambiar) {
        if (!debeCambiar && (Math.random() > FACTOR_CAMBIO)) {
            return; // no cambia la direccion
        }
        DiChuyen irIzquierda = new DiChuyen();
        irIzquierda.x = this.x - 1;
        irIzquierda.y = this.y;
        DiChuyen irDerecha = new DiChuyen();
        irDerecha.x = this.x + 1;
        irDerecha.y = this.y;

        irIzquierda.evaluar(pacMan, esHueco);
        irDerecha.evaluar(pacMan, esHueco);

        if ((irIzquierda.puntaje < 0) && (irDerecha.puntaje < 0)) {
            return; // no cambia de direccion
        }

        if ((Math.random() < FACTOR_CAPTURA) && (contadorCaptura == 0)) {
            contadorCaptura += (int) (Math.random() * 10 + 3);
        }
        DiChuyen seguirAdelante = new DiChuyen();
        seguirAdelante.x = this.x;
        seguirAdelante.y = this.y + direccionX;
        seguirAdelante.evaluar(pacMan, esHueco);

        if ((seguirAdelante.puntaje > 0) && (seguirAdelante.puntaje > irIzquierda.puntaje)
                && (seguirAdelante.puntaje > irDerecha.puntaje) && (contadorCaptura > 0)) {
            contadorCaptura--;
            return;
        }

        int decision = -1;
        if (irIzquierda.puntaje < 0) {
            decision = 1;
        } else {
            if (irDerecha.puntaje > 0) {
                if (contadorCaptura > 0) {
                    if (irDerecha.puntaje > irIzquierda.puntaje) {
                        decision = 1;
                        contadorCaptura--;
                    }
                } else {
                    if (Math.random() > 0.5) {
                        decision = 1;
                    }
                }
            }
        }

        direccionY = decision;
        direccionX = 0;
    }

    private void moverHorizontalmente() {

        contadorMovimientos++;

        if (contadorMovimientos > BUOC_TAO_ANIMATION - 1) {
            contadorMovimientos = 0;
            x += direccionY;
            imagenX.set(DuLieu.calcGridX(x));

            int siguienteX = direccionY + x;

            if (y == 14 && (siguienteX <= 1 || siguienteX >= 28)) {
                if (siguienteX < -1 && direccionY < 0) {
                    x = DuLieu.TAMANIO_CUADRO_X;
                    imagenX.set(DuLieu.calcGridX(x));
                } else if (siguienteX > 30 && direccionY > 0) {
                    x = 0;
                    imagenX.set(DuLieu.calcGridX(x));
                }
            } else if (siguienteX < 0 || siguienteX > DuLieu.TAMANIO_CUADRO_X) {
                cambiarDirectorioDeXaY(true);
            } else if (DuLieu.getData(siguienteX, y) == DuLieu.BLOCK) {
                cambiarDirectorioDeXaY(true);
            } else {
                cambiarDirectorioDeXaY(false);
            }

        } else {
            imagenX.set(imagenX.get() + (direccionY * TOC_DO_CHUYEN_DONG));
        }
    }

    private void moverVerticalmente() {

        contadorMovimientos++;

        if (contadorMovimientos > BUOC_TAO_ANIMATION - 1) {
            contadorMovimientos = 0;
            y += direccionX;
            imagenY.set(DuLieu.calcGridX(y));

            int siguienteY = direccionX + y;
            if (siguienteY < 0 || siguienteY > DuLieu.TAMANIO_CUADRO_Y) {
                cambiarDeDirectorioDeYaX(true);
            } else {
                if (DuLieu.getData(x, siguienteY) == DuLieu.BLOCK) {
                    cambiarDeDirectorioDeYaX(true);
                } else {
                    cambiarDeDirectorioDeYaX(false);
                }
            }
        } else {
            imagenY.set(imagenY.get() + (direccionX * TOC_DO_CHUYEN_DONG));
        }
    }

    private void moverHorizontalmenteDentroTablero() {

        contadorMovimientos++;

        if (contadorMovimientos > BUOC_TAO_ANIMATION - 1) {

            contadorMovimientos = 0;
            x += direccionY;
            imagenX.set(DuLieu.calcGridX(x));

            int siguienteX = direccionY + x;

            if (siguienteX < 12) {
                direccionY = 0;
                direccionX = 1;
            } else if (siguienteX > 17) {
                direccionY = 0;
                direccionX = -1;
            }
        } else {
            imagenX.set(imagenX.get() + (direccionY * TOC_DO_CHUYEN_DONG));
        }
    }

    private void moverVerticalmenteEnElTablero() {

        contadorMovimientos++;

        if (contadorMovimientos > BUOC_TAO_ANIMATION - 1) {
            contadorMovimientos = 0;
            y += direccionX;
            imagenY.set(DuLieu.calcGridX(y) + 8);

            int siguienteY = direccionX + y;

            if (siguienteY < 13) {
                direccionX = 0;
                direccionY = -1;
            } else if (siguienteY > 15) {
                direccionX = 0;
                direccionY = 1;
            }
        } else {
            imagenY.set(imagenY.get() + (direccionX * TOC_DO_CHUYEN_DONG));
        }
    }

    public void ocultar() {
        setVisible(false);
        tiempo.stop();
    }

    @Override
    public void caminarEnElPaso() {
        if (laberinto.juegoPauseado.get()) {
            if (!estaPauseado()) {
                tiempo.pause();
            }
            return;
        }

        if (estado == DI_CHUYEN || estado == ATRAPADO) {
            if (direccionY != 0) {
                if (estado == DI_CHUYEN) {
                    moverHorizontalmente();
                } else {
                    moverHorizontalmenteDentroTablero();
                }
            } else {
                if (direccionX != 0) {
                    if (estado == DI_CHUYEN) {
                        moverVerticalmente();
                    } else {
                        moverVerticalmenteEnElTablero();
                    }
                }
            }

            if (imagenActual.get() < (BUOC_TAO_ANIMATION - 1)) {
                imagenActual.set(imagenActual.get() + 1);
            } else {
                imagenActual.set(0);
                if (estado == ATRAPADO) {
                    contadorDeLaTrampa++;

                    if (contadorDeLaTrampa > tiempoDeLaTrampa && x == 14 && y == 13) {
                        y = 12;

                        direccionY = 0;
                        direccionX = -1;
                        estado = DI_CHUYEN;
                    }
                }
            }
        }

        if (esHueco) {

            contadorHueco++;

            if (contadorHueco == TIEMPO_MAX_HUECO - 30) {
                imagenes = IMAGEN_HUECO_FLASH;
            } else if (contadorHueco > TIEMPO_MAX_HUECO) {
                esHueco = false;
                imagenes = imagenDefecto;

                tiempo.stop();
                tiempo.setRate(1.0);
                tiempo.play();
            }
        }
    }
}
