package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.DeliveryAddressDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.DeliveryAddressRepository
import com.radovan.spring.services.DeliveryAddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeliveryAddressServiceImpl extends DeliveryAddressService {

  private var addressRepository:DeliveryAddressRepository = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def injectAll(addressRepository: DeliveryAddressRepository,tempConverter: TempConverter):Unit = {
    this.addressRepository = addressRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def getAddressById(addressId: Integer): DeliveryAddressDto = {
    val addressEntity = addressRepository.findById(addressId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The delivery address has not been found!")))
    tempConverter.deliveryAddressEntityToDto(addressEntity)
  }

  @Transactional
  override def createAddress(address: DeliveryAddressDto): DeliveryAddressDto = {
    val storedAddress = addressRepository.save(tempConverter.deliveryAddressDtoToEntity(address))
    tempConverter.deliveryAddressEntityToDto(storedAddress)
  }
}
