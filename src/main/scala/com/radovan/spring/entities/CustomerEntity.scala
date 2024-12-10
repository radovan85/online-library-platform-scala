package com.radovan.spring.entities

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToMany, OneToOne, Table}

import java.util
import java.sql.Timestamp
import java.time.LocalDate
import scala.beans.BeanProperty

@Entity
@Table(name = "customers")
@SerialVersionUID(1L)
class CustomerEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(name = "date_of_birth",nullable = false)
  @BeanProperty var dateOfBirth:LocalDate = _

  @Column(name = "registration_time",nullable = false)
  @BeanProperty var registrationTime:Timestamp = _

  @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "user_id", nullable = false)
  @BeanProperty var user:UserEntity = _

  @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "cart_id", nullable = false)
  @BeanProperty var cart:CartEntity = _

  @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "wishlist_id", nullable = false)
  @BeanProperty var wishList:WishListEntity = _

  @OneToOne(fetch = FetchType.EAGER,orphanRemoval = true,mappedBy = "customer")
  @BeanProperty var loyaltyCard:LoyaltyCardEntity = _

  @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "delivery_address_id")
  @BeanProperty var deliveryAddress: DeliveryAddressEntity= _

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", orphanRemoval = true)
  @BeanProperty var reviews:util.List[ReviewEntity]= _

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "customer",orphanRemoval = true)
  @BeanProperty var persistenceLogins:util.List[PersistenceLoginEntity]= _

}
