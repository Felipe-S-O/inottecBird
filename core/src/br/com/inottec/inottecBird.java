package br.com.inottec;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class inottecBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;

	// Atributos de configuracao
	private int larguraDispositivo;
	private int alturaDispositivo;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaInicialVertical;

	@Override
	public void create () {
		batch = new SpriteBatch();

		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaInicialVertical = alturaDispositivo / 2;

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		velocidadeQueda++;

		variacao += Gdx.graphics.getDeltaTime() * 5;
		if(variacao > 2) variacao = 0;

		if(Gdx.input.justTouched()){
			velocidadeQueda = -15;
		}

		if(posicaInicialVertical > 20 || velocidadeQueda < 20) {
			posicaInicialVertical = posicaInicialVertical - velocidadeQueda;
		}

		batch.begin();

		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);

		batch.draw(passaros [(int)variacao], 30, posicaInicialVertical);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
//		passaros.dispose();
	}
}
