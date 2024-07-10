package com.radovan.spring.repositories

import com.radovan.spring.entities.OrderAddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait OrderAddressRepository extends JpaRepository[OrderAddressEntity, Integer] {

}
