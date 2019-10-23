/**
 * 
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
