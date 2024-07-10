package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.WishListDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.WishListRepository
import com.radovan.spring.services.WishListService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WishListServiceImpl extends WishListService {

  private var wishListRepository:WishListRepository = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def injectAll(wishListRepository: WishListRepository,tempConverter: TempConverter):Unit = {
    this.wishListRepository = wishListRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def getWishListById(id: Integer): WishListDto = {
    val wishListEntity = wishListRepository.findById(id)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The wish list has not been found!")))
    tempConverter.wishListEntityToDto(wishListEntity)
  }
}
