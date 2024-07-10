package com.radovan.spring.entities

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "order_items")
@SerialVersionUID(1L)
class OrderItemEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(nullable = false)
  @BeanProperty var quantity:Integer = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @Column(name = "book_name", length = 50)
  @BeanProperty var bookName:String = _

  @Column(name = "book_price", nullable = false)
  @BeanProperty var bookPrice:Float = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "order_id", nullable = false)
  @BeanProperty var order:OrderEntity = _

}
