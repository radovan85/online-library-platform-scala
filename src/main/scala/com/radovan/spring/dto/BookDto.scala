package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class BookDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var isbn: String = _
  @BeanProperty var name: String = _
  @BeanProperty var publisher: String = _
  @BeanProperty var author: String = _
  @BeanProperty var description: String = _
  @BeanProperty var language: String = _
  @BeanProperty var publishedYear: Integer = _
  @BeanProperty var pageNumber: Integer = _
  @BeanProperty var price: Float = _
  @BeanProperty var averageRating: Float = _
  @BeanProperty var cover: String = _
  @BeanProperty var letter: String = _
  @BeanProperty var genreId: Integer = _
  @BeanProperty var imageId: Integer = _
  @BeanProperty var reviewsIds: Array[Integer] = _
  @BeanProperty var wishListsIds: Array[Integer] = _
  @BeanProperty var covers: Array[String] = _
  @BeanProperty var letters: Array[String] = _

  letters = Array("Han","Latin","Cyrillic")
  covers = Array("Paperback","Hardcover")

}
