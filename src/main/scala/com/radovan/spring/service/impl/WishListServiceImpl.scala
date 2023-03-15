package com.radovan.spring.service.impl

import com.radovan.spring.repository.WishListRepository
import com.radovan.spring.service.WishListService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WishListServiceImpl extends WishListService {

  @Autowired
  private var wishListRepository:WishListRepository = _

  override def deleteWishList(wishListId: Integer): Unit = {
    wishListRepository.clearWishList(wishListId)
    wishListRepository.deleteById(wishListId)
    wishListRepository.flush()
  }
}

