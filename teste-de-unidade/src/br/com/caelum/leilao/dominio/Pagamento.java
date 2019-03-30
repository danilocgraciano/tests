package br.com.caelum.leilao.dominio;

import java.time.LocalDate;

public class Pagamento {

	private double valor;
	private LocalDate date;

	public Pagamento(double valor, LocalDate date) {

		if (valor <= 0)
			throw new IllegalArgumentException("Valor do pagamento deve ser maior que zero");

		this.valor = valor;
		this.date = date;
	}

	public double getValor() {
		return valor;
	}

	public LocalDate getDate() {
		return date;
	}

}
