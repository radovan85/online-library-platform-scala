package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class CustomerDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var dateOfBirth: String = _
  @BeanProperty var registrationTime: String = _
  @BeanProperty var userId: Integer = _
  @BeanProperty var cartId: Integer = _
  @BeanProperty var wishListId: Integer = _
  @BeanProperty var loyaltyCardId: Integer = _
  @BeanProperty var deliveryAddressId: Integer = _
  @BeanProperty var reviewsIds: Array[Integer] = _
  @BeanProperty var persistenceLoginsIds: Array[Integer] = _

}
