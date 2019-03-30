package br.com.caelum.leilao.dao;

import java.util.List;

import br.com.caelum.leilao.dominio.Leilao;

public interface LeilaoDao {

	void save(Leilao leilao);

	List<Leilao> encerrados();

	List<Leilao> correntes();

	void atualiza(Leilao leilao);

}
