package com.radovan.spring.entities

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToMany, Table}

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "genres")
@SerialVersionUID(1L)
class BookGenreEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(nullable = false, length = 30,unique = true)
  @BeanProperty var name:String = _

  @Column(nullable = false, length = 90)
  @BeanProperty var description:String = _

  @OneToMany(mappedBy = "genre", fetch = FetchType.EAGER)
  @BeanProperty var books:util.List[BookEntity] = _
}
