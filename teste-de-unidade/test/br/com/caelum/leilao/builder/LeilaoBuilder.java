package br.com.caelum.leilao.builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;

public class LeilaoBuilder {

	private String descricao;
	private List<Lance> lances = new ArrayList<>();
	private LocalDate date;
	private boolean usado;
	private boolean encerrado;

	public LeilaoBuilder to(String descricao) {
		this.descricao = descricao;
		return this;
	}

	public LeilaoBuilder lance(Lance lance) {
		this.lances.add(lance);
		return this;
	}

	public Leilao build() {
		Leilao leilao = new Leilao(descricao, date);
		leilao.setUsado(usado);
		leilao.setEncerrado(encerrado);
		lances.forEach((l) -> leilao.propoe(l));
		return leilao;
	}

	public LeilaoBuilder onDate(LocalDate date) {
		this.date = date;
		return this;
	}

	public LeilaoBuilder setUsado(boolean usado) {
		this.usado = usado;
		return this;
	}
	
	public LeilaoBuilder setEncerrado(boolean encerrado) {
		this.encerrado = encerrado;
		return this;
	}

}
