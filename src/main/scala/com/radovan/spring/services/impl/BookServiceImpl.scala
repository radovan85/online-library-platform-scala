package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.BookDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.{BookRepository, CartItemRepository, WishListRepository}
import com.radovan.spring.services.{BookService, CartItemService, CartService, CustomerService, ReviewService, WishListService}
import com.radovan.spring.utils.RandomStringUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.text.DecimalFormat
import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

@Service
class BookServiceImpl extends BookService {

  private var bookRepository: BookRepository = _
  private var tempConverter: TempConverter = _
  private var randomStringUtil: RandomStringUtil = _
  private var wishListRepository: WishListRepository = _
  private var cartItemRepository: CartItemRepository = _
  private var cartService: CartService = _
  private var cartItemService: CartItemService = _
  private var customerService: CustomerService = _
  private var wishListService: WishListService = _
  private var reviewService:ReviewService = _
  private val decfor = new DecimalFormat("0.00")

  @Autowired
  private def injectAll(bookRepository: BookRepository, tempConverter: TempConverter, randomStringUtil: RandomStringUtil,
                        wishListRepository: WishListRepository, cartItemRepository: CartItemRepository, cartService: CartService,
                        cartItemService: CartItemService, customerService: CustomerService, wishListService: WishListService,
                        reviewService: ReviewService): Unit = {
    this.bookRepository = bookRepository
    this.tempConverter = tempConverter
    this.randomStringUtil = randomStringUtil
    this.wishListRepository = wishListRepository
    this.cartItemRepository = cartItemRepository
    this.cartService = cartService
    this.cartItemService = cartItemService
    this.customerService = customerService
    this.wishListService = wishListService
    this.reviewService = reviewService
  }

  @Transactional
  override def addBook(book: BookDto): BookDto = {
    val bookIdOption = Option(book.getId)
    bookIdOption match {
      case Some(bookId) =>
        val avgRating = calculateAverageRating(bookId)
        book.setAverageRating(avgRating)
      case None =>
    }

    val isbnOption = Option(book.getIsbn)
    isbnOption match {
      case Some(_) =>
      case None =>
        book.setIsbn(randomStringUtil.getAlphaNumericString(13).toUpperCase())
    }

    val storedBook = bookRepository.save(tempConverter.bookDtoToEntity(book))
    val returnValue = tempConverter.bookEntityToDto(storedBook)

    bookIdOption match {
      case Some(bookId) =>
        val allCartItems = cartItemRepository.findAllByBookId(bookId).asScala
        if (allCartItems.nonEmpty) {
          allCartItems.foreach(itemEntity => {
            var itemPrice = returnValue.getPrice * itemEntity.getQuantity
            if (cartItemService.hasDiscount(itemEntity.getId)) {
              itemPrice = itemPrice - ((itemPrice / 100) * 35)
            }

            itemPrice = decfor.format(itemPrice).toFloat
            itemEntity.setPrice(itemPrice)
            cartItemRepository.saveAndFlush(itemEntity)
          })

          cartService.refreshAllCarts()
        }
      case None =>
    }

    returnValue
  }

  @Transactional(readOnly = true)
  override def getBookById(bookId: Integer): BookDto = {
    val bookEntity = bookRepository.findById(bookId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The book has not been found!")))
    tempConverter.bookEntityToDto(bookEntity)
  }

  @Transactional(readOnly = true)
  override def getBookByIsbn(isbn: String): BookDto = {
    val bookOption = bookRepository.findByIsbn(isbn)
    bookOption match {
      case Some(bookEntity) =>
        tempConverter.bookEntityToDto(bookEntity)
      case None => throw new InstanceUndefinedException(new Error("The book has not been found!"))
    }
  }

  @Transactional
  override def deleteBook(bookId: Integer): Unit = {
    removeBookFromAllWishlist(bookId)
    cartItemService.eraseAllByBookId(bookId)
    bookRepository.deleteById(bookId)
    bookRepository.flush()
  }

  @Transactional(readOnly = true)
  override def listAll: Array[BookDto] = {
    val allBooks = bookRepository.findAll().asScala
    allBooks.collect {
      case book => tempConverter.bookEntityToDto(book)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def listAllByGenreId(genreId: Integer): Array[BookDto] = {
    val allBooks = listAll
    allBooks.collect {
      case book if book.genreId == genreId => book
    }
  }

  @Transactional(readOnly = true)
  override def search(keyword: String): Array[BookDto] = {
    val allBooks = bookRepository.findAllByKeyword(keyword).asScala
    allBooks.collect {
      case book => tempConverter.bookEntityToDto(book)
    }.toArray
  }

  @Transactional
  override def addToWishList(bookId: Integer): Unit = {
    val customer = customerService.getCurrentCustomer
    val wishList = wishListService.getWishListById(customer.getWishListId)
    val book = getBookById(bookId)
    val booksIds = wishList.getBooksIds.toBuffer
    if (!booksIds.contains(book.getId)) {
      booksIds += book.getId
      wishList.setBooksIds(booksIds.toArray)
      wishListRepository.saveAndFlush(tempConverter.wishListDtoToEntity(wishList))
    }
  }

  @Transactional(readOnly = true)
  override def listAllFromWishList: Array[BookDto] = {
    val customer = customerService.getCurrentCustomer
    val wishList = wishListService.getWishListById(customer.getWishListId)
    val books = new ArrayBuffer[BookDto]()
    wishList.getBooksIds.foreach(bookId => {
      books += getBookById(bookId)
    })

    books.toArray
  }

  @Transactional
  override def removeFromWishList(bookId: Integer): Unit = {
    val customer = customerService.getCurrentCustomer
    val wishList = wishListService.getWishListById(customer.getWishListId)
    val booksIds = wishList.getBooksIds.toBuffer
    booksIds -= bookId
    wishList.setBooksIds(booksIds.toArray)
    wishListRepository.saveAndFlush(tempConverter.wishListDtoToEntity(wishList))
  }

  @Transactional(readOnly = true)
  override def listAllByBookId: Array[BookDto] = {
    val allBooks = listAll
    allBooks.sortBy(_.getId)
  }

  @Transactional(readOnly = true)
  override def listAllByRating: Array[BookDto] = {
    val allBooks = listAll
    allBooks.sortBy(-_.getAverageRating)
  }

  @Transactional(readOnly = true)
  override def listAllByPrice: Array[BookDto] = {
    val allBooks = listAll
    allBooks.sortBy(_.getPrice)
  }

  @Transactional
  override def removeBookFromAllWishlist(bookId: Integer): Unit = {
    bookRepository.eraseBookFromAllWishlists(bookId)
    bookRepository.flush()
  }

  @Transactional
  override def refreshAvgRating(): Unit = {
    val allBooks = listAll
    allBooks.foreach(book => {
      val avgRating = calculateAverageRating(book.getId)
      book.setAverageRating(avgRating)
      bookRepository.saveAndFlush(tempConverter.bookDtoToEntity(book))
    })
  }

  def calculateAverageRating(bookId:Integer): Float ={
    var returnValue = 0f
    val allReviews = reviewService.listAllByBookId(bookId)
    allReviews.foreach(review => {
      returnValue = returnValue + review.getRating
    })
    if(allReviews.length > 0){
      returnValue = returnValue/allReviews.length
    }

    returnValue
  }
}
