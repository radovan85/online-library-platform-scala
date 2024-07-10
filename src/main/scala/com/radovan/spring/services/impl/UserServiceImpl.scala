package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.UserDto
import com.radovan.spring.entities.UserEntity
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.{RoleRepository, UserRepository}
import com.radovan.spring.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class UserServiceImpl extends UserService{

  private var userRepository:UserRepository = _
  private var roleRepository:RoleRepository = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def injectAll(userRepository: UserRepository,roleRepository: RoleRepository,
                        tempConverter: TempConverter):Unit = {
    this.userRepository = userRepository
    this.roleRepository = roleRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def getUserById(userId: Integer): UserDto = {
    val userEntity = userRepository.findById(userId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The user has not been found!")))
    tempConverter.userEntityToDto(userEntity)
  }

  @Transactional(readOnly = true)
  override def listAllUsers: Array[UserDto] = {
    val allUsers = userRepository.findAll().asScala
    allUsers.collect{
      case userEntity => tempConverter.userEntityToDto(userEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getUserByEmail(email: String): UserDto = {
    val userOption = userRepository.findByEmail(email)
    userOption match {
      case Some(user) => tempConverter.userEntityToDto(user)
      case None => throw new InstanceUndefinedException(new Error("The user has not been found!"))
    }
  }

  @Transactional(readOnly = true)
  override def getCurrentUser: UserDto = {
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    getUserById(authUser.getId)
  }

  @Transactional
  override def suspendUser(userId: Integer): Unit = {
    val user = getUserById(userId)
    user.setEnabled(0.asInstanceOf[Short])
    userRepository.saveAndFlush(tempConverter.userDtoToEntity(user))
  }

  @Transactional
  override def clearSuspension(userId: Integer): Unit = {
    val user = getUserById(userId)
    user.setEnabled(1.asInstanceOf[Short])
    userRepository.saveAndFlush(tempConverter.userDtoToEntity(user))
  }

  @Transactional
  override def updateUser(userId: Integer, user: UserDto): UserDto = {
    getUserById(userId)
    user.setId(userId)
    val updatedUser = userRepository.saveAndFlush(tempConverter.userDtoToEntity(user))
    tempConverter.userEntityToDto(updatedUser)
  }

  @Transactional(readOnly = true)
  override def isAdmin: Boolean = {
    var returnValue = false
    val currentUser = getCurrentUser
    val roleAdminOption = roleRepository.findByRole("ADMIN")
    roleAdminOption match {
      case Some(roleEntity) =>
        val rolesIds = currentUser.getRolesIds
        if(rolesIds.contains(roleEntity.getId)){
          returnValue = true
        }
      case None =>
    }

    returnValue
  }
}
