package com.radovan.spring.repositories

import com.radovan.spring.entities.BookImageEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait BookImageRepository extends JpaRepository[BookImageEntity, Integer] {

  @Query(value="select * from book_images where book_id = :bookId", nativeQuery = true)
  def findByBookId(@Param("bookId") bookId:Integer):Option[BookImageEntity]
}
