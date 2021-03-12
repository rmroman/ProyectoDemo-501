package mx.rmr.proyectodemo;

import com.badlogic.gdx.graphics.Texture;

/*
Representa los 'aliens' enemigos en el juego.
Autor: Roberto Mtz. Román
 */
public class Alien extends Objeto
{
    public Alien(Texture textura, float x, float y) {
        super(textura, x, y);   // El constructor de la superclase inicialice el sprite
    }

    // Mover al alien. Se mueve dx pixeles
    public void moverHorizontal(float dx) {
        sprite.setX( sprite.getX() + dx );
    }

    // Dibujar  (ya lo heredó)

    // 'Disparar'
}
