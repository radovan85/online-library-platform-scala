package com.radovan.spring.controllers

import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.services.{CustomerService, DeliveryAddressService, PersistenceLoginService, UserService}
import com.radovan.spring.utils.RegistrationForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PostMapping}

import java.security.Principal

@Controller
class MainController {

  private var customerService:CustomerService = _
  private var userService:UserService = _
  private var addressService:DeliveryAddressService = _
  private var persistenceLoginService:PersistenceLoginService = _

  @Autowired
  private def injectAll(customerService: CustomerService,userService: UserService,
                        addressService: DeliveryAddressService, persistenceLoginService: PersistenceLoginService):Unit = {

    this.customerService = customerService
    this.userService = userService
    this.addressService = addressService
    this.persistenceLoginService = persistenceLoginService
  }

  @GetMapping(value = Array("/"))
  def sayIndex = "index"

  @GetMapping(value = Array("/home"))
  def home = "fragments/homePage :: fragmentContent"

  @GetMapping(value = Array("/login"))
  def login = "fragments/login :: fragmentContent"

  @GetMapping(value = Array("/userRegistration"))
  def register(map: ModelMap): String = {
    map.put("tempForm", new RegistrationForm)
    "fragments/registration :: fragmentContent"
  }

  @PostMapping(value = Array("/userRegistration"))
  def storeUser(@ModelAttribute("tempForm") form: RegistrationForm): String = {
    customerService.storeCustomer(form)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/registerComplete"))
  def registrationCompl = "fragments/registration_completed :: fragmentContent"

  @GetMapping(value = Array("/registerFail"))
  def registrationFail = "fragments/registration_failed :: fragmentContent"

  @PostMapping(value = Array("/loggedout"))
  def logout: String = {
    val context = SecurityContextHolder.getContext
    context.setAuthentication(null)
    SecurityContextHolder.clearContext()
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/loginErrorPage"))
  def logError(map: ModelMap): String = {
    map.put("alert", "Invalid username or password!")
    "fragments/login :: fragmentContent"
  }

  @PostMapping(value = Array("/loginPassConfirm"))
  def confirmLoginPass(principal: Principal): String = {
    val authPrincipalOption = Option(principal)
    authPrincipalOption match {
      case Some(_) =>
      case None => throw new InstanceUndefinedException(new Error("Invalid user!"))
    }

    persistenceLoginService.addPersistenceLogin
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/aboutUs"))
  def aboutPage = "fragments/about :: fragmentContent"

  @PreAuthorize(value = "hasAuthority('ROLE_USER')")
  @GetMapping(value = Array("/accountInfo"))
  def userAccountInfo(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val address = addressService.getAddressById(customer.getDeliveryAddressId)
    map.put("authUser", authUser)
    map.put("address", address)
    "fragments/accountDetails :: fragmentContent"
  }
}
