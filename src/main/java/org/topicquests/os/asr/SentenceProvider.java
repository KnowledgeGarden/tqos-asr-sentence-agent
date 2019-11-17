/*
 * Copyright 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr;

import org.topicquests.asr.sentence.api.ISentenceClient;
import org.topicquests.hyperbrane.ConcordanceSentence;
import org.topicquests.hyperbrane.api.ISentence;
import org.topicquests.os.asr.api.ISentenceProvider;
import org.topicquests.os.asr.reader.sentences.SentencesEnvironment;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class SentenceProvider implements ISentenceProvider {
	private SentencesEnvironment environment;
	private ISentenceClient sentenceDatabase;

	/**
	 * 
	 */
	public SentenceProvider(SentencesEnvironment env) {
		environment = env;
		sentenceDatabase = environment.getSentenceDatabase();
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.ISentenceProvider#getSentence(java.lang.String)
	 */
	@Override
	public IResult getSentence(String locator) {
		IResult result = sentenceDatabase.get(locator);
		JSONObject jo = (JSONObject)result.getResultObject();
		if (jo != null)
			result.setResultObject(new ConcordanceSentence(jo));
		return null;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.api.ISentenceProvider#putSentence(org.topicquests.hyperbrane.api.ISentence)
	 */
	@Override
	public IResult putSentence(ISentence sentence) {
		IResult result = sentenceDatabase.put(sentence.getDocumentId(), sentence.getParagraphId(), 
				sentence.getID(), sentence.getData());
		return result;
	}

	@Override
	public IResult updateSentence(ISentence sentence) {
		IResult result = sentenceDatabase.update(sentence.getID(), sentence.getData());
		return result;
	}

}
