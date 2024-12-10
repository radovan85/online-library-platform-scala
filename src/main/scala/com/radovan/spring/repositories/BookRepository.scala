package com.radovan.spring.repositories

import com.radovan.spring.entities.BookEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util

@Repository
trait BookRepository extends JpaRepository[BookEntity, Integer] {


  def findByIsbn(isbn: String): Option[BookEntity]

  @Query(value = "SELECT * FROM books WHERE LOWER(isbn) LIKE '%' || LOWER(:keyword) || '%'"
    + " OR LOWER(publisher) LIKE '%' || LOWER(:keyword) || '%'"
    + " OR LOWER(author) LIKE '%' || LOWER(:keyword) || '%'"
    + " OR LOWER(description) LIKE '%' || LOWER(:keyword) || '%'"
    + " OR LOWER(name) LIKE '%' || LOWER(:keyword) || '%'", nativeQuery = true)
  def findAllByKeyword(@Param("keyword") keyword: String): util.List[BookEntity]


  @Modifying
  @Query(value = "delete from books_wishlists where book_id = :bookId", nativeQuery = true)
  def eraseBookFromAllWishlists(@Param("bookId") bookId: Integer): Unit
}
