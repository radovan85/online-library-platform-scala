package com.radovan.spring.entities

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import java.sql.Timestamp
import scala.beans.BeanProperty

@Entity
@Table(name = "reviews")
@SerialVersionUID(1L)
class ReviewEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(nullable = false, length = 255)
  @BeanProperty var text:String = _

  @Column(nullable = false)
  @BeanProperty var rating:Integer = _

  @Column(nullable = false, name = "create_time")
  @BeanProperty var createTime:Timestamp = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id", nullable = false)
  @BeanProperty var author:CustomerEntity = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "book_id", nullable = false)
  @BeanProperty var book:BookEntity = _

  @BeanProperty var authorized:Byte = _

}
