/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy.api;

import java.util.List;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public interface IMuitiplePredicateSentenceInterpreter {

	void processMultiPredicate(JSONObject sentenceObject,
			List<JSONObject> sentenceTokens,
			final String pattern, final String [] patternArray);
}
