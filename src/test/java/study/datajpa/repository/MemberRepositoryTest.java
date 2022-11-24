package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

	@Autowired MemberRepository memberRepository;
	@Autowired TeamRepository teamRepository;
	@PersistenceContext
	EntityManager em;

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


	// Spring data JPA도 순수 JPA repository랑 똑같이 복붙했지만 테스트가 성공한다.
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


	@Test
	public void findByNames() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);

		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

		for (Member member  : result) {
			System.out.println("member = " + member);
		}
	}

	@Test
	public void returnType(){
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);

		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> aaa = memberRepository.findListByUsername("AAA");

		// 단건 조회같은 경우 match가 안되면 결과가 null
		// 순수 JPA에서는 NoResultException이 터지는데 Spring Data JPA에서는 Exception이 내장되어 있어서 알아서 null로 잡아준다.
		Member findMember = memberRepository.findMemberByUsername("AAA");
		System.out.println("findMember = " + findMember);

		// Java 8 이후부터 Optional이 가능하면서 처리 주도권을 프론트엔드에 넘긴다.
		// 허나 아래와 같이 단건 조회같은 경우 Exception이 터진다.
		Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");
		System.out.println("optionalMember = " + optionalMember);

		// 이름이 없으면 빈 컬렉션을 반환하면서 size가 0이 된다.
		List<Member> result = memberRepository.findListByUsername("asdasfsqa");
		System.out.println("result = " + result.size());

	}

	@Test
	public void paging() {
		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));
		memberRepository.save(new Member("member6", 10));

		int age = 10;

		// page index는 0부터,
		// 페이징된 쿼리
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

		// when
		/**
		 * 실무 꿀팁
		 * 현재 Member 엔티티를 그대로 사용하고 있다.
		 * 여기서 이전 강의에 들었던 엔티티를 그대로 사용하면 엔티티가 노출되기 때문에 DTO를 활용하여 사용한다.
		 * 근데 여기서 DTO로 쉽게 변환시키는 방법이 있다. (231 line)
		 */
		// Page
		Page<Member> nativePage = memberRepository.findByAge(age, pageRequest);
		// 내부 Entity가 아닌 API로 반환되어 사용할 수 있는 MemberDto 타입으로 변환했다.
		Page<MemberDto> page = nativePage.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

		// Slice는 표준 페이징된 사이즈보다 1개 더 들고 온다. (다음 페이지 여부 확인)
//		Slice<Member> page = memberRepository.findByAge(age, pageRequest);
		// 반환 타입을 Page로 받으면, 알아서 totalCount 쿼리도 같이 날린다.
		System.out.println("page = " + page);

		// then
		List<MemberDto> content = page.getContent();
		// (slice에는 없는 기능)
		long totalElements = page.getTotalElements();   // totalCount

		for (MemberDto member : content) {
			System.out.println("member = " + member);
		}
		System.out.println("totalCount = " + totalElements);

		// content는 3개씩 size를 쪼갰으니 3이 맞다
		assertEquals(content.size(), 3);
		// 총 member 수 (slice에는 없는 기능)
		assertEquals(page.getTotalElements(), 6);
		// page 번호를 가져올 수 있다.
		assertEquals(page.getNumber(), 0);
		// 전체 페이지 수 (slice에는 없는 기능)
		assertEquals(page.getTotalPages(), 2);
		// 첫 번째 페이지인지
		assertTrue(page.isFirst());
		// 다음 페이지가 존재하는 지
		assertTrue(page.hasNext());
	}

	@Test
	public void bulkUpdate() {
		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 19));
		memberRepository.save(new Member("member3", 20));
		memberRepository.save(new Member("member4", 21));
		memberRepository.save(new Member("member5", 40));

		// when
		int resultCount = memberRepository.bulkAgePlus(20);
		// bulk성 쿼리는 영속성 데이터를 꼭 날려야 한다.
		// 남아있는 변경사항을 영속성 컨텍스트에 반영
//		em.flush();
		// 영속성 컨텍스트에 있는 데이터를 다 날린다.
//		em.clear();

		List<Member> result = memberRepository.findByUsername("member5");
		Member member5 = result.get(0);
		// member5 = Member(id=5, username=member5, age=40)
		// bulk 연산에서 영속성 컨텍스트를 무시하고 그냥 DB에 쿼리를 쏜다.
		System.out.println("member5 = " + member5);

		assertEquals(resultCount, 3);

	}

	@Test
	public void findMemberLazy() {
		// given
		// 멤버 객체가 각 팀들을 참조
		// member1 -> teamA
		// member2 -> teamB

		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamB);

		memberRepository.save(member1);
		memberRepository.save(member2);

		// 완전히 영속성 컨텍스트에 있는 1차 캐시 정보들을 db에 반영한다.
		// clear로 영속성 컨텍스트를 완전히 날린다.
		em.flush();
		em.clear();

		// when
		// select Member만 가져온다.
//		List<Member> members = memberRepository.findAll();
		List<Member> members = memberRepository.findMemberFetchJoin();

		for (Member member : members) {
			System.out.println("Member = " + member.getUsername());
			// findAll은 Proxy 객체
			// fetch join으로는 순수 entity Team 객체 생성
			System.out.println("Member.teamClass = " + member.getTeam().getClass());
			/**
			 * fetch join re-description
			 * 그러나 Team과 연관관계 매핑이 되어있어서 Team의 Name을 가져오다보니
			 * 지연로딩 과정에서 Member 쿼리내에 가짜 객체, 즉 Proxy 객체를 들고 와서
			 * 테스트 결과를 보면 join 안된 단쿼리로 team의 name 값을 들고 온다.
			 * 결국 1+N 현상이다. (1은 Member, N은 Member와 매핑되어 조회할 테이블 수다.
			 */
			System.out.println("Team = " + member.getTeam().getName());
		}


	}

	@Test
	public void findMemberLazyByEntityGraph() {
		// given
		// 멤버 객체가 각 팀들을 참조
		// member1 -> teamA
		// member2 -> teamB

		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member1", 20, teamB);

		memberRepository.save(member1);
		memberRepository.save(member2);

		em.flush();
		em.clear();

		// when
		// select Member만 가져온다.
		List<Member> members = memberRepository.findEntityGraphByUsername("member1");

		for (Member member : members) {
			System.out.println("Member = " + member.getUsername());
			System.out.println("Team = " + member.getTeam().getName());
		}


	}
}