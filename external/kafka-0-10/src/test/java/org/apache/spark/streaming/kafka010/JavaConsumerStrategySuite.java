/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.streaming.kafka010;

import java.io.Serializable;
import java.util.*;

import scala.collection.JavaConverters;

import org.apache.kafka.common.TopicPartition;

import org.junit.Assert;
import org.junit.Test;

public class JavaConsumerStrategySuite implements Serializable {

  @Test
  public void testConsumerStrategyConstructors() {
    final String topic1 = "topic1";
    final Collection<String> topics = Arrays.asList(topic1);
    final scala.collection.Iterable<String> sTopics =
      JavaConverters.collectionAsScalaIterableConverter(topics).asScala();
    final TopicPartition tp1 = new TopicPartition(topic1, 0);
    final TopicPartition tp2 = new TopicPartition(topic1, 1);
    final Collection<TopicPartition> parts = Arrays.asList(tp1, tp2);
    final scala.collection.Iterable<TopicPartition> sParts =
      JavaConverters.collectionAsScalaIterableConverter(parts).asScala();
    final Map<String, Object> kafkaParams = new HashMap<String, Object>();
    kafkaParams.put("bootstrap.servers", "not used");
    final scala.collection.Map<String, Object> sKafkaParams =
      JavaConverters.mapAsScalaMapConverter(kafkaParams).asScala();
    final Map<TopicPartition, Object> offsets = new HashMap<>();
    offsets.put(tp1, 23L);
    final scala.collection.Map<TopicPartition, Object> sOffsets =
      JavaConverters.mapAsScalaMapConverter(offsets).asScala();

    // make sure constructors can be called from java
    // final ConsumerStrategy<String, String> sub0 =          // does not compile in Scala 2.10
    //   Subscribe.<String, String>apply(topics, kafkaParams, offsets);
    final ConsumerStrategy<String, String> sub1 =
      Subscribe.<String, String>apply(sTopics, sKafkaParams, sOffsets);
    final ConsumerStrategy<String, String> sub2 =
      Subscribe.<String, String>apply(sTopics, sKafkaParams);
    final ConsumerStrategy<String, String> sub3 =
      Subscribe.<String, String>create(topics, kafkaParams, offsets);
    final ConsumerStrategy<String, String> sub4 =
      Subscribe.<String, String>create(topics, kafkaParams);

    Assert.assertEquals(
      sub1.executorKafkaParams().get("bootstrap.servers"),
      sub3.executorKafkaParams().get("bootstrap.servers"));

    // final ConsumerStrategy<String, String> asn0 =          // does not compile in Scala 2.10
    //   Assign.<String, String>apply(parts, kafkaParams, offsets);
    final ConsumerStrategy<String, String> asn1 =
      Assign.<String, String>apply(sParts, sKafkaParams, sOffsets);
    final ConsumerStrategy<String, String> asn2 =
      Assign.<String, String>apply(sParts, sKafkaParams);
    final ConsumerStrategy<String, String> asn3 =
      Assign.<String, String>create(parts, kafkaParams, offsets);
    final ConsumerStrategy<String, String> asn4 =
      Assign.<String, String>create(parts, kafkaParams);

    Assert.assertEquals(
      asn1.executorKafkaParams().get("bootstrap.servers"),
      asn3.executorKafkaParams().get("bootstrap.servers"));
  }

}
