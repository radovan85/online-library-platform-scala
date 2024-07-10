package com.radovan.spring.controllers

import com.radovan.spring.dto.DeliveryAddressDto
import com.radovan.spring.services.DeliveryAddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

@Controller
@RequestMapping(value = Array("/addresses"))
class DeliveryAddressController {

  @Autowired
  private var addressService:DeliveryAddressService = _

  @GetMapping(value = Array("/updateAddress/{addressId}"))
  def renderAddressForm(@PathVariable("addressId") addressId: Integer, map: ModelMap): String = {
    val address = new DeliveryAddressDto
    val currentAddress = addressService.getAddressById(addressId)
    map.put("address", address)
    map.put("currentAddress", currentAddress)
    "fragments/updateAddressForm :: fragmentContent"
  }

  @PostMapping(value = Array("/createAddress"))
  def createAddress(@ModelAttribute("address") address: DeliveryAddressDto): String = {
    addressService.createAddress(address)
    "fragments/homePage :: fragmentContent"
  }
}
