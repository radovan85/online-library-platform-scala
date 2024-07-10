package com.radovan.spring.controllers

import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.services.{BookService, CartItemService, CartService, CustomerService, LoyaltyCardService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

@Controller
@RequestMapping(value = Array("/cart"))
class CartController {

  private var cartService:CartService = _
  private var customerService:CustomerService = _
  private var cartItemService:CartItemService = _
  private var bookService:BookService = _
  private var loyaltyCardService:LoyaltyCardService = _

  @Autowired
  private def injectAll(cartService: CartService,customerService: CustomerService,cartItemService: CartItemService,
                        bookService: BookService,loyaltyCardService: LoyaltyCardService):Unit = {

    this.cartService = cartService
    this.customerService = customerService
    this.cartItemService = cartItemService
    this.bookService = bookService
    this.loyaltyCardService = loyaltyCardService
  }

  @PostMapping(value = Array("/addToCart"))
  def addCartItem(@ModelAttribute("cartItem") cartItem: CartItemDto): String = {
    cartItemService.addCartItem(cartItem)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/addItemCompleted"))
  def addItemCompleted = "fragments/itemAdded :: fragmentContent"

  @GetMapping(value = Array("/getCart"))
  def cartDetails(map: ModelMap): String = {
    val customer = customerService.getCurrentCustomer
    val cart = cartService.getCartById(customer.getCartId)
    val allCartItems = cartItemService.listAllByCartId(customer.getCartId)
    val allBooks = bookService.listAll
    val loyaltyCardIdOption = Option(customer.getLoyaltyCardId)
    loyaltyCardIdOption match {
      case Some(cardId) =>
        val loyaltyCard = loyaltyCardService.getCardById(cardId)
        map.put("discount", loyaltyCard.getDiscount)
      case None =>
        map.put("discount", 0.asInstanceOf[Integer])
    }

    map.put("allCartItems", allCartItems)
    map.put("allBooks", allBooks)
    map.put("cart", cart)
    "fragments/cart :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteItem/{itemId}"))
  def deleteCartItem(@PathVariable("itemId") itemId: Integer): String = {
    cartItemService.removeCartItem(itemId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteAllItems/{cartId}"))
  def deleteAllCartItems(@PathVariable("cartId") cartId: Integer): String = {
    cartItemService.eraseAllCartItems(cartId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/invalidCart"))
  def invalidCart = "fragments/invalidCart :: fragmentContent"
}
