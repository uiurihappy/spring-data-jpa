package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{

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

	// count 쿼리 분리
	@Query(value = "select m from Member m left join m.team t",
			// countQuery를 분리해야 하는 이유는 Page 타입일 때 연관된 테이블까지 join해서 count 쿼리를 날릴려하니 성능적으로 좋지 않다.
			// 따로 분리해서 count 쿼리를 작성하면 join 없이 해결할 수 있다.
			countQuery = "select count(m) from Member m")
	Page<Member> findByAge(int age, Pageable pageable);

	// JPA에서 Modifying Option으로 flush, clear를 해결할 수 있다.
	@Modifying(clearAutomatically = true)  // Modifying이 있어야 executeUpdate가 실행된다.
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);

	// fetch join으로 인해 쿼리 한번으로 1+N 문제를 해결
	@Query("select m from Member m left join fetch m.team")
	List<Member> findMemberFetchJoin();

	@Override
	// 내부적으로 fetch join 사용
	@EntityGraph(attributePaths = {"team"})
	List<Member> findAll();

	// fetch join: JPQL + EntityGraph
	@EntityGraph(attributePaths = {"team"})
	@Query("select m from Member m")
	List<Member> findByEntityGraph();

	// @NamedEntityGraph 사용으로
//	@EntityGraph(attributePaths = ("team")) // 대신
	@EntityGraph("Member.all") // 사용
	List<Member> findEntityGraphByUsername(@Param("username") String username);

	@QueryHints(value = @QueryHint( name = "org.hibernate.readOnly", value = "true"))
	Member findReadOnlyByUsername(String username);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Member> findLockByUsername(String username);
}
