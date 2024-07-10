package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class BookImageDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var name: String = _
  @BeanProperty var contentType: String = _
  @BeanProperty var size: Long = _
  @BeanProperty var data: Array[Byte] = _
  @BeanProperty var bookId: Integer = _
}
