package com.radovan.spring.service.impl

import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.repository.{CartItemRepository, CartRepository}
import com.radovan.spring.service.{CartItemService, CartService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class CartItemServiceImpl extends CartItemService {

  @Autowired
  private var cartItemRepository:CartItemRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var cartRepository:CartRepository = _

  override def addCartItem(cartItem: CartItemDto): CartItemDto = {
    val cartItemEntity = tempConverter.cartItemDtoToEntity(cartItem)
    val storedItem = cartItemRepository.save(cartItemEntity)
    val returnValue = tempConverter.cartItemEntityToDto(storedItem)
    returnValue
  }

  override def removeCartItem(cartId: Integer, itemId: Integer): Unit = {
    cartItemRepository.removeCartItem(cartId, itemId)
    cartItemRepository.flush()
    cartService.refreshCartState(cartId)
  }

  override def eraseAllCartItems(cartId: Integer): Unit = {
    cartItemRepository.removeAllByCartId(cartId)
    cartItemRepository.flush()
    cartService.refreshCartState(cartId)
  }

  override def listAllByCartId(cartId: Integer): util.List[CartItemDto] = {
    val cartItems = Optional.ofNullable(cartItemRepository.findAllByCartId(cartId))
    val returnValue = new util.ArrayList[CartItemDto]
    if (!cartItems.isEmpty) {
      for (item <- cartItems.get.asScala) {
        val itemDto = tempConverter.cartItemEntityToDto(item)
        returnValue.add(itemDto)
      }
    }
    returnValue
  }

  override def getCartItem(id: Integer): CartItemDto = {
    val cartItemEntity = Optional.ofNullable(cartItemRepository.getById(id))
    var returnValue:CartItemDto = null
    if (cartItemEntity.isPresent) returnValue = tempConverter.cartItemEntityToDto(cartItemEntity.get)
    returnValue
  }

  override def getCartItemByCartIdAndBookId(cartId: Integer, bookId: Integer): CartItemDto = {
    val cartItemEntity = Optional.ofNullable(cartItemRepository.findByCartIdAndBookId(cartId, bookId))
    var returnValue:CartItemDto = null
    if (cartItemEntity.isPresent) returnValue = tempConverter.cartItemEntityToDto(cartItemEntity.get)
    returnValue
  }

  override def getBookQuantity(cartId: Integer): Integer = {
    var returnValue = 0
    val bookQuantity = Optional.ofNullable(cartItemRepository.findBookQuantity(cartId))
    if (bookQuantity.isPresent) returnValue = bookQuantity.get
    returnValue
  }

  override def eraseAllByBookId(bookId: Integer): Unit = {
    cartItemRepository.removeAllByBookId(bookId)
    cartItemRepository.flush()
    val allCarts = Optional.ofNullable(cartRepository.findAll)
    if (!allCarts.isEmpty) {
      for (cartEntity <- allCarts.get.asScala) {
        cartService.refreshCartState(cartEntity.getCartId)
      }
    }
  }

  override def hasDiscount(itemId: Integer): Boolean = {
    var returnValue = false
    val itemEntity = Optional.ofNullable(cartItemRepository.getById(itemId))
    if (itemEntity.isPresent) {
      val cartEntity = itemEntity.get.getCart
      val customerEntity = cartEntity.getCustomer
      val loyaltyCard = Optional.ofNullable(customerEntity.getLoyaltyCard)
      if (loyaltyCard.isPresent) {
        val discount = loyaltyCard.get.getDiscount
        if (discount > 0) returnValue = true
      }
    }
    returnValue
  }
}

