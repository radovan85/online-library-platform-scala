package com.radovan.spring.services

import com.radovan.spring.dto.BookDto

trait BookService {

  def addBook(book:BookDto):BookDto

  def getBookById(bookId:Integer):BookDto

  def getBookByIsbn(isbn:String):BookDto

  def deleteBook(bookId:Integer):Unit

  def listAll:Array[BookDto]

  def listAllByGenreId(genreId:Integer):Array[BookDto]

  def search(keyword:String):Array[BookDto]

  def addToWishList(bookId:Integer):Unit

  def listAllFromWishList:Array[BookDto]

  def removeFromWishList(bookId:Integer):Unit

  def listAllByBookId:Array[BookDto]

  def listAllByRating:Array[BookDto]

  def listAllByPrice:Array[BookDto]

  def removeBookFromAllWishlist(bookId:Integer):Unit

  def refreshAvgRating():Unit


}
