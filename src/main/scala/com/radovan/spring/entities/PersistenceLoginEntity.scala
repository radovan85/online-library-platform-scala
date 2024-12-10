package com.radovan.spring.entities

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import java.sql.Timestamp
import scala.beans.BeanProperty

@Entity
@Table(name = "persistence_logins")
@SerialVersionUID(1L)
class PersistenceLoginEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(name = "create_time", nullable = false)
  @BeanProperty var createTime:Timestamp = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id", nullable = false)
  @BeanProperty var customer:CustomerEntity = _

}
