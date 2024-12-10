package com.radovan.spring.entities

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, OneToOne, Table}

import java.util
import java.sql.Timestamp
import scala.beans.BeanProperty

@Entity
@Table(name = "orders")
@SerialVersionUID(1L)
class OrderEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(name = "order_price",nullable = false)
  @BeanProperty var orderPrice:Float = _

  @Column(name = "created_time", nullable = false)
  @BeanProperty var createTime:Timestamp = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "cart_id", nullable = false)
  @BeanProperty var cart:CartEntity = _

  @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "address_id", nullable = false)
  @BeanProperty var address:OrderAddressEntity = _

  @Column(name = "book_quantity",nullable = false)
  @BeanProperty var bookQuantity:Integer = _

  @Column(nullable = false)
  @BeanProperty var discount:Integer = _

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "order",orphanRemoval = true)
  @BeanProperty var orderedItems:util.List[OrderItemEntity]= _

}
