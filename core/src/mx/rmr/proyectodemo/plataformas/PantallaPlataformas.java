package mx.rmr.proyectodemo.plataformas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import mx.rmr.proyectodemo.Juego;
import mx.rmr.proyectodemo.Pantalla;

public class PantallaPlataformas extends Pantalla
{
    // Mapa
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;


    private Texture fondo;

    public PantallaPlataformas(Juego juego) {
    }

    @Override
    public void show() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("mapas/MapaMario.tmx", TiledMap.class);
        manager.finishLoading();
        mapa = manager.get("mapas/MapaMario.tmx");
        rendererMapa = new OrthogonalTiledMapRenderer(mapa);


        fondo = new Texture("runner/fondoMario_5.jpg");
    }

    @Override
    public void render(float delta) {
        borrarPantalla(1,0,0);
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(fondo,0,0);
        batch.end();

        rendererMapa.setView(camara);
        rendererMapa.render();


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
