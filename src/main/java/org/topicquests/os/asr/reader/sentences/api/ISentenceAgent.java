/**
 * 
 */
package org.topicquests.os.asr.reader.sentences.api;

import org.topicquests.backside.kafka.consumer.api.IMessageConsumerListener;

/**
 * @author jackpark
 *
 */
public interface ISentenceAgent extends IMessageConsumerListener {
	public static final String
		KAFKA_PRODUCER_NAME			= "SentenceAgentProducerName",
		KAFKA_CONSUMER_NAME			= "SentenceAgentConsumerName";

	//API TODO
	
	void shutDown();
}
