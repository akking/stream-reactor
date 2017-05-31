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

object ReThinkConfigConstants {
  val RETHINK_HOST = "connect.rethink.sink.host"
  private[config] val RETHINK_HOST_DOC = "Rethink server host."
  val RETHINK_HOST_DEFAULT = "localhost"
  val RETHINK_DB = "connect.rethink.sink.db"
  private[config] val RETHINK_DB_DEFAULT = "connect_rethink_sink"
  private[config] val RETHINK_DB_DOC = "The reThink database to write to and create tables in."
  val RETHINK_PORT = "connect.rethink.sink.port"
  val RETHINK_PORT_DEFAULT = "28015"
  private[config] val RETHINK_PORT_DOC = "Client port of rethink server to connect to."

  val CONFLICT_ERROR = "error"
  val CONFLICT_REPLACE = "replace"
  val CONFLICT_UPDATE = "update"

  val SINK_ROUTE_QUERY = "connect.rethink.sink.kcql"
  private[config] val SINK_ROUTE_QUERY_DOC = "KCQL expression describing field selection and which topic to read from."

  val SOURCE_ROUTE_QUERY = "connect.rethink.source.kcql"
  private[config] val SOURCE_ROUTE_QUERY_DOC = "KCQL expression describing which rethink table changefeed to subscribe to."


  val ERROR_POLICY = "connect.rethink.size.error.policy"
  private[config] val ERROR_POLICY_DOC: String = "Specifies the action to be taken if an error occurs while inserting the data.\n" +
    "There are two available options: \n" + "NOOP - the error is swallowed \n" +
    "THROW - the error is allowed to propagate. \n" +
    "RETRY - The exception causes the Connect framework to retry the message. The number of retries is based on \n" +
    "The error will be logged automatically"
  private[config] val ERROR_POLICY_DEFAULT = "THROW"

  val ERROR_RETRY_INTERVAL = "connect.rethink.sink.retry.interval"
  private[config] val ERROR_RETRY_INTERVAL_DOC = "The time in milliseconds between retries."
  private[config] val ERROR_RETRY_INTERVAL_DEFAULT = "60000"
  val NBR_OF_RETRIES = "connect.rethink.sink.max.retries"
  private[config] val NBR_OF_RETRIES_DOC = "The maximum number of times to try the write again."
  private[config] val NBR_OF_RETIRES_DEFAULT = 20


  val PROGRESS_COUNTER_ENABLED = "connect.progress.enabled"
  val PROGRESS_COUNTER_ENABLED_DOC = "Enables the output for how many records have been processed"
  val PROGRESS_COUNTER_ENABLED_DEFAULT = false
  val PROGRESS_COUNTER_ENABLED_DISPLAY = "Enable progress counter"

  val USERNAME = "connect.rethink.username"
  val USERNAME_DOC = "The user name to connect to rethink with."

  val PASSWORD = "connect.rethink.password"
  val PASSWORD_DOC = "The password for the user."

  val CERT_FILE = "connect.rethink.cert.file"
  val CERT_FILE_DOC = "Certificate file to use for secure TLS connection to the rethinkdb servers. Cannot be used with username/password."
  val CERT_FILE_DEFAULT = ""

  val SSL_ENABLED = "connect.rethink.ssl.enabled"
  val SSL_ENABLED_DOC = "Secure Cassandra driver connection via SSL."
  val SSL_ENABLED_DEFAULT = "false"

  val TRUST_STORE_PATH = "connect.rethink.trust.store.path"
  val TRUST_STORE_PATH_DOC = "Path to the client Trust Store."
  val TRUST_STORE_PASSWD = "connect.rethink.trust.store.password"
  val TRUST_STORE_PASSWD_DOC = "Password for the client Trust Store."
  val TRUST_STORE_TYPE = "connect.rethink.trust.store.type"
  val TRUST_STORE_TYPE_DOC = "Type of the Trust Store, defaults to JKS"
  val TRUST_STORE_TYPE_DEFAULT = "JKS"

  val USE_CLIENT_AUTH = "connect.rethink.ssl.client.cert.auth"
  val USE_CLIENT_AUTH_DEFAULT = "false"
  val USE_CLIENT_AUTH_DOC = "Enable client certification authentication. Requires KeyStore options to be set."

  val KEY_STORE_PATH = "connect.rethink.key.store.path"
  val KEY_STORE_PATH_DOC = "Path to the client Key Store."
  val KEY_STORE_PASSWD = "connect.rethink.key.store.password"
  val KEY_STORE_PASSWD_DOC = "Password for the client Key Store"
  val KEY_STORE_TYPE = "connect.rethink.key.store.type"
  val KEY_STORE_TYPE_DOC = "Type of the Key Store, defauts to JKS"
  val KEY_STORE_TYPE_DEFAULT = "JKS"

  val AUTH_KEY = "connect.rethink.auth.key"
  val AUTH_KEY_DOC = "The authorization key to use in combination with the certificate file."
  val AUTH_KEY_DEFAULT = ""

  val BATCH_SIZE = "connect.rethink.batch.size"
  val BATCH_SIZE_DEFAULT = 100
  val BATCH_SIZE_DOC = "The number of events to take from the internal queue to batch together to send to Kafka. The records will" +
    "be flushed if the linger period has expired first."

  val SOURCE_LINGER_MS = "connect.source.linger.ms"
  val SOURCE_LINGER_MS_DEFAULT = 5000
  val SOURCE_LINGER_MS_DOC = "The number of milliseconds to wait before flushing the received messages to Kafka. The records will" +
    "be flushed if the batch size is reached before the linger period has expired."
}
