package study.datajpa.repository;

public interface NestedClosedProjections {

	// 첫번째는 최적화가 되지만, 두번째는 최적화가 안되어 Team 컬럼을 전체 조회한다.
	String getUsername();
	TeamInfo getTeam();
	// team에 대한 정보
	interface TeamInfo {
		String getName();
	}

}
