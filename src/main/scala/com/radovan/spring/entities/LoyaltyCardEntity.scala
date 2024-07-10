package com.radovan.spring.entities

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "loyalty_cards")
@SerialVersionUID(1L)
class LoyaltyCardEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(nullable = false)
  @BeanProperty var discount:Integer = _

  @Column(nullable = false)
  @BeanProperty var points:Integer = _

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id", nullable = false)
  @BeanProperty var customer:CustomerEntity = _

}
