package com.bookstore.order_service.domain;

import aj.org.objectweb.asm.commons.Remapper;
import com.bookstore.order_service.domain.models.OrderStatus;
import com.bookstore.order_service.domain.models.OrderSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

//OrderItem is a part of Order(aggregates), we ar creating for order repo
interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus orderStatus);

    default void updateOrderStatus(String orderNumber, OrderStatus orderStatus){
        OrderEntity entity = this.findByOrderNumber(orderNumber).orElseThrow();
        entity.setStatus(orderStatus);
        this.save(entity);
    }

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

//    @Query("""
//        select o.orderNumber as orderNumber, o.status as status
//        from OrderEntity o
//        where o.userName = :username
//    """)
@Query("""
        select new com.bookstore.order_service.domain.models.OrderSummary(o.orderNumber, o.status)
        from OrderEntity o
        where o.userName = :username
    """)
    List<OrderSummary> findByUserName(String username);

    @Query("""
    select distinct o
    from OrderEntity o left join fetch o.items
    where o.userName = :username and o.orderNumber = :orderNumber
    """)
    Optional<OrderEntity> findByUserNameAndOrderNumber(String username, String orderNumber);
}
