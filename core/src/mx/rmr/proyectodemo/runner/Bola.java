package mx.rmr.proyectodemo.runner;

import com.badlogic.gdx.graphics.Texture;

import mx.rmr.proyectodemo.Objeto;

/**
 * Cada una de las bola de fuego que lanza el personaje
 */
public class Bola extends Objeto
{
    private float vX = 350;     // pixeles/seg

    public Bola(Texture textura, float x, float y) {
        super(textura, x, y);
    }

    // Mover a la derecha la bola de fuego
    public void mover(float delta) {
        float dx = vX * delta;
        sprite.setX(sprite.getX() + dx);
    }

    public float getX() {
        return sprite.getX();
    }
}
