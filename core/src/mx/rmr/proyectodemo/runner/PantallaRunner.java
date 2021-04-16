package mx.rmr.proyectodemo.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import mx.rmr.proyectodemo.Juego;
import mx.rmr.proyectodemo.Pantalla;

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
    //private Goomba goomba;
    private Array<Goomba> arrGoombas;
    private Texture texturaGoomba;
    private float timerCreaGoomba;  // Acumulador tiempo
    private final float TIEMPO_CREAR_GOOMBA = 2;

    public PantallaRunner(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {
        crearFondo();
        crearMario();
        crearGoombas();
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
        // Dibujar  MArio
        mario.render(batch);
        // Dibujar Goomba
        //goomba.render(batch);
        for (Goomba goomba : arrGoombas) {
            goomba.render(batch);
        }
        batch.end();
    }

    private void actualizar(float delta) {
        actualizarFondo();
        actualizarGoombas(delta);
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
}
