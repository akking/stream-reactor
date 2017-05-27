/*
 * Copyright 2017 Datamountaineer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datamountaineer.streamreactor.connect.cassandra.config

import java.lang.Boolean
import java.net.ConnectException

import com.datamountaineer.kcql.{Field, Kcql}
import com.datamountaineer.streamreactor.connect.cassandra.config.TimestampType.TimestampType
import com.datamountaineer.streamreactor.connect.errors.{ErrorPolicy, ThrowErrorPolicy}
import com.datastax.driver.core.ConsistencyLevel
import com.typesafe.scalalogging.slf4j.StrictLogging
import org.apache.kafka.common.config.ConfigException

import scala.collection.JavaConversions._

/**
  * Created by andrew@datamountaineer.com on 22/04/16. 
  * stream-reactor
  */

trait CassandraSetting

object TimestampType extends Enumeration {
  type TimestampType = Value
  val TIMESTAMP, TIMEUUID, TOKEN = Value
}

case class CassandraSourceSetting(kcql: Kcql,
                                  keySpace: String,
                                  bulkImportMode: Boolean = true,
                                  primaryKeyColumn: Option[String] = None,
                                  timestampColType: TimestampType,
                                  pollInterval: Long = CassandraConfigConstants.DEFAULT_POLL_INTERVAL,
                                  consistencyLevel: Option[ConsistencyLevel],
                                  errorPolicy: ErrorPolicy = new ThrowErrorPolicy,
                                  taskRetires: Int = CassandraConfigConstants.NBR_OF_RETIRES_DEFAULT,
                                  fetchSize: Int = CassandraConfigConstants.FETCH_SIZE_DEFAULT
                                 ) extends CassandraSetting

case class CassandraSinkSetting(keySpace: String,
                                kcqls: Seq[Kcql],
                                fields: Map[String, Seq[Field]],
                                ignoreField: Map[String, Seq[Field]],
                                errorPolicy: ErrorPolicy,
                                threadPoolSize: Int,
                                consistencyLevel: Option[ConsistencyLevel],
                                taskRetries: Int = CassandraConfigConstants.NBR_OF_RETIRES_DEFAULT,
                                enableProgress: Boolean = CassandraConfigConstants.PROGRESS_COUNTER_ENABLED_DEFAULT) extends CassandraSetting

/**
  * Cassandra Setting used for both Readers and writers
  * Holds the table, topic, import mode and timestamp columns
  * Import mode and timestamp columns are only applicable for the source.
  **/
object CassandraSettings extends StrictLogging {

  def configureSource(config: CassandraConfigSource): Seq[CassandraSourceSetting] = {
    //get keyspace
    val keySpace = config.getString(CassandraConfigConstants.KEY_SPACE)
    require(!keySpace.isEmpty, CassandraConfigConstants.MISSING_KEY_SPACE_MESSAGE)
    val pollInterval = config.getLong(CassandraConfigConstants.POLL_INTERVAL)

    val bulk = config.getString(CassandraConfigConstants.IMPORT_MODE).toLowerCase() match {
      case CassandraConfigConstants.BULK => true
      case CassandraConfigConstants.INCREMENTAL => false
      case e => throw new ConnectException(s"Unsupported import mode $e.")
    }

    val consistencyLevel = config.getConsistencyLevel
    val timestampType = TimestampType.withName(config.getString(CassandraConfigConstants.TIMESTAMP_TYPE).toUpperCase)
    val errorPolicy = config.getErrorPolicy
    val kcqls = config.getKcql()
    val primaryKeyCols = kcqls.map(r => (r.getSource, r.getPrimaryKeys.toList)).toMap
    val fetchSize = config.getInt(CassandraConfigConstants.FETCH_SIZE)

    kcqls.map { r =>
      val tCols = primaryKeyCols(r.getSource)
      if (!bulk && tCols.size != 1) {
        throw new ConfigException("Only one primary key column is allowed to be specified in Incremental mode. " +
          s"Received ${tCols.mkString(",")} for source ${r.getSource}")
      }

      CassandraSourceSetting(
        kcql = r,
        keySpace = keySpace,
        primaryKeyColumn = if (bulk) None else Some(tCols.head),
        timestampColType = timestampType,
        bulkImportMode = bulk,
        pollInterval = pollInterval,
        errorPolicy = errorPolicy,
        consistencyLevel = consistencyLevel,
        fetchSize = fetchSize
      )
    }
  }

  def configureSink(config: CassandraConfigSink): CassandraSinkSetting = {
    //get keyspace
    val keySpace = config.getString(CassandraConfigConstants.KEY_SPACE)
    require(!keySpace.isEmpty, CassandraConfigConstants.MISSING_KEY_SPACE_MESSAGE)
    val errorPolicy = config.getErrorPolicy
    val retries = config.getNumberRetries
    val kcqls = config.getKcql()
    val fields = kcqls.map(k => k.getSource -> k.getFields.toSeq).toMap
    val ignoreFields = kcqls.map(k => k.getSource -> k.getIgnoredFields.toSeq).toMap
    val threadPoolSize = config.getThreadPoolSize
    val consistencyLevel = config.getConsistencyLevel

    val enableCounter = config.getBoolean(CassandraConfigConstants.PROGRESS_COUNTER_ENABLED)
    CassandraSinkSetting(keySpace,
      kcqls,
      fields,
      ignoreFields,
      errorPolicy,
      threadPoolSize,
      consistencyLevel,
      retries,
      enableCounter)
  }
}
