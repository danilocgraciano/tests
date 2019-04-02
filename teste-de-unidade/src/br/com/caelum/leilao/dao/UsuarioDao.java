package br.com.caelum.leilao.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.caelum.leilao.dominio.Usuario;

public class UsuarioDao {

	private EntityManager entityManager;

	public UsuarioDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Usuario porId(int id) {
		return null;
	}

	public Usuario porNomeEEmail(String nome, String email) {
		try {
			return (Usuario) entityManager.createQuery("from Usuario u where u.nome = :nome and u.email = :email")
					.setParameter("nome", nome).setParameter("email", email).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public void salvar(Usuario usuario) {
		entityManager.persist(usuario);
	}

	public void deletar(Usuario usuario) {
		entityManager.remove(usuario);
	}

}
