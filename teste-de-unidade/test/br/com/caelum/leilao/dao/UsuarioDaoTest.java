package br.com.caelum.leilao.dao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;

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

	@Test
	public void deveDeletarUsuario() {

		Usuario usuario = new Usuario("usuario", "usuario@email.com");
		usuarioDao.salvar(usuario);
		usuarioDao.deletar(usuario);

		entityManager.flush();
		entityManager.clear();

		Usuario deletado = usuarioDao.porNomeEEmail(usuario.getNome(), usuario.getEmail());

		assertNull(deletado);

	}

	@Test
	public void deveAlterarUmUsuario() {

		String nome = "usuario";
		String email = "usuario@email.com";

		Usuario usuario = new Usuario(nome, email);
		usuarioDao.salvar(usuario);

		entityManager.flush();
		entityManager.clear();

		usuario.setNome("jose");
		usuario.setEmail("jose@email.com");

		usuarioDao.atualizar(usuario);

		entityManager.flush();
		entityManager.clear();

		Usuario antigo = usuarioDao.porNomeEEmail(nome, email);
		Usuario alterado = usuarioDao.porNomeEEmail(usuario.getNome(), usuario.getEmail());

		assertNull(antigo);
		assertThat(alterado.getNome(), equalTo("jose"));
		assertThat(alterado.getEmail(), equalTo("jose@email.com"));
	}

}
