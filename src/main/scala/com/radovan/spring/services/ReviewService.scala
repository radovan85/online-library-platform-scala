package com.radovan.spring.services

import com.radovan.spring.dto.ReviewDto

trait ReviewService {

  def addReview(review:ReviewDto):ReviewDto

  def getReviewById(reviewId:Integer):ReviewDto

  def listAll:Array[ReviewDto]

  def listAllByBookId(bookId:Integer):Array[ReviewDto]

  def listAllAuthorized:Array[ReviewDto]

  def listAllOnHold:Array[ReviewDto]

  def deleteReview(reviewId:Integer):Unit

  def authorizeReview(reviewId:Integer):Unit
}
