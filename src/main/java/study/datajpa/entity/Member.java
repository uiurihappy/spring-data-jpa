package study.datajpa.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private String username;

	protected Member() {
	}

	public Member(String username) {
		this.username = username;
	}

//	public void changeUsername(String username){
//		this.username = username;
//	}
}
