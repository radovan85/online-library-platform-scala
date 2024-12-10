package com.radovan.spring.controllers

import com.radovan.spring.dto.{BookDto, BookGenreDto}
import com.radovan.spring.services.{BookGenreService, BookImageService, BookService, CustomerService, DeliveryAddressService, LoyaltyCardService, OrderAddressService, OrderItemService, OrderService, PersistenceLoginService, ReviewService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping, RequestPart}
import org.springframework.web.multipart.MultipartFile

@Controller
@RequestMapping(value = Array("/admin"))
class AdminController {

  private var bookService:BookService = _
  private var genreService:BookGenreService = _
  private var reviewService:ReviewService = _
  private var customerService:CustomerService = _
  private var userService:UserService = _
  private var loyaltyCardService:LoyaltyCardService = _
  private var orderService:OrderService = _
  private var orderItemService:OrderItemService = _
  private var addressService:DeliveryAddressService = _
  private var persistenceService:PersistenceLoginService = _
  private var orderAddressService:OrderAddressService = _
  private var imageService:BookImageService = _

  @Autowired
  private def injectAll(bookService: BookService,genreService: BookGenreService,reviewService: ReviewService,
                        customerService: CustomerService,userService: UserService,loyaltyCardService: LoyaltyCardService,
                        orderService: OrderService,orderItemService: OrderItemService,addressService: DeliveryAddressService,
                        persistenceService: PersistenceLoginService,orderAddressService: OrderAddressService,imageService: BookImageService):Unit = {

    this.bookService = bookService
    this.genreService = genreService
    this.reviewService = reviewService
    this.customerService = customerService
    this.userService = userService
    this.loyaltyCardService = loyaltyCardService
    this.orderService = orderService
    this.orderItemService = orderItemService
    this.addressService = addressService
    this.persistenceService = persistenceService
    this.orderAddressService = orderAddressService
    this.imageService = imageService
  }

  @GetMapping(value = Array("/createBook"))
  def renderBookForm(map: ModelMap): String = {
    map.put("book", new BookDto)
    map.put("allGenres", genreService.listAll)
    "fragments/bookForm :: fragmentContent"
  }

  @PostMapping(value = Array("/createBook"))
  def createBook(@ModelAttribute("book") book: BookDto): String = {
    bookService.addBook(book)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteBook/{bookId}"))
  def deleteBook(@PathVariable("bookId") bookId: Integer): String = {
    bookService.deleteBook(bookId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/updateBook/{bookId}"))
  def renderUpdateForm(@PathVariable("bookId") bookId: Integer, map: ModelMap): String = {
    map.put("book", new BookDto)
    map.put("currentBook", bookService.getBookById(bookId))
    map.put("allGenres", genreService.listAll)
    "fragments/bookUpdateForm :: fragmentContent"
  }

  @GetMapping(value = Array("/createGenre"))
  def renderGenreForm(map: ModelMap): String = {
    map.put("genre", new BookGenreDto)
    "fragments/genreForm :: fragmentContent"
  }

  @PostMapping(value = Array("/createGenre"))
  def createGenre(@ModelAttribute("genre") genre: BookGenreDto): String = {
    genreService.addGenre(genre)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/allGenres"))
  def listAllGenres(map: ModelMap): String = {
    map.put("allGenres", genreService.listAll)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/genreList :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteGenre/{genreId}"))
  def removeGenre(@PathVariable("genreId") genreId: Integer): String = {
    genreService.deleteGenre(genreId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/updateGenre/{genreId}"))
  def updateGenre(@PathVariable("genreId") genreId: Integer, map: ModelMap): String = {
    map.put("genre", new BookGenreDto)
    map.put("currentGenre", genreService.getGenreById(genreId))
    "fragments/genreUpdateForm :: fragmentContent"
  }

  @PostMapping(value = Array("/updateGenre/{genreId}"))
  def updateGenre(@ModelAttribute("genre") genre: BookGenreDto, @PathVariable("genreId") genreId: Integer): String = {
    genreService.updateGenre(genreId, genre)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/allReviews"))
  def reviewList(map: ModelMap): String = {
    map.put("approvedReviews", reviewService.listAllAuthorized)
    map.put("allReviewRequests", reviewService.listAllOnHold)
    map.put("allBooks", bookService.listAll)
    map.put("allCustomers", customerService.listAll)
    map.put("allUsers", userService.listAllUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/reviewList :: fragmentContent"
  }

  @GetMapping(value = Array("/allRequestedReviews"))
  def requestedReviewsList(map: ModelMap): String = {
    map.put("pendingReviews", reviewService.listAllOnHold)
    map.put("allBooks", bookService.listAll)
    map.put("allCustomers", customerService.listAll)
    map.put("allUsers", userService.listAllUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/reviewRequestsList :: fragmentContent"
  }

  @GetMapping(value = Array("/pendingReviewDetails/{reviewId}"))
  def getPendingReview(@PathVariable("reviewId") reviewId: Integer, map: ModelMap): String = {
    val currentReview = reviewService.getReviewById(reviewId)
    val tempCustomer = customerService.getCustomerById(currentReview.getAuthorId)
    val tempUser = userService.getUserById(tempCustomer.getUserId)
    val currentBook = bookService.getBookById(currentReview.getBookId)
    map.put("currentReview", currentReview)
    map.put("tempUser", tempUser)
    map.put("currentBook", currentBook)
    "fragments/pendingReviewDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/reviewDetails/{reviewId}"))
  def getReviewDetails(@PathVariable("reviewId") reviewId: Integer, map: ModelMap): String = {
    val currentReview = reviewService.getReviewById(reviewId)
    val tempCustomer = customerService.getCustomerById(currentReview.getAuthorId)
    val tempUser = userService.getUserById(tempCustomer.getUserId)
    val currentBook = bookService.getBookById(currentReview.getBookId)
    map.put("currentReview", currentReview)
    map.put("tempUser", tempUser)
    map.put("currentBook", currentBook)
    "fragments/reviewDetails :: fragmentContent"
  }

  @PostMapping(value = Array("/authorizeReview/{reviewId}"))
  def reviewAuthorization(@PathVariable("reviewId") reviewId: Integer): String = {
    reviewService.authorizeReview(reviewId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/rejectReview/{reviewId}"))
  def rejectReview(@PathVariable("reviewId") reviewId: Integer): String = {
    reviewService.deleteReview(reviewId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/checkCardRequests"))
  def allCardRequests(map: ModelMap): String = {
    map.put("allRequests", loyaltyCardService.listAllCardRequests)
    map.put("allCustomers", customerService.listAll)
    map.put("allUsers", userService.listAllUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/cardRequestList :: fragmentContent"
  }

  @GetMapping(value = Array("/getAllCards"))
  def getAllCards(map: ModelMap): String = {
    map.put("allCards", loyaltyCardService.listAllLoyaltyCards)
    map.put("allCustomers", customerService.listAll)
    map.put("allUsers", userService.listAllUsers)
    map.put("allCardRequests", loyaltyCardService.listAllCardRequests)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/loyaltyCardList :: fragmentContent"
  }

  @GetMapping(value = Array("/authorizeCard/{cardRequestId}"))
  def authorizeLoyaltyCard(@PathVariable("cardRequestId") cardRequestId: Integer, map: ModelMap): String = {
    loyaltyCardService.authorizeRequest(cardRequestId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/rejectCard/{cardRequestId}"))
  def rejectLoyaltyCard(@PathVariable("cardRequestId") cardRequestId: Integer, map: ModelMap): String = {
    loyaltyCardService.rejectRequest(cardRequestId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/allOrders"))
  def listAllOrders(map: ModelMap): String = {
    map.put("allOrders", orderService.listAll)
    map.put("allCustomers", customerService.listAll)
    map.put("allUsers", userService.listAllUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/orderList :: fragmentContent"
  }

  @GetMapping(value = Array("/allOrders/{customerId}"))
  def allOrdersByCustomerId(@PathVariable("customerId") customerId: Integer, map: ModelMap): String = {
    val allOrders = orderService.listAllByCustomerId(customerId)
    val customer = customerService.getCustomerById(customerId)
    val user = userService.getUserById(customer.getUserId)
    map.put("allOrders", allOrders)
    map.put("tempUser", user)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/customerOrderList :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteOrder/{orderId}"))
  def deleteOrder(@PathVariable("orderId") orderId: Integer): String = {
    orderService.deleteOrder(orderId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/getOrder/{orderId}"))
  def orderDetails(@PathVariable("orderId") orderId: Integer, map: ModelMap): String = {
    val order = orderService.getOrderById(orderId)
    val address = orderAddressService.getAddressById(order.getAddressId)
    val orderPrice = orderService.calculateOrderTotal(orderId)
    val orderedItems = orderItemService.listAllByOrderId(orderId)
    map.put("order", order)
    map.put("address", address)
    map.put("orderPrice", orderPrice.asInstanceOf[AnyRef])
    map.put("orderedItems", orderedItems)
    "fragments/orderDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/allCustomers"))
  def customerList(map: ModelMap): String = {
    map.put("allCustomers", customerService.listAll)
    map.put("allUsers", userService.listAllUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/customerList :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteCustomer/{customerId}"))
  def removeCustomer(@PathVariable("customerId") customerId: Integer): String = {
    customerService.deleteCustomer(customerId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/getCustomer/{customerId}"))
  def getCustomer(@PathVariable("customerId") customerId: Integer, map: ModelMap): String = {
    val customer = customerService.getCustomerById(customerId)
    val tempUser = userService.getUserById(customer.getUserId)
    val address = addressService.getAddressById(customer.getDeliveryAddressId)
    val persistenceOption = Option(persistenceService.getLastLogin(customerId))
    val ordersValue = orderService.calculateOrdersValue(customer.getCartId)
    map.put("tempCustomer", customer)
    map.put("tempUser", tempUser)
    map.put("address", address)
    map.put("persistenceOption", persistenceOption)
    map.put("ordersValue", ordersValue.asInstanceOf[AnyRef])
    "fragments/customerDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/suspendUser/{userId}"))
  def suspendUser(@PathVariable("userId") userId: Integer): String = {
    userService.suspendUser(userId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/reactivateUser/{userId}"))
  def reactivateUser(@PathVariable("userId") userId: Integer): String = {
    userService.clearSuspension(userId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/invalidPath"))
  def invalidImagePath = "fragments/invalidImagePath :: fragmentContent"

  @GetMapping(value = Array("/genreError"))
  def fireGenreExc = "fragments/genreError :: fragmentContent"

  @GetMapping(value = Array("/addImage/{bookId}"))
  def renderImageForm(@PathVariable("bookId") bookId: Long, map: ModelMap): String = {
    map.put("bookId", bookId.asInstanceOf[AnyRef])
    "fragments/imageForm :: fragmentContent"
  }

  @PostMapping(value = Array("/storeImage/{bookId}"))
  def storeImage(@RequestPart("file") file: MultipartFile, @PathVariable("bookId") bookId: Integer): String = {
    imageService.addImage(file, bookId)
    "fragments/homePage :: fragmentContent"
  }
}
