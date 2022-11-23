package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

	List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

	List<Member> findTop3HelloBy();

//	@Query(name = "Member.findByUsername")
	// named Param이 넘어가기 위해 Param 어노테이션 사용
	List<Member> findByUsername(@Param("username") String username);

	// 만약 쿼리문에서 오타를 내면, 애플리케이션 로딩 시점에 에러 발생시킨다.
	// 이름이 없는 네임드 쿼리이다.
	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param("username") String username, @Param("age") int age);

	@Query("select m.username from Member m")
	List<String> findUsernameList();

	@Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
	List<MemberDto> findMemberDto();

	@Query("select m from Member m where m.username in :names")
	List<Member> findByNames(@Param("names") List<String> names);


	List<Member> findListByUsername(String username);   // 컬렉션
	Member findMemberByUsername(String username);       // 단건
	Optional<Member> findOptionalByUsername(String username);    // 단건 Optional



}
