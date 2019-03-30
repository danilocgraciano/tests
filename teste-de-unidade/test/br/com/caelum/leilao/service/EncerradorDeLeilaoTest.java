package br.com.caelum.leilao.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.LeilaoBuilder;
import br.com.caelum.leilao.dao.LeilaoDao;
import br.com.caelum.leilao.dominio.Leilao;

public class EncerradorDeLeilaoTest {

	private LeilaoDao dao;
	private EncerradorDeLeilao encerrador;

	@Before
	public void setup() {
		dao = mock(LeilaoDao.class);
		encerrador = new EncerradorDeLeilao(dao);
	}

	@Test
	public void deveEncerrarLeiloesQuePassaramDoPrazo() {
		LocalDate antiga = LocalDate.of(1999, 1, 20);

		Leilao leilao1 = new LeilaoBuilder().to("TV de Plasma").onDate(antiga).build();
		Leilao leilao2 = new LeilaoBuilder().to("Geladeira").onDate(antiga).build();

		when(dao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));

		encerrador.encerra();

		assertThat(encerrador.getTotalEncerrados(), equalTo(2));
		assertThat(leilao1.isEncerrado(), equalTo(true));
		assertThat(leilao2.isEncerrado(), equalTo(true));

	}

	@Test
	public void naoDeveEncerrarLeiloesAindaVigentes() {

		LocalDate ontem = LocalDate.now().minus(Period.ofDays(1));

		Leilao leilao1 = new LeilaoBuilder().to("TV de Plasma").onDate(ontem).build();
		Leilao leilao2 = new LeilaoBuilder().to("Geladeira").onDate(ontem).build();

		when(dao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));

		encerrador.encerra();

		assertThat(encerrador.getTotalEncerrados(), equalTo(0));
		assertThat(leilao1.isEncerrado(), equalTo(false));
		assertThat(leilao2.isEncerrado(), equalTo(false));

	}

	@Test
	public void testaEncerradorComZeroLeiloes() {

		when(dao.correntes()).thenReturn(new ArrayList<>());

		encerrador.encerra();

		assertThat(encerrador.getTotalEncerrados(), equalTo(0));

	}

}
