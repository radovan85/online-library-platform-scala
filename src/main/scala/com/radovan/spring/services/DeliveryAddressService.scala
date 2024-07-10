package com.radovan.spring.services

import com.radovan.spring.dto.DeliveryAddressDto

trait DeliveryAddressService {

  def getAddressById(addressId:Integer):DeliveryAddressDto

  def createAddress(address:DeliveryAddressDto):DeliveryAddressDto
}
