package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.ReviewDto
import com.radovan.spring.exceptions.{ExistingInstanceException, InstanceUndefinedException}
import com.radovan.spring.repositories.ReviewRepository
import com.radovan.spring.services.{BookService, CustomerService, ReviewService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class ReviewServiceImpl extends ReviewService {

  private var reviewRepository:ReviewRepository = _
  private var tempConverter:TempConverter = _
  private var customerService:CustomerService = _
  private var bookService:BookService = _

  @Autowired
  private def injectAll(reviewRepository: ReviewRepository,tempConverter: TempConverter,
                        customerService: CustomerService,bookService: BookService):Unit = {
    this.reviewRepository = reviewRepository
    this.tempConverter = tempConverter
    this.customerService = customerService
    this.bookService = bookService
  }

  @Transactional
  override def addReview(review: ReviewDto): ReviewDto = {
    val customer = customerService.getCurrentCustomer
    val reviewOption = reviewRepository.findByCustomerIdAndBookId(customer.getId,review.getBookId)
    reviewOption match {
      case Some(_) => throw new ExistingInstanceException(new Error("You have reviewed this book already!"))
      case None =>
    }

    review.setAuthorId(customer.getId)
    review.setAuthorized(0.asInstanceOf[Short])
    val reviewEntity = tempConverter.reviewDtoToEntity(review)
    reviewEntity.setCreateTime(tempConverter.getCurrentUTCTimestamp)
    val storedReview = reviewRepository.save(reviewEntity)
    tempConverter.reviewEntityToDto(storedReview)
  }

  @Transactional(readOnly = true)
  override def getReviewById(reviewId: Integer): ReviewDto = {
    val reviewEntity = reviewRepository.findById(reviewId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The review has not been found!")))
    tempConverter.reviewEntityToDto(reviewEntity)
  }

  @Transactional(readOnly = true)
  override def listAll: Array[ReviewDto] = {
    val allReviews = reviewRepository.findAll().asScala
    allReviews.collect{
      case reviewEntity => tempConverter.reviewEntityToDto(reviewEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def listAllByBookId(bookId: Integer): Array[ReviewDto] = {
    val allReviews = listAll
    allReviews.collect{
      case review if review.getBookId == bookId => review
    }
  }

  @Transactional(readOnly = true)
  override def listAllAuthorized: Array[ReviewDto] = {
    val allReviews = listAll
    allReviews.collect{
      case review if review.getAuthorized == 1 => review
    }
  }

  @Transactional(readOnly = true)
  override def listAllOnHold: Array[ReviewDto] = {
    val allReviews = listAll
    allReviews.collect {
      case review if review.getAuthorized == 0 => review
    }
  }

  @Transactional
  override def deleteReview(reviewId: Integer): Unit = {
    getReviewById(reviewId)
    reviewRepository.deleteById(reviewId)
    reviewRepository.flush()
  }

  @Transactional
  override def authorizeReview(reviewId: Integer): Unit = {
    val review = getReviewById(reviewId)
    review.setAuthorized(1.asInstanceOf[Short])
    reviewRepository.saveAndFlush(tempConverter.reviewDtoToEntity(review))
    bookService.refreshAvgRating()
  }
}
