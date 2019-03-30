package br.com.caelum.leilao.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import br.com.caelum.leilao.dao.LeilaoDao;
import br.com.caelum.leilao.dominio.Leilao;

public class EncerradorDeLeilao {

	private LeilaoDao dao;
	private int totalEncerrados;

	public EncerradorDeLeilao(LeilaoDao dao) {
		this.dao = dao;

	}

	public void encerra() {
		List<Leilao> correntes = this.dao.correntes();
		correntes.forEach((l) -> {
			if (prazoLeilaoExpirado(l)) {
				encerra(l);
			}
		});
	}

	private boolean prazoLeilaoExpirado(Leilao l) {
		return ChronoUnit.DAYS.between(l.getDate(), LocalDate.now()) >= 7;
	}

	private void encerra(Leilao l) {
		l.setEncerrado(true);
		totalEncerrados++;
		dao.atualiza(l);
	}

	public int getTotalEncerrados() {
		return totalEncerrados;
	}
}
