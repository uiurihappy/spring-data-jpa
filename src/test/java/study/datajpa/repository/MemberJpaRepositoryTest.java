package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// Transaction 내에서 동작되도록 해야 한다.
@Transactional
// 롤백을 false로 하면 등록된 것을 확인할 수 있다.
// 왜냐면 테스트 끝나고 롤백을 하지 않고 커밋이 진행되도록 하여 row가 증가된 것을 확인할 수 있다.
@Rollback(false)
class MemberJpaRepositoryTest {

	@Autowired MemberJpaRepository memberJpaRepository;

	@Test
	public void testMember() {
		Member member = new Member("memberA");
		Member savedMember = memberJpaRepository.save(member);

		Member findMember = memberJpaRepository.find(savedMember.getId());

		assertEquals(findMember.getId(), member.getId());
		assertEquals(findMember.getUsername(), member.getUsername());
		assertEquals(findMember, member);
	}

	@Test
	public void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");

		memberJpaRepository.save(member1);
		memberJpaRepository.save(member2);

		// 단건 조회 검증
		Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
		Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

		assertEquals(findMember1, member1);
		assertEquals(findMember2, member2);

		// 더티 체킹 발생!
		findMember1.setUsername("member!!!!!!");

		// 리스트 조회 검증
		List<Member> all = memberJpaRepository.findAll();
		assertEquals(all.size(), 2);

		// 카운트 검증
		long count = memberJpaRepository.count();
		assertEquals(count, 2);

		// 식제 검증
		memberJpaRepository.deleteMember(member1);
		memberJpaRepository.deleteMember(member2);

		long deletedCount = memberJpaRepository.count();
		assertEquals(deletedCount, 0);

	}
}