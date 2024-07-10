package com.radovan.spring.entities

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "delivery_addresses")
@SerialVersionUID(1L)
class DeliveryAddressEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(nullable = false, length = 75)
  @BeanProperty var address:String = _

  @Column(nullable = false, length = 40)
  @BeanProperty var city:String = _

  @Column(nullable = false, length = 40)
  @BeanProperty var state:String = _

  @Column(name = "post_code", nullable = false, length = 10)
  @BeanProperty var postcode:String = _

  @Column(nullable = false, length = 40)
  @BeanProperty var country:String = _

  @OneToOne(mappedBy = "deliveryAddress", fetch = FetchType.EAGER)
  @BeanProperty var customer:CustomerEntity = _
}
