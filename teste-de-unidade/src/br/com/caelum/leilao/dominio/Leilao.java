package br.com.caelum.leilao.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Leilao {

	private String descricao;
	private List<Lance> lances;
	private LocalDate date;
	private boolean encerrado;

	public Leilao(String descricao, LocalDate date) {
		this.descricao = descricao;
		this.date = date;
		this.lances = new ArrayList<Lance>();
	}

	public void propoe(Lance lance) {

		if (lances.isEmpty() || (podeDarLance(lance) && getQtdeLancesPorUsuario(lance.getUsuario()) < 5))
			lances.add(lance);
	}

	private boolean podeDarLance(Lance lance) {
		return !getUltimoLance().getUsuario().equals(lance.getUsuario());
	}

	private int getQtdeLancesPorUsuario(Usuario usuario) {
		return lances.stream().filter(l -> l.getUsuario().equals(usuario)).collect(Collectors.toList()).size();
	}

	private Lance getUltimoLance() {
		return lances.get(lances.size() - 1);
	}

	public String getDescricao() {
		return descricao;
	}

	public List<Lance> getLances() {
		return Collections.unmodifiableList(lances);
	}

	public void dobraLance(Usuario usuario) {

		Lance ultimoLance = getUltimoLance(usuario);
		Lance lance = new Lance(usuario, ultimoLance.getValor() * 2);
		propoe(lance);

	}

	private Lance getUltimoLance(Usuario usuario) {
		List<Lance> lancesPorUsuario = lances.stream().filter(l -> l.getUsuario().equals(usuario))
				.collect(Collectors.toList());
		Lance ultimoLance = lancesPorUsuario.get(lancesPorUsuario.size() - 1);
		return ultimoLance;
	}

	public void setEncerrado(boolean encerrado) {
		this.encerrado = encerrado;
	}

	public boolean isEncerrado() {
		return this.encerrado;
	}

	public LocalDate getDate() {
		return this.date;
	}

}
