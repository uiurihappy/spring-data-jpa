package study.datajpa.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.criteria.*;

public class MemberSpec {

    // 구현 기술의 JPA criteria의 문법을 사용
    // 결과적으로 JPQL을 만들지만 추후에 너무 복잡해진다.
    public static Specification<Member> teamName(final String teamName) {

        return (root, query, criteriaBuilder) -> {

            if (StringUtils.isEmpty(teamName)) {
                return null;
            }
            /**
             * root는 처음 찍은 엔티티를 의미
             * query랑 queryBuilder를 criteria로 설계
              */
            Join<Member, Team> t = root.join("team", JoinType.INNER);// 회원과 조인

            return criteriaBuilder.equal(t.get("name"), teamName);
        };
    }

    public static Specification<Member> username(final String username) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("username"), username);

    }
}
