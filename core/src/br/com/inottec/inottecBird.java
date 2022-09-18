package br.com.inottec;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class inottecBird extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
	private Texture gameOver;
	private Random numeroRandomico;
	private BitmapFont fonte;
	private BitmapFont mensagem, mensagem2;
	private Circle passaroCirculo;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
	//private ShapeRenderer shape;

	// Atributos de configuracao
	private float larguraDispositivo;
	private float alturaDispositivo;
	private int estadoJogo = 0; // 0-> jogo não iniciado 1-> jogo iniciado 2-> tela Game over
	private int pontuacao = 0;

	private float variacao = 0;
	private float velocidadeQueda = 0;
	private float posicaInicialVertical;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoEntreCanos;
	private float deltaTime;
	private float alturaEntreCanosRandomica;
	private boolean marcauPonto = false;

	//Camera
	private OrthographicCamera camera;
	private Viewport viewport;
	private  final float VIRTUAL_WIDTH = 950;
	private  final float VIRTUAL_HEIGHT = 1200;


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
		fonte.getData().setScale(6);

		mensagem = new BitmapFont();
		mensagem.setColor(Color.WHITE);
		mensagem.getData().setScale(3);
		mensagem2 = new BitmapFont();
		mensagem2.setColor(Color.WHITE);
		mensagem2.getData().setScale(3);

		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
		gameOver = new Texture("game_over.png");

		/****************************************************
		 * Configuração da câmera
		 */
		camera = new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2, 0);
		viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);

		larguraDispositivo = VIRTUAL_WIDTH;
		alturaDispositivo = VIRTUAL_HEIGHT;
		posicaInicialVertical = alturaDispositivo / 2;
		posicaoMovimentoCanoHorizontal = larguraDispositivo;
		espacoEntreCanos = 250;

	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
        camera.update();

		//Limpar farmes anterores
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 5;
		if (variacao > 2) variacao = 0;

		if (estadoJogo == 0){//Não iniciado

			if (Gdx.input.justTouched()){
				estadoJogo = 1;
			}
		}else {//Iniciado

			velocidadeQueda++;
			if (posicaInicialVertical > 20 || velocidadeQueda < 20) {
				posicaInicialVertical = posicaInicialVertical - velocidadeQueda;
			}


			if (estadoJogo == 1){

				posicaoMovimentoCanoHorizontal -= deltaTime * 200;

				if (Gdx.input.justTouched()) {
					velocidadeQueda = -15;
				}
				//verifica se o cano saiu inteiramente da tela
				if (posicaoMovimentoCanoHorizontal < -100) {
					posicaoMovimentoCanoHorizontal = larguraDispositivo;
					alturaEntreCanosRandomica = numeroRandomico.nextInt(700) - 250;
					marcauPonto = false;
				}
				//Verifica pontuação
				if (posicaoMovimentoCanoHorizontal < 138 ){
					if (!marcauPonto){
						pontuacao++;
						marcauPonto = true;
					}

				}
			}else {

				if (Gdx.input.justTouched()){
					estadoJogo = 0;
					pontuacao = 0;
					velocidadeQueda = 0;
					posicaInicialVertical = alturaDispositivo/2;
					posicaoMovimentoCanoHorizontal = larguraDispositivo;
				}

			}

		}
		//Configuração dados de projeto da câmera
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal,alturaDispositivo/2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal,alturaDispositivo/2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
		batch.draw(passaros [(int)variacao], 140, posicaInicialVertical);
		fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50 );
		if (estadoJogo == 2){
			batch.draw(gameOver, larguraDispositivo/2 - gameOver.getWidth()/2, alturaDispositivo/2);
			mensagem.draw(batch, "Toque para Reiniciar!",larguraDispositivo/2 - 210, alturaDispositivo/2 - gameOver.getHeight()/2);
			mensagem2.draw(batch, "Felipe S.O | inottec",larguraDispositivo/2 -200, 80);
		}
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
		if (Intersector.overlaps(passaroCirculo, retanguloCanoBaixo) || Intersector.overlaps(passaroCirculo, retanguloCanoTopo)
				|| posicaInicialVertical <= 20 || posicaInicialVertical >= alturaDispositivo){

			estadoJogo = 2;
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
//		passaros.dispose();
	}
}
