package br.com.caelum.leilao.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import org.junit.Test;

import br.com.caelum.leilao.builder.LeilaoBuilder;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class LeilaoTest {

	@Test
	public void deveReceberUmLance() {

		Usuario joao = new Usuario("João");

		Leilao leilao = new LeilaoBuilder()
				.to("Bicicleta")
				.lance(new Lance(joao, 150))
				.build();

		assertThat(leilao.getLances().size(), equalTo(1));
		assertThat(leilao.getLances(), hasItems(new Lance(joao, 150)));

	}

	@Test
	public void deveReceberMaisDeUmLance() {

		Usuario joao = new Usuario("João");
		Usuario jose = new Usuario("José");

		Leilao leilao = new LeilaoBuilder()
				.to("Bicicleta")
				.lance(new Lance(joao, 150))
				.lance(new Lance(jose, 220))
				.build();

		assertThat(leilao.getLances().size(), equalTo(2));
		assertThat(leilao.getLances(), hasItems(new Lance(joao, 150), new Lance(jose, 220)));
		
	}

	@Test
	public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario() {
		Usuario joao = new Usuario("João");

		Leilao leilao = new LeilaoBuilder()
				.to("Bicicleta")
				.lance(new Lance(joao, 150))
				.lance(new Lance(joao, 180))
				.build();

		assertThat(leilao.getLances().size(), equalTo(1));
		assertThat(leilao.getLances(), not(hasItems(new Lance(joao, 180))));
	}

	@Test
	public void naoDeveAceitarMaisDeCincoLancesPorUsuario() {

		Usuario joao = new Usuario("João");
		Usuario jose = new Usuario("Jose");

		LeilaoBuilder builder = new LeilaoBuilder().to("Bicicleta");

		Lance lJoao1 = new Lance(joao, 100);
		Lance lJose1 = new Lance(jose, 110);
		builder.lance(lJoao1);
		builder.lance(lJose1);

		Lance lJoao2 = new Lance(joao, 200);
		Lance lJose2 = new Lance(jose, 210);
		builder.lance(lJoao2);
		builder.lance(lJose2);

		Lance lJoao3 = new Lance(joao, 300);
		Lance lJose3 = new Lance(jose, 310);
		builder.lance(lJoao3);
		builder.lance(lJose3);

		Lance lJoao4 = new Lance(joao, 400);
		Lance lJose4 = new Lance(jose, 410);
		builder.lance(lJoao4);
		builder.lance(lJose4);

		Lance lJoao5 = new Lance(joao, 500);
		Lance lJose5 = new Lance(jose, 510);
		builder.lance(lJoao5);
		builder.lance(lJose5);

		// lance a ser ignorado
		Lance lJoao6 = new Lance(joao, 600);
		builder.lance(lJoao6);

		Leilao leilao = builder.build();

		Lance ultimoLance = leilao.getLances().get(leilao.getLances().size() - 1);
		
		assertThat(leilao.getLances().size(), equalTo(10));
		assertThat(ultimoLance.getValor(), equalTo(510.0));

	}

	@Test
	public void deveDobrarUltimoLanceDeUmUsuario() {

		Usuario joao = new Usuario("Joao");
		Usuario jose = new Usuario("Jose");

		Leilao leilao = new LeilaoBuilder()
				.to("Bicicleta")
				.lance(new Lance(joao, 100))
				.lance(new Lance(jose, 110))
				.build();

		leilao.dobraLance(joao);

		Lance ultimoLance = leilao.getLances().get(leilao.getLances().size() - 1);
		assertThat(ultimoLance.getValor(), equalTo(200.0));

	}
	
}
