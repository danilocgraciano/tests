package br.com.caelum.leilao.service;

import org.junit.Test;

import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Usuario;

public class LanceTest {

	@Test(expected = IllegalArgumentException.class)
	public void testaValorLanceMenorIgualZero() {
		Usuario joao = new Usuario("joao");
		new Lance(joao, 0);
	}

}
