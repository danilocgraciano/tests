package br.com.caelum.leilao.builder;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;

public class LeilaoBuilder {

	private String descricao;
	private List<Lance> lances = new ArrayList<>();

	public LeilaoBuilder to(String descricao) {
		this.descricao = descricao;
		return this;
	}

	public LeilaoBuilder lance(Lance lance) {
		this.lances.add(lance);
		return this;
	}

	public Leilao build() {
		Leilao leilao = new Leilao(descricao);
		lances.forEach((l) -> leilao.propoe(l));
		return leilao;
	}

}
