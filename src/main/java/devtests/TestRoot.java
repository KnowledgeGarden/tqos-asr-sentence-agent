/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package devtests;

import org.topicquests.os.asr.reader.sentences.SentencesEnvironment;
import org.topicquests.os.asr.reader.sentences.api.ISentenceAgent;

/**
 * @author jackpark
 *
 */
public class TestRoot {
	protected SentencesEnvironment environment;
	protected ISentenceAgent agent;

	/**
	 * 
	 */
	public TestRoot() {
		environment = new SentencesEnvironment();
		agent = environment.getSentenceAgent();
	}

}
