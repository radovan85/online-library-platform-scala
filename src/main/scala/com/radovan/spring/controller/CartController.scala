package com.radovan.spring.controller

import java.lang.Double
import java.util.Optional

import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.exceptions.BookQuantityException
import com.radovan.spring.service.{BookService, CartItemService, CartService, CustomerService, LoyaltyCardService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping, RequestMethod}

import scala.collection.JavaConverters._

@Controller
@RequestMapping(value = Array("/cart"))
class CartController {

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var customerService:CustomerService = _

  @Autowired
  private var userService:UserService = _

  @Autowired
  private var cartItemService:CartItemService = _

  @Autowired
  private var bookService:BookService = _

  @Autowired
  private var loyaltyCardService:LoyaltyCardService = _

  @RequestMapping(value = Array("/addToCart"), method = Array(RequestMethod.POST))
  def addCartItem(@ModelAttribute("cartItem") cartItem: CartItemDto): String = {
    val bookId = cartItem.getBookId
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cart = cartService.getCartByCartId(customer.getCartId)
    val loyaltyCardId = Optional.ofNullable(customer.getLoyaltyCardId)
    val book = bookService.getBookById(bookId)
    val allCartItems = cartItemService.listAllByCartId(cart.getCartId)
    var tempQuantity = 0
    for (tempCartItem <- allCartItems.asScala) {
      tempQuantity = tempQuantity + tempCartItem.getQuantity
    }
    if (tempQuantity + cartItem.getQuantity > 50) {
      val error = new Error("Maximum 50 books allowed")
      throw new BookQuantityException(error)
    }
    if (loyaltyCardId.isPresent) {
      val loyaltyCard = loyaltyCardService.getCardByCardId(loyaltyCardId.get)
      val discount = loyaltyCard.getDiscount
      val existingCartItem = Optional.ofNullable(cartItemService.getCartItemByCartIdAndBookId(cart.getCartId, bookId))
      if (existingCartItem.isPresent) {
        cartItem.setCartItemId(existingCartItem.get.getCartItemId)
        cartItem.setCartId(cart.getCartId)
        cartItem.setQuantity(existingCartItem.get.getQuantity + cartItem.getQuantity)
        if (cartItem.getQuantity > 50.asInstanceOf[Integer]) cartItem.setQuantity(50.asInstanceOf[Integer])
        if (discount == 0.asInstanceOf[Integer]) cartItem.setPrice(book.getPrice * cartItem.getQuantity)
        else {
          var cartPrice = book.getPrice * cartItem.getQuantity
          cartPrice = cartPrice - ((cartPrice * discount) / 100.asInstanceOf[Double])
          cartItem.setPrice(cartPrice)
        }
        cartItemService.addCartItem(cartItem)
        cartService.refreshCartState(cart.getCartId)
      }
      else {
        cartItem.setQuantity(cartItem.getQuantity)
        if (cartItem.getQuantity > 50.asInstanceOf[Integer]) cartItem.setQuantity(50.asInstanceOf[Integer])
        if (discount == 0.asInstanceOf[Integer]) cartItem.setPrice(book.getPrice * cartItem.getQuantity)
        else {
          var cartPrice:Double = book.getPrice * cartItem.getQuantity
          cartPrice = cartPrice - ((cartPrice * discount) / 100.asInstanceOf[Integer])
          cartItem.setPrice(cartPrice)
        }
        cartItem.setCartId(cart.getCartId)
        cartItemService.addCartItem(cartItem)
        cartService.refreshCartState(cart.getCartId)
      }
    }
    else {
      val existingCartItem = Optional.ofNullable(cartItemService.getCartItemByCartIdAndBookId(cart.getCartId, bookId))
      if (existingCartItem.isPresent) {
        cartItem.setCartItemId(existingCartItem.get.getCartItemId)
        cartItem.setCartId(cart.getCartId)
        cartItem.setQuantity(existingCartItem.get.getQuantity + cartItem.getQuantity)
        if (cartItem.getQuantity > 50) cartItem.setQuantity(50)
        cartItem.setPrice(book.getPrice * cartItem.getQuantity)
        cartItemService.addCartItem(cartItem)
        cartService.refreshCartState(cart.getCartId)
      }
      else {
        cartItem.setQuantity(cartItem.getQuantity)
        if (cartItem.getQuantity > 50) cartItem.setQuantity(50)
        cartItem.setCartId(cart.getCartId)
        cartItem.setPrice(book.getPrice * cartItem.getQuantity)
        cartItemService.addCartItem(cartItem)
        cartService.refreshCartState(cart.getCartId)
      }
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/addItemCompleted"))
  def addItemCompleted() = "fragments/itemAdded :: ajaxLoadedContent"

  @RequestMapping(value = Array("/getCart")) def cartDetails(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cart = cartService.getCartByCartId(customer.getCartId)
    val allCartItems = cartItemService.listAllByCartId(customer.getCartId)
    val allBooks = bookService.listAll
    val loyaltyCardId = Optional.ofNullable(customer.getLoyaltyCardId)
    if (loyaltyCardId.isPresent) {
      val loyaltyCard = loyaltyCardService.getCardByCardId(loyaltyCardId.get)
      map.put("discount", loyaltyCard.getDiscount)
    }
    else map.put("discount", 0.asInstanceOf[Integer])
    map.put("allCartItems", allCartItems)
    map.put("allBooks", allBooks)
    map.put("cart", cart)
    "fragments/cart :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteItem/{cartId}/{itemId}"))
  def deleteCartItem(@PathVariable("cartId") cartId: Integer, @PathVariable("itemId") itemId: Integer): String = {
    cartItemService.removeCartItem(cartId, itemId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteAllItems/{cartId}"))
  def deleteAllCartItems(@PathVariable("cartId") cartId: Integer): String = {
    cartItemService.eraseAllCartItems(cartId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/invalidCart"))
  def invalidCart = "fragments/invalidCart :: ajaxLoadedContent"
}

