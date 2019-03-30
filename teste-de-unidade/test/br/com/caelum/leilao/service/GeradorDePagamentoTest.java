package br.com.caelum.leilao.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import br.com.caelum.leilao.builder.LeilaoBuilder;
import br.com.caelum.leilao.dao.LeilaoDao;
import br.com.caelum.leilao.dao.PagamentoDao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;
import br.com.caelum.leilao.dominio.Usuario;

public class GeradorDePagamentoTest {

	private GeradorDePagamento geradorDePagamento;
	private LeilaoDao leilaoDao;
	private PagamentoDao pagamentoDao;
	private Avaliador avaliador;

	@Before
	public void setup() {

		pagamentoDao = mock(PagamentoDao.class);
		leilaoDao = mock(LeilaoDao.class);
		avaliador = new Avaliador();

		geradorDePagamento = new GeradorDePagamento(pagamentoDao, leilaoDao, avaliador);
	}

	@Test
	public void deveGerarPagamentoParaUmLeilao() {

		Leilao leilao = new LeilaoBuilder()
				.to("Playstation")
				.onDate(LocalDate.now())
				.lance(new Lance(new Usuario("João"), 2000.0))
				.lance(new Lance(new Usuario("José"), 2500.0))
				.build();

		when(leilaoDao.encerrados()).thenReturn(Arrays.asList(leilao));

		geradorDePagamento.gera();

		ArgumentCaptor<Pagamento> argumento = ArgumentCaptor.forClass(Pagamento.class);
		verify(pagamentoDao).save(argumento.capture());
		
		Pagamento pagamentoGerado = argumento.getValue();

		assertThat(pagamentoGerado.getValor(), equalTo(2500.00));

	}

}
