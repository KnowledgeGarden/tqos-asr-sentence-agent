/**
 * 
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
