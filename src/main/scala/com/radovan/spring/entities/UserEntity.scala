package com.radovan.spring.entities

import jakarta.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, JoinTable, ManyToMany, Table}
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "users")
@SerialVersionUID(1L)
class UserEntity extends UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @BeanProperty var id: Integer = _

  @Column(nullable = false, name = "first_name", length = 30)
  @BeanProperty var firstName: String = _

  @Column(nullable = false, name = "last_name", length = 30)
  @BeanProperty var lastName: String = _

  @Column(nullable = false, unique = true, length = 50)
  @BeanProperty var email: String = _

  @Column(nullable = false)
  @BeanProperty var password: String = _

  @BeanProperty var enabled: Byte = _

  @ManyToMany(fetch = FetchType.EAGER, cascade = Array(CascadeType.MERGE))
  @JoinTable(name = "users_roles", joinColumns = Array(new JoinColumn(name = "User_id")), inverseJoinColumns = Array(new JoinColumn(name = "roles_id")))
  @Fetch(FetchMode.SUBSELECT)
  @BeanProperty var roles: util.List[RoleEntity] = _

  def this(firstName: String, lastName: String, email: String, password: String, enabled: Byte) {
    this()
    this.firstName = firstName
    this.lastName = lastName
    this.email = email
    this.password = password
    this.enabled = enabled
  }

  override def getAuthorities: util.Collection[_ <: GrantedAuthority] = roles

  override def getUsername: String = email

  override def isAccountNonExpired: Boolean = true

  override def isAccountNonLocked: Boolean = true

  override def isCredentialsNonExpired: Boolean = true

  override def isEnabled: Boolean = true

  override def toString: String = s"UserEntity [id=$id, firstName=$firstName, lastName=$lastName, email=$email, password=$password, enabled=$enabled]"

}

