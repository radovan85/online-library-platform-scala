package com.radovan.spring.repositories

import com.radovan.spring.entities.BookEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util

@Repository
trait BookRepository extends JpaRepository[BookEntity, Integer] {


  def findByIsbn(isbn: String): Option[BookEntity]

  @Query(value = "select * from books where isbn ilike CONCAT('%', :keyword, '%')"
    + " or publisher ilike CONCAT('%' ,:keyword, '%')"
    + " or author ilike CONCAT('%', :keyword, '%')"
    + " or description ilike CONCAT('%', :keyword, '%')"
    + " or name ilike CONCAT('%', :keyword, '%')", nativeQuery = true)
  def findAllByKeyword(@Param("keyword") keyword: String): util.List[BookEntity]

  @Modifying
  @Query(value = "delete from books_wishlists where book_id = :bookId", nativeQuery = true)
  def eraseBookFromAllWishlists(@Param("bookId") bookId: Integer): Unit
}
