package com.radovan.spring.controllers

import com.radovan.spring.dto.LoyaltyCardRequestDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.services.{CustomerService, LoyaltyCardService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestMapping}

@Controller
@RequestMapping(value=Array("/loyaltyCards"))
class LoyaltyCardController {

  private var loyaltyCardService:LoyaltyCardService = _
  private var customerService:CustomerService = _

  @Autowired
  private def injectAll(loyaltyCardService: LoyaltyCardService,customerService: CustomerService):Unit = {
    this.loyaltyCardService = loyaltyCardService
    this.customerService = customerService
  }

  @PostMapping(value = Array("/createCardRequest"))
  def createCardRequest: String = {
    loyaltyCardService.addCardRequest
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/cardRequestSent"))
  def cardRequestSent = "fragments/loyaltyCardRequestSent :: fragmentContent"

  @GetMapping(value = Array("/cardInfo"))
  def loyaltyCardInfo(map: ModelMap): String = {
    val customer = customerService.getCurrentCustomer
    var cardRequest:LoyaltyCardRequestDto = null
    try cardRequest = loyaltyCardService.getRequestByCustomerId(customer.getId)
    catch {
      case exc: InstanceUndefinedException =>

    }
    val allCards = loyaltyCardService.listAllLoyaltyCards
    map.put("customer", customer)
    map.put("cardRequest", cardRequest)
    map.put("allCards", allCards)
    "fragments/loyaltyCardDetails :: fragmentContent"
  }
}
