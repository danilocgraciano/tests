package br.com.caelum.leilao.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.LeilaoBuilder;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class LeilaoDaoImplTest {

	private EntityManager entityManager;
	private LeilaoDaoImpl leilaoDaoImpl;
	private UsuarioDao usuarioDao;

	@Before
	public void setup() {
		entityManager = ConnectionFactory.getEntityManager();
		leilaoDaoImpl = new LeilaoDaoImpl(entityManager);
		usuarioDao = new UsuarioDao(entityManager);
		entityManager.getTransaction().begin();
	}

	public void tearDown() {
		entityManager.getTransaction().rollback();
	}

	@Test
	public void deveContarOsLeiloesAtivos() {

		Usuario joao = new Usuario("joao", "joao@email.com");
		Usuario jose = new Usuario("jose", "jose@email.com");

		usuarioDao.salvar(joao);
		usuarioDao.salvar(jose);

		Leilao ativo = new LeilaoBuilder().to("Geladeira").onDate(LocalDate.now()).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).build();

		Leilao encerrado = new LeilaoBuilder().to("PLaystation").onDate(LocalDate.now()).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).build();

		encerrado.setEncerrado(true);

		leilaoDaoImpl.save(ativo);
		leilaoDaoImpl.save(encerrado);

		Long total = leilaoDaoImpl.total();

		assertThat(total, equalTo(1l));

	}

	@Test
	public void deveContarZeroLeiloesAtivos() {

		Usuario joao = new Usuario("joao", "joao@email.com");
		Usuario jose = new Usuario("jose", "jose@email.com");

		usuarioDao.salvar(joao);
		usuarioDao.salvar(jose);

		Leilao encerrado1 = new LeilaoBuilder().to("Geladeira").onDate(LocalDate.now()).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).build();

		Leilao encerrado2 = new LeilaoBuilder().to("PLaystation").onDate(LocalDate.now()).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).build();

		encerrado1.setEncerrado(true);
		encerrado2.setEncerrado(true);

		leilaoDaoImpl.save(encerrado1);
		leilaoDaoImpl.save(encerrado2);

		Long total = leilaoDaoImpl.total();

		assertThat(total, equalTo(0l));

	}

	@Test
	public void deveContarLeiloesNovos() {

		Usuario joao = new Usuario("joao", "joao@email.com");
		Usuario jose = new Usuario("jose", "jose@email.com");

		usuarioDao.salvar(joao);
		usuarioDao.salvar(jose);

		Leilao leilao1 = new LeilaoBuilder().to("Geladeira").onDate(LocalDate.now()).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).setUsado(false).build();

		Leilao leilao2 = new LeilaoBuilder().to("PLaystation").onDate(LocalDate.now()).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).setUsado(true).build();

		leilaoDaoImpl.save(leilao1);
		leilaoDaoImpl.save(leilao2);

		List<Leilao> leiloes = leilaoDaoImpl.novos();

		assertThat(leiloes.size(), equalTo(1));
		assertThat(leiloes, hasItems(leilao1));

	}

	@Test
	public void deveContarZeroLeiloesNovos() {

		Usuario joao = new Usuario("joao", "joao@email.com");
		Usuario jose = new Usuario("jose", "jose@email.com");

		usuarioDao.salvar(joao);
		usuarioDao.salvar(jose);

		Leilao leilao1 = new LeilaoBuilder().to("Geladeira").onDate(LocalDate.now()).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).setUsado(true).build();

		Leilao leilao2 = new LeilaoBuilder().to("PLaystation").onDate(LocalDate.now()).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).setUsado(true).build();

		leilaoDaoImpl.save(leilao1);
		leilaoDaoImpl.save(leilao2);

		List<Leilao> leiloes = leilaoDaoImpl.novos();

		assertThat(leiloes.size(), equalTo(0));

	}

	@Test
	public void deveContarLeiloesAntigos() {

		Usuario joao = new Usuario("joao", "joao@email.com");
		Usuario jose = new Usuario("jose", "jose@email.com");

		usuarioDao.salvar(joao);
		usuarioDao.salvar(jose);

		LocalDate data5diasAntes = LocalDate.now().minus(Period.ofDays(10));

		Leilao leilao1 = new LeilaoBuilder().to("Geladeira").onDate(data5diasAntes).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).setUsado(true).build();

		Leilao leilao2 = new LeilaoBuilder().to("PLaystation 2").onDate(data5diasAntes).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).setUsado(true).build();

		Leilao leilao3 = new LeilaoBuilder().to("PLaystation 4").onDate(LocalDate.now()).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).setUsado(true).build();

		leilaoDaoImpl.save(leilao1);
		leilaoDaoImpl.save(leilao2);
		leilaoDaoImpl.save(leilao3);

		List<Leilao> leiloes = leilaoDaoImpl.antigos();

		assertThat(leiloes.size(), equalTo(2));
		assertThat(leiloes, hasItems(leilao1, leilao2));

	}

	@Test
	public void deveContarZeroLeiloesAntigos() {

		List<Leilao> leiloes = leilaoDaoImpl.antigos();

		assertThat(leiloes.size(), equalTo(0));

	}

	@Test
	public void deveContarLeilaoExatos7Dias() {

		Usuario joao = new Usuario("joao", "joao@email.com");
		Usuario jose = new Usuario("jose", "jose@email.com");

		usuarioDao.salvar(joao);
		usuarioDao.salvar(jose);

		LocalDate data7diasAntes = LocalDate.now().minus(Period.ofDays(7));

		Leilao leilao = new LeilaoBuilder().to("Geladeira").onDate(data7diasAntes).lance(new Lance(joao, 2000.0))
				.lance(new Lance(jose, 2500.0)).setUsado(true).build();

		leilaoDaoImpl.save(leilao);

		List<Leilao> leiloes = leilaoDaoImpl.antigos();

		assertThat(leiloes.size(), equalTo(1));
		assertThat(leiloes, hasItems(leilao));

	}

}
