package mx.rmr.proyectodemo.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import mx.rmr.proyectodemo.Objeto;

/*
Representa un enemigo
Autor: Roberto Mtz. Román
 */
public class Goomba extends Objeto
{
    private Animation<TextureRegion> animacion;
    private float timerAnimacion = 0;
    // Física
    private float velocidadX = -300;     // ??? unidades   pixels/seg   (izquierda)

    public Goomba(Texture textura, float x, float y) {
        TextureRegion region = new TextureRegion(textura);
        TextureRegion[][] texturas = region.split(31, 29);

        // Animación
        TextureRegion[] arrFrames = { texturas[0][0], texturas[0][1] };
        animacion = new Animation<TextureRegion>(0.3f, arrFrames);
        animacion.setPlayMode(Animation.PlayMode.LOOP);
        timerAnimacion = 0;

        sprite = new Sprite(texturas[0][2]);
        sprite.setPosition(x, y);
    }

    @Override       // Sobrescribe
    public void render(SpriteBatch batch) {
        timerAnimacion += Gdx.graphics.getDeltaTime();
        TextureRegion frame = animacion.getKeyFrame(timerAnimacion);
        batch.draw(frame, sprite.getX(), sprite.getY());
    }

    // Mover enemigo  (d = v/t)
    public void moverIzquierda(float delta) {
        float dx = velocidadX * delta;
        sprite.setX(sprite.getX() + dx);
    }
}
