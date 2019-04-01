package br.com.caelum.leilao.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;

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
import br.com.caelum.leilao.infra.Relogio;

public class GeradorDePagamentoTest {

	private GeradorDePagamento geradorDePagamento;
	private LeilaoDao leilaoDao;
	private PagamentoDao pagamentoDao;
	private Avaliador avaliador;
	private Relogio relogio;

	@Before
	public void setup() {

		pagamentoDao = mock(PagamentoDao.class);
		leilaoDao = mock(LeilaoDao.class);
		avaliador = new Avaliador();
		relogio = mock(Relogio.class);

		geradorDePagamento = new GeradorDePagamento(pagamentoDao, leilaoDao, avaliador, relogio);
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
		
		when(relogio.hoje()).thenReturn(LocalDate.now());

		geradorDePagamento.gera();

		ArgumentCaptor<Pagamento> argumento = ArgumentCaptor.forClass(Pagamento.class);
		verify(pagamentoDao).save(argumento.capture());
		
		Pagamento pagamentoGerado = argumento.getValue();

		assertThat(pagamentoGerado.getValor(), equalTo(2500.00));

	}
	
	@Test
	public void deveGerarPagamentoNoProximoDiaUtilSendoHojeSabado(){
		
		when(relogio.hoje()).thenReturn(LocalDate.of(2019, 03, 30));
		Pagamento pagamento = geraPagamento(relogio);
		assertThat(pagamento.getDate().getDayOfWeek(), equalTo(DayOfWeek.MONDAY));
		
	}
	
	@Test
	public void deveGerarPagamentoNoProximoDiaUtilSendoHojeDomingo(){
		
		
		when(relogio.hoje()).thenReturn(LocalDate.of(2019, 03, 31));
		Pagamento pagamento = geraPagamento(relogio);
		assertThat(pagamento.getDate().getDayOfWeek(), equalTo(DayOfWeek.MONDAY));
		
	}

	@Test
	public void deveGerarPagamentoSegundaFeira(){
		
		when(relogio.hoje()).thenReturn(LocalDate.of(2019, 04, 01));
		Pagamento pagamento = geraPagamento(relogio);
		assertThat(pagamento.getDate().getDayOfWeek(), equalTo(DayOfWeek.MONDAY));
		
	}
	
	@Test
	public void deveGerarPagamentoTercaFeira(){
		
		when(relogio.hoje()).thenReturn(LocalDate.of(2019, 04, 02));
		Pagamento pagamento = geraPagamento(relogio);
		assertThat(pagamento.getDate().getDayOfWeek(), equalTo(DayOfWeek.TUESDAY));
		
	}
	
	@Test
	public void deveGerarPagamentoQuartaFeira(){
		
		when(relogio.hoje()).thenReturn(LocalDate.of(2019, 04, 03));
		Pagamento pagamento = geraPagamento(relogio);
		assertThat(pagamento.getDate().getDayOfWeek(), equalTo(DayOfWeek.WEDNESDAY));
		
	}
	
	@Test
	public void deveGerarPagamentoQuintaFeira(){
		
		when(relogio.hoje()).thenReturn(LocalDate.of(2019, 04, 04));
		Pagamento pagamento = geraPagamento(relogio);
		assertThat(pagamento.getDate().getDayOfWeek(), equalTo(DayOfWeek.THURSDAY));
		
	}
	
	@Test
	public void deveGerarPagamentoSextaFeira(){
		
		when(relogio.hoje()).thenReturn(LocalDate.of(2019, 04, 05));
		Pagamento pagamento = geraPagamento(relogio);
		assertThat(pagamento.getDate().getDayOfWeek(), equalTo(DayOfWeek.FRIDAY));
		
	}
	
	private Pagamento geraPagamento(Relogio relogio){
		
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
		
		return argumento.getValue();
		
	}

}
