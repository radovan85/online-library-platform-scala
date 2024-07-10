package com.radovan.spring.services

import com.radovan.spring.dto.WishListDto

trait WishListService {

  def getWishListById(id:Integer):WishListDto
}
