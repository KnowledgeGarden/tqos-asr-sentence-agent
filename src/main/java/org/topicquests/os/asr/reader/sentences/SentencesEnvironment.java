/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.sentences;

import org.topicquests.os.asr.AsrCoreEnvironment;
import org.topicquests.os.asr.dbpedia.SpotlightClient;
import org.topicquests.os.asr.reader.sentences.api.ISentenceAgent;
/**
 * @author jackpark
 *
 */
public class SentencesEnvironment extends AsrCoreEnvironment {
	private ISentenceAgent agent;
	/**
	 * 
	 */
	public SentencesEnvironment() {
		super();
		agent = new SentenceAgent(this);
		
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			@Override
			public void run() {
				shutDown();
			}
		});
	}
	

	public ISentenceAgent getSentenceAgent() {
		return agent;
	}
	
	public void shutDown() {
		super.shutDown();
		
	}

}
