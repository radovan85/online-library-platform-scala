package com.radovan.spring.converter

import com.radovan.spring.dto.{BookDto, BookGenreDto, BookImageDto, CartDto, CartItemDto, CustomerDto, DeliveryAddressDto, LoyaltyCardDto, LoyaltyCardRequestDto, OrderAddressDto, OrderDto, OrderItemDto, PersistenceLoginDto, ReviewDto, RoleDto, UserDto, WishListDto}
import com.radovan.spring.entities.{BookEntity, BookGenreEntity, BookImageEntity, CartEntity, CartItemEntity, CustomerEntity, DeliveryAddressEntity, LoyaltyCardEntity, LoyaltyCardRequestEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, PersistenceLoginEntity, ReviewEntity, RoleEntity, UserEntity, WishListEntity}
import com.radovan.spring.repositories.BookGenreRepository
import com.radovan.spring.repositories.BookImageRepository
import com.radovan.spring.repositories.BookRepository
import com.radovan.spring.repositories.CartItemRepository
import com.radovan.spring.repositories.CartRepository
import com.radovan.spring.repositories.CustomerRepository
import com.radovan.spring.repositories.DeliveryAddressRepository
import com.radovan.spring.repositories.LoyaltyCardRepository
import com.radovan.spring.repositories.OrderAddressRepository
import com.radovan.spring.repositories.OrderItemRepository
import com.radovan.spring.repositories.OrderRepository
import com.radovan.spring.repositories.PersistenceLoginRepository
import com.radovan.spring.repositories.ReviewRepository
import com.radovan.spring.repositories.RoleRepository
import com.radovan.spring.repositories.UserRepository
import com.radovan.spring.repositories.WishListRepository
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.text.DecimalFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import scala.collection.JavaConverters._
import java.sql.Timestamp
import java.time.{Instant, LocalDate}
import scala.collection.mutable.ArrayBuffer

@Component
class TempConverter {



  private var genreRepository: BookGenreRepository = _
  private var bookRepository: BookRepository = _
  private var reviewRepository: ReviewRepository = _
  private var customerRepository: CustomerRepository = _
  private var cartItemRepository: CartItemRepository = _
  private var cartRepository: CartRepository = _
  private var userRepository: UserRepository = _
  private var orderItemRepository: OrderItemRepository = _
  private var orderRepository: OrderRepository = _
  private var roleRepository: RoleRepository = _
  private var wishListRepository: WishListRepository = _
  private var loyaltyCardRepository: LoyaltyCardRepository = _
  private var addressRepository: DeliveryAddressRepository = _
  private var persistenceRepository: PersistenceLoginRepository = _
  private var orderAddressRepository: OrderAddressRepository = _
  private var bookImageRepository: BookImageRepository = _
  private var mapper: ModelMapper = _

  private val decfor = new DecimalFormat("0.00")
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  private val zoneId = ZoneId.of("UTC")

  @Autowired
  private def injectAll(genreRepository: BookGenreRepository, bookRepository: BookRepository, reviewRepository: ReviewRepository,
                        customerRepository: CustomerRepository, cartItemRepository: CartItemRepository, cartRepository: CartRepository,
                        userRepository: UserRepository, orderItemRepository: OrderItemRepository, orderRepository: OrderRepository,
                        roleRepository: RoleRepository, wishListRepository: WishListRepository, loyaltyCardRepository: LoyaltyCardRepository,
                        addressRepository: DeliveryAddressRepository, persistenceRepository: PersistenceLoginRepository, orderAddressRepository: OrderAddressRepository,
                        bookImageRepository: BookImageRepository, mapper: ModelMapper): Unit = {

    this.genreRepository = genreRepository
    this.bookRepository = bookRepository
    this.reviewRepository = reviewRepository
    this.customerRepository = customerRepository
    this.cartItemRepository = cartItemRepository
    this.cartRepository = cartRepository
    this.userRepository = userRepository
    this.orderItemRepository = orderItemRepository
    this.orderRepository = orderRepository
    this.roleRepository = roleRepository
    this.wishListRepository = wishListRepository
    this.loyaltyCardRepository = loyaltyCardRepository
    this.addressRepository = addressRepository
    this.persistenceRepository = persistenceRepository
    this.orderAddressRepository = orderAddressRepository
    this.bookImageRepository = bookImageRepository
    this.mapper = mapper
  }

  def bookEntityToDto(book: BookEntity): BookDto = {
    val returnValue = mapper.map(book, classOf[BookDto])
    val genreOption = Option(book.getGenre)
    if (genreOption.isDefined) {
      returnValue.setGenreId(genreOption.get.getId)
    }

    val imageOption = Option(book.getImage)
    if (imageOption.isDefined) {
      returnValue.setImageId(imageOption.get.getId)
    }

    val reviewsOption = Option(book.getReviews.asScala)
    val rolesIds = new ArrayBuffer[Integer]()
    reviewsOption match {
      case Some(roles) =>
        roles.foreach(roleEntity => {
          rolesIds += roleEntity.getId
        })
      case None =>
    }

    returnValue.setReviewsIds(rolesIds.toArray)

    val wishlistsOption = Option(book.getWishLists.asScala)
    val wishlistsIds = new ArrayBuffer[Integer]()
    wishlistsOption match {
      case Some(wishlists) =>
        wishlists.foreach(wishlistEntity => {
          wishlistsIds += wishlistEntity.getId
        })
      case None =>
    }

    returnValue.setWishListsIds(wishlistsIds.toArray)
    returnValue.setAverageRating(decfor.format(returnValue.getAverageRating).toFloat)
    returnValue.setPrice(decfor.format(returnValue.getPrice).toFloat)
    returnValue
  }

  def bookDtoToEntity(book: BookDto): BookEntity = {
    val returnValue = mapper.map(book, classOf[BookEntity])

    val genreIdOption = Option(book.genreId)
    genreIdOption match {
      case Some(genreId) =>
        val genreEntity = genreRepository.findById(genreId).orElse(null)
        if (genreEntity != null) {
          returnValue.setGenre(genreEntity)
        }
      case None =>
    }

    val imageIdOption = Option(book.getImageId)
    imageIdOption match {
      case Some(imageId) =>
        val imageEntity = bookImageRepository.findById(imageId).orElse(null)
        if (imageEntity != null) {
          returnValue.setImage(imageEntity)
        }
      case None =>
    }

    val reviewsIdsOption = Option(book.getReviewsIds)
    val reviews = new ArrayBuffer[ReviewEntity]()
    reviewsIdsOption match {
      case Some(reviewsIds) =>
        reviewsIds.foreach(reviewId => {
          val reviewEntity = reviewRepository.findById(reviewId).orElse(null)
          if (reviewEntity != null) {
            reviews += reviewEntity
          }
        })
      case None =>
    }
    returnValue.setReviews(reviews.asJava)

    val wishListsIds = Option(book.getWishListsIds)
    val wishLists = new ArrayBuffer[WishListEntity]()
    wishListsIds match {
      case Some(wishListsIds) =>
        wishListsIds.foreach(wishListId => {
          val wishListEntity = wishListRepository.findById(wishListId).orElse(null)
          if (wishListEntity != null) {
            wishLists += wishListEntity
          }
        })
      case None =>
    }

    returnValue.setWishLists(wishLists.asJava)
    returnValue.setAverageRating(decfor.format(returnValue.getAverageRating).toFloat)
    returnValue.setPrice(decfor.format(returnValue.getPrice).toFloat)
    returnValue
  }

  def bookGenreEntityToDto(genre: BookGenreEntity): BookGenreDto = {
    val returnValue = mapper.map(genre, classOf[BookGenreDto])
    val booksOption = Option(genre.getBooks.asScala)
    val booksIds = new ArrayBuffer[Integer]()
    booksOption match {
      case Some(books) =>
        books.foreach(bookEntity => {
          booksIds += bookEntity.getId
        })
      case None =>
    }

    returnValue.setBooksIds(booksIds.toArray)
    returnValue
  }

  def bookGenreDtoToEntity(genre: BookGenreDto): BookGenreEntity = {
    val returnValue = mapper.map(genre, classOf[BookGenreEntity])
    val booksIdsOption = Option(genre.getBooksIds)
    val books = new ArrayBuffer[BookEntity]()
    booksIdsOption match {
      case Some(booksIds) =>
        booksIds.foreach(bookId => {
          val bookEntity = bookRepository.findById(bookId).orElse(null)
          if (bookEntity != null) {
            books += bookEntity
          }
        })
      case None =>
    }

    returnValue.setBooks(books.asJava)
    returnValue
  }

  def bookImageEntityToDto(image: BookImageEntity): BookImageDto = {
    val returnValue = mapper.map(image, classOf[BookImageDto])
    val bookOption = Option(image.getBook)
    if (bookOption.isDefined) {
      returnValue.setBookId(bookOption.get.getId)
    }

    returnValue
  }

  def bookImageDtoToEntity(image: BookImageDto): BookImageEntity = {
    val returnValue = mapper.map(image, classOf[BookImageEntity])
    val bookIdOption = Option(image.getBookId)
    bookIdOption match {
      case Some(bookId) =>
        val bookEntity = bookRepository.findById(bookId).orElse(null)
        if (bookEntity != null) {
          returnValue.setBook(bookEntity)
        }
      case None =>
    }

    returnValue
  }

  def cartEntityToDto(cart: CartEntity): CartDto = {
    val returnValue = mapper.map(cart, classOf[CartDto])
    val customerOption = Option(cart.getCustomer)
    customerOption match {
      case Some(customer) => returnValue.setCustomerId(customer.getId)
      case None =>
    }

    val cartItemsOption = Option(cart.getCartItems)
    val cartItemsIds = new ArrayBuffer[Integer]()
    cartItemsOption match {
      case Some(cartItems) =>
        cartItems.forEach(cartItemEntity => {
          cartItemsIds += cartItemEntity.getId
        })
      case None =>
    }

    returnValue.setCartItemsIds(cartItemsIds.toArray)
    returnValue.setCartPrice(decfor.format(returnValue.getCartPrice).toFloat)
    returnValue
  }

  def cartDtoToEntity(cart: CartDto): CartEntity = {
    val returnValue = mapper.map(cart, classOf[CartEntity])
    val customerIdOption = Option(cart.getCustomerId)
    customerIdOption match {
      case Some(customerId) =>
        val customerEntity = customerRepository.findById(customerId).orElse(null)
        if (customerEntity != null) {
          returnValue.setCustomer(customerEntity)
        }
      case None =>
    }

    val cartItemsIdsOption = Option(cart.getCartItemsIds)
    val cartItems = new ArrayBuffer[CartItemEntity]()
    cartItemsIdsOption match {
      case Some(cartItemsIds) =>
        cartItemsIds.foreach(itemId => {
          val cartItemEntity = cartItemRepository.findById(itemId).orElse(null)
          if (cartItemEntity != null) {
            cartItems += cartItemEntity
          }
        })
      case None =>
    }

    returnValue.setCartItems(cartItems.asJava)
    returnValue.setCartPrice(decfor.format(returnValue.getCartPrice).toFloat)
    returnValue
  }

  def cartItemEntityToDto(cartItem:CartItemEntity):CartItemDto = {
    val returnValue = mapper.map(cartItem, classOf[CartItemDto])
    val bookOption = Option(cartItem.getBook)
    if(bookOption.isDefined){
      returnValue.setBookId(bookOption.get.getId)
    }

    val cartOption = Option(cartItem.getCart)
    if(cartOption.isDefined){
      returnValue.setCartId(cartOption.get.getId)
    }

    returnValue.setPrice(decfor.format(returnValue.getPrice).toFloat)
    returnValue
  }

  def cartItemDtoToEntity(cartItem: CartItemDto):CartItemEntity = {
    val returnValue = mapper.map(cartItem, classOf[CartItemEntity])
    val bookIdOption = Option(cartItem.getBookId)
    bookIdOption match {
      case Some(bookId) =>
        val bookEntity = bookRepository.findById(bookId).orElse(null)
        if(bookEntity!=null){
          returnValue.setBook(bookEntity)
        }
      case None =>
    }

    val cartIdOption = Option(cartItem.getCartId)
    cartIdOption match {
      case Some(cartId) =>
        val cartEntity = cartRepository.findById(cartId).orElse(null)
        if(cartEntity!=null){
          returnValue.setCart(cartEntity)
        }
      case None =>
    }

    returnValue.setPrice(decfor.format(returnValue.getPrice).toFloat)
    returnValue
  }

  def customerEntityToDto(customer: CustomerEntity): CustomerDto = {
    val returnValue = mapper.map(customer, classOf[CustomerDto])

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateOfBirthOption = Option(customer.getDateOfBirth)
    if (dateOfBirthOption.isDefined) {
      returnValue.setDateOfBirth(dateOfBirthOption.get.format(dateFormatter))
    }

    val registrationTimeOption = Option(customer.getRegistrationTime)
    registrationTimeOption match {
      case Some(registrationTime) =>
        val registrationTimeStr = registrationTime.toLocalDateTime.atZone(zoneId).format(formatter)
        returnValue.setRegistrationTime(registrationTimeStr)
      case None =>
    }

    val userOption = Option(customer.getUser)
    if (userOption.isDefined) {
      returnValue.setUserId(userOption.get.getId)
    }

    val cartOption = Option(customer.getCart)
    if (cartOption.isDefined) {
      returnValue.setCartId(cartOption.get.getId)
    }

    val wishListOption = Option(customer.getWishList)
    if (wishListOption.isDefined) {
      returnValue.setWishListId(wishListOption.get.getId)
    }

    val loyaltyCardOption = Option(customer.getLoyaltyCard)
    if (loyaltyCardOption.isDefined) {
      returnValue.setLoyaltyCardId(loyaltyCardOption.get.getId)
    }

    val deliveryAddressOption = Option(customer.getDeliveryAddress)
    if (deliveryAddressOption.isDefined) {
      returnValue.setDeliveryAddressId(deliveryAddressOption.get.getId)
    }

    val reviewsOption = Option(customer.getReviews.asScala)
    val reviewsIds = new ArrayBuffer[Integer]()
    reviewsOption match {
      case Some(reviews) =>
        reviews.foreach(reviewEntity => {
          reviewsIds += reviewEntity.getId
        })
      case None =>
    }

    returnValue.setReviewsIds(reviewsIds.toArray)

    val persistenceLoginsOption = Option(customer.getPersistenceLogins.asScala)
    val persistenceLoginsIds = new ArrayBuffer[Integer]()
    persistenceLoginsOption match {
      case Some(persistenceLogins) =>
        persistenceLogins.foreach(persistenceLogin => {
          persistenceLoginsIds += persistenceLogin.getId
        })
      case None =>
    }

    returnValue.setPersistenceLoginsIds(persistenceLoginsIds.toArray)
    returnValue
  }

  def customerDtoToEntity(customer:CustomerDto):CustomerEntity = {
    val returnValue = mapper.map(customer, classOf[CustomerEntity])

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateOfBirthOptional = Option(customer.getDateOfBirth)
    if(dateOfBirthOptional.isDefined){
      returnValue.setDateOfBirth(LocalDate.parse(dateOfBirthOptional.get, dateFormatter))
    }

    val userIdOption = Option(customer.getUserId)
    userIdOption match {
      case Some(userId) =>
        val userEntity = userRepository.findById(userId).orElse(null)
        if(userEntity!=null){
          returnValue.setUser(userEntity)
        }
      case None =>
    }

    val cartIdOption = Option(customer.getCartId)
    cartIdOption match {
      case Some(cartId) =>
        val cartEntity = cartRepository.findById(cartId).orElse(null)
        if(cartEntity!=null){
          returnValue.setCart(cartEntity)
        }
      case None =>
    }

    val wishListIdOption = Option(customer.getWishListId)
    wishListIdOption match {
      case Some(wishListId) =>
        val wishListEntity = wishListRepository.findById(wishListId).orElse(null)
        if(wishListEntity!=null){
          returnValue.setWishList(wishListEntity)
        }
      case None =>
    }

    val loyaltyCardIdOption = Option(customer.getLoyaltyCardId)
    loyaltyCardIdOption match {
      case Some(cardId) =>
        val loyaltyCardEntity = loyaltyCardRepository.findById(cardId).orElse(null)
        if(loyaltyCardEntity!=null){
          returnValue.setLoyaltyCard(loyaltyCardEntity)
        }
      case None =>
    }

    val deliveryAddressIdOption = Option(customer.getDeliveryAddressId)
    deliveryAddressIdOption match {
      case Some(addressId) =>
        val addressEntity = addressRepository.findById(addressId).orElse(null)
        if(addressEntity!=null){
          returnValue.setDeliveryAddress(addressEntity)
        }
      case None =>
    }

    val reviewsIdsOption = Option(customer.getReviewsIds)
    val reviews = new ArrayBuffer[ReviewEntity]()
    reviewsIdsOption match {
      case Some(reviewsIds) =>
        reviewsIds.foreach(reviewId => {
          val reviewEntity = reviewRepository.findById(reviewId).orElse(null)
          if(reviewEntity!=null){
            reviews += reviewEntity
          }
        })
      case None =>
    }

    returnValue.setReviews(reviews.asJava)

    val persistenceLoginsIdsOption = Option(customer.getPersistenceLoginsIds)
    val persistenceLogins = new ArrayBuffer[PersistenceLoginEntity]()
    persistenceLoginsIdsOption match {
      case Some(persistenceLoginsIds) =>
        persistenceLoginsIds.foreach(persistenceId => {
          val persistenceLoginEntity = persistenceRepository.findById(persistenceId).orElse(null)
          if(persistenceLoginEntity!=null){
            persistenceLogins += persistenceLoginEntity
          }
        })
      case None =>
    }

    returnValue.setPersistenceLogins(persistenceLogins.asJava)
    returnValue
  }

  def deliveryAddressEntityToDto(address:DeliveryAddressEntity):DeliveryAddressDto = {
    val returnValue = mapper.map(address, classOf[DeliveryAddressDto])
    val customerOption = Option(address.getCustomer)
    if(customerOption.isDefined){
      returnValue.setCustomerId(customerOption.get.getId)
    }

    returnValue
  }

  def deliveryAddressDtoToEntity(address:DeliveryAddressDto):DeliveryAddressEntity = {
    val returnValue = mapper.map(address, classOf[DeliveryAddressEntity])
    val customerIdOption = Option(address.getCustomerId)
    customerIdOption match {
      case Some(customerId) =>
        val customerEntity = customerRepository.findById(customerId).orElse(null)
        if(customerEntity!=null){
          returnValue.setCustomer(customerEntity)
        }
      case None =>
    }

    returnValue
  }

  def loyaltyCardEntityToDto(loyaltyCard:LoyaltyCardEntity):LoyaltyCardDto = {
    val returnValue = mapper.map(loyaltyCard, classOf[LoyaltyCardDto])
    val customerOption = Option(loyaltyCard.getCustomer)
    if (customerOption.isDefined) {
      returnValue.setCustomerId(customerOption.get.getId)
    }

    returnValue
  }

  def loyaltyCardDtoToEntity(loyaltyCard:LoyaltyCardDto):LoyaltyCardEntity = {
    val returnValue = mapper.map(loyaltyCard, classOf[LoyaltyCardEntity])
    val customerIdOption = Option(loyaltyCard.getCustomerId)
    customerIdOption match {
      case Some(customerId) =>
        val customerEntity = customerRepository.findById(customerId).orElse(null)
        if (customerEntity != null) {
          returnValue.setCustomer(customerEntity)
        }
      case None =>
    }

    returnValue
  }

  def cardRequestEntityToDto(cardRequest:LoyaltyCardRequestEntity):LoyaltyCardRequestDto = {
    val returnValue = mapper.map(cardRequest, classOf[LoyaltyCardRequestDto])
    val customerOption = Option(cardRequest.getCustomer)
    if (customerOption.isDefined) {
      returnValue.setCustomerId(customerOption.get.getId)
    }

    returnValue
  }

  def cardRequestDtoToEntity(cardRequest: LoyaltyCardRequestDto):LoyaltyCardRequestEntity = {
    val returnValue = mapper.map(cardRequest, classOf[LoyaltyCardRequestEntity])
    val customerIdOption = Option(cardRequest.getCustomerId)
    customerIdOption match {
      case Some(customerId) =>
        val customerEntity = customerRepository.findById(customerId).orElse(null)
        if (customerEntity != null) {
          returnValue.setCustomer(customerEntity)
        }
      case None =>
    }

    returnValue
  }

  def orderAddressEntityToDto(address:OrderAddressEntity):OrderAddressDto = {
    val returnValue = mapper.map(address, classOf[OrderAddressDto])
    val orderOption = Option(address.getOrder)
    orderOption match {
      case Some(order) => returnValue.setOrderId(order.getId)
      case None =>
    }

    returnValue
  }

  def orderAddressDtoToEntity(address: OrderAddressDto):OrderAddressEntity = {
    val returnValue = mapper.map(address, classOf[OrderAddressEntity])
    val orderIdOption = Option(address.getOrderId)
    orderIdOption match {
      case Some(orderId) =>
        val orderEntity = orderRepository.findById(orderId).orElse(null)
        if(orderEntity!=null){
          returnValue.setOrder(orderEntity)
        }
      case None =>
    }

    returnValue
  }

  def orderEntityToDto(order:OrderEntity):OrderDto = {
    val returnValue = mapper.map(order,classOf[OrderDto])
    val createTimeOption = Option(order.getCreateTime)
    createTimeOption match {
      case Some(createTime) =>
        val createTimeStr = createTime.toLocalDateTime.atZone(zoneId).format(formatter)
        returnValue.setCreateTime(createTimeStr)
      case None =>
    }

    val cartOption = Option(order.getCart)
    if (cartOption.isDefined) {
      returnValue.setCartId(cartOption.get.getId)
    }

    val addressOption = Option(order.getAddress)
    if(addressOption.isDefined){
      returnValue.setAddressId(addressOption.get.getId)
    }

    val orderedItemsOption = Option(order.getOrderedItems.asScala)
    val orderedItemsIds = new ArrayBuffer[Integer]()
    orderedItemsOption match {
      case Some(orderedItems) =>
        orderedItems.foreach(itemEntity => {
          orderedItemsIds += itemEntity.getId
        })
      case None =>
    }

    returnValue.setOrderedItemsIds(orderedItemsIds.toArray)
    returnValue
  }

  def orderDtoToEntity(order:OrderDto):OrderEntity = {
    val returnValue = mapper.map(order, classOf[OrderEntity])

    val cartIdOption = Option(order.getCartId)
    cartIdOption match {
      case Some(cartId) =>
        val cartEntity = cartRepository.findById(cartId).orElse(null)
        if (cartEntity != null) {
          returnValue.setCart(cartEntity)
        }
      case None =>
    }

    val addressIdOption = Option(order.getAddressId)
    addressIdOption match {
      case Some(addressId) =>
        val addressEntity = orderAddressRepository.findById(addressId).orElse(null)
        if(addressEntity!=null){
          returnValue.setAddress(addressEntity)
        }
      case None =>
    }

    val orderedItemsIdsOption = Option(order.getOrderedItemsIds)
    val orderedItems = new ArrayBuffer[OrderItemEntity]()
    orderedItemsIdsOption match {
      case Some(orderedItemsIds) =>
        orderedItemsIds.foreach(itemId => {
          val itemEntity = orderItemRepository.findById(itemId).orElse(null)
          if(itemEntity!=null){
            orderedItems += itemEntity
          }
        })
      case None =>
    }

    returnValue.setOrderedItems(orderedItems.asJava)
    returnValue
  }

  def orderItemEntityToDto(orderItem:OrderItemEntity):OrderItemDto = {
    val returnValue = mapper.map(orderItem, classOf[OrderItemDto])
    val orderOption = Option(orderItem.getOrder)
    orderOption match {
      case Some(order) => returnValue.setOrderId(order.getId)
      case None =>
    }

    returnValue
  }

  def orderItemDtoToEntity(orderItem: OrderItemDto):OrderItemEntity = {
    val returnValue = mapper.map(orderItem, classOf[OrderItemEntity])
    val orderIdOption = Option(orderItem.getOrderId)
    orderIdOption match {
      case Some(orderId) =>
        val orderEntity = orderRepository.findById(orderId).orElse(null)
        if (orderEntity != null) {
          returnValue.setOrder(orderEntity)
        }
      case None =>
    }

    returnValue
  }

  def persistenceLoginEntityToDto(persistence:PersistenceLoginEntity):PersistenceLoginDto = {
    val returnValue = mapper.map(persistence, classOf[PersistenceLoginDto])
    val createTimeOption = Option(persistence.getCreateTime)
    createTimeOption match {
      case Some(createTime) =>
        val createTimeStr = createTime.toLocalDateTime.atZone(zoneId).format(formatter)
        returnValue.setCreateTime(createTimeStr)
      case None =>
    }

    val customerOption = Option(persistence.getCustomer)
    if (customerOption.isDefined) {
      returnValue.setCustomerId(customerOption.get.getId)
    }

    returnValue
  }

  def persistenceLoginDtoToEntity(persistence:PersistenceLoginDto):PersistenceLoginEntity = {
    val returnValue = mapper.map(persistence, classOf[PersistenceLoginEntity])
    val customerIdOption = Option(persistence.getCustomerId)
    customerIdOption match {
      case Some(customerId) =>
        val customerEntity = customerRepository.findById(customerId).orElse(null)
        if (customerEntity != null) {
          returnValue.setCustomer(customerEntity)
        }
      case None =>
    }

    returnValue
  }

  def reviewEntityToDto(review:ReviewEntity):ReviewDto = {
    val returnValue = mapper.map(review, classOf[ReviewDto])
    val createTimeOption = Option(review.getCreateTime)
    createTimeOption match {
      case Some(createTime) =>
        val createTimeStr = createTime.toLocalDateTime.atZone(zoneId).format(formatter)
        returnValue.setCreateTime(createTimeStr)
      case None =>
    }

    val authorOption = Option(review.getAuthor)
    if (authorOption.isDefined) {
      returnValue.setAuthorId(authorOption.get.getId)
    }

    val bookOption = Option(review.getBook)
    if(bookOption.isDefined){
      returnValue.setBookId(bookOption.get.getId)
    }

    val authorizedOption = Option(review.getAuthorized)
    authorizedOption match {
      case Some(authorized) => returnValue.setAuthorized(authorized.asInstanceOf[Short])
      case None =>
    }

    returnValue
  }

  def reviewDtoToEntity(review: ReviewDto):ReviewEntity = {
    val returnValue = mapper.map(review, classOf[ReviewEntity])
    val authorIdOption = Option(review.getAuthorId)
    authorIdOption match {
      case Some(authorId) =>
        val customerEntity = customerRepository.findById(authorId).orElse(null)
        if(customerEntity!=null){
          returnValue.setAuthor(customerEntity)
        }
      case None =>
    }

    val bookIdOption = Option(review.getBookId)
    bookIdOption match {
      case Some(bookId) =>
        val bookEntity = bookRepository.findById(bookId).orElse(null)
        if(bookEntity!=null){
          returnValue.setBook(bookEntity)
        }
      case None =>
    }

    val authorizedOption = Option(review.getAuthorized)
    authorizedOption match {
      case Some(authorized) => returnValue.setAuthorized(authorized.asInstanceOf[Byte])
      case None =>
    }

    returnValue
  }

  def roleEntityToDto(role: RoleEntity): RoleDto = {
    val returnValue = mapper.map(role, classOf[RoleDto])
    val usersOpt = Option(role.getUsers.asScala)
    val usersIds = new ArrayBuffer[Integer]()
    if (usersOpt.isDefined) {
      usersOpt.get.foreach(userEntity => usersIds += userEntity.getId)
    }

    returnValue.setUsersIds(usersIds.toArray)
    returnValue
  }

  def roleDtoToEntity(role: RoleDto): RoleEntity = {
    val returnValue = mapper.map(role, classOf[RoleEntity])
    val usersIdsOpt = Option(role.getUsersIds)
    val users = new ArrayBuffer[UserEntity]()
    usersIdsOpt match {
      case Some(usersIds) =>
        usersIds.foreach(userId => {
          val userEntity = userRepository.findById(userId).orElse(null)
          if (userEntity != null) {
            users += userEntity
          }
        })
      case None =>
    }

    returnValue.setUsers(users.asJava)
    returnValue
  }

  def userEntityToDto(user: UserEntity): UserDto = {
    val returnValue = mapper.map(user, classOf[UserDto])
    val rolesOpt = Option(user.getRoles.asScala)
    val rolesIds = new ArrayBuffer[Integer]()
    if (rolesOpt.isDefined) {
      rolesOpt.get.foreach(role => rolesIds += role.getId)
    }

    val enabledOpt = Option(user.getEnabled)
    enabledOpt match {
      case Some(enabled) => returnValue.setEnabled(enabled.asInstanceOf[Short])
      case None =>
    }

    returnValue.setRolesIds(rolesIds.toArray)
    returnValue
  }

  def userDtoToEntity(user: UserDto): UserEntity = {
    val returnValue = mapper.map(user, classOf[UserEntity])
    val rolesIdsOpt = Option(user.getRolesIds)
    val roles = new ArrayBuffer[RoleEntity]()
    rolesIdsOpt match {
      case Some(rolesIds) =>
        rolesIds.foreach(roleId => {
          val roleEntity = roleRepository.findById(roleId).orElse(null)
          if (roleEntity != null) {
            roles += roleEntity
          }
        })
      case None =>
    }

    val enabledOpt = Option(user.getEnabled)
    enabledOpt match {
      case Some(enabled) => returnValue.setEnabled(enabled.asInstanceOf[Byte])
      case None =>
    }

    returnValue.setRoles(roles.asJava)
    returnValue
  }

  def wishListEntityToDto(wishList:WishListEntity):WishListDto = {
    val returnValue = mapper.map(wishList, classOf[WishListDto])
    val booksOption = Option(wishList.getBooks.asScala)
    val booksIds = new ArrayBuffer[Integer]()
    booksOption match {
      case Some(books) =>
        books.foreach(bookEntity => {
          booksIds += bookEntity.getId
        })
      case None =>
    }

    returnValue.setBooksIds(booksIds.toArray)

    val customerOption = Option(wishList.getCustomer)
    if (customerOption.isDefined) {
      returnValue.setCustomerId(customerOption.get.getId)
    }

    returnValue
  }

  def wishListDtoToEntity(wishList: WishListDto):WishListEntity = {
    val returnValue = mapper.map(wishList, classOf[WishListEntity])
    val booksIdsOption = Option(wishList.getBooksIds)
    val books = new ArrayBuffer[BookEntity]()
    booksIdsOption match {
      case Some(booksIds) =>
        booksIds.foreach(bookId => {
          val bookEntity = bookRepository.findById(bookId).orElse(null)
          if (bookEntity != null) {
            books += bookEntity
          }
        })
      case None =>
    }

    returnValue.setBooks(books.asJava)

    val customerIdOption = Option(wishList.getCustomerId)
    customerIdOption match {
      case Some(customerId) =>
        val customerEntity = customerRepository.findById(customerId).orElse(null)
        if (customerEntity != null) {
          returnValue.setCustomer(customerEntity)
        }
      case None =>
    }

    returnValue
  }

  def addressToOrderAddress(address: DeliveryAddressDto): OrderAddressDto = {
    val returnValue = mapper.map(address, classOf[OrderAddressDto])
    returnValue.setId(null)
    returnValue
  }

  def getCurrentUTCTimestamp: Timestamp = {
    val currentTime = Instant.now.atZone(zoneId)
    Timestamp.valueOf(currentTime.toLocalDateTime)
  }

  def cartItemToOrderItem(cartItem: CartItemDto): OrderItemDto = {
    val returnValue = mapper.map(cartItem, classOf[OrderItemDto])
    val bookIdOption = Option(cartItem.getBookId)
    bookIdOption match {
      case Some(bookId) =>
        val bookEntity = bookRepository.findById(bookId).orElse(null)
        if (bookEntity != null) {
          returnValue.setBookName(bookEntity.getName)
          returnValue.setBookPrice(bookEntity.getPrice)
        }
      case None =>
    }

    returnValue.setId(null)
    returnValue
  }
}
