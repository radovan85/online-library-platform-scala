package com.radovan.spring.repositories

import com.radovan.spring.entities.CartItemEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util

@Repository
trait CartItemRepository extends JpaRepository[CartItemEntity, Integer] {

  @Query(value = "select * from cart_items where book_id = :bookId", nativeQuery = true)
  def findAllByBookId(@Param("bookId") bookId: Integer): util.List[CartItemEntity]

  @Query(value = "select * from cart_items where cart_id = :cartId and book_id = :bookId", nativeQuery = true)
  def findByCartIdAndBookId(@Param("cartId") cartId: Integer, @Param("bookId") bookId: Integer): Option[CartItemEntity]

  @Modifying
  @Query(value = "delete from cart_items where id = :itemId", nativeQuery = true)
  def removeCartItem(@Param("itemId") itemId: Integer): Unit

  @Modifying
  @Query(value="delete from cart_items where book_id = :bookId",nativeQuery=true)
  def removeAllByBookId(@Param("bookId") bookId: Integer):Unit

  @Query(value="select sum(price) from cart_items where cart_id = :cartId",nativeQuery = true)
  def calculateGrandTotal(@Param("cartId") cartId:Integer):Option[Float]

  @Query(value="select sum(pr.price * items.quantity) from cart_items as items inner join products as pr on items.product_id = pr.id where items.cart_id = :cartId",nativeQuery=true)
  def calculateFullPrice(@Param("cartId") cartId:Integer):Option[Float]
}
