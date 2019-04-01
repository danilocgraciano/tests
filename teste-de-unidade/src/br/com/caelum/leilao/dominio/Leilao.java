package br.com.caelum.leilao.dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Leilao {

	@Id
	@GeneratedValue
	private int id;
	private String descricao;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "leilao")
	private List<Lance> lances;
	@Column(name = "data", columnDefinition = "DATE")
	private LocalDate data;
	private boolean encerrado;
	@ManyToOne
	private Usuario dono;
	private boolean usado;

	Leilao() {

	}

	public Leilao(String descricao, LocalDate data) {
		this.descricao = descricao;
		this.data = data;
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

	public LocalDate getData() {
		return this.data;
	}

	public int getId() {
		return id;
	}

	public Usuario getDono() {
		return dono;
	}

	public void setDono(Usuario dono) {
		this.dono = dono;
	}

	public boolean isUsado() {
		return usado;
	}

	public void setUsado(boolean usado) {
		this.usado = usado;
	}

}
