package br.com.caelum.leilao.service;

import java.time.LocalDate;
import java.util.List;

import br.com.caelum.leilao.dao.LeilaoDao;
import br.com.caelum.leilao.dao.PagamentoDao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Pagamento;

public class GeradorDePagamento {

	private PagamentoDao pagamentoDao;
	private LeilaoDao leilaoDao;
	private Avaliador avaliador;

	public GeradorDePagamento(PagamentoDao pagamentoDao, LeilaoDao leilaoDao, Avaliador avaliador) {
		this.pagamentoDao = pagamentoDao;
		this.leilaoDao = leilaoDao;
		this.avaliador = avaliador;
	}

	public void gera() {
		List<Leilao> encerrados = this.leilaoDao.encerrados();
		encerrados.forEach((leilao) -> {
			this.avaliador.avalia(leilao);
			double maiorLance = this.avaliador.getMaiorLance();
			Pagamento pagamento = new Pagamento(maiorLance, LocalDate.now());
			this.pagamentoDao.save(pagamento);
		});
	}

}
