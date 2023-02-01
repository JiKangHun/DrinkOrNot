package com.ssafy.cadang.repository;

import com.ssafy.cadang.domain.Order;
import com.ssafy.cadang.domain.OrderStatus;
import com.ssafy.cadang.dto.record.query.CaffeineRankingDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RecordReposiotry extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "drink")
    @Query("select o from Order o where o.regDate < :regDate and o.user.id=:userId and o.orderStatus in :orderStatuses order by o.regDate desc")
    Slice<Order> findByIdLessThanAndUserIdAndOrderStatusIn(@Param("regDate") LocalDateTime regDate, @Param(("userId")) Long userId, @Param("orderStatuses") OrderStatus[] orderStatuses, Pageable pageable);

    @EntityGraph(attributePaths = "drink")
    Optional<Order> findById(Long id);

    @Query("select o from Order o join fetch o.drink d where o.regDate < :regDate and o.user.id=:userId and o.orderStatus in :orderStatuses and (o.storeName LIKE :keyword or d.drinkName LIKE :keyword)  order by o.regDate desc")
    Slice<Order> findBySearchKeyword(@Param("regDate") LocalDateTime regDate, @Param(("userId")) Long userId, @Param(("keyword")) String keyword, @Param("orderStatuses") OrderStatus[] orderStatuses, Pageable pageable);

    // ----- 랭킹 -------
    @Query("SELECT new com.ssafy.cadang.dto.record.query.CaffeineRankingDto(d.drinkName, MAX(f.franchiseName), MAX(o.caffeine) as caffeine) " +
            "FROM Order o " +
            "JOIN o.drink d " +
            "JOIN d.franchise f " +
            "WHERE o.user.id = :userId " +
            "AND month(o.regDate) = :month " +
            "GROUP BY d.franchise.id, d.drinkName " +
            "ORDER BY caffeine DESC")
    List<CaffeineRankingDto> findTop3ByCaffeine(@Param("userId") Long userId, @Param("month") int month, Pageable pageable);

}
