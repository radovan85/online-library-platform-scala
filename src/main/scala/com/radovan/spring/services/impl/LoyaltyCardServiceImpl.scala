package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.{LoyaltyCardDto, LoyaltyCardRequestDto}
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.{LoyaltyCardRepository, LoyaltyCardRequestRepository}
import com.radovan.spring.services.{CustomerService, LoyaltyCardService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class LoyaltyCardServiceImpl extends LoyaltyCardService {

  private var requestRepository:LoyaltyCardRequestRepository = _
  private var cardRepository:LoyaltyCardRepository = _
  private var tempConverter:TempConverter = _
  private var customerService:CustomerService = _

  @Autowired
  private def injectAll(requestRepository: LoyaltyCardRequestRepository,cardRepository: LoyaltyCardRepository,
                        tempConverter: TempConverter,customerService: CustomerService):Unit = {
    this.requestRepository = requestRepository
    this.cardRepository = cardRepository
    this.tempConverter = tempConverter
    this.customerService = customerService
  }

  @Transactional(readOnly = true)
  override def listAllCardRequests: Array[LoyaltyCardRequestDto] = {
    val allCardRequests = requestRepository.findAll().asScala
    allCardRequests.collect{
      case cardRequest => tempConverter.cardRequestEntityToDto(cardRequest)
    }.toArray
  }

  @Transactional
  override def authorizeRequest(cardRequestId: Integer): Unit = {
    val cardRequestEntity = requestRepository.findById(cardRequestId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("Card request has not been found!")))
    val card = new LoyaltyCardDto
    card.setDiscount(0)
    card.setPoints(0)
    val customer = customerService.getCustomerById(cardRequestEntity.getCustomer.getId)
    card.setCustomerId(customer.getId)
    val storedCard = cardRepository.save(tempConverter.loyaltyCardDtoToEntity(card))
    customer.setLoyaltyCardId(storedCard.getId)
    customerService.updateCustomer(customer.getId, customer)
    requestRepository.deleteById(cardRequestId)
    requestRepository.flush()
  }

  @Transactional
  override def rejectRequest(cardRequestId: Integer): Unit = {
    requestRepository.deleteById(cardRequestId)
    requestRepository.flush()
  }

  @Transactional
  override def addCardRequest(): LoyaltyCardRequestDto = {
    val customer = customerService.getCurrentCustomer
    val cardRequest = new LoyaltyCardRequestDto
    cardRequest.setCustomerId(customer.getId)
    val storedRequest = requestRepository.save(tempConverter.cardRequestDtoToEntity(cardRequest))
    tempConverter.cardRequestEntityToDto(storedRequest)
  }

  @Transactional(readOnly = true)
  override def getRequestByCustomerId(customerId: Integer): LoyaltyCardRequestDto = {
    val cardRequestEntity = requestRepository.findByCustomerId(customerId)
      .getOrElse(throw new InstanceUndefinedException(new Error("Card request has not been found!")))
    tempConverter.cardRequestEntityToDto(cardRequestEntity)
  }

  @Transactional(readOnly = true)
  override def listAllLoyaltyCards: Array[LoyaltyCardDto] = {
    val allCards = cardRepository.findAll().asScala
    allCards.collect{
      case cardEntity => tempConverter.loyaltyCardEntityToDto(cardEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getCardById(cardId: Integer): LoyaltyCardDto = {
    val cardEntity = cardRepository.findById(cardId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The card has not been found!")))
    tempConverter.loyaltyCardEntityToDto(cardEntity)
  }

  @Transactional
  override def updateLoyaltyCard(cardId: Integer, card: LoyaltyCardDto): LoyaltyCardDto = {
    getCardById(cardId)
    card.setId(cardId)
    val updatedCard = cardRepository.saveAndFlush(tempConverter.loyaltyCardDtoToEntity(card))
    tempConverter.loyaltyCardEntityToDto(updatedCard)
  }
}
