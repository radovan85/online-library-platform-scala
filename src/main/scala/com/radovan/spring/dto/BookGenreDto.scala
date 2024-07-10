package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class BookGenreDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var name: String = _
  @BeanProperty var description: String = _
  @BeanProperty var booksIds: Array[Integer] = _
}
