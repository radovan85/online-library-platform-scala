package com.radovan.spring.controllers

import com.radovan.spring.dto.OrderDto
import com.radovan.spring.services.{BookService, CartItemService, CartService, CustomerService, DeliveryAddressService, LoyaltyCardService, OrderService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, RequestMapping}

@Controller
@RequestMapping(value = Array("/orders"))
class OrderController {

  private var customerService:CustomerService = _
  private var cartService:CartService = _
  private var cartItemService:CartItemService = _
  private var addressService:DeliveryAddressService = _
  private var bookService:BookService = _
  private var loyaltyCardService:LoyaltyCardService = _
  private var orderService:OrderService = _

  @Autowired
  private def injectAll(customerService: CustomerService,cartService: CartService,cartItemService: CartItemService,
                        addressService: DeliveryAddressService,bookService: BookService,
                        loyaltyCardService: LoyaltyCardService,orderService: OrderService):Unit = {

    this.customerService = customerService
    this.cartService = cartService
    this.cartItemService = cartItemService
    this.addressService = addressService
    this.bookService = bookService
    this.loyaltyCardService = loyaltyCardService
    this.orderService = orderService
  }

  @GetMapping(value = Array("/confirmOrder/{cartId}"))
  def confirmOrder(@PathVariable("cartId") cartId: Integer, map: ModelMap): String = {
    val order:OrderDto = new OrderDto
    cartService.validateCart(cartId)
    val customer = customerService.getCustomerByCartId(cartId)
    val allCartItems = cartItemService.listAllByCartId(cartId)
    val address = addressService.getAddressById(customer.getDeliveryAddressId)
    val cart = cartService.getCartById(customer.getCartId)
    val allBooks = bookService.listAll
    val bookQuantity = cartItemService.getBookQuantity(cartId)

    val cardIdOption = Option(customer.getLoyaltyCardId)
    cardIdOption match {
      case Some(cardId) =>
        val card = loyaltyCardService.getCardById(cardId)
        map.put("discount", card.getDiscount)
      case None => map.put("discount", 0.asInstanceOf[Integer])
    }

    map.put("order", order)
    map.put("customer", customer)
    map.put("allCartItems", allCartItems)
    map.put("address", address)
    map.put("cart", cart)
    map.put("allBooks", allBooks)
    map.put("bookQuantity", bookQuantity)
    "fragments/orderConfirmation :: fragmentContent"
  }

  @GetMapping(value = Array("/processOrder"))
  def processOrder(map: ModelMap): String = {
    var pointsCollected:Integer = null
    var pointsSpent:Integer = 0
    var cardPoints:Integer = null
    val order = orderService.addOrder()
    val orderPrice = order.getOrderPrice
    val cart = cartService.getCartById(order.getCartId)
    val customer = customerService.getCustomerById(cart.getCustomerId)

    val loyaltyCardIdOption = Option(customer.getLoyaltyCardId)
    loyaltyCardIdOption match {
      case Some(cardId) =>
        val loyaltyCard = loyaltyCardService.getCardById(cardId)
        cardPoints = loyaltyCard.getPoints
        pointsCollected = (orderPrice / 10).toInt
        if(pointsCollected >= 100) pointsCollected = 100
        if(cardPoints >= 100){
          pointsSpent = 100
          cardPoints = cardPoints - pointsSpent
        }
        cardPoints = cardPoints + pointsCollected
        loyaltyCard.setPoints(cardPoints)
        if (loyaltyCard.getPoints >= 100) loyaltyCard.setDiscount(35)
        else loyaltyCard.setDiscount(0)
        loyaltyCardService.updateLoyaltyCard(cardId, loyaltyCard)
      case None =>
    }

    map.put("discount", order.getDiscount)
    map.put("pointsCollected", pointsCollected)
    map.put("pointsSpent", pointsSpent)
    map.put("cardPoints", cardPoints)
    "fragments/orderCompleted :: fragmentContent"
  }
}
