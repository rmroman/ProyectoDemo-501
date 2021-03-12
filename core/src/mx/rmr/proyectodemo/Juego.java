package mx.rmr.proyectodemo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
Representa a la APLICACIóN que corre, el objeto está vivo durante toda la ejecución
Autor: Roberto Mtz. Román
 */
public class Juego extends Game
{
	@Override
	public void create () {
		// Muestre la primera pantalla
		setScreen(new PantallaMenu(this));		// Primera pantalla VISIBLE
	}
}
