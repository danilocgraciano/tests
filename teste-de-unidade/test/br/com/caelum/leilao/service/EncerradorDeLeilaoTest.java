package br.com.caelum.leilao.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import br.com.caelum.leilao.builder.LeilaoBuilder;
import br.com.caelum.leilao.dao.LeilaoDao;
import br.com.caelum.leilao.dominio.Leilao;

public class EncerradorDeLeilaoTest {

	private LeilaoDao dao;
	private EncerradorDeLeilao encerrador;
	private EnviadorDeEmail email;

	@Before
	public void setup() {
		dao = mock(LeilaoDao.class);
		email = mock(EnviadorDeEmail.class);
		encerrador = new EncerradorDeLeilao(dao, email);
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

		InOrder inOrder = inOrder(dao, email);
		inOrder.verify(dao, times(1)).atualiza(leilao1);
		inOrder.verify(email, times(1)).envia(leilao1);
		inOrder.verify(dao, times(1)).atualiza(leilao2);
		inOrder.verify(email, times(1)).envia(leilao2);

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

		verify(dao, never()).atualiza(leilao1);
		verify(dao, never()).atualiza(leilao2);

	}

	@Test
	public void testaEncerradorComZeroLeiloes() {

		when(dao.correntes()).thenReturn(new ArrayList<>());

		encerrador.encerra();

		assertThat(encerrador.getTotalEncerrados(), equalTo(0));

	}

	@Test
	public void deveContinuarAExecucaoSeUmaExcecaoOcorrerNoBancoDeDados() {

		LocalDate antiga = LocalDate.of(1999, 1, 20);

		Leilao leilao1 = new LeilaoBuilder().to("TV de Plasma").onDate(antiga).build();
		Leilao leilao2 = new LeilaoBuilder().to("Geladeira").onDate(antiga).build();

		when(dao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
		doThrow(new RuntimeException()).when(dao).atualiza(leilao1);

		encerrador.encerra();

		verify(dao, times(1)).atualiza(leilao2);
		verify(email, times(1)).envia(leilao2);

		verify(email, never()).envia(leilao1);

	}

	@Test
	public void deveContinuarAExecucaoSeUmaExcecaoOcorrerNoEnviadorDeEmail() {

		LocalDate antiga = LocalDate.of(1999, 1, 20);

		Leilao leilao1 = new LeilaoBuilder().to("TV de Plasma").onDate(antiga).build();
		Leilao leilao2 = new LeilaoBuilder().to("Geladeira").onDate(antiga).build();

		when(dao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
		doThrow(new RuntimeException()).when(email).envia(leilao1);

		encerrador.encerra();

		verify(dao, times(1)).atualiza(leilao2);
		verify(email, times(1)).envia(leilao2);

		verify(dao, times(1)).atualiza(leilao1);

	}

	@Test
	public void deveSuprimirEnvioEmailCasoDaoSempreFalhe() {
		LocalDate antiga = LocalDate.of(1999, 1, 20);

		Leilao leilao1 = new LeilaoBuilder().to("TV de Plasma").onDate(antiga).build();
		Leilao leilao2 = new LeilaoBuilder().to("Geladeira").onDate(antiga).build();

		when(dao.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));
		doThrow(new RuntimeException()).when(dao).atualiza(any(Leilao.class));

		encerrador.encerra();

		verify(email, never()).envia(any(Leilao.class));

	}

}
