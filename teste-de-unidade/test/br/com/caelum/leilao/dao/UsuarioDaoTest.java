package br.com.caelum.leilao.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.dominio.Usuario;

public class UsuarioDaoTest {

	private EntityManager entityManager;
	private UsuarioDao usuarioDao;

	@Before
	public void setup() {
		entityManager = ConnectionFactory.getEntityManager();
		usuarioDao = new UsuarioDao(entityManager);

		entityManager.getTransaction().begin();
	}

	@After
	public void tearDown() {
		entityManager.getTransaction().rollback();
	}

	@Test
	public void deveEncontrarPorNomeEEmail() {

		String nome = "danilo";
		String email = "danilo@email.com";
		Usuario usuario = new Usuario(nome, email);
		usuarioDao.salvar(usuario);

		usuario = usuarioDao.porNomeEEmail(nome, email);

		assertThat(usuario.getNome(), equalTo(nome));
		assertThat(usuario.getEmail(), equalTo(email));

	}

	@Test
	public void testeEmailInvalido() {

		String nome = "danilo";
		String email = "danilo2@email.com";
		Usuario usuario = new Usuario(nome, email);
		usuario = usuarioDao.porNomeEEmail(nome, email);

		assertNull(usuario);

	}

	@Test
	public void testeNomeInvalido() {

		String nome = "danilo2";
		String email = "danilo@email.com";
		Usuario usuario = new Usuario(nome, email);
		usuario = usuarioDao.porNomeEEmail(nome, email);

		assertNull(usuario);

	}

	@Test
	public void testeNomeEmailInvalidos() {

		String nome = "danilo2";
		String email = "danilo2@email.com";
		Usuario usuario = new Usuario(nome, email);
		usuario = usuarioDao.porNomeEEmail(nome, email);

		assertNull(usuario);

	}

}
