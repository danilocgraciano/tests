package br.com.caelum.leilao.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.leilao.dominio.Matematica;

public class MatematicaTest {

	@Test
	public void testaNumeroMenorQue10() {

		Matematica matematica = new Matematica();
		int resposta = matematica.contaMaluca(5);
		assertEquals(resposta, 10);

	}

	@Test
	public void testaNumeroIgual10() {

		Matematica matematica = new Matematica();
		int resposta = matematica.contaMaluca(10);
		assertEquals(resposta, 20);

	}

	@Test
	public void testaNumeroMaiorQue10MenorQue30() {

		Matematica matematica = new Matematica();
		int resposta = matematica.contaMaluca(15);
		assertEquals(resposta, 45);

	}

	@Test
	public void testaNumeroIgual30() {

		Matematica matematica = new Matematica();
		int resposta = matematica.contaMaluca(30);
		assertEquals(resposta, 90);

	}

	@Test
	public void testaNumeroMaior30() {

		Matematica matematica = new Matematica();
		int resposta = matematica.contaMaluca(40);
		assertEquals(resposta, 160);

	}

}
