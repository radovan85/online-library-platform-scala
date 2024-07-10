package com.radovan.spring.repositories

import com.radovan.spring.entities.ReviewEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait ReviewRepository extends JpaRepository[ReviewEntity, Integer] {

  @Query(value="select * from reviews where customer_id = :customerId and book_id = :bookId",nativeQuery = true)
  def findByCustomerIdAndBookId(@Param("customerId") customerId: Integer,@Param("bookId") bookId: Integer):Option[ReviewEntity]

}
