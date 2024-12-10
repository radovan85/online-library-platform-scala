package com.radovan.spring.repositories

import com.radovan.spring.entities.LoyaltyCardRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait LoyaltyCardRequestRepository extends JpaRepository[LoyaltyCardRequestEntity, Integer] {

  def findByCustomerId(customerId: Integer):Option[LoyaltyCardRequestEntity]

}
