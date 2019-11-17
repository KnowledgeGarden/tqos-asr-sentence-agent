/**
 * 
 */
package org.topicquests.os.asr.reader.spacy.api;

import java.util.List;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public interface ISinglePredicateSentenceInterpreter {

	void processSinglePredicate(JSONObject sentenceObject,
			List<JSONObject> sentenceTokens,
			final String pattern, final String [] patternArray);
}
