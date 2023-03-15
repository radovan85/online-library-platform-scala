package com.radovan.spring.dto

import java.sql.Timestamp

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class ReviewDto extends Serializable {

  @BeanProperty var reviewId:Integer = _
  @BeanProperty var text:String = _
  @BeanProperty var rating:Integer = _
  @BeanProperty var createdAt:Timestamp = _
  @BeanProperty var createdAtStr:String = _
  @BeanProperty var authorId:Integer = _
  @BeanProperty var bookId:Integer = _
  @BeanProperty var authorized:Byte = _


}

