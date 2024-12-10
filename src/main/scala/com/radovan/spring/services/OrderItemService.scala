package com.radovan.spring.services

import com.radovan.spring.dto.OrderItemDto

trait OrderItemService {

  def listAllByOrderId(orderId:Integer):Array[OrderItemDto]

  def eraseAllByOrderId(orderId:Integer):Unit
}
