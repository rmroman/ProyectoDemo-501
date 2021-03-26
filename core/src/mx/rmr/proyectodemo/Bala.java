package mx.rmr.proyectodemo;

import com.badlogic.gdx.graphics.Texture;

// Proyectil,
// Autor: Roberto Martínez Román
public class Bala extends Objeto
{
    public Bala(Texture textura, float x, float y) {
        super(textura, x, y);
    }

    // Mover VERTICAL
    public void mover(float dy) {
        sprite.setY( sprite.getY() + dy);
    }
}
