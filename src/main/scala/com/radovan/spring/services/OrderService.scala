package com.radovan.spring.services

import com.radovan.spring.dto.OrderDto

trait OrderService {

  def addOrder():OrderDto

  def listAll:Array[OrderDto]

  def getOrderById(orderId:Integer):OrderDto

  def deleteOrder(orderId:Integer):Unit

  def listAllByCustomerId(customerId:Integer):Array[OrderDto]

  def calculateOrderTotal(orderId:Integer):Float

  def calculateOrdersValue(cartId:Integer):Float
}
