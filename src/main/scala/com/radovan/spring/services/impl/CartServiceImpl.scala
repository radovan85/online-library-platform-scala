package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartDto
import com.radovan.spring.exceptions.InvalidCartException
import com.radovan.spring.repositories.{CartItemRepository, CartRepository}
import com.radovan.spring.services.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.text.DecimalFormat
import scala.jdk.CollectionConverters._

@Service
class CartServiceImpl extends CartService {

  private var cartRepository:CartRepository = _
  private var cartItemRepository:CartItemRepository = _
  private var tempConverter:TempConverter = _
  private val decfor = new DecimalFormat("0.00")

  @Autowired
  private def injectAll(cartRepository: CartRepository,cartItemRepository: CartItemRepository,
                        tempConverter: TempConverter):Unit = {
    this.cartRepository = cartRepository
    this.cartItemRepository = cartItemRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def getCartById(cartId: Integer): CartDto = {
    val cartEntity = cartRepository.findById(cartId)
      .orElseThrow(() => new Error("The cart has not been found!"))
    tempConverter.cartEntityToDto(cartEntity)
  }

  @Transactional
  override def refreshCartState(cartId: Integer): Unit = {
    val cart = getCartById(cartId)
    val priceOption = cartItemRepository.calculateGrandTotal(cart.getId)
    priceOption match {
      case Some(price) => cart.setCartPrice(decfor.format(price).toFloat)
      case None => cart.setCartPrice(0f)
    }

    cartRepository.saveAndFlush(tempConverter.cartDtoToEntity(cart))
  }

  @Transactional(readOnly = true)
  override def calculateGrandTotal(cartId: Integer): Float = {
    cartItemRepository.calculateGrandTotal(cartId).getOrElse(0f)
  }

  @Transactional(readOnly = true)
  override def validateCart(cartId: Integer): CartDto = {
    val cart = getCartById(cartId)
    if (cart.getCartItemsIds.length == 0) {
      throw new InvalidCartException(new Error("The cart is empty!"))
    }

    cart
  }

  @Transactional(readOnly = true)
  override def calculateFullPrice(cartId: Integer): Float = {
    cartItemRepository.calculateFullPrice(cartId).getOrElse(0f)
  }

  @Transactional
  override def refreshAllCarts(): Unit = {
    val allCarts = cartRepository.findAll().asScala
    allCarts.foreach(cartEntity => {
      refreshCartState(cartEntity.getId)
    })
  }
}
