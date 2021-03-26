package mx.rmr.proyectodemo;

import com.badlogic.gdx.graphics.Texture;

/*
La nave del jugador
Autor: Roberto Mtz
 */
public class Nave extends Objeto
{
    public Nave(Texture textura, float x, float y) {
        super(textura, x, y);
    }

    // Para mover el sprite ( + ->,  - <-)
    public void mover(float dx) {
        sprite.setX( sprite.getX() + dx );
    }
}
