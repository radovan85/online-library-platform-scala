package com.radovan.spring.dto

import java.sql.Timestamp

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class PersistenceLoginDto extends Serializable {

  @BeanProperty var id:Integer = _
  @BeanProperty var createdAt:Timestamp = _
  @BeanProperty var createdAtStr:String = _
  @BeanProperty var customerId:Integer = _

}

