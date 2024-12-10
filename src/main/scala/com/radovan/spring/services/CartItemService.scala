package com.radovan.spring.services

import com.radovan.spring.dto.CartItemDto

trait CartItemService {

  def addCartItem(item:CartItemDto):CartItemDto

  def removeCartItem(itemId:Integer):Unit

  def eraseAllCartItems(cartId:Integer):Unit

  def listAllByCartId(cartId:Integer):Array[CartItemDto]

  def getItemById(itemId:Integer):CartItemDto

  def getCartItemByCartIdAndBookId(cartId:Integer,bookId:Integer):CartItemDto

  def getBookQuantity(cartId:Integer):Integer

  def eraseAllByBookId(bookId:Integer):Unit

  def hasDiscount(itemId:Integer):Boolean

}
