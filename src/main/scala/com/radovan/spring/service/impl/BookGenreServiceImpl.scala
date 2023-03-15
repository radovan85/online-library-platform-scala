package com.radovan.spring.service.impl

import java.util
import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.BookGenreDto
import com.radovan.spring.exceptions.DeleteGenreException
import com.radovan.spring.repository.{BookGenreRepository, BookRepository}
import com.radovan.spring.service.BookGenreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
@Transactional
class BookGenreServiceImpl extends BookGenreService {

  @Autowired
  private var genreRepository:BookGenreRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  @Autowired
  private var bookRepository:BookRepository = _

  override def addGenre(genre: BookGenreDto): BookGenreDto = {
    val genreEntity = tempConverter.bookGenreDtoToEntity(genre)
    val storedGenre = genreRepository.save(genreEntity)
    val returnValue = tempConverter.bookGenreEntityToDto(storedGenre)
    returnValue
  }

  override def getGenreById(genreId: Integer): BookGenreDto = {
    var returnValue:BookGenreDto = null
    val genreEntity = Optional.ofNullable(genreRepository.getById(genreId))
    if (genreEntity.isPresent) returnValue = tempConverter.bookGenreEntityToDto(genreEntity.get)
    returnValue
  }

  override def getGenreByName(name: String): BookGenreDto = {
    var returnValue:BookGenreDto = null
    val genreEntity = Optional.ofNullable(genreRepository.findByName(name))
    if (genreEntity.isPresent) returnValue = tempConverter.bookGenreEntityToDto(genreEntity.get)
    returnValue
  }

  override def deleteGenre(genreId: Integer): Unit = {
    val books = bookRepository.findAllByGenreId(genreId)
    if (!books.isEmpty) {
      val error = new Error("Genre contains books")
      throw new DeleteGenreException(error)
    }
    genreRepository.deleteById(genreId)
    genreRepository.flush()
  }

  override def listAll: util.List[BookGenreDto] = {
    val returnValue = new util.ArrayList[BookGenreDto]
    val allGenres = Optional.ofNullable(genreRepository.findAll)
    if (!allGenres.isEmpty) {
      for (genre <- allGenres.get.asScala) {
        val genreDto = tempConverter.bookGenreEntityToDto(genre)
        returnValue.add(genreDto)
      }
    }
    returnValue
  }
}

