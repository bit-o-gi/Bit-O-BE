package bit.anniversary.repository;

import java.time.LocalDateTime;
import java.util.List;

import bit.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bit.anniversary.entity.Anniversary;

@Repository
public interface AnRepository extends JpaRepository<Anniversary,Long> {

	// NOTE: 특정 날짜 범위 안에 있는 기념일들을 조회
	List<Anniversary> findAllByAnniversaryDateBetween(LocalDateTime startDate, LocalDateTime endDate);

	@Query("""
    SELECT a FROM Anniversary a
    WHERE (:startDate IS NULL OR a.anniversaryDate >= :startDate)
      AND (:endDate IS NULL OR a.anniversaryDate <= :endDate)
      AND (a.writer.id = :userId OR a.withPeople.id = :userId)
""")
	Page<Anniversary> findAllByDateRangeAndUserInvolvedById(
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate,
			@Param("userId") Long userId,
			Pageable pageable
	);

	// NOTE: 특정 날짜 이후의 기념일 조회
	List<Anniversary> findAllByAnniversaryDateAfter(LocalDateTime date);

	// NOTE: 특정 날짜 이전의 기념일 조회
	List<Anniversary> findAllByAnniversaryDateBefore(LocalDateTime date);

}
