package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepository {

	@PersistenceContext
	private EntityManager em;

	public Team save(Team team) {
		em.persist(team);
		return team;
	}

	public void delete(Team team){
		em.remove(team);
	}

	public List<Team> findAll(){
		return em.createQuery( "select t from Team t", Team.class).getResultList();
	}

	// Optional로 조회
	public Optional<Team> findById(Long id) {
		Team team = em.find(Team.class, id);
		// member가 null일수도 아닐 수도 있다.
		return Optional.ofNullable(team);
	}

	// count
	public long count(){
		// 단건인 경우에 대해서는 SingleResult를 사용
		return em.createQuery(
				"select count(t)" +
						"from Team t",
				Long.class
		).getSingleResult();
	}
}
