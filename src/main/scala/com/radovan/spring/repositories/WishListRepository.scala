package com.radovan.spring.repositories

import com.radovan.spring.entities.WishListEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait WishListRepository extends JpaRepository[WishListEntity, Integer] {

}
