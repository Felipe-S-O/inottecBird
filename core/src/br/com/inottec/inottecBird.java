package br.com.inottec;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class inottecBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Random numeroRandomico;
	private BitmapFont fonte;
	private Circle passaroCirculo;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
	//private ShapeRenderer shape;

	// Atributos de configuracao
	private int larguraDispositivo;
	private int alturaDispositivo;
	private int estadoJogo = 0; // 0 -> jogo não iniciado 1 -> jogo iniciado
	private int pontuacao = 0;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoEntreCanos;
	private float deltaTime;
	private float alturaEntreCanosRandomica;
	private boolean marcauPonto = false;

	@Override
	public void create () {
		batch = new SpriteBatch();
		numeroRandomico = new Random();
		passaroCirculo = new Circle();
		/*retanguloCanoBaixo = new Rectangle();
		retanguloCanoTopo = new Rectangle();
		shape = new ShapeRenderer();*/
		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(10);

		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");

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
		variacao += deltaTime * 5;
		if (variacao > 2) variacao = 0;

		if (estadoJogo == 0){//Não iniciado

			if (Gdx.input.justTouched()){
				estadoJogo = 1;
			}
		}else {
			posicaoMovimentoCanoHorizontal -= deltaTime * 200;
			velocidadeQueda++;

			if (Gdx.input.justTouched()) {
				velocidadeQueda = -15;
			}

			if (posicaInicialVertical > 20 || velocidadeQueda < 20) {
				posicaInicialVertical = posicaInicialVertical - velocidadeQueda;
			}

			//verifica se o cano saiu inteiramente da tela
			if (posicaoMovimentoCanoHorizontal < -100) {
				posicaoMovimentoCanoHorizontal = larguraDispositivo;
				alturaEntreCanosRandomica = numeroRandomico.nextInt(1000) - 250;
				marcauPonto = false;
			}
			//Verifica pontuação
			if (posicaoMovimentoCanoHorizontal < 138 ){
				if (!marcauPonto){
					pontuacao++;
					marcauPonto = true;
				}

			}
		}
		batch.begin();

		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal,alturaDispositivo/2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal,alturaDispositivo/2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
		batch.draw(passaros [(int)variacao], 140, posicaInicialVertical);
		fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50 );
		batch.end();

		passaroCirculo.set(140+passaros[0].getWidth()/2,posicaInicialVertical+passaros[0].getHeight()/2, passaros[0].getWidth()/2);
        retanguloCanoBaixo = new Rectangle(
			posicaoMovimentoCanoHorizontal, alturaDispositivo/2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica
			, canoBaixo.getWidth(), canoBaixo.getHeight()
		);
		retanguloCanoTopo = new Rectangle(
		 	posicaoMovimentoCanoHorizontal, alturaDispositivo/2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica
			, canoTopo.getWidth(), canoTopo.getHeight()
		);
		//Desenhar formas
		/*shape.begin(ShapeRenderer.ShapeType.Filled);
		shape.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);
		shape.rect(retanguloCanoTopo.x, retanguloCanoTopo.y, retanguloCanoTopo.width, retanguloCanoTopo.height);
		shape.rect(retanguloCanoBaixo.x, retanguloCanoBaixo.y, retanguloCanoBaixo.width, retanguloCanoBaixo.height);
		shape.setColor(Color.RED);
		shape.end();*/

		//Teste de colisão
		if (Intersector.overlaps(passaroCirculo, retanguloCanoBaixo) || Intersector.overlaps(passaroCirculo, retanguloCanoTopo)){

			Gdx.app.log("Colisão", "Houve colisão");

		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
//		passaros.dispose();
	}
}
