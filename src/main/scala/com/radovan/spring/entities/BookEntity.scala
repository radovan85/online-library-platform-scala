package com.radovan.spring.entities

import java.util
import jakarta.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToMany, ManyToOne, OneToMany, OneToOne, Table, Transient}

import scala.beans.BeanProperty


@Entity
@Table(name = "books")
@SerialVersionUID(1L)
class BookEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id: Integer = _

  @Column(nullable = false, length = 13)
  @BeanProperty var isbn: String = _

  @Column(nullable = false, length = 50)
  @BeanProperty var name: String = _

  @Column(nullable = false, length = 40)
  @BeanProperty var publisher: String = _

  @Column(nullable = false, length = 40)
  @BeanProperty var author: String = _

  @Column(nullable = false, length = 90)
  @BeanProperty var description: String = _

  @Column(nullable = false, length = 30)
  @BeanProperty var language: String = _

  @Column(name = "published_year", nullable = false)
  @BeanProperty var publishedYear: Integer = _

  @Column(name = "page_number", nullable = false)
  @BeanProperty var pageNumber: Integer = _

  @Column(nullable = false)
  @BeanProperty var price: Float = _

  @Column(name = "average_rating")
  @BeanProperty var averageRating: Float = _

  @Column(nullable = false, length = 30)
  @BeanProperty var cover: String = _

  @Column(nullable = false, length = 30)
  @BeanProperty var letter: String = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "genre_id", nullable = false)
  @BeanProperty var genre: BookGenreEntity = _

  @OneToOne(mappedBy = "book", fetch = FetchType.EAGER,cascade = Array(CascadeType.ALL))
  @BeanProperty var image: BookImageEntity = _

  @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "book")
  @BeanProperty var reviews: util.List[ReviewEntity] = _

  @Transient
  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "books")
  @BeanProperty var wishLists: util.List[WishListEntity] = _

}
