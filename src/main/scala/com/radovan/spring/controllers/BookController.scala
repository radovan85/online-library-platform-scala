package com.radovan.spring.controllers

import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.services.{BookGenreService, BookService, CustomerService, ReviewService, UserService}
import com.radovan.spring.utils.ThymeleafUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, PostMapping, RequestMapping}

@Controller
@RequestMapping(value = Array("/books"))
class BookController {

  private var bookService:BookService = _
  private var genreService:BookGenreService = _
  private var reviewService:ReviewService = _
  private var userService:UserService = _
  private var customerService:CustomerService = _
  private var thymeleafUtils:ThymeleafUtils = _

  @Autowired
  private def injectAll(bookService: BookService,genreService: BookGenreService,reviewService: ReviewService,
                        userService: UserService,customerService: CustomerService,thymeleafUtils: ThymeleafUtils):Unit = {

    this.bookService = bookService
    this.genreService = genreService
    this.reviewService = reviewService
    this.userService = userService
    this.customerService = customerService
    this.thymeleafUtils = thymeleafUtils
  }

  @GetMapping(value = Array("/allBooks"))
  def allBooksList(map: ModelMap): String = {
    map.put("allBooks", bookService.listAll)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    map.put("utils", thymeleafUtils)
    "fragments/bookList :: fragmentContent"
  }

  @GetMapping(value = Array("/allBooksById"))
  def allBooksListSortedById(map: ModelMap): String = {
    map.put("allBooks", bookService.listAllByBookId)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    map.put("utils", thymeleafUtils)
    "fragments/bookList :: fragmentContent"
  }

  @GetMapping(value = Array("/allBooksByRating"))
  def allBooksListSortedByRating(map: ModelMap): String = {
    map.put("allBooks", bookService.listAllByRating)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    map.put("utils", thymeleafUtils)
    "fragments/bookList :: fragmentContent"
  }

  @GetMapping(value = Array("/allBooksByPrice"))
  def allBooksListSortedByPrice(map: ModelMap): String = {
    map.put("allBooks", bookService.listAllByPrice)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    map.put("utils", thymeleafUtils)
    "fragments/bookList :: fragmentContent"
  }

  @GetMapping(value = Array("/bookDetails/{bookId}"))
  def getBookDetails(@PathVariable("bookId") bookId: Integer, map: ModelMap): String = {
    map.put("currentBook", bookService.getBookById(bookId))
    map.put("allGenres", genreService.listAll)
    map.put("allReviews", reviewService.listAllByBookId(bookId))
    map.put("allUsers", userService.listAllUsers)
    map.put("allCustomers", customerService.listAll)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    map.put("utils", thymeleafUtils)
    "fragments/bookDetails :: fragmentContent"
  }

  @PreAuthorize(value = "hasAuthority('ROLE_USER')")
  @PostMapping(value = Array("/addToWishList/{bookId}"))
  def toWishlist(@PathVariable("bookId") bookId: Integer): String = {
    bookService.addToWishList(bookId)
    "fragments/homePage :: fragmentContent"
  }

  @PreAuthorize(value = "hasAuthority('ROLE_USER')")
  @GetMapping(value = Array("/getWishList"))
  def getWishListBooks(map: ModelMap): String = {
    map.put("allBooks", bookService.listAllFromWishList)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    map.put("utils", thymeleafUtils)
    "fragments/wishlist :: fragmentContent"
  }

  @PreAuthorize(value = "hasAuthority('ROLE_USER')")
  @GetMapping(value = Array("/deleteFromWishList/{bookId}"))
  def deleteFromWishList(@PathVariable("bookId") bookId: Integer): String = {
    bookService.removeFromWishList(bookId)
    "fragments/homePage :: fragmentContent"
  }

  @PreAuthorize(value = "hasAuthority('ROLE_USER')")
  @GetMapping(value = Array("/addToCart/{bookId}"))
  def addBookToCart(@PathVariable("bookId") bookId: Integer, map: ModelMap): String = {
    map.put("selectedBook", bookService.getBookById(bookId))
    map.put("cartItem", new CartItemDto)
    map.put("utils", thymeleafUtils)
    "fragments/cartItemForm :: fragmentContent"
  }

  @GetMapping(value = Array("/searchBooks/{keyword}"))
  def searchBooks(@PathVariable("keyword") keyword: String, map: ModelMap): String = {
    map.put("allBooks", bookService.search(keyword))
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    map.put("utils", thymeleafUtils)
    "fragments/searchList :: fragmentContent"
  }
}
