package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class PersistenceLoginDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var createTime: String = _
  @BeanProperty var customerId: Integer = _

}
