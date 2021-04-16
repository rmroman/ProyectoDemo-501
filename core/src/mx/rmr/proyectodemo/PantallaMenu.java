package mx.rmr.proyectodemo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import mx.rmr.proyectodemo.runner.PantallaRunner;

public class PantallaMenu extends Pantalla
{
    // Referencia al juego principal
    private Juego juego;

    // Fondo de la pantalla del menú
    private Texture texturaFondo;

    // Escena
    private Stage escenaMenu;

    public PantallaMenu(Juego juego) {
        this.juego = juego;
    }

    /*
    Se ejecuta al inicio, antes de mostrar la pantalla
    INICIALIZAR los objetos
     */
    @Override
    public void show() {

        crearMenu();
    }

    private void crearMenu() {
        // Fondo
        texturaFondo = new Texture("menu/fondoMenu.jpg");

        // MENU, necesitamos una escena
        escenaMenu = new Stage(vista);

        // Actores.... Botón
        Button btnMario = crearBoton("menu/btn_jugar_mario.png", "menu/btnInverso_jugar_space.png");
        btnMario.setPosition(ANCHO/3, 2*ALTO/3, Align.center);
        // Agregar a la escena el botón
        escenaMenu.addActor(btnMario);

        Button btnSpace = crearBoton("menu/btn_jugar_space.png",
                "menu/btnInverso_jugar_space.png");
        btnSpace.setPosition(2*ANCHO/3, 2*ALTO/3, Align.center);
        escenaMenu.addActor(btnSpace);
        // REGISTRAR el evento de click para el botón
        btnSpace.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar de pantalla (SpaceInvaders)
                juego.setScreen(new PantallaSpaceInvaders(juego));
            }
        });

        Button btnRunner = crearBoton("menu/btn_jugar_runner.png", "menu/btnInverso_jugar_space.png");
        btnRunner.setPosition(ANCHO/3, ALTO/3, Align.center);
        escenaMenu.addActor(btnRunner);
        // Agregar
        btnRunner.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar de pantalla (Runner)
                juego.setScreen(new PantallaRunner(juego));
            }
        });

        Button btnConfiguracion = crearBoton("menu/btn_configuracion.png", "menu/btnInverso_jugar_space.png");
        btnConfiguracion.setPosition(2*ANCHO/3, ALTO/3, Align.center);
        escenaMenu.addActor(btnConfiguracion);

        // La ESCENA se encarga DE ATENDER LOS EVENTOS DE ENTRADA
        Gdx.input.setInputProcessor(escenaMenu);
    }

    private Button crearBoton(String archivo, String archivoInverso) {
        Texture texturaBoton = new Texture(archivo);
        TextureRegionDrawable trdBtnMario = new TextureRegionDrawable(texturaBoton);
        // Inverso
        Texture texturaInverso = new Texture(archivoInverso);
        TextureRegionDrawable trdBtnInverso = new TextureRegionDrawable(texturaInverso);

        return new Button(trdBtnMario, trdBtnInverso);
    }

    // Se ejecuta automáticamente (60 veces/seg)
    // delta es el tiempo que ha transcurrido entre frames
    @Override       // SOBRESCRIBE EL METODO
    public void render(float delta) {
        borrarPantalla(0, 1, 0);

        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        batch.draw(texturaFondo, 0, 0);
        batch.end();

        // Escena (DESPUES DEL FONDO)
        escenaMenu.draw();
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
