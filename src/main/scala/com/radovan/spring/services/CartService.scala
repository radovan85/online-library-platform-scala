package com.radovan.spring.services

import com.radovan.spring.dto.CartDto

trait CartService {

  def getCartById(cartId:Integer):CartDto

  def refreshCartState(cartId:Integer):Unit

  def calculateGrandTotal(cartId:Integer):Float

  def validateCart(cartId:Integer):CartDto

  def calculateFullPrice(cartId:Integer):Float

  def refreshAllCarts():Unit
}
