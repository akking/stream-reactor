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
  val config: ConfigDef = ReThinkConfig().base
    .define(ReThinkConfigConstants.SOURCE_ROUTE_QUERY, Type.STRING, Importance.HIGH,
      ReThinkConfigConstants.SOURCE_ROUTE_QUERY,
      "Connection", 4, ConfigDef.Width.MEDIUM, ReThinkConfigConstants.SOURCE_ROUTE_QUERY)
}

case class ReThinkSourceConfig(props: util.Map[String, String])
  extends AbstractConfig(ReThinkSourceConfig.config, props)
    with KcqlSettings
    with DatabaseSettings {
  override val kcqlConstant: String = ReThinkConfigConstants.SOURCE_ROUTE_QUERY
  override val databaseConstant: String = ReThinkConfigConstants.RETHINK_DB
}
