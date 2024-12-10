package com.radovan.spring.repositories

import com.radovan.spring.entities.PersistenceLoginEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait PersistenceLoginRepository extends JpaRepository[PersistenceLoginEntity, Integer] {

  @Query(value = "SELECT * FROM persistence_logins WHERE customer_id = :customerId ORDER BY create_time DESC FETCH FIRST 1 ROWS ONLY", nativeQuery = true)
  def findLastLogin(@Param("customerId") customerId: Integer): Option[PersistenceLoginEntity]

}
