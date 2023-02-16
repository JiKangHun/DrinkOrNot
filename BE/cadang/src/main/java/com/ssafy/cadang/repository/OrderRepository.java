package com.ssafy.cadang.repository;

import com.ssafy.cadang.domain.Order;
import com.ssafy.cadang.domain.OrderStatus;
import com.ssafy.cadang.dto.cafe.query.DrinkNumCheckDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(" select o from Order o join fetch o.drink d where o.user.id=:userId order by o.regDate DESC")
    List<Order> findAllByUserid(@Param("userId") Long userId);   //고객 주문 내역 조회

    @Query(" select o from Order o join fetch o.drink d " +
            "where o.store.id=:storeId and o.orderStatus " +
            "not in :orderStatusList order by o.regDate ")
    List<Order> findAllByStoreid(@Param("storeId") Long storeId, @Param("orderStatusList") List<OrderStatus> orderStatusList);   //가게 주문 내역 조회

    @Query(" select o from Order o join fetch o.drink d where o.store.id=:storeId " +
            "and o.orderStatus =:orderStatus order by o.regDate DESC")
    List<Order> findAllByStoreidAndOrderStatus(@Param("storeId") Long storeId, @Param("orderStatus") OrderStatus orderStatus); // 가게 신규 주문 조회

    @Query(" select o from Order o join fetch o.drink d where o.user.id=:userId " +
            "and o.orderStatus not in :orderStatusList order by o.regDate DESC")
    List<Order> findAllByUserIdAndOrderStatus(@Param("userId") Long userId,
                                              @Param("orderStatusList") List<OrderStatus> orderStatusList); // 고객 주문 현황 조회

}