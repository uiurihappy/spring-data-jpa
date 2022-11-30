package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

/** Interface 기반 Projections
 * 데이터를 원하는 것만 select 할 수 있는 이점
 * findBy컬럼 등등 조회한다.
 * Interface 기반 클로저 프로젝션, 오픈 프로젝션이라 부른다.
 *
 */
public interface UsernameOnly {
	// getter 프로퍼티 명으로 만들면 된다.
	// String getUsername();

	// 이런 식으로 작성도 가능
	// member entity를 가져와서 해당 target 프로퍼티의 where 조건으로 맞출 수 있다.
	@Value("#{target.username + ' ' + target.age}")
	String getUsernameAndAge();
}
