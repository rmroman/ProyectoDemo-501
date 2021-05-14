package mx.rmr.proyectodemo.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import mx.rmr.proyectodemo.Juego;
import mx.rmr.proyectodemo.Pantalla;
import mx.rmr.proyectodemo.PantallaSpaceInvaders;

public class PantallaRunner extends Pantalla
{
    private final Juego juego;

    // Fondo infinito
    private Texture texturaFondo;
    private float xFondo = 0;

    // Personaje (Mario)
    private Mario mario;
    private Texture texturaMario;

    // Enemigo (Goomba)
    private Array<Goomba> arrGoombas;
    private Texture texturaGoomba;
    private float timerCreaGoomba;  // Acumulador tiempo
    private final float TIEMPO_CREAR_GOOMBA = 2;

    // Disparo del personaje
    private Array<Bola> arrBolas;
    private Texture texturaBola;        // Imagen

    // PAUSA
    private EscenaPausa escenaPausa;

    private ProcesadorEntrada procesadorEntrada;

    // ESTADOS del JUEGO  (JUGANDO, PAUSADO)
    private EstadoJuego estadoJuego = EstadoJuego.JUGANDO;

    public PantallaRunner(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        crearFondo();
        crearMario();
        crearGoombas();
        crearBolas();
        // Pone el InputProcessor
        procesadorEntrada = new ProcesadorEntrada();
        Gdx.input.setInputProcessor(procesadorEntrada);
    }

    private void crearBolas() {
        arrBolas = new Array<>();
        texturaBola = new Texture("runner/bolaFuego.png");
    }

    private void crearGoombas() {
        texturaGoomba = new Texture("runner/goomba.png");
        //goomba = new Goomba(texturaGoomba, ANCHO-62, 54);
        arrGoombas = new Array<>();
    }

    private void crearMario() {
        texturaMario = new Texture("runner/MarioSprites.png");
        mario = new Mario(texturaMario, ANCHO/2, 54);
    }

    private void crearFondo() {
        texturaFondo = new Texture("runner/fondoMario_5.jpg");
    }

    @Override
    public void render(float delta) {
        // Actualizar
        actualizar(delta);

        borrarPantalla(0,0,0);

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        // Dibujar fondo
        batch.draw(texturaFondo, xFondo, 0);
        batch.draw(texturaFondo, xFondo + texturaFondo.getWidth(), 0);
        // Dibujar  Mario
        mario.render(batch);
        // Dibujar Goomba
        for (Goomba goomba : arrGoombas) {
            goomba.render(batch);
        }
        // Dibujar las bolas de fuego
        for (Bola bola : arrBolas) {
            bola.render(batch);
        }
        batch.end();
        // Dibujar la PAUSA
        if (estadoJuego == EstadoJuego.PAUSADO && escenaPausa != null) {
            escenaPausa.draw();
        }
    }

    // Se actualizan todos los objetos del nivel (escenario)
    private void actualizar(float delta) {
        if (estadoJuego == EstadoJuego.JUGANDO) {
            actualizarFondo();
            actualizarGoombas(delta);
            actualizarBolas(delta);
        }
    }

    // Mover todos los proyectiles (bolas de fuego)
    private void actualizarBolas(float delta) {
        //for (Bola bola : arrBolas) {
        for (int i=arrBolas.size-1; i>=0; i--) {
            Bola bola = arrBolas.get(i);
            bola.mover(delta);
            // Prueba si la bola debe desaparecer, porque salió de la pantalla
            if (bola.getX() > ANCHO) {      // Lógicamente necesito solo la x de la bola
                // Borrar el objeto
                arrBolas.removeIndex(i);
            }
        }
    }

    private void actualizarGoombas(float delta) {
        // CREAR GOOMBAS
        timerCreaGoomba += delta;
        if (timerCreaGoomba>=TIEMPO_CREAR_GOOMBA) {
            timerCreaGoomba = 0;
            // Crear Enemigo
            float xGoomba = MathUtils.random(ANCHO, ANCHO*1.5f);
            Goomba goomba = new Goomba(texturaGoomba, xGoomba, 54);
            arrGoombas.add(goomba);
        }

        // Mover los enemigos
        for (Goomba goomba : arrGoombas) {
            goomba.moverIzquierda(delta);         // FÍSICA
        }
    }

    private void actualizarFondo() {
        xFondo -= 2;
        if (xFondo <= -texturaFondo.getWidth()) {
            xFondo = 0;
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private class ProcesadorEntrada implements InputProcessor
    {
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Vector3 v = new Vector3(screenX, screenY, 0);
            camara.unproject(v);    // Cambia coordenadas físicas a lógica

            if (v.x >= ANCHO/2) {
                // Dispara!!!
                Bola bola = new Bola(texturaBola, mario.getSprite().getX(), mario.getSprite().getY());
                arrBolas.add(bola);
            } else {
                // Salta!!!
                //mario.saltar();     // Top-Down
                // PONER LA PAUSA
                if (escenaPausa == null) {      // Inicialización lazy
                    escenaPausa = new EscenaPausa(vista);
                }
                estadoJuego = EstadoJuego.PAUSADO;
                // CAMBIAR el procesador de entrada
                Gdx.input.setInputProcessor(escenaPausa);       // Click sobre Botón
            }

            return true;
        }

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



        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

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

    // La escena que se muestra cuendo el juagador pausa le juego
    private class EscenaPausa extends Stage
    {
        private Texture texturaFondo;

        public EscenaPausa(Viewport vista) {
            super(vista);       // Pasa la vista al constructor de Stage
            // Imagen de la ventana de pausa
            texturaFondo = new Texture("pausa/fondoPausa.png");
            Image imgFondo = new Image(texturaFondo);
            imgFondo.setPosition(ANCHO/2, ALTO/2, Align.center);
            addActor(imgFondo);
            // Botón para continuar
            Texture texturaBtn = new Texture("pausa/btnContinuar.png");
            TextureRegionDrawable trd = new TextureRegionDrawable(texturaBtn);
            // Agregar la imagen inversa (presionada)
            Button btn = new Button(trd);
            addActor(btn);
            btn.setPosition(ANCHO/2, 0.3f*ALTO, Align.center);
            // Agregar el listener del botón
            btn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    // QUITAR LA PAUSA
                    if ( estadoJuego==EstadoJuego.PAUSADO ) {       // No es necesario
                        estadoJuego = EstadoJuego.JUGANDO;
                        Gdx.input.setInputProcessor(procesadorEntrada);
                        // No es necesario hacer la escenaPausa NULL
                    }
                }
            } );
        }
    }

    private enum EstadoJuego
    {
        JUGANDO,
        PAUSADO
    }
}
