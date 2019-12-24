/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.sentences;

import java.util.Map;

import org.topicquests.asr.general.GeneralDatabaseEnvironment;
import org.topicquests.asr.general.document.api.IDocumentClient;
import org.topicquests.asr.paragraph.api.IParagraphClient;
import org.topicquests.asr.sentence.api.ISentenceClient;
import org.topicquests.os.asr.DocumentProvider;
import org.topicquests.os.asr.ParagraphProvider;
import org.topicquests.os.asr.SentenceProvider;
import org.topicquests.os.asr.api.IDocumentProvider;
import org.topicquests.os.asr.api.IParagraphProvider;
import org.topicquests.os.asr.api.ISentenceProvider;
import org.topicquests.os.asr.api.IStatisticsClient;
import org.topicquests.os.asr.paragraph.ParagraphDocHttpClient;
import org.topicquests.os.asr.reader.sentences.api.ISentenceAgent;
import org.topicquests.support.RootEnvironment;
import org.topicquests.support.config.Configurator;
/**
 * @author jackpark
 *
 */
public abstract class SentencesEnvironment extends RootEnvironment {
	private static SentencesEnvironment instance;
	private IStatisticsClient stats;
	private ISentenceProvider sentenceProvider;
	private IDocumentProvider documentProvider;
	private IParagraphProvider paragraphProvider;
	private IDocumentClient documentDatabase;
	private ISentenceClient sentenceDatabase;
	private IParagraphClient paragraphDatabase;
	private GeneralDatabaseEnvironment generalEnvironment;
	private Map<String,Object>kafkaProps;
	private ISentenceAgent sentenceAgent;
	private ParagraphDocHttpClient paraDocClient;

	/**
	 * This environment is made to be extended
	 * @param configPath
	 * @param logPath
	 */
	public SentencesEnvironment(String configPath, String logPath) {
		super(configPath, logPath);
		String schemaName = getStringProperty("DatabaseSchema");
		generalEnvironment = new GeneralDatabaseEnvironment(schemaName);
		sentenceDatabase = generalEnvironment.getSentenceClient();
		documentDatabase = generalEnvironment.getDocumentClient();
		paragraphDatabase = generalEnvironment.getParagraphClient();
		documentProvider = new DocumentProvider(this);
		sentenceProvider = new SentenceProvider(this);
		paragraphProvider = new ParagraphProvider(this);
		kafkaProps = Configurator.getProperties("kafka-topics.xml");
		paraDocClient = new ParagraphDocHttpClient(this);
		instance = this;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				shutDown();
			}
		});
	}
	
	public static SentencesEnvironment getInstance() {
		return instance;
	}
	
	public ParagraphDocHttpClient getParaDocClient() {
		return paraDocClient;
	}
	
	public Map<String, Object> getKafkaTopicProperties() {
		return kafkaProps;
	}
		
	public GeneralDatabaseEnvironment getGeneralDatabaseEnvironment() {
		return generalEnvironment;
	}

	public IDocumentProvider getDocProvider() {
		return documentProvider;
	}
	public IDocumentClient getDocumentDatabase () {
		return documentDatabase;
	}
	public ISentenceClient getSentenceDatabase() {
		return sentenceDatabase;
	}
	
	public IParagraphClient getParagraphDatabase() {
		return paragraphDatabase;
	}
	
	public IParagraphProvider getParagraphProvider() {
		return paragraphProvider;
	}

	public ISentenceProvider getSentenceProvider() {
		return sentenceProvider;
	}
	
	public abstract void shutDown();

}
