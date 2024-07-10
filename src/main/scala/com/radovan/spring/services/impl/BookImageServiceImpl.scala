package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.BookImageDto
import com.radovan.spring.exceptions.{FileUploadException, InstanceUndefinedException}
import com.radovan.spring.repositories.BookImageRepository
import com.radovan.spring.services.{BookImageService, BookService}
import com.radovan.spring.utils.FileValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

import java.util.Objects
import scala.collection.JavaConverters._

@Service
class BookImageServiceImpl extends BookImageService {

  private var imageRepository: BookImageRepository = _
  private var tempConverter: TempConverter = _
  private var fileValidator: FileValidator = _
  private var bookService: BookService = _

  @Autowired
  private def injectAll(imageRepository: BookImageRepository, tempConverter: TempConverter,
                        fileValidator: FileValidator, bookService: BookService): Unit = {
    this.imageRepository = imageRepository
    this.tempConverter = tempConverter
    this.fileValidator = fileValidator
    this.bookService = bookService
  }

  @Transactional
  override def addImage(file: MultipartFile, bookId: Integer): BookImageDto = {
    val book = bookService.getBookById(bookId)
    fileValidator.validateFile(file)
    val imageOption = imageRepository.findByBookId(bookId)
    imageOption match {
      case Some(imageEntity) =>
        deleteImage(imageEntity.getId)
      case None =>
    }

    try {
      val image = new BookImageDto
      image.setBookId(book.getId)
      image.setName(StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename)))
      image.setContentType(file.getContentType)
      image.setSize(file.getSize)
      image.setData(file.getBytes)
      val imageIdOption = Option(book.getImageId)
      imageIdOption match {
        case Some(imageId) => image.setId(imageId)
        case None =>
      }

      val storedImage = imageRepository.save(tempConverter.bookImageDtoToEntity(image))
      tempConverter.bookImageEntityToDto(storedImage)
    } catch {
      case e: Exception =>
        throw new FileUploadException(new Error("Failed to upload file: " + e.getMessage))
    }

  }

  @Transactional
  override def deleteImage(imageId: Integer): Unit = {
    imageRepository.deleteById(imageId)
    imageRepository.flush()
  }

  @Transactional(readOnly = true)
  override def listAll: Array[BookImageDto] = {
    val allImages = imageRepository.findAll().asScala
    allImages.collect {
      case imageEntity => tempConverter.bookImageEntityToDto(imageEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getImageById(imageId: Integer): BookImageDto = {
    val imageEntity = imageRepository.findById(imageId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The image has not been found!")))
    tempConverter.bookImageEntityToDto(imageEntity)
  }
}
