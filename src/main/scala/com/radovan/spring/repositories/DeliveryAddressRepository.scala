package com.radovan.spring.repositories

import com.radovan.spring.entities.DeliveryAddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait DeliveryAddressRepository extends JpaRepository[DeliveryAddressEntity, Integer] {

}
