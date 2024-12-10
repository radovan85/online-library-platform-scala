package com.radovan.spring.controllers

import com.radovan.spring.dto.ReviewDto
import com.radovan.spring.services.{BookService, ReviewService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

import scala.collection.mutable.ArrayBuffer

@Controller
@RequestMapping(value = Array("/reviews"))
class ReviewController {

  private var reviewService:ReviewService = _
  private var bookService:BookService = _

  @Autowired
  private def injectAll(reviewService: ReviewService,bookService: BookService):Unit = {
    this.reviewService = reviewService
    this.bookService = bookService
  }

  @GetMapping(Array("/createReview/{bookId}"))
  def renderReviewForm(@PathVariable("bookId") bookId: Integer, map: ModelMap): String = {
    val review = new ReviewDto
    val book = bookService.getBookById(bookId)
    val ratings = new ArrayBuffer[Integer]()
    for (x <- 1 to 5) {
      ratings += x
    }
    map.put("review", review)
    map.put("book", book)
    map.put("ratings", ratings.toArray)
    "fragments/reviewForm :: fragmentContent"
  }

  @PostMapping(Array("/createReview"))
  def createReview(@ModelAttribute("review") review: ReviewDto): String = {
    reviewService.addReview(review)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(Array("/reviewSentCompleted"))
  def reviewCompleted(): String = {
    "fragments/reviewSent :: fragmentContent"
  }
}
