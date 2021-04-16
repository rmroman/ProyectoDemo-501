package mx.rmr.proyectodemo.utilerias;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/*
Despliega texto en la pantalla gráfica
Autor: Roberto Mtz. Román
 */
public class Texto
{
    private BitmapFont font;

    public Texto(String archivo) {
        font = new BitmapFont(Gdx.files.internal(archivo));
        font.setColor(0,1,0,1f);
    }

    /*
    Despliega el texto 'mensaje' en la coordenadas centrado en (x,y)
     */
    public void mostrarMensaje(SpriteBatch batch, String mensaje, float x, float y) {
        GlyphLayout glyph = new GlyphLayout();
        glyph.setText(font, mensaje);
        float anchoTexto = glyph.width;
        font.draw(batch, glyph, x-anchoTexto/2, y);
    }
}
