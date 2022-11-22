package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

	@Autowired MemberRepository memberRepository;
	@Autowired TeamRepository teamRepository;

	@Test
	public void testMember() {
		System.out.println("memberRepository = " + memberRepository.getClass());

		Member member = new Member("memberA");
		Member savedMember = memberRepository.save(member);

		Member findMember = memberRepository.findById(savedMember.getId()).get();

		assertEquals(findMember.getId(), member.getId());
		assertEquals(findMember.getUsername(), member.getUsername());
		assertEquals(findMember, member);
	}


	// Spring data jpa도 순수 jpa repository랑 똑같이 복붙했지만 테스트가 성공한다.
	@Test
	public void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");

		memberRepository.save(member1);
		memberRepository.save(member2);

		// 단건 조회 검증
		Member findMember1 = memberRepository.findById(member1.getId()).get();
		Member findMember2 = memberRepository.findById(member2.getId()).get();

		assertEquals(findMember1, member1);
		assertEquals(findMember2, member2);

		// 더티 체킹 발생!
		findMember1.setUsername("member!!!!!!");

		// 리스트 조회 검증
		List<Member> all = memberRepository.findAll();
		assertEquals(all.size(), 2);

		// 카운트 검증
		long count = memberRepository.count();
		assertEquals(count, 2);

		// 식제 검증
		memberRepository.delete(member1);
		memberRepository.delete(member2);

		long deletedCount = memberRepository.count();
		assertEquals(deletedCount, 0);

	}

	@Test
	public void findByUsernameAndAgeGreaterThen() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);

		memberRepository.save(m1);
		memberRepository.save(m2);

		// 메서드 이름만으로도 data JPA가 잡아준다.
		List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

		assertEquals(result.get(0).getUsername(),"AAA");
		assertEquals(result.get(0).getAge(),20);
		assertEquals(result.size(), 1);
	}

	@Test
	public void findHelloBy() {
		List<Member> helloBy = memberRepository.findTop3HelloBy();
	}

	@Test
	public void namedQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);

		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByUsername("AAA");
		Member findMember = result.get(0);
		assertEquals(findMember, m1);
	}

	@Test
	public void testQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);

		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findUser("AAA", 10);
		Member findMember = result.get(0);
		assertEquals(findMember, m1);
	}

	@Test
	public void findUserNameListQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);

		memberRepository.save(m1);
		memberRepository.save(m2);

		List<String> usernameList = memberRepository.findUsernameList();
		for (String s : usernameList) {
			System.out.println("name = " + s);
		}
	}

	@Test
	public void findMemberDto(){
		Team team = new Team("teamA");
		teamRepository.save(team);

		Member m1 = new Member("AAA", 10);
		memberRepository.save(m1);

		m1.setTeam(team);

		List<MemberDto> memberDto = memberRepository.findMemberDto();
		for (MemberDto dto : memberDto) {
			System.out.println("dto = " + dto);
			System.out.println("dtoID = " + dto.getId());
			System.out.println("dtoUsername = " + dto.getUsername());
			System.out.println("dtoTeamName = " + dto.getTeamName());

		}

	}

}