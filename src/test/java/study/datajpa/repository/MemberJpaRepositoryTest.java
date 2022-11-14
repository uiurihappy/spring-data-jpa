package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

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
}