package com.radovan.spring.service.impl

import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.BookDto
import com.radovan.spring.entity.UserEntity
import com.radovan.spring.repository.{BookRepository, CartItemRepository, CartRepository, CustomerRepository, WishListRepository}
import com.radovan.spring.service.{BookService, CartItemService, CartService}
import com.radovan.spring.utils.RandomStringUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class BookServiceImpl extends BookService {

  @Autowired
  private var bookRepository:BookRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  @Autowired
  private var randomStringUtil:RandomStringUtil = _

  @Autowired
  private var customerRepository:CustomerRepository = _

  @Autowired
  private var wishListRepository:WishListRepository = _

  @Autowired
  private var cartRepository:CartRepository = _

  @Autowired
  private var cartItemRepository:CartItemRepository = _

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var cartItemService:CartItemService = _

  override def addBook(book: BookDto): BookDto = {
    val bookId = Optional.ofNullable(book.getBookId)
    if(bookId.isPresent){
      val avgRating = Optional.ofNullable(bookRepository.calculateAverageRating(bookId.get()))
      if(avgRating.isPresent){
        book.setAverageRating(avgRating.get)
      }
    }
    val isbn = Optional.ofNullable(book.getISBN)
    if (!isbn.isPresent) book.setISBN(randomStringUtil.getAlphaNumericString(13).toUpperCase)
    val bookEntity = tempConverter.bookDtoToEntity(book)
    val storedBook = bookRepository.save(bookEntity)
    val returnValue = tempConverter.bookEntityToDto(storedBook)
    if (bookId.isPresent) {
      val allCartItems = Optional.ofNullable(cartItemRepository.findAllByBookId(returnValue.getBookId))
      if (!allCartItems.isEmpty) {
        for (itemEntity <- allCartItems.get.asScala) {
          var itemPrice = returnValue.getPrice * itemEntity.getQuantity
          if (cartItemService.hasDiscount(itemEntity.getCartItemId)) itemPrice = itemPrice - ((itemPrice / 100) * 35)
          itemEntity.setPrice(itemPrice)
          cartItemRepository.saveAndFlush(itemEntity)
        }
        val allCarts = Optional.ofNullable(cartRepository.findAll)
        if (!allCarts.isEmpty) {
          for (cartEntity <- allCarts.get.asScala) {
            cartService.refreshCartState(cartEntity.getCartId)
          }
        }
      }
    }
    returnValue
  }

  override def getBookById(bookId: Integer): BookDto = {
    var returnValue:BookDto = null
    val bookEntity = Optional.ofNullable(bookRepository.getById(bookId))
    if (bookEntity.isPresent) returnValue = tempConverter.bookEntityToDto(bookEntity.get)
    returnValue
  }

  override def getBookByISBN(isbn: String): BookDto = {
    var returnValue:BookDto = null
    val bookEntity = Optional.ofNullable(bookRepository.findByISBN(isbn))
    if (bookEntity.isPresent) returnValue = tempConverter.bookEntityToDto(bookEntity.get)
    returnValue
  }

  override def deleteBook(bookId: Integer): Unit = {
    bookRepository.deleteById(bookId)
    bookRepository.flush()
  }

  override def listAll: util.List[BookDto] = {
    val returnValue = new util.ArrayList[BookDto]
    val allBooks = Optional.ofNullable(bookRepository.findAll)
    if (!allBooks.isEmpty) {
      for (book <- allBooks.get.asScala) {
        val bookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  override def listAllByGenreId(genreId: Integer): util.List[BookDto] = {
    val returnValue = new util.ArrayList[BookDto]
    val allBooks = Optional.ofNullable(bookRepository.findAllByGenreId(genreId))
    if (!allBooks.isEmpty) {
      for (book <- allBooks.get.asScala) {
        val bookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  override def search(keyword: String): util.List[BookDto] = {
    val returnValue = new util.ArrayList[BookDto]
    val allBooks = Optional.ofNullable(bookRepository.findAllByKeyword(keyword))
    if (!allBooks.isEmpty) {
      for (book <- allBooks.get.asScala) {
        val bookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  override def addToWishList(bookId: Integer): Unit = {
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerEntity = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    if (customerEntity.isPresent) {
      val customerId = customerEntity.get.getCustomerId
      val wishListEntity = Optional.ofNullable(wishListRepository.findByCustomerId(customerId))
      if (wishListEntity.isPresent) {
        val bookEntity = Optional.ofNullable(bookRepository.getById(bookId))
        if (bookEntity.isPresent) {
          val wishListValue = wishListEntity.get
          val bookValue = bookEntity.get
          val booksList = wishListValue.getBooks
          val booksIds = Optional.ofNullable(wishListRepository.findBookIds(wishListValue.getWishListId))
          if (!booksIds.isEmpty) if (!booksIds.get.contains(bookValue.getBookId)) {
            booksList.add(bookValue)
            wishListValue.setBooks(booksList)
            wishListRepository.saveAndFlush(wishListValue)
          }
        }
      }
    }
  }

  override def listAllFromWishList: util.List[BookDto] = {
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val returnValue = new util.ArrayList[BookDto]
    val customerEntity = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    if (customerEntity.isPresent) {
      val wishListEntity = Optional.ofNullable(wishListRepository.findByCustomerId(customerEntity.get.getCustomerId))
      if (wishListEntity.isPresent) {
        val allBooks = Optional.ofNullable(wishListEntity.get.getBooks)
        if (!allBooks.isEmpty) {
          for (book <- allBooks.get.asScala) {
            val bookDto = tempConverter.bookEntityToDto(book)
            returnValue.add(bookDto)
          }
        }
      }
    }
    returnValue
  }

  override def removeFromWishList(bookId: Integer): Unit = {
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerEntity = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    if (customerEntity.isPresent) {
      val wishListEntity = Optional.ofNullable(wishListRepository.findByCustomerId(customerEntity.get.getCustomerId))
      if (wishListEntity.isPresent) {
        val wishList = tempConverter.wishListEntityToDto(wishListEntity.get)
        val booksIds = wishList.getBooksIds
        booksIds.remove(Integer.valueOf(bookId))
        wishList.setBooksIds(booksIds)
        val wishListValue = tempConverter.wishListDtoToEntity(wishList)
        wishListRepository.saveAndFlush(wishListValue)
      }
    }
  }

  override def listAllByBookId: util.List[BookDto] = {
    val returnValue = new util.ArrayList[BookDto]
    val allBooks = Optional.ofNullable(bookRepository.findAllSortedById)
    if (!allBooks.isEmpty) {
      for (book <- allBooks.get.asScala) {
        val bookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  override def listAllByRating: util.List[BookDto] = {
    val returnValue = new util.ArrayList[BookDto]
    val allBooks = Optional.ofNullable(bookRepository.findAllSortedByRating)
    if (!allBooks.isEmpty) {
      for (book <- allBooks.get.asScala) {
        val bookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  override def listAllByPrice: util.List[BookDto] = {
    val returnValue = new util.ArrayList[BookDto]
    val allBooks = Optional.ofNullable(bookRepository.findAllSortedByPrice)
    if (!allBooks.isEmpty) {
      for (book <- allBooks.get.asScala) {
        val bookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  override def removeBookFromAllWishlist(bookId: Integer): Unit = {
    bookRepository.eraseBookFromAllWishlists(bookId)
    bookRepository.flush()
  }

  override def refreshAvgRating(): Unit = {
    val allBooks = Optional.ofNullable(bookRepository.findAll)
    if (!allBooks.isEmpty) {
      for (book <- allBooks.get.asScala) {
        val avgRatingOpt = Optional.ofNullable(bookRepository.calculateAverageRating(book.getBookId))
        if (avgRatingOpt.isPresent) book.setAverageRating(avgRatingOpt.get)
        else book.setAverageRating(null)
        bookRepository.saveAndFlush(book)
      }
    }
  }
}

