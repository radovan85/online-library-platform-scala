package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.exceptions.{BookQuantityException, InstanceUndefinedException}
import com.radovan.spring.repositories.CartItemRepository
import com.radovan.spring.services.{BookService, CartItemService, CartService, CustomerService, LoyaltyCardService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class CartItemServiceImpl extends CartItemService {

  private var cartItemRepository: CartItemRepository = _
  private var tempConverter: TempConverter = _
  private var cartService: CartService = _
  private var customerService: CustomerService = _
  private var cardService: LoyaltyCardService = _
  private var bookService: BookService = _

  @Autowired
  private def injectAll(cartItemRepository: CartItemRepository, tempConverter: TempConverter, cartService: CartService,
                        customerService: CustomerService, cardService: LoyaltyCardService, bookService: BookService): Unit = {
    this.cartItemRepository = cartItemRepository
    this.tempConverter = tempConverter
    this.cartService = cartService
    this.customerService = customerService
    this.cardService = cardService
    this.bookService = bookService
  }

  @Transactional
  override def addCartItem(item: CartItemDto): CartItemDto = {
    var returnValue:CartItemDto = null
    val bookId = item.getBookId
    val customer = customerService.getCurrentCustomer
    val cart = cartService.getCartById(customer.getCartId)
    val loyaltyCardIdOption = Option(customer.getLoyaltyCardId)
    val book = bookService.getBookById(bookId)
    val allCartItems = listAllByCartId(cart.getId)
    var tempQuantity:Integer = 0
    allCartItems.foreach(tempItem => {
      tempQuantity = tempQuantity + tempItem.getQuantity
    })

    if(tempQuantity + item.getQuantity > 50){
      throw new BookQuantityException(new Error("Maximum 50 books allowed!"))
    }

    loyaltyCardIdOption match {
      case Some(cardId) =>
        val loyaltyCard = cardService.getCardById(cardId)
        val discount = loyaltyCard.getDiscount
        val existingCartItemOption = cartItemRepository.findByCartIdAndBookId(cart.getId,bookId)
        existingCartItemOption match {
          case Some(existingItem) =>
            item.setId(existingItem.getId)
            item.setCartId(cart.getId)
            item.setQuantity(existingItem.getQuantity + item.getQuantity)
            if(item.getQuantity > 50){
              item.setQuantity(50)
            }

            if(discount==0){
              item.setPrice(book.getPrice * item.getQuantity)
            }else {
              var cartPrice = book.getPrice * item.getQuantity
              cartPrice = cartPrice - ((cartPrice * discount)/100)
              item.setPrice(cartPrice)
            }

            val storedItem = cartItemRepository.save(tempConverter.cartItemDtoToEntity(item))
            returnValue = tempConverter.cartItemEntityToDto(storedItem)
            cartService.refreshCartState(cart.getId)
          case None =>
            if(item.getQuantity > 50){
              item.setQuantity(50)
            }

            if(discount == 0){
              item.setPrice(book.getPrice * item.getQuantity)
            }else {
              var cartPrice = book.getPrice * item.getQuantity
              cartPrice = cartPrice - ((cartPrice * discount) / 100)
              item.setPrice(cartPrice)
            }

            item.setCartId(cart.getId)
            val storedItem = cartItemRepository.save(tempConverter.cartItemDtoToEntity(item))
            returnValue = tempConverter.cartItemEntityToDto(storedItem)
            cartService.refreshCartState(cart.getId)
        }
      case None =>
        val existingCartItemOption = cartItemRepository.findByCartIdAndBookId(cart.getId, book.getId)
        existingCartItemOption match {
          case Some(existingItem) =>
            item.setId(existingItem.getId)
            item.setCartId(cart.getId)
            item.setQuantity(existingItem.getQuantity + item.getQuantity)
            if (item.getQuantity > 50) {
              item.setQuantity(50)
            }

            item.setPrice(book.getPrice * item.getQuantity)
            val storedItem = cartItemRepository.save(tempConverter.cartItemDtoToEntity(item))
            returnValue = tempConverter.cartItemEntityToDto(storedItem)
            cartService.refreshCartState(cart.getId)
          case None =>
            if (item.getQuantity > 50) {
              item.setQuantity(50)
            }
            item.setCartId(cart.getId)
            item.setPrice(book.getPrice * item.getQuantity)
            val storedItem = cartItemRepository.save(tempConverter.cartItemDtoToEntity(item))
            returnValue = tempConverter.cartItemEntityToDto(storedItem)
            cartService.refreshCartState(cart.getId)
        }
    }

    returnValue
  }

  @Transactional
  override def removeCartItem(itemId: Integer): Unit = {
    val item = getItemById(itemId)
    cartItemRepository.removeCartItem(itemId)
    cartItemRepository.flush()
    cartService.refreshCartState(item.getCartId)
  }

  @Transactional
  override def eraseAllCartItems(cartId: Integer): Unit = {
    val allCartItems = listAllByCartId(cartId)
    allCartItems.foreach(cartItem => removeCartItem(cartItem.getId))
  }

  @Transactional(readOnly = true)
  override def listAllByCartId(cartId: Integer): Array[CartItemDto] = {
    val allCartItems = cartItemRepository.findAll().asScala
    allCartItems.collect{
      case itemEntity if itemEntity.getCart.getId == cartId => tempConverter.cartItemEntityToDto(itemEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getItemById(itemId: Integer): CartItemDto = {
    val itemEntity = cartItemRepository.findById(itemId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The item has not been found!")))
    tempConverter.cartItemEntityToDto(itemEntity)
  }

  @Transactional(readOnly = true)
  override def getCartItemByCartIdAndBookId(cartId: Integer, bookId: Integer): CartItemDto = {
    val itemOption = cartItemRepository.findByCartIdAndBookId(cartId, bookId)
    itemOption match {
      case Some(itemEntity) => tempConverter.cartItemEntityToDto(itemEntity)
      case None => throw new InstanceUndefinedException(new Error("The item has not been found!"))
    }
  }

  @Transactional(readOnly = true)
  override def getBookQuantity(cartId: Integer): Integer = {
    val allCartItems = listAllByCartId(cartId)
    allCartItems.map(_.getQuantity.intValue()).sum
  }

  @Transactional
  override def eraseAllByBookId(bookId: Integer): Unit = {
    cartItemRepository.removeAllByBookId(bookId)
    cartItemRepository.flush()
    cartService.refreshAllCarts()
  }

  @Transactional(readOnly = true)
  override def hasDiscount(itemId: Integer): Boolean = {
    var returnValue = false
    val cartItem = getItemById(itemId)
    val cart = cartService.getCartById(cartItem.getCartId)
    val customer = customerService.getCustomerById(cart.getCustomerId)
    val cardIdOption = Option(customer.getLoyaltyCardId)
    cardIdOption match {
      case Some(cardId) =>
        val card = cardService.getCardById(cardId)
        val discount = card.getDiscount
        if(discount > 0){
          returnValue = true
        }
      case None =>
    }

    returnValue
  }




}
