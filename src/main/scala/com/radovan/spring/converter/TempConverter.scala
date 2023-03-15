package com.radovan.spring.converter

import java.sql.Timestamp
import java.time.{LocalDate, ZoneId}
import java.time.format.DateTimeFormatter
import java.util
import java.util.Optional

import com.radovan.spring.dto.{BookDto, BookGenreDto, CartDto, CartItemDto, CustomerDto, DeliveryAddressDto, LoyaltyCardDto, LoyaltyCardRequestDto, OrderAddressDto, OrderDto, OrderItemDto, PersistenceLoginDto, ReviewDto, RoleDto, UserDto, WishListDto}
import com.radovan.spring.entity.{BookEntity, BookGenreEntity, CartEntity, CartItemEntity, CustomerEntity, DeliveryAddressEntity, LoyaltyCardEntity, LoyaltyCardRequestEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, PersistenceLoginEntity, ReviewEntity, RoleEntity, UserEntity, WishListEntity}
import com.radovan.spring.repository.{BookGenreRepository, BookRepository, CartItemRepository, CartRepository, CustomerRepository, DeliveryAddressRepository, LoyaltyCardRepository, OrderAddressRepository, OrderItemRepository, OrderRepository, PersistenceLoginRepository, ReviewRepository, RoleRepository, UserRepository, WishListRepository}
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters._

class TempConverter {

  @Autowired
  private var genreRepository:BookGenreRepository = _

  @Autowired
  private var bookRepository:BookRepository = _

  @Autowired
  private var reviewRepository:ReviewRepository = _

  @Autowired
  private var customerRepository:CustomerRepository = _

  @Autowired
  private var cartItemRepository:CartItemRepository = _

  @Autowired
  private var cartRepository:CartRepository = _

  @Autowired
  private var userRepository:UserRepository = _

  @Autowired
  private var orderItemRepository:OrderItemRepository = _

  @Autowired
  private var orderRepository:OrderRepository = _

  @Autowired
  private var roleRepository:RoleRepository = _

  @Autowired
  private var wishListRepository:WishListRepository = _

  @Autowired
  private var loyaltyCardRepository:LoyaltyCardRepository = _

  @Autowired
  private var addressRepository:DeliveryAddressRepository = _

  @Autowired
  private var persistenceRepository:PersistenceLoginRepository = _

  @Autowired
  private var orderAddressRepository:OrderAddressRepository = _

  @Autowired
  private var mapper:ModelMapper = _

  def bookEntityToDto(bookEntity: BookEntity): BookDto = {
    val returnValue = mapper.map(bookEntity, classOf[BookDto])
    val genre = Optional.ofNullable(bookEntity.getGenre)
    if (genre.isPresent) returnValue.setGenreId(genre.get.getGenreId)
    val reviews = Optional.ofNullable(bookEntity.getReviews)
    val reviewsIds = new util.ArrayList[Integer]
    if (!reviews.isEmpty) {
      for (review <- reviews.get.asScala) {
        reviewsIds.add(review.getReviewId)
      }
    }
    returnValue.setReviewsIds(reviewsIds)
    returnValue
  }

  def bookDtoToEntity(book: BookDto): BookEntity = {
    val returnValue = mapper.map(book, classOf[BookEntity])
    val genreId = Optional.ofNullable(book.getGenreId)
    if (genreId.isPresent) {
      val genre = genreRepository.getById(genreId.get)
      returnValue.setGenre(genre)
    }
    val reviewsIds = Optional.ofNullable(book.getReviewsIds)
    val reviews = new util.ArrayList[ReviewEntity]
    if (!reviewsIds.isEmpty) {
      for (reviewId <- reviewsIds.get.asScala) {
        val review = reviewRepository.getById(reviewId)
        reviews.add(review)
      }
    }
    returnValue.setReviews(reviews)
    returnValue
  }

  def bookGenreEntityToDto(genreEntity: BookGenreEntity): BookGenreDto = {
    val returnValue = mapper.map(genreEntity, classOf[BookGenreDto])
    val books = Optional.ofNullable(genreEntity.getBooks)
    val booksIds = new util.ArrayList[Integer]
    if (!books.isEmpty) {
      for (book <- books.get.asScala) {
        booksIds.add(book.getBookId)
      }
    }
    returnValue.setBooksIds(booksIds)
    returnValue
  }

  def bookGenreDtoToEntity(genre: BookGenreDto): BookGenreEntity = {
    val returnValue = mapper.map(genre, classOf[BookGenreEntity])
    val booksIds = Optional.ofNullable(genre.getBooksIds)
    val books = new util.ArrayList[BookEntity]
    if (!booksIds.isEmpty) {
      for (bookId <- booksIds.get.asScala) {
        val bookEntity = bookRepository.getById(bookId)
        books.add(bookEntity)
      }
    }
    returnValue.setBooks(books)
    returnValue
  }

  def cartEntityToDto(cartEntity: CartEntity): CartDto = {
    val returnValue = mapper.map(cartEntity, classOf[CartDto])
    val cartPrice = Optional.ofNullable(cartEntity.getCartPrice)
    if (!cartPrice.isPresent) returnValue.setCartPrice(0d)
    val customerEntity = Optional.ofNullable(cartEntity.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    val itemsIds = new util.ArrayList[Integer]
    val cartItems = Optional.ofNullable(cartEntity.getCartItems)
    if (!cartItems.isEmpty) {
      for (itemEntity <- cartItems.get.asScala) {
        val itemId = itemEntity.getCartItemId
        itemsIds.add(itemId)
      }
    }
    returnValue.setCartItemsIds(itemsIds)
    returnValue
  }

  def cartDtoToEntity(cartDto: CartDto): CartEntity = {
    val returnValue = mapper.map(cartDto, classOf[CartEntity])
    val cartPrice = Optional.ofNullable(cartDto.getCartPrice)
    if (!cartPrice.isPresent) returnValue.setCartPrice(0d)
    val customerId = Optional.ofNullable(cartDto.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    val cartItems = new util.ArrayList[CartItemEntity]
    val itemIds = Optional.ofNullable(cartDto.getCartItemsIds)
    if (!itemIds.isEmpty) {
      for (itemId <- itemIds.get.asScala) {
        val itemEntity = cartItemRepository.getById(itemId)
        cartItems.add(itemEntity)
      }
    }
    returnValue.setCartItems(cartItems)
    returnValue
  }

  def cartItemEntityToDto(cartItemEntity: CartItemEntity): CartItemDto = {
    val returnValue = mapper.map(cartItemEntity, classOf[CartItemDto])
    val book = Optional.ofNullable(cartItemEntity.getBook)
    if (book.isPresent) returnValue.setBookId(book.get.getBookId)
    val cart = Optional.ofNullable(cartItemEntity.getCart)
    if (cart.isPresent) returnValue.setCartId(cart.get.getCartId)
    returnValue
  }

  def cartItemDtoToEntity(cartItemDto: CartItemDto): CartItemEntity = {
    val returnValue = mapper.map(cartItemDto, classOf[CartItemEntity])
    val cartId = Optional.ofNullable(cartItemDto.getCartId)
    if (cartId.isPresent) {
      val cartEntity = cartRepository.getById(cartId.get)
      returnValue.setCart(cartEntity)
    }
    val bookId = Optional.ofNullable(cartItemDto.getBookId)
    if (bookId.isPresent) {
      val bookEntity = bookRepository.getById(bookId.get)
      returnValue.setBook(bookEntity)
    }
    returnValue
  }

  def customerEntityToDto(customerEntity: CustomerEntity): CustomerDto = {
    val returnValue = mapper.map(customerEntity, classOf[CustomerDto])
    val userEntity = Optional.ofNullable(customerEntity.getUser)
    if (userEntity.isPresent) returnValue.setUserId(userEntity.get.getId)
    val cartEntity = Optional.ofNullable(customerEntity.getCart)
    if (cartEntity.isPresent) returnValue.setCartId(cartEntity.get.getCartId)
    val wishListEntity = Optional.ofNullable(customerEntity.getWishList)
    if (wishListEntity.isPresent) returnValue.setWishListId(wishListEntity.get.getWishListId)
    val loyaltyCard = Optional.ofNullable(customerEntity.getLoyaltyCard)
    if (loyaltyCard.isPresent) returnValue.setLoyaltyCardId(loyaltyCard.get.getLoyaltyCardId)
    val address = Optional.ofNullable(customerEntity.getDeliveryAddress)
    if (address.isPresent) returnValue.setDeliveryAddressId(address.get.getDeliveryAddressId)
    val reviews = Optional.ofNullable(customerEntity.getReviews)
    val reviewsIds = new util.ArrayList[Integer]
    if (reviews.isPresent) {
      for (review <- reviews.get.asScala) {
        reviewsIds.add(review.getReviewId)
      }
    }
    returnValue.setReviewsIds(reviewsIds)
    val persistenceLoginsIds = new util.ArrayList[Integer]
    val persistenceLogins = Optional.ofNullable(customerEntity.getPersistenceLogins)
    if (!persistenceLogins.isEmpty) {
      for (persistence <- persistenceLogins.get.asScala) {
        persistenceLoginsIds.add(persistence.getId)
      }
    }
    returnValue.setPersistenceLoginsIds(persistenceLoginsIds)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val registrationDateOpt = Optional.ofNullable(customerEntity.getRegistrationDate)
    if (registrationDateOpt.isPresent) {
      val registrationDateStr = registrationDateOpt.get.toLocalDateTime.atZone(ZoneId.of("Europe/Belgrade")).format(formatter)
      returnValue.setRegistrationDateStr(registrationDateStr)
    }
    returnValue
  }

  def customerDtoToEntity(customer: CustomerDto): CustomerEntity = {
    val returnValue = mapper.map(customer, classOf[CustomerEntity])
    val userId = Optional.ofNullable(customer.getUserId)
    if (userId.isPresent) {
      val userEntity = userRepository.getById(userId.get)
      returnValue.setUser(userEntity)
    }
    val cartId = Optional.ofNullable(customer.getCartId)
    if (cartId.isPresent) {
      val cartEntity = cartRepository.getById(cartId.get)
      returnValue.setCart(cartEntity)
    }
    val wishListId = Optional.ofNullable(customer.getWishListId)
    if (wishListId.isPresent) {
      val wishListEntity = wishListRepository.getById(wishListId.get)
      returnValue.setWishList(wishListEntity)
    }
    val loyaltyCardId = Optional.ofNullable(customer.getLoyaltyCardId)
    if (loyaltyCardId.isPresent) {
      val cardEntity = loyaltyCardRepository.getById(loyaltyCardId.get)
      returnValue.setLoyaltyCard(cardEntity)
    }
    val delieryAddressId = Optional.ofNullable(customer.getDeliveryAddressId)
    if (delieryAddressId.isPresent) {
      val addressEntity = addressRepository.getById(delieryAddressId.get)
      returnValue.setDeliveryAddress(addressEntity)
    }
    val reviewsIds = Optional.ofNullable(customer.getReviewsIds)
    val reviews = new util.ArrayList[ReviewEntity]
    if (!reviewsIds.isEmpty) {
      for (reviewId <- reviewsIds.get.asScala) {
        val reviewEntity = reviewRepository.getById(reviewId)
        reviews.add(reviewEntity)
      }
    }
    returnValue.setReviews(reviews)
    val persistenceLoginsIds = Optional.ofNullable(customer.getPersistenceLoginsIds)
    val persistenceLogins = new util.ArrayList[PersistenceLoginEntity]
    if (!persistenceLoginsIds.isEmpty) {
      for (persistenceId <- persistenceLoginsIds.get.asScala) {
        val persistence = persistenceRepository.getById(persistenceId)
        persistenceLogins.add(persistence)
      }
    }
    returnValue.setPersistenceLogins(persistenceLogins)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateOfBirthStrOpt = Optional.ofNullable(customer.getDateOfBirthStr)
    if (dateOfBirthStrOpt.isPresent) {
      val dateOfBirthLocal = LocalDate.parse(dateOfBirthStrOpt.get, formatter)
      val dateOfBirth = Timestamp.valueOf(dateOfBirthLocal.atStartOfDay)
      returnValue.setDateOfBirth(dateOfBirth)
    }
    returnValue
  }

  def loyaltyCardEntityToDto(card: LoyaltyCardEntity): LoyaltyCardDto = {
    val returnValue = mapper.map(card, classOf[LoyaltyCardDto])
    val customerEntity = Optional.ofNullable(card.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def loyaltyCardDtoToEntity(card: LoyaltyCardDto): LoyaltyCardEntity = {
    val returnValue = mapper.map(card, classOf[LoyaltyCardEntity])
    val customerId = Optional.ofNullable(card.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def orderEntityToDto(orderEntity: OrderEntity): OrderDto = {
    val returnValue = mapper.map(orderEntity, classOf[OrderDto])
    val customerEntity = Optional.ofNullable(orderEntity.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    val addressEntity = Optional.ofNullable(orderEntity.getAddress)
    if (addressEntity.isPresent) returnValue.setAddressId(addressEntity.get.getAddressId)
    val orderedItems = Optional.ofNullable(orderEntity.getOrderedItems)
    val orderedItemsIds = new util.ArrayList[Integer]
    if (!orderedItems.isEmpty) {
      for (item <- orderedItems.get.asScala) {
        orderedItemsIds.add(item.getOrderItemId)
      }
    }
    returnValue.setOrderedItemsIds(orderedItemsIds)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val createdAtOpt = Optional.ofNullable(orderEntity.getCreatedAt)
    if (createdAtOpt.isPresent) {
      val time = createdAtOpt.get.toLocalDateTime.atZone(ZoneId.of("Europe/Belgrade"))
      val createdAtStr = time.format(formatter)
      returnValue.setCreatedAtStr(createdAtStr)
    }
    returnValue
  }

  def orderDtoToEntity(order: OrderDto): OrderEntity = {
    val returnValue = mapper.map(order, classOf[OrderEntity])
    val customerId = Optional.ofNullable(order.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    val addressId = Optional.ofNullable(order.getAddressId)
    if (addressId.isPresent) {
      val addressEntity = orderAddressRepository.getById(addressId.get)
      returnValue.setAddress(addressEntity)
    }
    val orderedItemsIds = Optional.ofNullable(order.getOrderedItemsIds)
    val orderedItems = new util.ArrayList[OrderItemEntity]
    if (!orderedItemsIds.isEmpty) {
      for (itemId <- orderedItemsIds.get.asScala) {
        val item = orderItemRepository.getById(itemId)
        orderedItems.add(item)
      }
    }
    returnValue.setOrderedItems(orderedItems)
    returnValue
  }

  def orderItemEntityToDto(itemEntity: OrderItemEntity): OrderItemDto = {
    val returnValue = mapper.map(itemEntity, classOf[OrderItemDto])
    val orderEntity = Optional.ofNullable(itemEntity.getOrder)
    if (orderEntity.isPresent) returnValue.setOrderId(orderEntity.get.getOrderId)
    returnValue
  }

  def orderItemDtoToEntity(itemDto: OrderItemDto): OrderItemEntity = {
    val returnValue = mapper.map(itemDto, classOf[OrderItemEntity])
    val orderId = Optional.ofNullable(itemDto.getOrderId)
    if (orderId.isPresent) {
      val orderEntity = orderRepository.getById(orderId.get)
      returnValue.setOrder(orderEntity)
    }
    returnValue
  }

  def reviewEntityToDto(reviewEntity: ReviewEntity): ReviewDto = {
    val returnValue = mapper.map(reviewEntity, classOf[ReviewDto])
    val author = Optional.ofNullable(reviewEntity.getAuthor)
    if (author.isPresent) returnValue.setAuthorId(author.get.getCustomerId)
    val bookEntity = Optional.ofNullable(reviewEntity.getBook)
    if (bookEntity.isPresent) returnValue.setBookId(bookEntity.get.getBookId)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val createdAtOpt = Optional.ofNullable(reviewEntity.getCreatedAt)
    if (createdAtOpt.isPresent) {
      val createdAtZoned = createdAtOpt.get.toLocalDateTime.atZone(ZoneId.of("Europe/Belgrade"))
      val createdAtStr = createdAtZoned.format(formatter)
      returnValue.setCreatedAtStr(createdAtStr)
    }
    returnValue
  }

  def reviewDtoToEntity(review: ReviewDto): ReviewEntity = {
    val returnValue = mapper.map(review, classOf[ReviewEntity])
    val authorId = Optional.ofNullable(review.getAuthorId)
    if (authorId.isPresent) {
      val author = customerRepository.getById(authorId.get)
      returnValue.setAuthor(author)
    }
    val bookId = Optional.ofNullable(review.getBookId)
    if (bookId.isPresent) {
      val bookEntity = bookRepository.getById(bookId.get)
      returnValue.setBook(bookEntity)
    }
    returnValue
  }

  def wishListEntityToDto(wishList: WishListEntity): WishListDto = {
    val returnValue = mapper.map(wishList, classOf[WishListDto])
    val books = Optional.ofNullable(wishList.getBooks)
    val booksIds = new util.ArrayList[Integer]
    if (!books.isEmpty) {
      for (book <- books.get.asScala) {
        booksIds.add(book.getBookId)
      }
    }
    returnValue.setBooksIds(booksIds)
    val customerEntity = Optional.ofNullable(wishList.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def wishListDtoToEntity(wishList: WishListDto): WishListEntity = {
    val returnValue = mapper.map(wishList, classOf[WishListEntity])
    val booksIds = Optional.ofNullable(wishList.getBooksIds)
    val books = new util.ArrayList[BookEntity]
    if (!booksIds.isEmpty) {
      for (bookId <- booksIds.get.asScala) {
        val bookEntity = bookRepository.getById(bookId)
        books.add(bookEntity)
      }
    }
    returnValue.setBooks(books)
    val customerId = Optional.ofNullable(wishList.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def cardRequestEntityToDto(request: LoyaltyCardRequestEntity): LoyaltyCardRequestDto = {
    val returnValue = mapper.map(request, classOf[LoyaltyCardRequestDto])
    val customerEntity = Optional.ofNullable(request.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def cardRequestDtoToEntity(request: LoyaltyCardRequestDto): LoyaltyCardRequestEntity = {
    val returnValue = mapper.map(request, classOf[LoyaltyCardRequestEntity])
    val customerId = Optional.ofNullable(request.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def deliveryAddressEntityToDto(address: DeliveryAddressEntity): DeliveryAddressDto = {
    val returnValue = mapper.map(address, classOf[DeliveryAddressDto])
    val customerEntity = Optional.ofNullable(address.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def deliveryAddressDtoToEntity(address: DeliveryAddressDto): DeliveryAddressEntity = {
    val returnValue = mapper.map(address, classOf[DeliveryAddressEntity])
    val customerId = Optional.ofNullable(address.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def cartItemToOrderItemEntity(cartItemEntity: CartItemEntity): OrderItemEntity = {
    val returnValue = mapper.map(cartItemEntity, classOf[OrderItemEntity])
    val book = Optional.ofNullable(cartItemEntity.getBook)
    if (book.isPresent) {
      returnValue.setBookName(book.get.getName)
      returnValue.setBookPrice(book.get.getPrice)
    }
    returnValue
  }

  def persistenceEntityToDto(persistence: PersistenceLoginEntity): PersistenceLoginDto = {
    val returnValue = mapper.map(persistence, classOf[PersistenceLoginDto])
    val customerEntity = Optional.ofNullable(persistence.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val createdAtOpt = Optional.ofNullable(persistence.getCreatedAt)
    if (createdAtOpt.isPresent) {
      val createdAtZoned = createdAtOpt.get.toLocalDateTime.atZone(ZoneId.of("Europe/Belgrade"))
      val createdAtStr = createdAtZoned.format(formatter)
      returnValue.setCreatedAtStr(createdAtStr)
    }
    returnValue
  }

  def persistenceDtoToEntity(persistence: PersistenceLoginDto): PersistenceLoginEntity = {
    val returnValue = mapper.map(persistence, classOf[PersistenceLoginEntity])
    val customerId = Optional.ofNullable(persistence.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def orderAddressEntityToDto(address: OrderAddressEntity): OrderAddressDto = {
    val returnValue = mapper.map(address, classOf[OrderAddressDto])
    val orderEntity = Optional.ofNullable(address.getOrder)
    if (orderEntity.isPresent) returnValue.setOrderId(orderEntity.get.getOrderId)
    returnValue
  }

  def orderAddressDtoToEntity(address: OrderAddressDto): OrderAddressEntity = {
    val returnValue = mapper.map(address, classOf[OrderAddressEntity])
    val orderId = Optional.ofNullable(address.getOrderId)
    if (orderId.isPresent) {
      val orderEntity = orderRepository.getById(orderId.get)
      returnValue.setOrder(orderEntity)
    }
    returnValue
  }

  def addressToOrderAddress(address: DeliveryAddressEntity): OrderAddressEntity = {
    val returnValue = mapper.map(address, classOf[OrderAddressEntity])
    returnValue
  }

  def userEntityToDto(userEntity: UserEntity): UserDto = {
    val returnValue = mapper.map(userEntity, classOf[UserDto])
    returnValue.setEnabled(userEntity.getEnabled)
    val roles = Optional.ofNullable(userEntity.getRoles)
    val rolesIds = new util.ArrayList[Integer]
    if (!roles.isEmpty) {
      for (roleEntity <- roles.get.asScala) {
        rolesIds.add(roleEntity.getId)
      }
    }
    returnValue.setRolesIds(rolesIds)
    returnValue
  }

  def userDtoToEntity(userDto: UserDto): UserEntity = {
    val returnValue = mapper.map(userDto, classOf[UserEntity])
    val roles = new util.ArrayList[RoleEntity]
    val rolesIds = Optional.ofNullable(userDto.getRolesIds)
    if (!rolesIds.isEmpty) {
      for (roleId <- rolesIds.get.asScala) {
        val role = roleRepository.getById(roleId)
        roles.add(role)
      }
    }
    returnValue.setRoles(roles)
    returnValue
  }

  def roleEntityToDto(roleEntity: RoleEntity): RoleDto = {
    val returnValue = mapper.map(roleEntity, classOf[RoleDto])
    val users = Optional.ofNullable(roleEntity.getUsers)
    val userIds = new util.ArrayList[Integer]
    if (!users.isEmpty) {
      for (user <- users.get.asScala) {
        userIds.add(user.getId)
      }
    }
    returnValue.setUsersIds(userIds)
    returnValue
  }

  def roleDtoToEntity(roleDto: RoleDto): RoleEntity = {
    val returnValue = mapper.map(roleDto, classOf[RoleEntity])
    val usersIds = Optional.ofNullable(roleDto.getUsersIds)
    val users = new util.ArrayList[UserEntity]
    if (!usersIds.isEmpty) {
      for (userId <- usersIds.get.asScala) {
        val userEntity = userRepository.getById(userId)
        users.add(userEntity)
      }
    }
    returnValue.setUsers(users)
    returnValue
  }
}
