package com.radovan.spring.repositories

import com.radovan.spring.entities.LoyaltyCardEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait LoyaltyCardRepository extends JpaRepository[LoyaltyCardEntity, Integer] {

}
