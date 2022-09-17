package br.com.inottec;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class inottecBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canotopo;
	private Random numeroRandomico;

	// Atributos de configuracao
	private int larguraDispositivo;
	private int alturaDispositivo;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoEntreCanos;
	private float deltaTime;
	private float alturaEntreCanosRandomica;

	@Override
	public void create () {
		batch = new SpriteBatch();

		numeroRandomico = new Random();
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		canotopo = new Texture("cano_topo_maior.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaInicialVertical = alturaDispositivo / 2;
		posicaoMovimentoCanoHorizontal = larguraDispositivo;
		espacoEntreCanos = 300;

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		deltaTime = Gdx.graphics.getDeltaTime();

		posicaoMovimentoCanoHorizontal -=  deltaTime * 200;
		velocidadeQueda++;

		variacao += deltaTime * 5;
		if(variacao > 2) variacao = 0;

		if(Gdx.input.justTouched()){
			velocidadeQueda = -15;
		}

		if(posicaInicialVertical > 20 || velocidadeQueda < 20) {
			posicaInicialVertical = posicaInicialVertical - velocidadeQueda;
		}

		//verifica se o cano saiu inteiramente da tela
		if (posicaoMovimentoCanoHorizontal < - 100){
			posicaoMovimentoCanoHorizontal = larguraDispositivo;
			alturaEntreCanosRandomica = numeroRandomico.nextInt(1000)-250;
		}

		batch.begin();

		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(canotopo, posicaoMovimentoCanoHorizontal,alturaDispositivo/2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal,alturaDispositivo/2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
		batch.draw(passaros [(int)variacao], 140, posicaInicialVertical);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
//		passaros.dispose();
	}
}
