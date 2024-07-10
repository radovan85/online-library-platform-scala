package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderDto
import com.radovan.spring.entities.OrderItemEntity
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.{OrderAddressRepository, OrderItemRepository, OrderRepository}
import com.radovan.spring.services.{CartItemService, CartService, CustomerService, DeliveryAddressService, LoyaltyCardService, OrderService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._

@Service
class OrderServiceImpl extends OrderService {

  private var orderRepository:OrderRepository = _
  private var orderItemRepository:OrderItemRepository = _
  private var cartItemService:CartItemService = _
  private var customerService:CustomerService = _
  private var tempConverter:TempConverter = _
  private var cartService:CartService = _
  private var orderAddressRepository:OrderAddressRepository = _
  private var deliveryAddressService:DeliveryAddressService = _
  private var cardService:LoyaltyCardService = _

  @Autowired
  private def injectAll(orderRepository: OrderRepository,orderItemRepository: OrderItemRepository,cartItemService: CartItemService,
                        customerService: CustomerService,tempConverter: TempConverter,cartService: CartService,
                        orderAddressRepository: OrderAddressRepository,deliveryAddressService: DeliveryAddressService,cardService: LoyaltyCardService):Unit = {
    this.orderRepository = orderRepository
    this.orderItemRepository = orderItemRepository
    this.cartItemService = cartItemService
    this.customerService = customerService
    this.tempConverter = tempConverter
    this.cartService = cartService
    this.orderAddressRepository = orderAddressRepository
    this.deliveryAddressService = deliveryAddressService
    this.cardService = cardService
  }

  @Transactional
  override def addOrder(): OrderDto = {
    val order = new OrderDto
    order.setDiscount(0)
    val customer = customerService.getCurrentCustomer
    val orderedItems = new ArrayBuffer[OrderItemEntity]()
    val cart = cartService.validateCart(customer.getCartId)
    val allCartItems = cartItemService.listAllByCartId(cart.getId)
    val deliveryAddress = deliveryAddressService.getAddressById(customer.getDeliveryAddressId)
    val orderAddress = tempConverter.addressToOrderAddress(deliveryAddress)
    val storedAddress = orderAddressRepository.save(tempConverter.orderAddressDtoToEntity(orderAddress))
    val bookQuantity = cartItemService.getBookQuantity(cart.getId)
    val cardIdOption = Option(customer.getLoyaltyCardId)
    cardIdOption match {
      case Some(cardId) =>
        val card = cardService.getCardById(cardId)
        order.setDiscount(card.getDiscount)
      case None =>
    }

    order.setAddressId(storedAddress.getId)
    order.setCartId(cart.getId)
    order.setOrderPrice(cart.getCartPrice)
    order.setBookQuantity(bookQuantity)
    val orderEntity = tempConverter.orderDtoToEntity(order)
    orderEntity.setCreateTime(tempConverter.getCurrentUTCTimestamp)
    var storedOrder = orderRepository.save(orderEntity)

    allCartItems.foreach(cartItem => {
      val orderItem = tempConverter.cartItemToOrderItem(cartItem)
      orderItem.setOrderId(storedOrder.getId)
      val storedItem = orderItemRepository.save(tempConverter.orderItemDtoToEntity(orderItem))
      orderedItems += storedItem
    })

    storedOrder.getOrderedItems.clear()
    storedOrder.getOrderedItems.addAll(orderedItems.asJava)
    storedOrder = orderRepository.saveAndFlush(storedOrder)

    cartItemService.eraseAllCartItems(cart.getId)
    cartService.refreshCartState(cart.getId)
    tempConverter.orderEntityToDto(storedOrder)
  }

  @Transactional(readOnly = true)
  override def listAll: Array[OrderDto] = {
    val allOrders = orderRepository.findAll().asScala
    allOrders.collect{
      case orderEntity => tempConverter.orderEntityToDto(orderEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getOrderById(orderId: Integer): OrderDto = {
    val orderEntity = orderRepository.findById(orderId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The order has not been found!")))
    tempConverter.orderEntityToDto(orderEntity)
  }

  @Transactional
  override def deleteOrder(orderId: Integer): Unit = {
    getOrderById(orderId)
    orderRepository.deleteById(orderId)
    orderRepository.flush()
  }

  @Transactional(readOnly = true)
  override def listAllByCustomerId(customerId: Integer): Array[OrderDto] = {
    val customer = customerService.getCustomerById(customerId)
    val allOrders = listAll
    allOrders.collect{
      case order if order.getCartId == customer.getCartId => order
    }
  }

  @Transactional(readOnly = true)
  override def calculateOrderTotal(orderId: Integer): Float = {
    orderItemRepository.calculateGrandTotal(orderId).getOrElse(0f)
  }

  @Transactional(readOnly = true)
  override def calculateOrdersValue(cartId: Integer): Float = {
    orderRepository.getOrdersValue(cartId).getOrElse(0f)
  }
}
