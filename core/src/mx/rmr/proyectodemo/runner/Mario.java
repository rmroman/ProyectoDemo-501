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
    }

    // Reescribimos el método render, porque vamos a mostrar la animación
    public void render(SpriteBatch batch) {
        float delta = Gdx.graphics.getDeltaTime();  // ???  ~> 1/60...
        timerAnimacion += delta;        // Lo acumulamos
        TextureRegion frame = animacionCorrer.getKeyFrame(timerAnimacion);
        batch.draw(frame, sprite.getX(), sprite.getY());
    }

}
