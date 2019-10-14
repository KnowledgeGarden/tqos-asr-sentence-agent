/**
 * 
 */
package org.topicquests.os.asr.reader.spacy.api;

import org.topicquests.hyperbrane.api.ISentence;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public interface ISpacyInterpreter {

	
	IResult processSentence(ISentence sentence);
}
