package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class ReviewDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var text: String = _
  @BeanProperty var rating: Integer = _
  @BeanProperty var createTime: String = _
  @BeanProperty var authorId: Integer = _
  @BeanProperty var bookId: Integer = _
  @BeanProperty var authorized: Short = _

}
