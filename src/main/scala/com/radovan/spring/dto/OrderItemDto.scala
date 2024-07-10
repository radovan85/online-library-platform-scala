package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderItemDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var quantity: Integer = _
  @BeanProperty var price: Float = _
  @BeanProperty var bookName: String = _
  @BeanProperty var bookPrice: Float = _
  @BeanProperty var orderId: Integer = _

}
