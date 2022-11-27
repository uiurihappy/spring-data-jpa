package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

	@PersistenceContext
	private EntityManager em;

	public Member save(Member member) {
		em.persist(member);
		return member;
	}
	public void deleteMember(Member member ){
		em.remove(member);
	}
	// 단건 조회
	public Member find(Long id){
		return em.find(Member.class, id);
	}

	public List<Member> findAll(){
		// JPQL
		return em.createQuery("select m from Member m", Member.class).getResultList();

	}

	// Optional로 조회
	public Optional<Member> findById(Long id) {
		Member member = em.find(Member.class, id);
		// member가 null일수도 아닐 수도 있다.
		return Optional.ofNullable(member);
	}

	// count
	public long count(){
		// 단건인 경우에 대해서는 SingleResult를 사용
		return em.createQuery(
				"select count(m)" +
						"from Member m",
				Long.class
		).getSingleResult();
	}

	public List<Member> findByUsernameAndAgeGreaterThen(String username, int age) {
		return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
				.setParameter("username", username)
				.setParameter("age", age)
				.getResultList();
	}

	// 네임드 쿼리 작성방법
	public List<Member> findByUsername(String username) {
		return em.createNamedQuery("Member.findByUsername", Member.class)
				.setParameter("username", username)
				.getResultList();
	}

	public List<Member> findByPage(int age, int offset, int limit)  {
		return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
				.setParameter("age", age)
				// 어디서부터 가져올거니
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();
	}

	public long totalCount(int age ) {
		return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
				.setParameter("age", age)
				.getSingleResult();
	}

	public int bulkAgePlus(int age){
		return em.createQuery(
				"update Member m set m.age = m.age + 1" +
						" where m.age >= :age")
				.setParameter("age", age)
				.executeUpdate();
	}
}
