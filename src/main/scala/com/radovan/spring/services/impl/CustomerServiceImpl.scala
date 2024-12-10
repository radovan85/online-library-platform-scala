package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CustomerDto
import com.radovan.spring.entities.{CartEntity, RoleEntity, UserEntity, WishListEntity}
import com.radovan.spring.exceptions.{ExistingInstanceException, InstanceUndefinedException}
import com.radovan.spring.repositories.{CartRepository, CustomerRepository, DeliveryAddressRepository, RoleRepository, UserRepository, WishListRepository}
import com.radovan.spring.services.{CustomerService, OrderService, UserService}
import com.radovan.spring.utils.RegistrationForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters._

@Service
class CustomerServiceImpl extends CustomerService {

  private var customerRepository:CustomerRepository = _
  private var tempConverter:TempConverter = _
  private var cartRepository:CartRepository = _
  private var userRepository:UserRepository = _
  private var roleRepository:RoleRepository = _
  private var wishListRepository:WishListRepository = _
  private var addressRepository:DeliveryAddressRepository = _
  private var passwordEncoder:BCryptPasswordEncoder = _
  private var userService:UserService = _
  private var orderService:OrderService = _

  @Autowired
  private def injectAll(customerRepository: CustomerRepository,tempConverter: TempConverter,cartRepository: CartRepository,
                        userRepository: UserRepository,roleRepository: RoleRepository,wishListRepository: WishListRepository,
                        addressRepository: DeliveryAddressRepository,passwordEncoder: BCryptPasswordEncoder,userService: UserService,
                        orderService: OrderService):Unit = {
    this.customerRepository = customerRepository
    this.tempConverter = tempConverter
    this.cartRepository = cartRepository
    this.userRepository = userRepository
    this.roleRepository = roleRepository
    this.wishListRepository = wishListRepository
    this.addressRepository = addressRepository
    this.passwordEncoder = passwordEncoder
    this.userService = userService
    this.orderService = orderService
  }

  @Transactional
  override def storeCustomer(form: RegistrationForm): CustomerDto = {
    val user = form.getUser
    val testUserOption = userRepository.findByEmail(user.getEmail)
    testUserOption match {
      case Some(_) => throw new ExistingInstanceException(new Error("This email already exists!"))
      case None =>
    }

    val roleEntity = roleRepository.findByRole("ROLE_USER")
      .getOrElse(roleRepository.save(new RoleEntity("ROLE_USER")))
    user.setPassword(passwordEncoder.encode(user.getPassword))
    user.setEnabled(1.asInstanceOf[Short])
    val roles = new ArrayBuffer[RoleEntity]()
    roles += roleEntity
    val userEntity = tempConverter.userDtoToEntity(user)
    userEntity.setRoles(roles.asJava)
    val storedUser = userRepository.save(userEntity)
    val usersOption = Option(roleEntity.getUsers.asScala)
    val users = new ArrayBuffer[UserEntity]()
    usersOption match {
      case Some(roleUsers) =>
        roleUsers.foreach(roleUser => {
          users += roleUser
        })
      case None =>
    }

    users += storedUser
    roleEntity.setUsers(users.asJava)
    roleRepository.saveAndFlush(roleEntity)

    val cartEntity = new CartEntity
    cartEntity.setCartPrice(0f)
    val storedCart = cartRepository.save(cartEntity)

    val wishListEntity = new WishListEntity
    val storedWishList = wishListRepository.save(wishListEntity)

    val deliveryAddress = form.getAddress
    val storedAddress = addressRepository.save(tempConverter.deliveryAddressDtoToEntity(deliveryAddress))

    val customer = form.getCustomer
    customer.setUserId(storedUser.getId)
    customer.setCartId(storedCart.getId)
    customer.setWishListId(storedWishList.getId)
    customer.setDeliveryAddressId(storedAddress.getId)

    val customerEntity = tempConverter.customerDtoToEntity(customer)
    customerEntity.setRegistrationTime(tempConverter.getCurrentUTCTimestamp)
    val storedCustomer = customerRepository.save(customerEntity)

    storedCart.setCustomer(storedCustomer)
    cartRepository.saveAndFlush(storedCart)

    storedWishList.setCustomer(storedCustomer)
    wishListRepository.saveAndFlush(storedWishList)

    storedAddress.setCustomer(storedCustomer)
    addressRepository.saveAndFlush(storedAddress)

    tempConverter.customerEntityToDto(storedCustomer)

  }

  @Transactional(readOnly = true)
  override def listAll: Array[CustomerDto] = {
    val allCustomers = customerRepository.findAll().asScala
    allCustomers.collect{
      case customerEntity => tempConverter.customerEntityToDto(customerEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getCustomerById(customerId: Integer): CustomerDto = {
    val customerEntity = customerRepository.findById(customerId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The customer has not been found!")))
    tempConverter.customerEntityToDto(customerEntity)
  }

  @Transactional(readOnly = true)
  override def getCustomerByUserId(userId: Integer): CustomerDto = {
    val customerEntity = customerRepository.findByUserId(userId)
      .getOrElse(throw new InstanceUndefinedException(new Error("The customer has not been found!")))
    tempConverter.customerEntityToDto(customerEntity)
  }

  @Transactional(readOnly = true)
  override def getCustomerByCartId(cartId: Integer): CustomerDto = {
    val customerEntity = customerRepository.findByCartId(cartId)
      .getOrElse(throw new InstanceUndefinedException(new Error("The customer has not been found!")))
    tempConverter.customerEntityToDto(customerEntity)
  }

  @Transactional
  override def updateCustomer(customerId: Integer, customer: CustomerDto): CustomerDto = {
    getCustomerById(customerId)
    customer.setId(customerId)
    val updatedCustomer = customerRepository.saveAndFlush(tempConverter.customerDtoToEntity(customer))
    tempConverter.customerEntityToDto(updatedCustomer)
  }

  @Transactional
  override def addCustomer(customer: CustomerDto): CustomerDto = {
    val storedCustomer = customerRepository.save(tempConverter.customerDtoToEntity(customer))
    tempConverter.customerEntityToDto(storedCustomer)
  }

  @Transactional
  override def deleteCustomer(customerId: Integer): Unit = {
    getCustomerById(customerId)
    val allOrders = orderService.listAllByCustomerId(customerId)
    allOrders.foreach(order => {
      orderService.deleteOrder(order.getId)
    })
    customerRepository.deleteById(customerId)
    customerRepository.flush()
  }

  @Transactional(readOnly = true)
  override def getCurrentCustomer: CustomerDto = {
    val authUser = userService.getCurrentUser
    getCustomerByUserId(authUser.getId)
  }
}
