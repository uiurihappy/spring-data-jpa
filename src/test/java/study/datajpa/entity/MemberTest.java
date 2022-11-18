package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class MemberTest {

	@PersistenceContext
	EntityManager em;

	@Test
	public void testEntity() {
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");

		em.persist(teamA);
		em.persist(teamB);

		Member memeber1 = new Member("memeber1", 10, teamA);
		Member memeber2 = new Member("memeber2", 20, teamA);
		Member memeber3 = new Member("memeber3", 30, teamB);
		Member memeber4 = new Member("memeber4", 40, teamB);

		em.persist(memeber1);
		em.persist(memeber2);
		em.persist(memeber3);
		em.persist(memeber4);

		// 초기화
		// 영속성 컨텍스트에 DB insert 쿼리를 날려준다.
		em.flush();
		// DB에 날렸던 쿼리들을 지우고 영속성 컨텍스트에 존재하는 캐시들 삭제해준다.
		em.clear();

		// 확인
		List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

		for (Member member : members) {
			System.out.println("member = " + member);
			System.out.println("-> member.team = " + member.getTeam());
		}
	}
}