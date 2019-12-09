/*
 * Copyright 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.ks.kafka;

import org.topicquests.backside.kafka.consumer.StringMessageConsumer;
import org.topicquests.backside.kafka.consumer.api.IMessageConsumerListener;
import org.topicquests.support.api.IEnvironment;

/**
 * @author jackpark
 *
 */
public class KafkaConsumer extends StringMessageConsumer {

	/**
	 * @param e
	 * @param topic
	 * @param listener
	 */
	public KafkaConsumer(IEnvironment e, String groupId, String topic, IMessageConsumerListener listener) {
		super(e, groupId, topic, listener, false);
		//All the work isin the listener
	}

}
