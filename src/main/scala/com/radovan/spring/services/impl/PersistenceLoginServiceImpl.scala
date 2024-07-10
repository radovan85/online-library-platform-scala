package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.PersistenceLoginDto
import com.radovan.spring.entities.PersistenceLoginEntity
import com.radovan.spring.repositories.PersistenceLoginRepository
import com.radovan.spring.services.{CustomerService, PersistenceLoginService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PersistenceLoginServiceImpl extends PersistenceLoginService {

  private var persistenceRepository:PersistenceLoginRepository = _
  private var tempConverter:TempConverter = _
  private var customerService:CustomerService = _
  private var userService:UserService = _

  @Autowired
  private def injectAll(persistenceRepository: PersistenceLoginRepository,tempConverter: TempConverter,
                        customerService: CustomerService, userService: UserService):Unit = {
    this.persistenceRepository = persistenceRepository
    this.tempConverter = tempConverter
    this.customerService = customerService
    this.userService = userService
  }

  @Transactional
  override def addPersistenceLogin(): PersistenceLoginDto = {
    if(userService.isAdmin){
      null
    }else {
      val customer = customerService.getCurrentCustomer
      val persistence = new PersistenceLoginEntity
      persistence.setCustomer(tempConverter.customerDtoToEntity(customer))
      persistence.setCreateTime(tempConverter.getCurrentUTCTimestamp)
      val storedPersistence = persistenceRepository.save(persistence)
      tempConverter.persistenceLoginEntityToDto(storedPersistence)
    }
  }

  @Transactional(readOnly = true)
  override def getLastLogin(customerId: Integer): PersistenceLoginDto = {
    var returnValue:PersistenceLoginDto = null
    val customer = customerService.getCustomerById(customerId)
    val persistenceLogin = persistenceRepository.findLastLogin(customer.getId).getOrElse(null)
    if(persistenceLogin!=null){
      returnValue = tempConverter.persistenceLoginEntityToDto(persistenceLogin)
    }

    returnValue
  }
}
