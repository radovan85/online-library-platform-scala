package com.radovan.spring.entities

import jakarta.persistence.{Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "card_requests")
@SerialVersionUID(1L)
class LoyaltyCardRequestEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id", nullable = false)
  @BeanProperty var customer:CustomerEntity = _
}
