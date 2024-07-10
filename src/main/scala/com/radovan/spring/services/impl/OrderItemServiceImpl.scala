package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderItemDto
import com.radovan.spring.repositories.OrderItemRepository
import com.radovan.spring.services.OrderItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class OrderItemServiceImpl extends OrderItemService {

  private var itemRepository: OrderItemRepository = _
  private var tempConverter: TempConverter = _

  @Autowired
  private def injectAll(itemRepository: OrderItemRepository, tempConverter: TempConverter): Unit = {
    this.itemRepository = itemRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def listAllByOrderId(orderId: Integer): Array[OrderItemDto] = {
    def allOrderedItems = itemRepository.findAll().asScala
    allOrderedItems.collect {
      case orderItem if orderItem.getOrder.getId == orderId => tempConverter.orderItemEntityToDto(orderItem)
    }
  }.toArray

  @Transactional
  override def eraseAllByOrderId(orderId: Integer): Unit = {
    val allOrderedItems = listAllByOrderId(orderId)
    allOrderedItems.foreach(item => {
      itemRepository.deleteById(item.getId)
      itemRepository.flush()
    })
  }
}
