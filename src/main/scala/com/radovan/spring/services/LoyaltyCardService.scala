package com.radovan.spring.services

import com.radovan.spring.dto.{LoyaltyCardDto, LoyaltyCardRequestDto}

trait LoyaltyCardService {

  def listAllCardRequests:Array[LoyaltyCardRequestDto]

  def authorizeRequest(cardRequestId:Integer):Unit

  def rejectRequest(cardRequestId:Integer):Unit

  def addCardRequest():LoyaltyCardRequestDto

  def getRequestByCustomerId(customerId:Integer):LoyaltyCardRequestDto

  def listAllLoyaltyCards:Array[LoyaltyCardDto]

  def getCardById(cardId:Integer):LoyaltyCardDto

  def updateLoyaltyCard(cardId:Integer,card:LoyaltyCardDto):LoyaltyCardDto
}
