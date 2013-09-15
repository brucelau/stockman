package com.mlh.stockman.core

import com.datastax.driver.core.{ Cluster, ProtocolOptions, Session }
import com.datastax.driver.core.exceptions._
import com.typesafe.scalalogging.slf4j.Logging
import com.mlh.stockman.StockmanConfig

trait CassandraCluster {
  def cluster: Cluster
  val clusterSession: Session
  val session: Session
}

class CassandraClient extends CassandraCluster with Logging {
  //def system: ActorSystem

  import StockmanConfig.CassandraConfig._

  lazy val clusterSession = cluster.connect()
  lazy val session = cluster.connect(db)

  lazy val cluster: Cluster =
    Cluster.builder().
    addContactPoints(hosts).
    withPort(port).
    withoutMetrics().
    withCompression(ProtocolOptions.Compression.SNAPPY).
    build()

  def ensureKeyspace = {

    try {
      clusterSession.execute(s"""CREATE KEYSPACE ${db} WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '3' }""")

      session.execute("""
        CREATE TABLE portfolios (
          userId uuid PRIMARY KEY,
          portfolioId uuid,
          name text
        ) WITH COMPACT STORAGE""")

    } catch {
      case aee: AlreadyExistsException => println("aee: " + aee)
      case e: Exception => println("ex: " + e)
    }
  }

  def dropKeyspace = {
    try {
      session.execute(s"""DROP KEYSPACE ${db}""")
    } catch {
      case e: Exception => println("ex: " + e)
    }
  }

  def printInfo = {
    //val md = cluster.getMetadata

    //log.info("Hosts: " + md.getAllHosts.map(h => h.toString).mkString(","))
    //log.info("Cluster: " + md.getClusterName)
    //log.info("Keyspaces: " + md.getKeyspaces.map(k => k.getName).mkString(","))
  }
}



