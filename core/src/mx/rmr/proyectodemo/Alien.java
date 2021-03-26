package mx.rmr.proyectodemo;

import com.badlogic.gdx.graphics.Texture;

/*
Representa los 'aliens' enemigos en el juego.
Autor: Roberto Mtz. Román
 */
public class Alien extends Objeto
{
    // Animación
    private Texture texturaArriba;
    private Texture texturaAbajo;
    private Texture texturaExplota;
    private EstadoAlien estado;     // ARRIBA, ABAJO, EXPLOTA

    public Alien(Texture textura, float x, float y) {
        super(textura, x, y);   // El constructor de la superclase inicialice el sprite
    }

    // Constructor (recibe 3 texturas)
    public Alien(Texture texturaArriba, Texture texturaAbajo, Texture texturaExplota, float x, float y) {
        super(texturaArriba, x, y);
        this.texturaArriba = texturaArriba;
        this.texturaAbajo = texturaAbajo;
        this.texturaExplota = texturaExplota;
        estado = EstadoAlien.ARRIBA;
    }

    // Cambiar el estado
    public void cambiarEstado() {
        switch (estado) {
            case ARRIBA:
                estado = EstadoAlien.ABAJO;
                sprite.setTexture(texturaAbajo);
                break;
            case ABAJO:
                estado = EstadoAlien.ARRIBA;
                sprite.setTexture(texturaArriba);
                break;
        }
    }

    public EstadoAlien getEstado() {
        return estado;
    }

    public void setEstado(EstadoAlien nuevoEstado) {
        this.estado = nuevoEstado;
        if (nuevoEstado == EstadoAlien.EXPLOTA) {
            sprite.setTexture(texturaExplota);
        }
    }

    // Mover al alien. Se mueve dx pixeles
    public void moverHorizontal(float dx) {
        sprite.setX( sprite.getX() + dx );
    }

    // Dibujar  (ya lo heredó)

    // 'Disparar'
}
