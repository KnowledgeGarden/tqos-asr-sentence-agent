/**
 * 
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
