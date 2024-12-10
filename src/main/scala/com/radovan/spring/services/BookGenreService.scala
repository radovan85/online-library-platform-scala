package com.radovan.spring.services

import com.radovan.spring.dto.BookGenreDto

trait BookGenreService {

  def addGenre(genre:BookGenreDto):BookGenreDto

  def getGenreById(genreId:Integer):BookGenreDto

  def getGenreByName(name:String):BookGenreDto

  def deleteGenre(genreId:Integer):Unit

  def updateGenre(genreId:Integer,genre:BookGenreDto):BookGenreDto

  def listAll:Array[BookGenreDto]


}
