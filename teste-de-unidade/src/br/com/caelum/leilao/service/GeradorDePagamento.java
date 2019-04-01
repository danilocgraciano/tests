package br.com.caelum.leilao.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import br.com.caelum.leilao.dao.LeilaoDao;
import br.com.caelum.leilao.dao.PagamentoDao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;
import br.com.caelum.leilao.infra.Relogio;
import br.com.caelum.leilao.infra.RelogioDoSistema;

public class GeradorDePagamento {

	private PagamentoDao pagamentoDao;
	private LeilaoDao leilaoDao;
	private Avaliador avaliador;
	private Relogio relogio;

	public GeradorDePagamento(PagamentoDao pagamentoDao, LeilaoDao leilaoDao, Avaliador avaliador, Relogio relogio) {
		this.pagamentoDao = pagamentoDao;
		this.leilaoDao = leilaoDao;
		this.avaliador = avaliador;
		this.relogio = relogio;
	}

	public GeradorDePagamento(PagamentoDao pagamentoDao, LeilaoDao leilaoDao, Avaliador avaliador) {
		this(pagamentoDao, leilaoDao, avaliador, new RelogioDoSistema());
	}

	public void gera() {
		List<Leilao> encerrados = this.leilaoDao.encerrados();
		encerrados.forEach((leilao) -> {
			this.avaliador.avalia(leilao);
			double maiorLance = this.avaliador.getMaiorLance();
			Pagamento pagamento = new Pagamento(maiorLance, pegaDiaUtil());
			this.pagamentoDao.save(pagamento);
		});
	}

	private LocalDate pegaDiaUtil() {
		LocalDate date = relogio.hoje();

		if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY))
			date = date.plusDays(2);

		if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY))
			date = date.plusDays(1);

		return date;
	}

}
