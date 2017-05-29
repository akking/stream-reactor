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

package com.datamountaineer.streamreactor.connect.rethink.config

import java.util

import com.datamountaineer.streamreactor.temp._
import org.apache.kafka.common.config.ConfigDef.{Importance, Type}
import org.apache.kafka.common.config.{AbstractConfig, ConfigDef}

/**
  * Created by andrew@datamountaineer.com on 24/03/16. 
  * stream-reactor
  */
object ReThinkSinkConfig {
  val config: ConfigDef = new ConfigDef()
    .define(ReThinkConfigConstants.RETHINK_HOST, Type.STRING,
      ReThinkConfigConstants.RETHINK_HOST_DEFAULT,
      Importance.HIGH, ReThinkConfigConstants.RETHINK_HOST_DOC,
      "Connection", 1, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.RETHINK_HOST)
    .define(ReThinkConfigConstants.RETHINK_DB, Type.STRING,
      ReThinkConfigConstants.RETHINK_DB_DEFAULT,
      Importance.HIGH, ReThinkConfigConstants.RETHINK_DB_DOC,
      "Connection", 2, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.RETHINK_DB)
    .define(ReThinkConfigConstants.RETHINK_PORT, Type.INT,
      ReThinkConfigConstants.RETHINK_PORT_DEFAULT,
      Importance.MEDIUM, ReThinkConfigConstants.RETHINK_PORT_DOC,
      "Connection", 3, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.RETHINK_PORT)
    .define(ReThinkConfigConstants.SINK_ROUTE_QUERY, Type.STRING, Importance.HIGH,
      ReThinkConfigConstants.SINK_ROUTE_QUERY_DOC,
      "Connection", 4, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.SINK_ROUTE_QUERY)
    .define(ReThinkConfigConstants.ERROR_POLICY, Type.STRING,
      ReThinkConfigConstants.ERROR_POLICY_DEFAULT,
      Importance.HIGH, ReThinkConfigConstants.ERROR_POLICY_DOC,
      "Connection", 5, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.ERROR_POLICY)
    .define(ReThinkConfigConstants.ERROR_RETRY_INTERVAL, Type.INT,
      ReThinkConfigConstants.ERROR_RETRY_INTERVAL_DEFAULT,
      Importance.MEDIUM, ReThinkConfigConstants.ERROR_RETRY_INTERVAL_DOC,
      "Connection", 6, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.ERROR_RETRY_INTERVAL)
    .define(ReThinkConfigConstants.NBR_OF_RETRIES, Type.INT,
      ReThinkConfigConstants.NBR_OF_RETIRES_DEFAULT,
      Importance.MEDIUM, ReThinkConfigConstants.NBR_OF_RETRIES_DOC,
      "Connection", 7, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.NBR_OF_RETRIES)
    .define(ReThinkConfigConstants.PROGRESS_COUNTER_ENABLED, Type.BOOLEAN, ReThinkConfigConstants.PROGRESS_COUNTER_ENABLED_DEFAULT,
      Importance.MEDIUM, ReThinkConfigConstants.PROGRESS_COUNTER_ENABLED_DOC,
      "Metrics", 1, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.PROGRESS_COUNTER_ENABLED_DISPLAY)

}

case class ReThinkSinkConfig(props: util.Map[String, String])
  extends AbstractConfig(ReThinkSinkConfig.config, props)
    with ErrorPolicySettings
    with NumberRetriesSettings
    with KcqlSettings
    with DatabaseSettings
    with RetryIntervalSettings {
  override val errorPolicyConstant: String = ReThinkConfigConstants.ERROR_POLICY
  override val kcqlConstant: String = ReThinkConfigConstants.SINK_ROUTE_QUERY
  override val numberRetriesConstant: String = ReThinkConfigConstants.NBR_OF_RETRIES
  override val databaseConstant: String = ReThinkConfigConstants.RETHINK_DB
  override val retryIntervalConstant: String = ReThinkConfigConstants.ERROR_RETRY_INTERVAL
}
