package com.radovan.spring.services

import com.radovan.spring.dto.BookImageDto
import org.springframework.web.multipart.MultipartFile

trait BookImageService {

  def addImage(file:MultipartFile,bookId:Integer):BookImageDto

  def deleteImage(imageId:Integer):Unit

  def listAll:Array[BookImageDto]

  def getImageById(imageId:Integer):BookImageDto
}
