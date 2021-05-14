package mx.rmr.proyectodemo.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mx.rmr.proyectodemo.Objeto;

/*
Representa el personaje que estará en el escenario
Autor: Roberto Mtz. Román
 */
public class Mario extends Objeto
{
    private Animation<TextureRegion> animacionCorrer;
    private float timerAnimacion;       // Para saber el frame que corresponde mostrar

    // Salto
    private final float yBase = 54;     // Suelo, piso
    private float tAire;                // Tiempo que lleva en el aire
    private float tVuelo;               // Tiempo de vuelo Total
    private final float v0y = 200;      // Componente en y de la velocidad
    private final float g = 150;      // Pixeles/s^2

    private EstadoMario estado;         // SALTANDO, CAMINANDO, SUBIENDO, BAJANDO, CIMA


    public Mario(Texture texture, float x, float y) {
        TextureRegion region = new TextureRegion(texture);
        TextureRegion[][] texturas = region.split(38, 76);

        // Cuadros para caminar
        TextureRegion[] arrFramesCaminar = { texturas[2][0], texturas[1][4], texturas[1][3] };
        animacionCorrer = new Animation<TextureRegion>(0.2f, arrFramesCaminar);
        animacionCorrer.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;

        // IDLE
        sprite = new Sprite(texturas[1][2]);
        sprite.setPosition(x, y);

        // Estado inicial
        estado = EstadoMario.CAMINANDO;
    }

    // Reescribimos el método render, porque vamos a mostrar la animación
    public void render(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();  // ???  ~> 1/60...
        switch (estado) {
            case CAMINANDO:
                timerAnimacion += delta;        // Lo acumulamos
                TextureRegion frame = animacionCorrer.getKeyFrame(timerAnimacion);
                batch.draw(frame, sprite.getX(), sprite.getY());
                break;
            case SALTANDO:
                actualizar();       // Saltando, calcular la nueva posición
                super.render(batch);
                break;
        }
    }

    // Calcula el movimiento vertical
    private void actualizar() {
        float delta = Gdx.graphics.getDeltaTime();
        tAire += 5*delta;
        float y = yBase + v0y*tAire - 0.5f * g * tAire*tAire;       // Fórmula
        sprite.setY(y);
        // ¿Cómo saber que ya terminó la simulación?
        if (tAire >= tVuelo || y <= yBase) {
            estado = EstadoMario.CAMINANDO;
            sprite.setY(yBase);
        }
    }

    // Accesor de sprite
    public Sprite getSprite() {
        return sprite;
    }

    public void saltar() {
        if (estado != EstadoMario.SALTANDO) {
            tAire = 0;      // Inicia el salto
            tVuelo = 2 * v0y / g;
            estado = EstadoMario.SALTANDO;
        }
    }
}
