/**
 * 
 */
package org.topicquests.os.asr.reader.sentences;

import java.util.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.topicquests.ks.kafka.KafkaConsumer;
import org.topicquests.ks.kafka.KafkaProducer;
import org.topicquests.os.asr.reader.sentences.api.ISentenceAgent;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class SentenceAgent implements ISentenceAgent {
	private SentencesEnvironment environment;
	private KafkaConsumer kConsumer;
	private KafkaProducer kProducer;
	private final String
		MODEL_1		= "en_core_sci_lg", // sentences, entities, noun_chunks, POS
		MODEL_2		= "en_ner_jnlpba_md",
		MODEL_3		= "en_ner_bc5cdr_md",
		MODEL_4		= "en_ner_bionlp13cg_md",
		MODEL_5		= "en_ner_craft_md";

	/**
	 * @param env
	 */
	public SentenceAgent(SentencesEnvironment env) {
		environment = env;
		kConsumer = new KafkaConsumer(env, ISentenceAgent.KAFKA_CONSUMER_NAME+Long.toString(System.currentTimeMillis()), this);
		kProducer = new KafkaProducer(env, ISentenceAgent.KAFKA_PRODUCER_NAME+Long.toString(System.currentTimeMillis()));
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

	@Override
	public IResult acceptSpacyJSON(JSONObject spacy) {
		IResult result = new ResultPojo();
		IResult r;
		System.out.println("A "+spacy);
		if (MODEL_1.contentEquals(spacy.getAsString("Model")))
			r = processEnCoreSciLgModel(spacy);
		// TODO Auto-generated method stub
		return result;
	}
	
	
	IResult processEnCoreSciLgModel(JSONObject spacy) {
		IResult result = new ResultPojo();
		List<JSONObject> sentences = (List<JSONObject>)spacy.get("sentences");
		System.out.println("S "+sentences);
		return result;
	}

}
