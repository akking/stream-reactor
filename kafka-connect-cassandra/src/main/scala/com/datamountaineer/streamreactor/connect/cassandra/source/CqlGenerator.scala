package com.datamountaineer.streamreactor.connect.cassandra.source

import com.datamountaineer.streamreactor.connect.cassandra.config.CassandraSourceSetting
import com.landoop.connect.sql.StructSql
import com.typesafe.scalalogging.slf4j.StrictLogging
import org.apache.kafka.common.config.ConfigException

import scala.collection.JavaConversions._

class CqlGenerator(private val setting: CassandraSourceSetting) extends StrictLogging {

  private val config = setting.kcql
  private val table = config.getSource
  private val keySpace = setting.keySpace
  private val selectColumns = getSelectColumns
  private val incrementMode = determineMode
  private val limitRowsSize = config.getBatchSize

  private val defaultTimestamp = "1900-01-01 00:00:00.0000000Z"

  /**
    * Build the CQL for the given table.
    *
    * @return the CQL statement (as a String)
    */
  def getCqlStatement: String = {
    // build the correct CQL statement based on the KCQL mode
    val selectStatement = if (setting.bulkImportMode) {
      generateCqlForBulkMode
    } else {
      // if we are not in bulk mode 
      // we must be in incremental mode
      logger.info(s"the increment mode is $incrementMode")
      incrementMode.toUpperCase match {
        case "TIMEUUID" => generateCqlForTimeUuidMode
        case "TIMESTAMP" => generateCqlForTimestampMode
        case "TOKEN" => generateCqlForTokenMode
        case _ => throw new ConfigException(s"unknown incremental mode ($incrementMode)")
      }
    }
    logger.info(s"generated CQL: $selectStatement")
    selectStatement
  }

  /**
    * Build the CQL for the given table when no offset is available.
    *
    * @return the CQL statement (as a String)
    */
  def getCqlStatementNoOffset: String = {
    // build the correct CQL statement based on the KCQL mode
    val selectStatement = if (setting.bulkImportMode) {
      generateCqlForBulkMode
    } else {
      // if we are not in bulk mode 
      // we must be in incremental mode
      logger.info(s"the increment mode is $incrementMode")
      incrementMode.toUpperCase match {
        case "TIMEUUID" => generateCqlForTimeUuidMode
        case "TIMESTAMP" => generateCqlForTimestampMode
        case "TOKEN" => generateCqlForTokenModeNoOffset
        case _ => throw new ConfigException(s"unknown incremental mode ($incrementMode)")
      }
    }
    logger.info(s"generated CQL: $selectStatement")
    selectStatement
  }

  def getDefaultOffsetValue(offset: Option[String]): Option[String] = {
    incrementMode.toUpperCase match {
      case "TIMESTAMP" | "TIMEUUID" => Some(offset.getOrElse(defaultTimestamp))
      case "TOKEN" => offset
    }
  }

  def isTokenBased() = {
    incrementMode.toUpperCase match {
      case "TIMESTAMP" | "TIMEUUID" => false
      case "TOKEN" => true
    }
  }

  /**
    * get the columns for the SELECT statement
    *
    * @return the comma separated columns
    */
  private def getSelectColumns(): String = {
    val fieldList = config.getFields.map(fa => fa.getName)
    // if no columns set then select all the columns in the table
    val selectColumns = if (fieldList == null || fieldList.isEmpty) "*" else fieldList.mkString(",")
    logger.debug(s"the fields to select are $selectColumns")
    selectColumns
  }

  private def checkCqlForPrimaryKey(pkCol: String) = {
    logger.info(s"checking CQL for PK: $pkCol")
    if (!selectColumns.contains(pkCol) && !selectColumns.contentEquals("*")) {
      val msg = s"the primary key column (pkCol) must appear in the SELECT statement"
      logger.error(msg)
      throw new ConfigException(msg)
    }
  }

  private def generateCqlForTokenMode: String = {
    val pkCol = setting.primaryKeyColumn.getOrElse("")
    checkCqlForPrimaryKey(pkCol)
    val whereClause = s" WHERE token($pkCol) > token(?) LIMIT $limitRowsSize"
    generateCqlForBulkMode + whereClause
  }

  private def generateCqlForTokenModeNoOffset: String = {
    val pkCol = setting.primaryKeyColumn.getOrElse("")
    checkCqlForPrimaryKey(pkCol)
    val whereClause = s" LIMIT $limitRowsSize"
    generateCqlForBulkMode + whereClause
  }

  private def generateCqlForTimeUuidMode: String = {
    val pkCol = setting.primaryKeyColumn.getOrElse("")
    checkCqlForPrimaryKey(pkCol)
    val whereClause = s" WHERE $pkCol > maxTimeuuid(?) AND $pkCol <= minTimeuuid(?) ALLOW FILTERING"
    generateCqlForBulkMode + whereClause
  }

  private def generateCqlForTimestampMode: String = {
    val pkCol = setting.primaryKeyColumn.getOrElse("")
    checkCqlForPrimaryKey(pkCol)
    val whereClause = s" WHERE $pkCol > ? AND $pkCol <= ? ALLOW FILTERING"
    generateCqlForBulkMode + whereClause
  }

  private def generateCqlForBulkMode: String = {
    s"SELECT $selectColumns FROM $keySpace.$table"
  }

  /**
    * determine the incremental mode in use
    * if the INCREMENTALMODE is used in KCQL it
    * will take precedence over the configuration
    * setting
    *
    * @return the incremental mode
    */
  private def determineMode: String = {
    val incMode = if (config.getIncrementalMode != null && !config.getIncrementalMode.isEmpty) {
      config.getIncrementalMode.toUpperCase()
    } else {
      setting.timestampColType.toString()
    }
    incMode
  }

}