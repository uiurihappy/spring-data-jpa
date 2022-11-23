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

	// namedQuery의 장점
	// 바로 아래 메서드는 문자를 오타내고 테스트하면 실행에 문자없지만 고객이 API를 실행하면 syntax 오류가 난다.
	// 하지만 namedQuery는 JSON 파싱되는 과정부터 에러를 내서 버그를 잡을 수 있다.
	@Test
	public void findByUsernameAndAgeGreaterThen() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);

		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);

		List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("AAA", 15);

		assertEquals(result.get(0).getUsername(),"AAA");
		assertEquals(result.get(0).getAge(),20);
		assertEquals(result.size(), 1);
	}

	@Test
	public void namedQueryFindByUsername(){
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);

		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);

		List<Member> result = memberJpaRepository.findByUsername("AAA");
		Member findMember = result.get(0);
		assertEquals(findMember, m1);
	}

	@Test
	public void paging() {
		// given
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 10));
		memberJpaRepository.save(new Member("member3", 10));
		memberJpaRepository.save(new Member("member4", 10));
		memberJpaRepository.save(new Member("member5", 10));
		memberJpaRepository.save(new Member("member6", 10));

		int age = 10;
		int offset = 1;
		int limit = 3;
		// when
		List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
		long totalCount = memberJpaRepository.totalCount(age);

		/**
		 * 페이지 계산 공식 적용
		 * totalPage = totalCount / size ...
		 * 마지막 페이지,
		 * 최초 페이지
		 */

		// then
		assertEquals(members.size(), 3);
		assertEquals(totalCount, 6);
	}
}