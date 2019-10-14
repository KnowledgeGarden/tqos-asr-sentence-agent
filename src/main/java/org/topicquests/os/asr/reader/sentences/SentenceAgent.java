/**
 * 
 */
package org.topicquests.os.asr.reader.sentences;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.topicquests.ks.kafka.KafkaConsumer;
import org.topicquests.ks.kafka.KafkaProducer;
import org.topicquests.os.asr.reader.sentences.api.ISentenceAgent;

/**
 * @author jackpark
 *
 */
public class SentenceAgent implements ISentenceAgent {
	private SentencesEnvironment environment;
	private KafkaConsumer kConsumer;
	private KafkaProducer kProducer;

	/**
	 * @param env
	 */
	public SentenceAgent(SentencesEnvironment env) {
		environment = env;
		kConsumer = new KafkaConsumer(env, ISentenceAgent.KAFKA_CONSUMER_NAME, this);
		kProducer = new KafkaProducer(env, ISentenceAgent.KAFKA_PRODUCER_NAME);
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.consumer.api.IMessageConsumerListener#acceptRecord(org.apache.kafka.clients.consumer.ConsumerRecord)
	 */
	@Override
	public boolean acceptRecord(ConsumerRecord record) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.reader.sentences.api.ISentenceAgent#shutDown()
	 */
	@Override
	public void shutDown() {
		kProducer.close();
	}

}
