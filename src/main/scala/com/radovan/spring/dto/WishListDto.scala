package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class WishListDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var booksIds: Array[Integer] = _
  @BeanProperty var customerId: Integer = _

}
