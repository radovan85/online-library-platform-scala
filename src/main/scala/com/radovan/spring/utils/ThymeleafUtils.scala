


package com.radovan.spring.utils

import com.radovan.spring.dto.{BookDto, BookImageDto}
import com.radovan.spring.services.{BookImageService, BookService}

import java.util.Base64
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class ThymeleafUtils {

  private var imageService: BookImageService = _
  private var bookService: BookService = _

  @Autowired
  def injectAll(imageService: BookImageService,bookService: BookService):Unit = {
    this.imageService = imageService
    this.bookService = bookService
  }

  def displayImage(bookId: Integer): String = {
    val book: BookDto = bookService.getBookById(bookId)
    val imageIdOption = Option(book.getImageId)
    imageIdOption match {
      case Some(imageId) =>
        val image: BookImageDto = imageService.getImageById(imageId)
        Base64.getEncoder.encodeToString(image.getData)
      case None => null
    }
  }
}
