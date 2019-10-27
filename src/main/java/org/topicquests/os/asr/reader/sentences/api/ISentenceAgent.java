/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.sentences.api;

import org.topicquests.backside.kafka.consumer.api.IMessageConsumerListener;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public interface ISentenceAgent extends IMessageConsumerListener {
	public static final String
		KAFKA_PRODUCER_NAME			= "SentenceAgentProducerName",
		KAFKA_CONSUMER_NAME			= "SentenceAgentConsumerName";

	//API TODO
	
	/**
	 * Process a spacy result
	 * @param spacy
	 * @return
	 */
	IResult acceptSpacyJSON(JSONObject spacy);
	
	void shutDown();
}
