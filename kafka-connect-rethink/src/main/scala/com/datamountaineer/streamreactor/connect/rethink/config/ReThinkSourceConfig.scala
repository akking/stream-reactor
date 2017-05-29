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

import com.datamountaineer.streamreactor.temp.{DatabaseSettings, KcqlSettings}
import org.apache.kafka.common.config.ConfigDef.{Importance, Type}
import org.apache.kafka.common.config.{AbstractConfig, ConfigDef}

/**
  * Created by andrew@datamountaineer.com on 22/09/16. 
  * stream-reactor
  */
object ReThinkSourceConfig {
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
    .define(ReThinkConfigConstants.SOURCE_ROUTE_QUERY, Type.STRING, Importance.HIGH,
      ReThinkConfigConstants.SOURCE_ROUTE_QUERY,
      "Connection", 4, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.SOURCE_ROUTE_QUERY)
    .define(ReThinkConfigConstants.PROGRESS_COUNTER_ENABLED, Type.BOOLEAN, ReThinkConfigConstants.PROGRESS_COUNTER_ENABLED_DEFAULT,
      Importance.MEDIUM, ReThinkConfigConstants.PROGRESS_COUNTER_ENABLED_DOC,
      "Metrics", 1, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.PROGRESS_COUNTER_ENABLED_DISPLAY)
}

case class ReThinkSourceConfig(props: util.Map[String, String])
  extends AbstractConfig(ReThinkSourceConfig.config, props)
    with KcqlSettings
    with DatabaseSettings {
  override val kcqlConstant: String = ReThinkConfigConstants.SOURCE_ROUTE_QUERY
  override val databaseConstant: String = ReThinkConfigConstants.RETHINK_DB
}
