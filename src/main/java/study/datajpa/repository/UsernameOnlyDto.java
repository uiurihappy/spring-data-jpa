package study.datajpa.repository;

public class UsernameOnlyDto {

	private final String username;

	// 생성자의 parameter 이름으로 매칭하여 projection이 된다.
	public UsernameOnlyDto(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
