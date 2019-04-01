package br.com.caelum.leilao.dao;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.caelum.leilao.dominio.Leilao;

public class LeilaoDaoImpl implements LeilaoDao {

	private EntityManager entityManager;

	public LeilaoDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void save(Leilao leilao) {
		entityManager.persist(leilao);
	}

	@Override
	public List<Leilao> encerrados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Leilao> correntes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void atualiza(Leilao leilao) {
		entityManager.merge(leilao);

	}

	@Override
	public Long total() {
		return (Long) entityManager.createQuery("select count(l) from Leilao l where l.encerrado = false")
				.getSingleResult();
	}

	@Override
	public List<Leilao> novos() {
		return entityManager.createQuery("from Leilao l where l.usado = false").getResultList();
	}

	@Override
	public List<Leilao> antigos() {

		LocalDate seteDiasAtras = LocalDate.now();
		seteDiasAtras = seteDiasAtras.minus(Period.ofDays(7));

		return entityManager.createQuery("from Leilao l where l.data <= :data").setParameter("data", seteDiasAtras)
				.getResultList();
	}

}
