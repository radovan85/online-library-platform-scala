package com.radovan.spring.services

import com.radovan.spring.dto.PersistenceLoginDto

trait PersistenceLoginService {

  def addPersistenceLogin():PersistenceLoginDto

  def getLastLogin(customerId:Integer):PersistenceLoginDto
}
