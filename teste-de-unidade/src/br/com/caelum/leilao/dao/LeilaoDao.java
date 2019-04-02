package br.com.caelum.leilao.dao;

import java.time.LocalDate;
import java.util.List;

import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public interface LeilaoDao {

	void save(Leilao leilao);

	List<Leilao> encerrados();

	List<Leilao> correntes();

	void atualiza(Leilao leilao);

	Long total();

	List<Leilao> novos();

	List<Leilao> antigos();

	List<Leilao> porPeriodo(LocalDate inicio, LocalDate fim);
	
	List<Leilao> listaLeiloesDoUsuario(Usuario usuario);

}
