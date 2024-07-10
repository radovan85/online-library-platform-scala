package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class CartItemDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var price: Float = _
  @BeanProperty var quantity: Integer = _
  @BeanProperty var cartId: Integer = _
  @BeanProperty var bookId: Integer = _
}
