package pacman;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;



// ăn được ghost khi đổi màu sau đó tạo ghost mới
public class States extends Parent {

  public static final boolean DEBUG = false;

  private int contadorFantasmeComido;

  public BooleanProperty juegoPauseado;

  private static final HienDiem[] PUNTAJE_TEXTO = {
    new HienDiem("200", false) 
    ,
    new HienDiem("400", false) 
    ,
    new HienDiem("800", false) 
    ,
    new HienDiem("1600", false) 
  };

  public PacMan pacMan;

  public final Ghost[] fantasmas;

  private final ChienDichPacMan campamentoPacman;
  private static final Image IMAGEN_PACMAN = new Image(States.class.getResourceAsStream("images/izq1.png"));
  private final SimpleIntegerProperty nivel;
  private boolean agregarVidaFlag;
  private final SimpleIntegerProperty contadorDeVidas;
  public BooleanProperty esperarParaIniciar;

  private final Group mensaje;
 private final BooleanProperty ulimoResultadoDeJuego;
 private final Text resultaadoDelJuegoTexto;
 private int contadorFlashing;

 private final Timeline cronogramaFlashing;
  private final Group grupo;
  public States() {

    setFocused(true);

    juegoPauseado = new SimpleBooleanProperty(false);

    pacMan = new PacMan(this, 15, 24);

    final Ghost fantasmaRojo = new Ghost(
            new Image(getClass().getResourceAsStream("images/fantasmarojo1.png")),
            new Image(getClass().getResourceAsStream("images/fantasmarojo2.png")),
            this,
            pacMan,
            15, // x
            14, // y
            0,  // x 
            -1, // y 
            1); // 

    final Ghost fantasmaRosado = new Ghost(
            new Image(getClass().getResourceAsStream("images/fantasmarosado1.png")),
            new Image(getClass().getResourceAsStream("images/fantasmarosado2.png")),
            this,
            pacMan,
            14,
            15,
            1,  // x 
            0,  // y 
            5); //
    final Ghost fantasmaCyan = new Ghost(
            new Image(getClass().getResourceAsStream("images/fantasmacyan1.png")),
            new Image(getClass().getResourceAsStream("images/fantasmacyan2.png")),
            this,
            pacMan,
            12,
            15,
            1,   // x
            0,   // y
            20); // t

    final Ghost fantasmaNaranja = new Ghost(
            new Image(getClass().getResourceAsStream("images/fantasmanaranja1.png")),
            new Image(getClass().getResourceAsStream("images/fantasmanaranja2.png")),
            this,
            pacMan,
            16,
            15,
            1,   // 
            0,   // 
            30); // 
    fantasmas = new Ghost[] {fantasmaRojo, fantasmaRosado, fantasmaCyan, fantasmaNaranja};

    campamentoPacman = new ChienDichPacMan(this);
    campamentoPacman.setCenterX(0);
    campamentoPacman.setCenterY(0);
    campamentoPacman.setRadiusX(13);
    campamentoPacman.setRadiusY(13);
    campamentoPacman.setStartAngle(90);
    campamentoPacman.setLength(360);
    campamentoPacman.setType(ArcType.ROUND);
    campamentoPacman.setFill(Color.YELLOW);
    campamentoPacman.setVisible(false);

    contadorDeVidas = new SimpleIntegerProperty(2);

    final ImageView vidasImagen1 = new ImageView(IMAGEN_PACMAN);
    vidasImagen1.setX(DuLieu.calcGridX(18));
    vidasImagen1.setY(DuLieu.calcGridYFloat(DuLieu.TAMANIO_CUADRO_Y + 0.8f));
    vidasImagen1.visibleProperty().bind(contadorDeVidas.greaterThan(0));
    vidasImagen1.setCache(true);
    final ImageView vidasImagen2 = new ImageView(IMAGEN_PACMAN);
    vidasImagen2.setX(DuLieu.calcGridX(16));
    vidasImagen2.setY(DuLieu.calcGridYFloat(DuLieu.TAMANIO_CUADRO_Y + 0.8f));
    vidasImagen2.visibleProperty().bind(contadorDeVidas.greaterThan(1));
    vidasImagen2.setCache(true);
    final ImageView vidasImagen3 = new ImageView(IMAGEN_PACMAN);
    vidasImagen3.setX(DuLieu.calcGridX(14));
    vidasImagen3.setY(DuLieu.calcGridYFloat(DuLieu.TAMANIO_CUADRO_Y + 0.8f));
    vidasImagen3.visibleProperty().bind(contadorDeVidas.greaterThan(2));
    vidasImagen3.setCache(true);
    final ImageView[] vidasImagenes = new ImageView[] {vidasImagen1, vidasImagen2, vidasImagen3};

    nivel = new SimpleIntegerProperty(1);
    agregarVidaFlag = true;
    esperarParaIniciar = new SimpleBooleanProperty(true);

    mensaje = new Group();
    final Rectangle rectanguloMensaje = new Rectangle(DuLieu.calcGridX(5),
            DuLieu.calcGridYFloat(17.5f),
            DuLieu.GRID * 19,
            DuLieu.GRID * 5);
    rectanguloMensaje.setStroke(Color.RED);
    rectanguloMensaje.setStrokeWidth(5);
    rectanguloMensaje.setFill(Color.CYAN);
    rectanguloMensaje.setOpacity(0.75);
    rectanguloMensaje.setArcWidth(25);
    rectanguloMensaje.setArcHeight(25);

    final StringBinding mensajebinding = new StringBinding() {

            {
                super.bind(juegoPauseado);
            }

            @Override
            protected String computeValue() {
                if (juegoPauseado.get()) {
                    return " Bấm phím 'P' \n để KẾT THÚC";
                } else {
                    return "Bấm phím bất kỳ \n để Bắt đầu!";
                }
            }
        };

    final Text mensajeDeTexto = new Text(DuLieu.calcGridX(6),
            DuLieu.calcGridYFloat(20.5f),
            "Bấm phím bất kỳ \n để Bắt đầu!");
    mensajeDeTexto.textProperty().bind(mensajebinding);
    mensajeDeTexto.setFont(new Font(18));
    mensajeDeTexto.setFill(Color.RED);
    mensaje.getChildren().add(rectanguloMensaje);
    mensaje.getChildren().add(mensajeDeTexto);

    ulimoResultadoDeJuego = new SimpleBooleanProperty(false);

    final StringBinding lastGameResultBinding = new StringBinding() {

        {
            super.bind(ulimoResultadoDeJuego);
        }

        @Override
        protected String computeValue() {
            if (ulimoResultadoDeJuego.get()) {
                return "  Winner ";
            } else {
                return "GameOver   ";
            }
        }
    };

    resultaadoDelJuegoTexto = new Text(DuLieu.calcGridX(11),
            DuLieu.calcGridY(11) + 8,
            " Winner ");
    resultaadoDelJuegoTexto.textProperty().bind(lastGameResultBinding);
    resultaadoDelJuegoTexto.setFont(new Font(20));
    resultaadoDelJuegoTexto.setFill(Color.RED);
    resultaadoDelJuegoTexto.setVisible(false);

    contadorFlashing = 0;

    cronogramaFlashing = new Timeline();
    cronogramaFlashing.setCycleCount(5);
    final KeyFrame kf = new KeyFrame(Duration.seconds(0.5), new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
         resultaadoDelJuegoTexto.setVisible(!resultaadoDelJuegoTexto.isVisible());
         if (++contadorFlashing == 5) {
           mensaje.setVisible(true);
           esperarParaIniciar.set(true);
         }
      }

    });
    cronogramaFlashing.getKeyFrames().add(kf);

    grupo = new Group();

    final Rectangle fondoNegro = new Rectangle(0, 0,
            DuLieu.calcGridX(DuLieu.TAMANIO_CUADRO_X + 2),
            DuLieu.calcGridY(DuLieu.TAMANIO_CUADRO_Y + 3));
    fondoNegro.setFill(Color.BLACK);
    fondoNegro.setCache(true);
    grupo.getChildren().add(fondoNegro);

    grupo.getChildren().add(new WallHinhChuNhat(0, 0, DuLieu.TAMANIO_CUADRO_X, DuLieu.TAMANIO_CUADRO_Y));

    grupo.getChildren().add(new WallHinhChuNhat(14, -0.5f, 15, 4));
    grupo.getChildren().add(new WallBlack(13.8f, -1, 15.3f, 0));

    grupo.getChildren().add(new WallHinhChuNhat(2, 2, 5, 4)); 
    grupo.getChildren().add(new WallHinhChuNhat(7, 2, 12, 4)); 
    grupo.getChildren().add(new WallHinhChuNhat(17, 2, 22, 4));
    grupo.getChildren().add(new WallHinhChuNhat(24, 2, 27, 4));
    grupo.getChildren().add(new WallHinhChuNhat(2, 6, 5, 7)); 

    grupo.getChildren().add(new WallHinhChuNhat(14, 6, 15, 10));
    grupo.getChildren().add(new WallHinhChuNhat(10, 6, 19, 7));
    grupo.getChildren().add(new WallLinear(14.05f, 7, 14.95f, 7));

    grupo.getChildren().add(new WallHinhChuNhat(7.5f, 9, 12, 10));
    grupo.getChildren().add(new WallHinhChuNhat(7, 6, 8, 13));
    grupo.getChildren().add(new WallLinear(8, 9, 8, 10));

    grupo.getChildren().add(new WallHinhChuNhat(17, 9, 21.5f, 10));
    grupo.getChildren().add(new WallHinhChuNhat(21, 6, 22, 13));
    grupo.getChildren().add(new WallLinear(21, 9, 21, 10));

    grupo.getChildren().add(new WallHinhChuNhat(24, 6, 27, 7)); 

    grupo.getChildren().add(new WallHinhChuNhat(10, 12, 19, 17));
    grupo.getChildren().add(new WallHinhChuNhat(10.5f, 12.5f, 18.5f, 16.5f));
    final Rectangle cageRect = new Rectangle(DuLieu.calcGridX(13),
            DuLieu.calcGridY(12),
            DuLieu.GRID * 3,
            DuLieu.GRID / 2);
    cageRect.setStroke(Color.GREY);
    cageRect.setFill(Color.GREY);
    cageRect.setCache(true);
    grupo.getChildren().add(cageRect);

    grupo.getChildren().add(new WallHinhChuNhat(7, 15, 8, 20));

    grupo.getChildren().add(new WallHinhChuNhat(21, 15, 22, 20));

    grupo.getChildren().add(new WallHinhChuNhat(14, 19, 15, 23));
    grupo.getChildren().add(new WallHinhChuNhat(10, 19, 19, 20));
    grupo.getChildren().add(new WallLinear(14.05f, 20, 14.95f, 20));

    grupo.getChildren().add(new WallHinhChuNhat(4, 22, 5, 26));
    grupo.getChildren().add(new WallHinhChuNhat(2, 22, 5, 23));
    grupo.getChildren().add(new WallBlack(4, 22.05f, 5, 23.2f));

    grupo.getChildren().add(new WallHinhChuNhat(7, 22, 12, 23)); 

    grupo.getChildren().add(new WallHinhChuNhat(24, 22, 25, 26));
    grupo.getChildren().add(new WallHinhChuNhat(24, 22, 27, 23));
    grupo.getChildren().add(new WallBlack(24, 22.05f, 25, 23.2f));

    grupo.getChildren().add(new WallHinhChuNhat(17, 22, 22, 23)); 

    grupo.getChildren().add(new WallHinhChuNhat(-1, 25, 2, 26));
    grupo.getChildren().add(new WallHinhChuNhat(27, 25, DuLieu.TAMANIO_CUADRO_X + 1, 26)); 

    grupo.getChildren().add(new WallHinhChuNhat(7, 25, 8, 29));
    grupo.getChildren().add(new WallHinhChuNhat(2, 28, 12, 29));
    grupo.getChildren().add(new WallLinear(7.05f, 28, 7.95f, 28));

    grupo.getChildren().add(new WallHinhChuNhat(14, 25, 15, 29));
    grupo.getChildren().add(new WallHinhChuNhat(10, 25, 19, 26));
    grupo.getChildren().add(new WallLinear(14.05f, 26, 14.95f, 26));

    grupo.getChildren().add(new WallHinhChuNhat(21, 25, 22, 29));
    grupo.getChildren().add(new WallHinhChuNhat(17, 28, 27, 29));
    grupo.getChildren().add(new WallLinear(21.05f, 28, 21.95f, 28));

    final Rectangle outerWall = new Rectangle(DuLieu.calcGridXFloat(-0.5f),
            DuLieu.calcGridYFloat(-0.5f),
            (DuLieu.TAMANIO_CUADRO_X + 1) * DuLieu.GRID,
            (DuLieu.TAMANIO_CUADRO_Y + 1) * DuLieu.GRID);
    outerWall.setStrokeWidth(DuLieu.GRID_TRAIL);
    outerWall.setStroke(Color.BLUE);
    outerWall.setFill(null);
    outerWall.setArcWidth(12);
    outerWall.setArcHeight(12);
    outerWall.setCache(true);
    grupo.getChildren().add(outerWall);

    grupo.getChildren().add(new WallHinhChuNhat(-1, 9, 5, 13)); 
    grupo.getChildren().add(new WallHinhChuNhat(-1, 9.5f, 4.5f, 12.5f)); 
    grupo.getChildren().add(new WallHinhChuNhat(-1, 15, 5, 20)); 
    grupo.getChildren().add(new WallHinhChuNhat(-1, 15.5f, 4.5f, 19.5f)); 

    grupo.getChildren().add(new WallHinhChuNhat(DuLieu.TAMANIO_CUADRO_X - 5, 9, DuLieu.TAMANIO_CUADRO_X + 1, 13)); 
    grupo.getChildren().add(new WallHinhChuNhat(DuLieu.TAMANIO_CUADRO_X - 4.5f, 9.5f, DuLieu.TAMANIO_CUADRO_X + 1, 12.5f)); 
    grupo.getChildren().add(new WallHinhChuNhat(DuLieu.TAMANIO_CUADRO_X - 5, 15, DuLieu.TAMANIO_CUADRO_X + 1, 20)); 
    grupo.getChildren().add(new WallHinhChuNhat(DuLieu.TAMANIO_CUADRO_X - 4.5f, 15.5f, DuLieu.TAMANIO_CUADRO_X + 1, 19.5f)); 

    grupo.getChildren().add(new WallBlack(-2, 8, -0.5f, DuLieu.TAMANIO_CUADRO_Y)); 
    grupo.getChildren().add(new WallBlack(-0.5f, 8, 0, 9.5f)); 
    grupo.getChildren().add(new WallBlack(-0.5f, 19.5f, 0, DuLieu.TAMANIO_CUADRO_Y)); 

    grupo.getChildren().add(new WallBlack(DuLieu.TAMANIO_CUADRO_X + 0.5f, 8, DuLieu.TAMANIO_CUADRO_X + 2, DuLieu.TAMANIO_CUADRO_Y)); 
    grupo.getChildren().add(new WallBlack(DuLieu.TAMANIO_CUADRO_X, 8, DuLieu.TAMANIO_CUADRO_X + 0.5f, 9.5f));
    grupo.getChildren().add(new WallBlack(DuLieu.TAMANIO_CUADRO_X, 19.5f, DuLieu.TAMANIO_CUADRO_X + 0.5f, DuLieu.TAMANIO_CUADRO_Y)); 

    grupo.getChildren().add(new WallBlack(-1, 13, 1, 15)); 
    grupo.getChildren().add(new WallBlack(DuLieu.TAMANIO_CUADRO_X - 1, 13, DuLieu.TAMANIO_CUADRO_X + 1, 15)); 

    grupo.getChildren().add(new WallLinear(Color.BLUE, -0.5f, 9, -0.5f, 9.5f));
    grupo.getChildren().add(new WallLinear(Color.BLUE, -0.5f, 19.5f, -0.5f, 20));
    grupo.getChildren().add(new WallLinear(Color.BLUE, DuLieu.TAMANIO_CUADRO_X + 0.5f, 9, DuLieu.TAMANIO_CUADRO_X + 0.5f, 9.5f));
    grupo.getChildren().add(new WallLinear(Color.BLUE, DuLieu.TAMANIO_CUADRO_X + 0.5f, 19.5f, DuLieu.TAMANIO_CUADRO_X + 0.5f, 20));

    final Text puntajeDeTexto = new Text(DuLieu.calcGridX(0),
            DuLieu.calcGridY(DuLieu.TAMANIO_CUADRO_Y + 2),
            "ĐIỂM: " + pacMan.puntaje);
    puntajeDeTexto.textProperty().bind(pacMan.puntaje.asString("ĐIỂM: %1d  "));
    puntajeDeTexto.setFont(new Font(20));
    puntajeDeTexto.setFill(Color.YELLOW);
    puntajeDeTexto.setCache(true);
    grupo.getChildren().add(puntajeDeTexto);

    grupo.getChildren().addAll(PUNTAJE_TEXTO);
    grupo.getChildren().add(campamentoPacman);
    grupo.getChildren().addAll(vidasImagenes);
    grupo.getChildren().add(resultaadoDelJuegoTexto);

    final Text nivelDeTexto = new Text(DuLieu.calcGridX(22),
            DuLieu.calcGridY(DuLieu.TAMANIO_CUADRO_Y + 2),
            "LEVEL: " + nivel);
    nivelDeTexto.textProperty().bind(nivel.asString("LEVEL: %1d "));
    nivelDeTexto.setFont(new Font(20));
    nivelDeTexto.setFill(Color.YELLOW);
    nivelDeTexto.setCache(true);
    grupo.getChildren().add(nivelDeTexto);
    grupo.setFocusTraversable(true); 
    grupo.setOnKeyPressed(new EventHandler<KeyEvent>() {

      @Override public void handle(KeyEvent ke) {
        onKeyPressed(ke);
      }
    });


    colocarPuntoHorizontalmente(2,12,1);
    colocarPuntoHorizontalmente(17,27,1);
    
    colocarPuntoHorizontalmente(2,27,5);

    colocarPuntoHorizontalmente(2,5,8);
    colocarPuntoHorizontalmente(24,27,8);

    colocarPuntoHorizontalmente(10,13,8);
    colocarPuntoHorizontalmente(16,19,8);

    colocarPuntoHorizontalmente(2,12,21);
    colocarPuntoHorizontalmente(17,27,21);

    colocarPuntoHorizontalmente(2,2,24);
    colocarPuntoHorizontalmente(27,27,24);

    colocarPuntoHorizontalmente(7,12,24);
    colocarPuntoHorizontalmente(17,22,24);

    colocarPuntoHorizontalmente(2,5,27);
    colocarPuntoHorizontalmente(24,27,27);

    colocarPuntoHorizontalmente(10,12,27);
    colocarPuntoHorizontalmente(17,19,27);

    colocarPuntoHorizontalmente(2,27,30); 


    colocarPuntoVerticalmente(1,1,8);
    colocarPuntoVerticalmente(28,1,8);

    colocarPuntoVerticalmente(1,21,24);
    colocarPuntoVerticalmente(28,21,24);

    colocarPuntoVerticalmente(1,27,30);
    colocarPuntoVerticalmente(28,27,30);
    
    colocarPuntoVerticalmente(3,24,27);
    colocarPuntoVerticalmente(26,24,27);

    colocarPuntoVerticalmente(6,1,27);
    colocarPuntoVerticalmente(23,1,27);

    colocarPuntoVerticalmente(9,5,8);
    colocarPuntoVerticalmente(20,5,8);

    colocarPuntoVerticalmente(9,24,27);
    colocarPuntoVerticalmente(20,24,27);

    colocarPuntoVerticalmente(13,1,4);
    colocarPuntoVerticalmente(16,1,4);

    colocarPuntoVerticalmente(13,21,24);
    colocarPuntoVerticalmente(16,21,24);

    colocarPuntoVerticalmente(13,27,30);
    colocarPuntoVerticalmente(16,27,30);

    grupo.getChildren().add(pacMan);

    grupo.getChildren().addAll(fantasmas);

    grupo.getChildren().add(new WallBlack(-2, 13, -0.5f, 15));
    grupo.getChildren().add(new WallBlack(29.5f, 13, 31, 15));

    grupo.getChildren().add(mensaje);


    getChildren().add(grupo);

    if (DEBUG) {
      DuLieu.indulieu();
      DuLieu.indot();
    }
  }


  public void onKeyPressed(KeyEvent e) {
    if (esperarParaIniciar.get()) {
      esperarParaIniciar.set(false);
      iniciarNuevoJuego();
      return;
    }

    if (e.getCode() == KeyCode.P) {
      if (juegoPauseado.get()) {
        finalDelJuego();
      } else {
        pausearJuego();
      }

      return;
    }

    if (juegoPauseado.get()) {
      return;
    }

    if (e.getCode() == KeyCode.DOWN) {
      pacMan.setBufferDeTeclado(ObjectDiChuyen.DI_CHUYEN_XUONG);
    } else if (e.getCode() == KeyCode.UP) {
      pacMan.setBufferDeTeclado(ObjectDiChuyen.DI_CHUYEN_LEN);
    } else if (e.getCode() == KeyCode.RIGHT) {
      pacMan.setBufferDeTeclado(ObjectDiChuyen.DI_CHUYEN_QUYEN);
    } else if (e.getCode() == KeyCode.LEFT) {
      pacMan.setBufferDeTeclado(ObjectDiChuyen.DI_CHUYEN_TRAI);
    }

  }


  public final Dot crearPunto(int x1, int y1, int tipo) {
    Dot d = new Dot(DuLieu.calcGridX(x1), DuLieu.calcGridY(y1), tipo);

    if (tipo == DuLieu.DIEM_MAGIC) {
      d.playCronograma();

      d.deberiaPararLaAnimacion.bind(juegoPauseado.or(esperarParaIniciar)); 
    }
    DuLieu.setData(x1, y1, tipo);

    DuLieu.setDot(x1, y1, d);
    return d;
  }

  public final void colocarPuntoHorizontalmente(int x1, int x2, int y) {

    Dot punto;
    for (int x = x1; x <= x2; x++) {
      if (DuLieu.getData(x, y) == DuLieu.TRONG) {
        int tipoPunto;
        if ((x == 28 || x == 1) && (y == 3 || y == 24)) {
          tipoPunto = DuLieu.DIEM_MAGIC;
        } else {
          tipoPunto = DuLieu.DIEM_NORMAL;
        }

        punto = crearPunto(x, y, tipoPunto);
        grupo.getChildren().add(punto);
      }
      else {
        if (DEBUG) {
          System.out.println("!! Lỗi! Không xác định. (" + x + ", " + y + ")");
        }
      }
    }
  }

  public final void colocarPuntoVerticalmente(int x, int y1, int y2) {
    Dot punto;
    for (int y = y1; y <= y2; y++) {
      if (DuLieu.getData(x, y) == DuLieu.TRONG) {
        int tipoPunto;

        if ( (x == 28 || x == 1) && (y == 3 || y == 24) ) {
          tipoPunto = DuLieu.DIEM_MAGIC;
        }
        else {
          tipoPunto = DuLieu.DIEM_NORMAL;
        }

        punto = crearPunto(x, y, tipoPunto);
        grupo.getChildren().add(punto);
      }
      else {
        if (DEBUG) {
          System.out.println("!!  Lỗi! Không xác định. (" + x + ", " + y + ")");
        }
      }
    }
  }


  public void hacerFantasmaHueco() {

    contadorFantasmeComido = 0;

    for (Ghost g : fantasmas) {
      g.cambiarAHuecoFantasma();
    }
  }

  public boolean hasMet(Ghost g) {

    int distanciaMantenida = 22;

    int x1 = g.imagenX.get();
    int x2 = pacMan.imagenX.get();

    int diffX = Math.abs(x1 - x2);

    if (diffX >= distanciaMantenida) {
      return false;
    }

    int y1 = g.imagenY.get();
    int y2 = pacMan.imagenY.get();
    int diffY = Math.abs(y1 - y2);

    if (diffY >= distanciaMantenida) {
      return false;
    }
    if (diffY * diffY + diffX * diffX <= distanciaMantenida * distanciaMantenida) {
      return true;
    }

    return false;
  }

  public void pacmanEncuentraAFantasma() {

    for (Ghost g : fantasmas) {
      if (hasMet(g)) {
        if (g.esHueco) {
          pacmanComeFantasma(g);
        }
        else {
          for (Ghost fantasma : fantasmas) {
            fantasma.parar();
          }
          pacMan.parar();

          campamentoPacman.iniciarAnimacion(pacMan.imagenX.get(), pacMan.imagenY.get());
          break;
        }
      }
    }
  }

  public void pacmanComeFantasma(Ghost g) {
    contadorFantasmeComido++;

    int multiplicador = 1;
    for (int i = 1; i <= contadorFantasmeComido; i++) {
      multiplicador += multiplicador;
    }

    pacMan.puntaje.set(pacMan.puntaje.get() + multiplicador * 100);
    if ( agregarVidaFlag && (pacMan.puntaje.get() >= 10000) ) {
      agregarVida();
    }

    HienDiem st = PUNTAJE_TEXTO[contadorFantasmeComido - 1];
    st.setX(g.imagenX.get() - 10);
    st.setY(g.imagenY.get());

    g.parar();
    g.resetearEstado();
    g.contadorDeLaTrampa = -10;

    st.mostrarTexto();
  }

  public void finalDelJuego() {
// kết thúc trò chơi
    if (!juegoPauseado.get()) {
      return;
    }

    mensaje.setVisible(false);

    for (Ghost g : fantasmas) {
      if (g.estaPauseado()) {
        g.iniciar();
      }
    }

    if (pacMan.estaPauseado()) {
      pacMan.iniciar();
    }

    if (campamentoPacman.estaPauseado()) {
      campamentoPacman.iniciar();
    }

    if (cronogramaFlashing.getStatus() == Animation.Status.PAUSED) {
      cronogramaFlashing.play();
    }

    juegoPauseado.set(false);

  }

  public void pausearJuego() {
// tạm dừng trò chơi
    if ( esperarParaIniciar.get() || juegoPauseado.get() ) {
      return;
    }

    mensaje.setVisible(true);

    for (Ghost g : fantasmas) {
      if (g.estaCorriendo()) {
        g.pausear();
      }
    }

    if (pacMan.estaCorriendo()) {
      pacMan.pausear();
    }

    if (campamentoPacman.estaCorriendo()) {
      campamentoPacman.pausa();
    }

    if (cronogramaFlashing.getStatus() == Animation.Status.RUNNING) {
      cronogramaFlashing.pause();
    }
    juegoPauseado.set(true);
  }

  public void iniciarNuevoJuego() {
    // bắt đầu trò chơi mới
    mensaje.setVisible(false);
    pacMan.resetearEstado();

    resultaadoDelJuegoTexto.setVisible(false);

    if (!ulimoResultadoDeJuego.get()) {
      nivel.set(1);
      agregarVidaFlag = true;
      pacMan.puntaje.set(0);
      pacMan.contarPuntoComido = 0;

      contadorDeVidas.set(2);
    }
    else {
      ulimoResultadoDeJuego.set(false);
      nivel.set(nivel.get() + 1);
    }
//leVEL MOI
    for (int x = 1; x <= DuLieu.TAMANIO_CUADRO_X; x++) {
      for (int y = 1; y <= DuLieu.TAMANIO_CUADRO_Y; y++) {
        Dot punto = (Dot) DuLieu.getPunto(x, y);
        if ( (punto != null) && !punto.isVisible() ) {
          punto.setVisible(true);
        }
      }
    }
    for (Ghost g : fantasmas) {
      g.resetearEstado();
    }

}

  public void iniciarNuevoNivel() {
//bắt đầu cấp bật mới 
    ulimoResultadoDeJuego.set(true);

    pacMan.ocultar();
    pacMan.contarPuntoComido = 0;

    for (Ghost g : fantasmas) {
      g.ocultar();
    }

    contadorFlashing = 0;
    cronogramaFlashing.playFromStart();
  }

  public void iniciarNuevaVida() {
// bắt đầu mạng mới
    if (contadorDeVidas.get() > 0) {
      contadorDeVidas.set(contadorDeVidas.get() - 1);
    }
    else {
      ulimoResultadoDeJuego.set(false);
      contadorFlashing = 0;
      cronogramaFlashing.playFromStart();
      return;
    }

    pacMan.resetearEstado();

    for (Ghost g : fantasmas) {
      g.resetearEstado();
    }
  }

  public void agregarVida() {
// thêm mạng
    if (agregarVidaFlag) {
      contadorDeVidas.set(contadorDeVidas.get() + 1);
      agregarVidaFlag = false;
    }
  }

}
