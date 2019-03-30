package br.com.caelum.leilao.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import br.com.caelum.leilao.dao.LeilaoDao;
import br.com.caelum.leilao.dominio.Leilao;

public class EncerradorDeLeilao {

	private LeilaoDao dao;
	private int totalEncerrados;
	private EnviadorDeEmail email;

	public EncerradorDeLeilao(LeilaoDao dao, EnviadorDeEmail email) {
		this.dao = dao;
		this.email = email;

	}

	public void encerra() {
		List<Leilao> correntes = this.dao.correntes();
		correntes.forEach((leilao) -> {
			if (prazoExpirado(leilao)) {
				encerra(leilao);
			}
		});
	}

	private boolean prazoExpirado(Leilao leilao) {
		return ChronoUnit.DAYS.between(leilao.getData(), LocalDate.now()) >= 7;
	}

	private void encerra(Leilao leilao) {
		leilao.setEncerrado(true);
		totalEncerrados++;
		dao.atualiza(leilao);
		email.envia(leilao);
	}

	public int getTotalEncerrados() {
		return totalEncerrados;
	}
}
