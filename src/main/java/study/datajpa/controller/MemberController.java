package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 도메인 클래스 컨버터 사용 전
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        return memberRepository.findById(id).get().getUsername();
    }

    // 도메인 클래스 컨버터 사용 후

    /**
     * Pk 노출 이슈 때문에 그렇다.
     * 단순 조회용으로 사용하는 것이 낫다.
     * 트랜잭션 범위에 없는 것에 대해 다 조회하기에 그렇다.
     * @param member
     * @return
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }


    // localhost:3001/members?page=1&size=3&sort=id,desc&sort=username,desc
    @GetMapping("/members")
    // 해당 controller 에서만 설정하고 싶으면
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username", direction = Sort.Direction.DESC) Pageable pageable) {
        // page size 글로벌 설정 default값은 20으로 application.yml에서 해결할 수 있음
        // Page를 반환 타입으로 할시에 위 주석처럼 요청 파라미터로 해결할 수 있는 가지수가 많아짐

        /**
         *   data:
         *     web:
         *       pageable:
         *         # page 사이즈
         *         default-page-size: 10
         *         # 최대 page 사이즈
         *         max-page-size: 2000
         */

        Page<Member> page = memberRepository.findAll(pageable);

        // 엔티티를 그대로 노출하면 안되므로 DTO로 변환 (중요!!!)
        // 메서드 레퍼런스로 변환
        Page<MemberDto> list = page.map(MemberDto::new);

        /** 페이지를 0이 아닌 1부터 하는 방법
         * 1. PageRequest request = PageRequest.of(page: 1, size: 2);
         * 그대로 request를 갖다 쓴다.
         * 2. spring.data.web.pageable.one-indexed-parameters를 true로 설정하여 사용
         * 권장하지는 않는다. 페이지 인덱스의 데이터가 맞지 않아서 그렇다.
         * 내 생각엔 그냥 프론트와 정책정하고 0부터 하는 게 나아보임
         * 1부터 달라고하면 별 수 있나..ㅋㅋ 줘야지
         */

        return list;
        // or return memberRepository.findAll(pageable).map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        // 이렇게도 가능
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++){
            memberRepository.save(new Member("user " + i, i));
        }
    }

}
