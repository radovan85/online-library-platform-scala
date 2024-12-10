package com.radovan.spring.entities

import jakarta.persistence.{Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, JoinTable, ManyToMany, OneToOne, Table}

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "wishlists")
@SerialVersionUID(1L)
class WishListEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id: Integer = _

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "books_wishlists", joinColumns = Array(new JoinColumn(name = "wishlist_id")), inverseJoinColumns =
    Array(new JoinColumn(name = "book_id")))
  @BeanProperty var books: util.List[BookEntity] = _

  @OneToOne(fetch = FetchType.EAGER, mappedBy = "wishList")
  @BeanProperty var customer: CustomerEntity = _
}
