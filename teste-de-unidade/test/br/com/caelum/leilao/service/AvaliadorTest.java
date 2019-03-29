package br.com.caelum.leilao.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.LeilaoBuilder;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class AvaliadorTest {

	private Avaliador leiloeiro;

	@Before
	public void setup() {
		leiloeiro = new Avaliador();
	}

	@Test
	public void deveEntenderLancesOrdemCrescente() {

		Usuario joao = new Usuario("João");
		Usuario jose = new Usuario("José");
		Usuario maria = new Usuario("Maria");

		Leilao leilao = new LeilaoBuilder()
				.to("Video Game")
				.lance(new Lance(joao, 250))
				.lance(new Lance(jose, 300))
				.lance(new Lance(maria, 400))
				.build();

		leiloeiro.avalia(leilao);

		assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
		assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));

	}

	@Test
	public void deveEntenderLancesOrdemDecrescente() {

		Usuario joao = new Usuario("João");
		Usuario jose = new Usuario("José");
		Usuario maria = new Usuario("Maria");

		Leilao leilao = new LeilaoBuilder()
				.to("Video Game")
				.lance(new Lance(maria, 400))
				.lance(new Lance(jose, 300))
				.lance(new Lance(joao, 250))
				.build();

		leiloeiro.avalia(leilao);
		
		assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
		assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));
	}

	@Test
	public void deveEntenderLancesSemOrdem() {

		Usuario joao = new Usuario("João");
		Usuario jose = new Usuario("José");
		Usuario maria = new Usuario("Maria");

		Leilao leilao = new LeilaoBuilder()
				.to("Video Game")
				.lance(new Lance(maria, 400))
				.lance(new Lance(joao, 250))
				.lance(new Lance(jose, 300))
				.build();

		leiloeiro.avalia(leilao);
		
		assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
		assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));

	}

	@Test
	public void deveEntenderLeilaoDeUmLance() {

		Usuario joao = new Usuario("João");

		Leilao leilao = new LeilaoBuilder()
				.to("Video Game")
				.lance(new Lance(joao, 250))
				.build();

		leiloeiro.avalia(leilao);

		assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
		assertThat(leiloeiro.getMaiorLance(), equalTo(250.0));
	}

	@Test(expected=RuntimeException.class)
	public void deveEntenderLeilaoSemLances() {

		Leilao leilao = new LeilaoBuilder()
				.to("Video Game")
				.build();

		leiloeiro.avalia(leilao);

	}

	@Test
	public void deveCalcularMedia() {

		Usuario joao = new Usuario("João");
		Usuario jose = new Usuario("José");
		Usuario maria = new Usuario("Maria");

		Lance l1 = new Lance(maria, 400);
		Lance l2 = new Lance(joao, 250);
		Lance l3 = new Lance(jose, 300);
		
		Leilao leilao = new LeilaoBuilder()
				.to("Video Game")
				.lance(l1)
				.lance(l2)
				.lance(l3)
				.build();

		leiloeiro.avalia(leilao);

		double media = (l1.getValor() + l2.getValor() + l3.getValor()) / 3;

		assertThat(leiloeiro.getMedia(), equalTo(media));

	}

	@Test
	public void deveEncontrarOsTresMaioresLances() {

		Usuario joao = new Usuario("João");
		Usuario jose = new Usuario("José");
		Usuario maria = new Usuario("Maria");
		Usuario carlos = new Usuario("Carlos");

		Leilao leilao = new LeilaoBuilder()
				.to("Video Game")
				.lance(new Lance(maria, 400))
				.lance(new Lance(joao, 250))
				.lance(new Lance(jose, 300))
				.lance(new Lance(carlos, 500))
				.lance(new Lance(carlos, 100))
				.build();

		leiloeiro.avalia(leilao);	

		assertThat(leiloeiro.getTresMaiores().size(), equalTo(3));
		assertThat(leiloeiro.getTresMaiores(), hasItems(new Lance(carlos,500),new Lance(maria,400),new Lance(jose,300)));
		
	}

	@Test
	public void deveEncontrarOs3MaioresLancesComApenas2Lances() {
		Usuario joao = new Usuario("João");
		Usuario jose = new Usuario("José");

		Leilao leilao = new LeilaoBuilder()
				.to("Video Game")
				.lance(new Lance(jose, 300))
				.lance(new Lance(joao, 250))
				.build();
				
		leiloeiro.avalia(leilao);

		assertThat(leiloeiro.getTresMaiores().size(), equalTo(2));
		assertThat(leiloeiro.getTresMaiores(), hasItems(new Lance(jose,300), new Lance(joao,250)));
		
	}

}
