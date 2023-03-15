package com.radovan.spring.entity

import java.sql.Timestamp

import javax.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty


@Entity
@Table(name = "persistence_logins")
@SerialVersionUID(1L)
class PersistenceLoginEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @Column(name = "created_at")
  @BeanProperty var createdAt:Timestamp = _

  @ManyToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _

}

