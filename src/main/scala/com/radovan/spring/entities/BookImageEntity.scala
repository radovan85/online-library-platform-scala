package com.radovan.spring.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

import scala.beans.BeanProperty


@Entity
@Table(name = "book_images")
@SerialVersionUID(1L)
class BookImageEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @BeanProperty var name:String = _

  @Column(name = "content_type")
  @BeanProperty var contentType:String = _

  @BeanProperty var size:Long = _

  @Lob
  @BeanProperty var data:Array[Byte] = _

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "book_id", nullable = false)
  @BeanProperty var book:BookEntity = _


}
