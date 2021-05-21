package mx.rmr.proyectodemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import mx.rmr.proyectodemo.utilerias.Texto;


/*
Representa un clon del juego Space Invaders
Autor: Roberto Mtz. Román
 */
public class PantallaSpaceInvaders extends Pantalla
{
    private static final float DELTA_X = 10;

    private Juego juego;

    // Código

    // Enemigos
    //private Alien alien;    // borrar!!
    private Array<Alien> arrAliens;     // Guardar TODOS los aliens
    private float timerMover = 0;       // Contar tiempo para que los aliens den 'pasos'
    private final float LIMITE_PASOS_ALIENS = 0.5f;
    private float DX_PASO_ALIEN = 10;     // +, derecha     -, izquierda
    private int contadorPasos = 10;             // Hacen 20 pasos en total.

    // Personaje (Nave)
    private Nave nave;
    // Indican si se mueve en cierta dirección
    private boolean moviendoIzquierda = false;
    private boolean moviendoDerecha = false;

    // Proyectil (disparo de la nave)
    private Bala bala;
    private Texture texturaBala;

    // Botón de disparo
    private Texture texturaDisparo;

    // Marcador (vidas, niveles, energía, estado)
    private int puntos = 0;
    private Texto texto;        // Escribe texto en la pantalla

    // Botón BACK (regresa al menú principal)
    private Texture texturaBack;


    public PantallaSpaceInvaders(Juego juego) {
        this.juego = juego;
    }

    // Inicializan todos los objetos
    @Override
    public void show() {
        crearEnemigos();
        crearNave();
        crearBotonDisparo();
        crearTexturaBala();
        crearTexto();
        crearTexturaBack();
        recuperarMarcador();
        
        // Ahora la misma pantalla RECIBE y PROCESA los eventos
        Gdx.input.setInputProcessor( new ProcesadorEntrada() );

        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    private void recuperarMarcador() {
        Preferences prefs = Gdx.app.getPreferences("PUNTAJE");
        puntos = prefs.getInteger("puntos", 0);
    }

    private void crearTexturaBack() {
        texturaBack = new Texture("space/back.png");
    }

    private void crearTexto() {
        texto = new Texto("fonts/digital.fnt");
    }

    private void crearTexturaBala() {
        texturaBala = new Texture("space/bala.png");
    }

    private void crearBotonDisparo() {
        texturaDisparo = new Texture("space/disparo.png");
    }

    private void crearNave() {
        Texture texturaNave = new Texture("space/nave.png");
        nave = new Nave(texturaNave, ANCHO/2, 0.07f*ALTO);
    }

    private void crearEnemigos() {
        Texture texturaAlienArriba = new Texture("space/enemigoArriba.png");
        Texture texturaAlienAbajo = new Texture("space/enemigoAbajo.png");
        Texture texturaExplota = new Texture("space/enemigoExplota.png");
        // CREAR 55 aliens (11 columnas x 5 filas)
        arrAliens = new Array<>(11*5);
        for (int renglon=0; renglon<5; renglon++) { // Recorre los renglones (0->4)
            for (int columna=0; columna<11; columna++) {
                //Alien alien = new Alien(texturaAlienArriba, 310 + columna*60, ALTO/2 + renglon*60);
                Alien alien = new Alien(texturaAlienArriba, texturaAlienAbajo, texturaExplota,
                        310 + columna*60, ALTO/2 + renglon*60);
                arrAliens.add(alien);       // Lo guarda en el arreglo
            }
        }
    }

    // Hasta 60 veces/seg
    @Override
    public void render(float delta) {
        actualizar();

        borrarPantalla(0, 0, 0);    // Borrar con color negro

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        //alien.render(batch);
        // Dibujar los 55 aliens que están el arreglo
        for (Alien alien : arrAliens) {     // Automáticamente visita CADA objeto del arreglo
            alien.render(batch);
        }
        // Dibujar nave
        nave.render(batch);

        // Dibujar bala
        if (bala != null) {     // existe???
            bala.render(batch);
        }

        // Dibujar botón disparo
        batch.draw(texturaDisparo, ANCHO-texturaDisparo.getWidth()*2, texturaDisparo.getHeight()/2);

        // Dibujar marcador
        texto.mostrarMensaje(batch, Integer.toString(puntos), 0.95f*ANCHO, 0.9f*ALTO);

        // Dibujar BACK
        batch.draw(texturaBack, texturaBack.getWidth()/2, ALTO-3*texturaBack.getHeight()/2);

        batch.end();
    }

    // Ejecuta 60 veces/seg
    private void actualizar() {
        // Mover nave?
        if (moviendoDerecha) {
            nave.mover(DELTA_X);
        }
        if (moviendoIzquierda) {
            nave.mover(-DELTA_X);
        }

        // Actualizar bala
        if (bala != null) {
            bala.mover(5);
            // Preguntar si se sale de la pantalla
            if (bala.sprite.getY()>ALTO) {
                bala = null;    // Invalidar esta bala
            }
        }

        // Probar colisiones
        if (bala!=null) {
            probarColisiones();
        }

        // Mover aliens
        moverAliens();

        // Eliminar los aliens marcados
        //depurarAliens();
    }

    private void depurarAliens() {
        for (int i=arrAliens.size-1; i>=0; i--) {
            Alien alien = arrAliens.get(i);
            if (alien.getEstado()==EstadoAlien.EXPLOTA) {
                arrAliens.removeIndex(i);
            }
        }
    }

    // Mueve los aliens en la pantalla  <----->
    private void moverAliens() {
        timerMover += Gdx.graphics.getDeltaTime();
        if (timerMover>=LIMITE_PASOS_ALIENS) {
            timerMover = 0;
            for (Alien alien : arrAliens) {
                alien.moverHorizontal(DX_PASO_ALIEN);
                alien.cambiarEstado();
            }
            contadorPasos++;
            if (contadorPasos>=20) {
                contadorPasos = 0;
                DX_PASO_ALIEN = -DX_PASO_ALIEN;
            }
            depurarAliens();
        }
    }

    /*
    Prueba la colisión de la bala contra TODOS los enemigos
    La bala EXISTE
     */
    private void probarColisiones() {
        // NO podemos usar el iterador
        for (int i=arrAliens.size-1; i>=0; i--) {
            Alien alien = arrAliens.get(i);
            if (bala.sprite.getBoundingRectangle().overlaps(alien.sprite.getBoundingRectangle())) {
                // Le pego!!!!!
                alien.setEstado(EstadoAlien.EXPLOTA);
                // Contar puntos
                puntos += 150;
                // Desaparecer la bala
                bala = null;    // No regresar al for
                break;
            }
            if (alien.getEstado()==EstadoAlien.EXPLOTA) {
                arrAliens.removeIndex(i);
            }
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    // Cuando la pantalla se oculta, se ejecuta este método.
    // LIBERAR todos los recursos (texturas, audios, fuentes, colecciones)
    @Override
    public void dispose() {
        texturaBala.dispose();
        texturaDisparo.dispose();
        arrAliens.clear();  // Elimina los objetos
    }

    private class ProcesadorEntrada implements InputProcessor 
    {

        @Override
        public boolean keyDown(int keycode) {
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        // Cuando el usuario toca la pantalla (pone el dedo sobre la pantalla)
        // Mover la nave, a la derecha si toco en la parte derecha de la pantalla,
        // a la izquierda en otro caso
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {

            Vector3 v = new Vector3(screenX, screenY, 0);
            camara.unproject(v);    // Convierte de coordenadas FÍSICAS a LÓGICAS
            
            // v ya tiene coordenadas lógicas
            float anchoBack = texturaBack.getWidth();
            float altoBack = texturaBack.getHeight();
            float xBack = anchoBack/2;
            float yBack = ALTO - 1.5f*altoBack;

            // PRIMERO, verificar el botón de back
            Rectangle rectBack = new Rectangle(xBack, yBack, anchoBack, altoBack);
            if (rectBack.contains(v.x, v.y)) {
                // SALIR, guardar el marcador
                Preferences preferencias = Gdx.app.getPreferences("PUNTAJE");
                preferencias.putInteger("puntos", puntos);
                preferencias.flush();   // Guardan en disco
                juego.setScreen(new PantallaMenu(juego));
            } else
            // SEGUNDO, probar si se hizo click dentro del botón de disparo
            if ( v.x>=ANCHO-2*texturaDisparo.getWidth() &&  v.x<=ANCHO - texturaDisparo.getWidth()
                    && v.y>=texturaDisparo.getHeight()/2 && v.y<=1.5f*texturaDisparo.getHeight()) {
                // Disparar !!!!!
                if (bala == null) {
                    bala = new Bala(texturaBala,
                            nave.sprite.getX() + nave.sprite.getWidth() / 2 - texturaBala.getWidth() / 2,
                            nave.sprite.getY() + nave.sprite.getHeight());
                }
            } else {
                // No disparó
                if (v.x < ANCHO / 2) {
                    // primera mitad, mover a la izq.
                    //nave.mover(-DELTA_X);
                    moviendoIzquierda = true;
                } else {
                    // segunda mitad, mover derecha
                    //nave.mover(DELTA_X);
                    moviendoDerecha = true;
                }
            }
            
            return true;    // Porque el juego ya procesó el evento
        }

        // Cuando el usuario quita el dedo de la pantalla
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            Vector3 v = new Vector3(screenX, screenY, 0);
            camara.unproject(v);    // Convierte de coordenadas FÍSICAS a LÓGICAS

            // v ya tiene coordenadas lógicas
            moviendoDerecha = false;
            moviendoIzquierda = false;

            return true;    // Porque el juego ya procesó el evento
        }

        // Cuando arrastro el dedo sobre la pantalla
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}
