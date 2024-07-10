package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var orderPrice: Float = _
  @BeanProperty var createTime: String = _
  @BeanProperty var cartId: Integer = _
  @BeanProperty var addressId: Integer = _
  @BeanProperty var bookQuantity: Integer = _
  @BeanProperty var discount: Integer = _
  @BeanProperty var orderedItemsIds: Array[Integer] = _
}
