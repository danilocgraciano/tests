package br.com.caelum.leilao.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;

public class Avaliador {

	// public double maiorDeTodos = Double.NEGATIVE_INFINITY;
	// public double menorDeTodos = Double.POSITIVE_INFINITY;

	private Lance maiorLance = null;
	private Lance menorLance = null;

	private double media = 0;

	public List<Lance> tresMaioresList = new ArrayList<Lance>();

	public void avalia(Leilao l) {

		maiorLance = l.getLances().stream().max(Comparator.comparing(Lance::getValor)).orElseThrow(()->new RuntimeException("Não é possível avaliar um leilao sem lances!"));

		menorLance = l.getLances().stream().min(Comparator.comparing(Lance::getValor)).orElseThrow(()->new RuntimeException("Não é possível avaliar um leilao sem lances!"));

		double total = l.getLances().stream().mapToDouble(Lance::getValor).sum();

		List<Lance> lancesList = new ArrayList<>(l.getLances());

		lancesList.sort((l1, l2) -> (int) (l2.getValor() - l1.getValor()));

		tresMaioresList = lancesList.subList(0, (l.getLances().size() > 3) ? 3 : l.getLances().size());

		media = total == 0 ? 0 : total / l.getLances().size();

	}

	public double getMaiorLance() {
		return (maiorLance == null) ? 0 : maiorLance.getValor();
	}

	public double getMenorLance() {
		return (menorLance == null) ? 0 : menorLance.getValor();
	}

	public double getMedia() {
		return media;
	}

	public List<Lance> getTresMaiores() {
		return tresMaioresList;
	}

}
