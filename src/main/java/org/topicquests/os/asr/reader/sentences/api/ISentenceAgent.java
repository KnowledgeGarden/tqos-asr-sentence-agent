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
//	public static final String
//		KAFKA_PRODUCER_NAME			= "SentenceAgentProducerName",
//		KAFKA_CONSUMER_NAME			= "SentenceAgentConsumerName";

	//API TODO
	
	/**
	 * <p>Process a spacy model</p>
	 * <p>There are 5 total models; run this for each model</p>>
	 * @param paragraphObject
	 * @param spacy
	 * @return
	 */
	void acceptSpacyJSON(JSONObject paragraphObject, JSONObject spacy);
	
	/**
	 * When all models have been completed, run this. This will
	 * build all the WordGrams and ISentences and push the paragraphObject
	 * out to Kafka for the TupleAgent
	 * @param paragraphObject
	 * @return
	 */
	IResult finishParagraph(JSONObject paragraphObject);
	
	void shutDown();
}
