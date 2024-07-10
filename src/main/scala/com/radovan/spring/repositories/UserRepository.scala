package com.radovan.spring.repositories

import com.radovan.spring.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait UserRepository extends JpaRepository[UserEntity, Integer] {

  def findByEmail(email: String):Option[UserEntity]

}
