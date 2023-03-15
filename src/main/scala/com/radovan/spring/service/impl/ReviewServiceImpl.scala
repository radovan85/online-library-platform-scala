package com.radovan.spring.service.impl

import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}
import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.ReviewDto
import com.radovan.spring.entity.{CustomerEntity, ReviewEntity, UserEntity}
import com.radovan.spring.exceptions.ReviewAlreadyExistsException
import com.radovan.spring.repository.{CustomerRepository, ReviewRepository}
import com.radovan.spring.service.{BookService, ReviewService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class ReviewServiceImpl extends ReviewService {

  @Autowired
  private var reviewRepository: ReviewRepository = _

  @Autowired
  private var tempConverter: TempConverter = _

  @Autowired
  private var customerRepository: CustomerRepository = _

  @Autowired
  private var bookService: BookService = _

  override def addReview(review: ReviewDto): ReviewDto = {
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    if (customerEntity.isPresent) review.setAuthorId(customerEntity.get.getCustomerId)
    val existingReview: Optional[ReviewEntity] = Optional.ofNullable(reviewRepository.findByCustomerIdAndBookId(customerEntity.get.getCustomerId, review.getBookId))
    if (existingReview.isPresent) {
      val error: Error = new Error("Existing Review")
      throw new ReviewAlreadyExistsException(error)
    }
    review.setAuthorized(0.toByte)
    val reviewEntity: ReviewEntity = tempConverter.reviewDtoToEntity(review)
    val currentTime: ZonedDateTime = LocalDateTime.now.atZone(ZoneId.of("Europe/Belgrade"))
    val createdAt: Timestamp = new Timestamp(currentTime.toInstant.getEpochSecond * 1000L)
    reviewEntity.setCreatedAt(createdAt)
    val storedReview: ReviewEntity = reviewRepository.save(reviewEntity)
    val returnValue: ReviewDto = tempConverter.reviewEntityToDto(storedReview)
    returnValue
  }

  override def getReviewById(reviewId: Integer): ReviewDto = {
    var returnValue: ReviewDto = null
    val reviewEntity: Optional[ReviewEntity] = Optional.ofNullable(reviewRepository.getById(reviewId))
    if (reviewEntity.isPresent) returnValue = tempConverter.reviewEntityToDto(reviewEntity.get)
    returnValue
  }

  override def listAll: util.List[ReviewDto] = {
    val returnValue: util.List[ReviewDto] = new util.ArrayList[ReviewDto]
    val allReviews: Optional[util.List[ReviewEntity]] = Optional.ofNullable(reviewRepository.findAll)
    if (!allReviews.isEmpty) {
      for (review <- allReviews.get.asScala) {
        val reviewDto: ReviewDto = tempConverter.reviewEntityToDto(review)
        returnValue.add(reviewDto)
      }
    }
    returnValue
  }

  override def listAllByBookId(bookId: Integer): util.List[ReviewDto] = {
    val returnValue: util.List[ReviewDto] = new util.ArrayList[ReviewDto]
    val allReviews: Optional[util.List[ReviewEntity]] = Optional.ofNullable(reviewRepository.findAllAuthorizedByBookId(bookId))
    if (!allReviews.isEmpty) {
      for (review <- allReviews.get.asScala) {
        val reviewDto: ReviewDto = tempConverter.reviewEntityToDto(review)
        returnValue.add(reviewDto)
      }
    }
    returnValue
  }

  override def deleteReview(reviewId: Integer): Unit = {
    reviewRepository.deleteById(reviewId)
    reviewRepository.flush()
  }

  override def listAllAuthorized: util.List[ReviewDto] = {
    val returnValue: util.List[ReviewDto] = new util.ArrayList[ReviewDto]
    val allReviews: Optional[util.List[ReviewEntity]] = Optional.ofNullable(reviewRepository.findAllAuthorized)
    if (!allReviews.isEmpty) {
      for (review <- allReviews.get.asScala) {
        val reviewDto: ReviewDto = tempConverter.reviewEntityToDto(review)
        returnValue.add(reviewDto)
      }
    }
    returnValue
  }

  override def listAllOnHold: util.List[ReviewDto] = {
    val returnValue: util.List[ReviewDto] = new util.ArrayList[ReviewDto]
    val allReviews: Optional[util.List[ReviewEntity]] = Optional.ofNullable(reviewRepository.findAllOnHold)
    if (!allReviews.isEmpty) {
      for (review <- allReviews.get.asScala) {
        val reviewDto: ReviewDto = tempConverter.reviewEntityToDto(review)
        returnValue.add(reviewDto)
      }
    }
    returnValue
  }

  override def authorizeReview(reviewId: Integer): Unit = {
    val reviewEntity: Optional[ReviewEntity] = Optional.ofNullable(reviewRepository.getById(reviewId))
    if (reviewEntity.isPresent) {
      val reviewValue: ReviewEntity = reviewEntity.get
      reviewValue.setAuthorized(1.toByte)
      reviewRepository.saveAndFlush(reviewValue)
      bookService.refreshAvgRating()
    }
  }

  override def deleteAllByCustomerId(customerId: Integer): Unit = {
    reviewRepository.deleteAllByCustomerId(customerId)
    reviewRepository.flush()
    bookService.refreshAvgRating()
  }
}

