package com.radovan.spring.repositories

import com.radovan.spring.entities.BookGenreEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait BookGenreRepository extends JpaRepository[BookGenreEntity, Integer] {

  def findByName(name:String):Option[BookGenreEntity]
}
