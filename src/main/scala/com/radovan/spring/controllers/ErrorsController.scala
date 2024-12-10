package com.radovan.spring.controllers

import com.radovan.spring.exceptions.{BookQuantityException, DataNotValidatedException, ExistingInstanceException, FileUploadException, InstanceUndefinedException, InvalidCartException, SuspendedUserException}
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler}
import org.springframework.web.multipart.MultipartException

@ControllerAdvice
class ErrorsController {

  @ExceptionHandler(Array(classOf[MultipartException]))
  def handleMultipartException(request: HttpServletRequest, e: Exception) = new ResponseEntity[String]("Error: " + e.getMessage, HttpStatus.NOT_ACCEPTABLE)

  @ExceptionHandler(Array(classOf[FileUploadException]))
  def handleFileUploadException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.NOT_ACCEPTABLE)

  @ExceptionHandler(Array(classOf[InvalidCartException]))
  def handleInvalidCartException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.UNPROCESSABLE_ENTITY)

  @ExceptionHandler(Array(classOf[SuspendedUserException]))
  def handleSuspendedUserException(error: Error): ResponseEntity[String] = {
    SecurityContextHolder.clearContext()
    new ResponseEntity[String](error.getMessage, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
  }

  @ExceptionHandler(Array(classOf[BookQuantityException]))
  def handleBookQuantityException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.UNPROCESSABLE_ENTITY)

  @ExceptionHandler(Array(classOf[ExistingInstanceException]))
  def handleExistingInstanceException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.CONFLICT)

  @ExceptionHandler(Array(classOf[InstanceUndefinedException]))
  def handleInstanceUndefinedException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.UNPROCESSABLE_ENTITY)

  @ExceptionHandler(Array(classOf[DataNotValidatedException]))
  def handleDataNotValidatedException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.NOT_ACCEPTABLE)
}
