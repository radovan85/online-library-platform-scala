package com.radovan.spring.controller

import com.radovan.spring.service.{CustomerService, LoyaltyCardService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod}

@Controller
@RequestMapping(Array("/loyaltyCards"))
class LoyaltyCardController {

  @Autowired
  private var loyaltyCardService:LoyaltyCardService = _

  @Autowired
  private var userService:UserService = _

  @Autowired
  private var customerService:CustomerService = _

  @RequestMapping(value = Array("/createCardRequest"), method = Array(RequestMethod.POST))
  def createCardRequest: String = {
    loyaltyCardService.addCardRequest()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/cardRequestSent"))
  def cardRequestSent = "fragments/loyaltyCardRequestSent :: ajaxLoadedContent"

  @RequestMapping(value = Array("/cardInfo"))
  def loyaltyCardInfo(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cardRequest = loyaltyCardService.getRequestByCustomerId(customer.getCustomerId)
    val allCards = loyaltyCardService.listAllLoyaltyCards
    map.put("customer", customer)
    map.put("cardRequest", cardRequest)
    map.put("allCards", allCards)
    "fragments/loyaltyCardDetails :: ajaxLoadedContent"
  }
}

