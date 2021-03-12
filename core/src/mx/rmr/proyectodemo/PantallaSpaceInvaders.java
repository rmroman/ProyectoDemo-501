package mx.rmr.proyectodemo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/*
Representa un clon del juego Space Invaders
Autor: Roberto Mtz. Román
 */
public class PantallaSpaceInvaders extends Pantalla
{
    private Juego juego;

    // Enemigos
    //private Alien alien;    // borrar!!
    private Array<Alien> arrAliens;     // Guardar TODOS los aliens

    public PantallaSpaceInvaders(Juego juego) {
        this.juego = juego;
    }

    // Inicializan todos los objetos
    @Override
    public void show() {
        crearEnemigos();
    }

    private void crearEnemigos() {
        Texture texturaAlien = new Texture("space/enemigoArriba.png");
        //alien = new Alien(texturaAlien, ANCHO/2, ALTO/2);
        // CREAR 55 aliens (11 columnas x 5 filas)
        arrAliens = new Array<>(11*5);
        for (int renglon=0; renglon<5; renglon++) { // Recorre los renglones (0->4)
            for (int columna=0; columna<11; columna++) {
                Alien alien = new Alien(texturaAlien, 310 + columna*60, ALTO/2 + renglon*60);
                arrAliens.add(alien);       // Lo guarda en el arreglo
            }
        }
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0, 0, 0);    // Borrar con color negro

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        //alien.render(batch);
        // Dibujar los 55 aliens que están el arreglo
        for (Alien alien : arrAliens) {     // Automáticamente visita CADA objeto del arreglo
            alien.render(batch);
        }
        batch.end();
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
