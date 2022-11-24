package study.datajpa.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
// 인자없는 생성자 롬복 활용, 접근 제어자 제어도 가능
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 가급적 연관관계 필드는 ToString 하지 않는 게 좋다.
@ToString(of = {"id", "username", "age"})
// 실무에서는 거의 사용안함
@NamedQuery(
		name = "Member.findByUsername",
		query="select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private String username;
	private int age;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	public Member(String username) {
		this.username = username;
	}

	public Member(String username, int age, Team team) {
		this.username = username;
		this.age = age;
		if (team != null) {
			changeTeam(team);
		}
	}

	public Member(String username, int age) {
		this.username = username;
		this.age = age;
	}

	public void changeTeam(Team team){
		this.team = team;
		// 객체이기에 멤버도 변경
		team.getMembers().add(this);
	}
}
