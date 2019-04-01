package br.com.caelum.leilao;

import br.com.caelum.leilao.dao.ConnectionFactory;

public class Main {

	public static void main(String[] args) {
		ConnectionFactory.getEntityManager();
	}

}
