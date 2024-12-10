package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.BookGenreDto
import com.radovan.spring.exceptions.{ExistingInstanceException, InstanceUndefinedException}
import com.radovan.spring.repositories.BookGenreRepository
import com.radovan.spring.services.{BookGenreService, BookService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._

@Service
class BookGenreServiceImpl extends BookGenreService{

  private var genreRepository:BookGenreRepository = _
  private var tempConverter:TempConverter = _
  private var bookService:BookService = _

  @Autowired
  private def injectAll(genreRepository: BookGenreRepository,tempConverter: TempConverter,bookService: BookService):Unit = {
    this.genreRepository = genreRepository
    this.tempConverter = tempConverter
    this.bookService = bookService
  }

  @Transactional
  override def addGenre(genre: BookGenreDto): BookGenreDto = {
    val name = genre.getName
    val genreOptional = genreRepository.findByName(name)
    genreOptional match {
      case Some(_) => throw new ExistingInstanceException(new Error("This genre exists already!"))
      case None =>
    }

    val storedGenre = genreRepository.save(tempConverter.bookGenreDtoToEntity(genre))
    tempConverter.bookGenreEntityToDto(storedGenre)
  }

  @Transactional(readOnly = true)
  override def getGenreById(genreId: Integer): BookGenreDto = {
    val genreEntity = genreRepository.findById(genreId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The genre has not been found!")))
    tempConverter.bookGenreEntityToDto(genreEntity)
  }

  @Transactional(readOnly = true)
  override def getGenreByName(name: String): BookGenreDto = {
    val genreEntity = genreRepository.findByName(name).getOrElse(throw new InstanceUndefinedException(new Error("The genre has not been found!")))
    tempConverter.bookGenreEntityToDto(genreEntity)
  }

  @Transactional
  override def deleteGenre(genreId: Integer): Unit = {
    getGenreById(genreId)
    val allBooks = bookService.listAllByGenreId(genreId)
    allBooks.foreach(book => {
      bookService.deleteBook(book.getId)
    })

    genreRepository.deleteById(genreId)
    genreRepository.flush()
  }

  @Transactional
  override def updateGenre(genreId: Integer, genre: BookGenreDto): BookGenreDto = {
    val tempGenre = getGenreById(genreId)
    val name = genre.getName
    val genreOption = genreRepository.findByName(name)
    genreOption match {
      case Some(genreEntity) =>
        if(genreEntity.getId != tempGenre.getId){
          throw new ExistingInstanceException(new Error("This genre exists already!"))
        }
      case None =>
    }

    genre.setId(genreId)
    val updatedGenre = genreRepository.saveAndFlush(tempConverter.bookGenreDtoToEntity(genre))
    tempConverter.bookGenreEntityToDto(updatedGenre)
  }

  @Transactional(readOnly = true)
  override def listAll: Array[BookGenreDto] = {
    val allGenres = genreRepository.findAll().asScala
    allGenres.collect{
      case genre => tempConverter.bookGenreEntityToDto(genre)
    }.toArray
  }
}
