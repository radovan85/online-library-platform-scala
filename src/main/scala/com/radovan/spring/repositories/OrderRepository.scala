package com.radovan.spring.repositories

import com.radovan.spring.entities.OrderEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait OrderRepository extends JpaRepository[OrderEntity, Integer] {

  @Query(value="select sum(order_price) from orders where cart_id = :cartId",nativeQuery = true)
  def getOrdersValue(@Param("cartId") cartId:Integer):Option[Float]
}
