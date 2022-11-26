package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("userA"));
    }

}
