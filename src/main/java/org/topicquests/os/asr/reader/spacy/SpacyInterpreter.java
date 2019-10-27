/**
 * 
 */
package org.topicquests.os.asr.reader.spacy;

import org.topicquests.hyperbrane.api.ISentence;
import org.topicquests.os.asr.reader.sentences.SentencesEnvironment;
import org.topicquests.os.asr.reader.spacy.api.ISpacyInterpreter;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class SpacyInterpreter implements ISpacyInterpreter {
	private SentencesEnvironment environment;

	/**
	 * 
	 */
	public SpacyInterpreter(SentencesEnvironment env) {
		environment = env;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.reader.spacy.api.ISpacyInterpreter#processSentence(org.topicquests.hyperbrane.api.ISentence)
	 */
	@Override
	public IResult processSentence(ISentence sentence) {
		IResult result = new ResultPojo();
		// TODO Auto-generated method stub
		return result;
	}

}