package com.radovan.spring.repositories

import com.radovan.spring.entities.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait RoleRepository extends JpaRepository[RoleEntity, Integer] {

  def findByRole(role: String):Option[RoleEntity]

}
