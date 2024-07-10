package com.radovan.spring.services

import com.radovan.spring.dto.CustomerDto
import com.radovan.spring.utils.RegistrationForm

trait CustomerService {

  def storeCustomer(form:RegistrationForm):CustomerDto

  def listAll:Array[CustomerDto]

  def getCustomerById(customerId:Integer):CustomerDto

  def getCustomerByUserId(userId:Integer):CustomerDto

  def getCustomerByCartId(cartId:Integer):CustomerDto

  def updateCustomer(customerId:Integer,customer:CustomerDto):CustomerDto

  def addCustomer(customer: CustomerDto):CustomerDto

  def deleteCustomer(customerId:Integer):Unit

  def getCurrentCustomer:CustomerDto

}
