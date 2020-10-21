/*
 * Copyright 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.gramolizer;

import org.topicquests.os.asr.gramolizer.api.ISGModel;
import org.topicquests.os.asr.reader.sentences.SentencesEnvironment;
import org.topicquests.os.asr.wordgram.api.IWordGramAgentModel;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public class SGModel implements ISGModel {
	private SentencesEnvironment environment;
	private final String USER_ID = "SystemUser";
	private IWordGramAgentModel model;

	/**
	 * 
	 */
	public SGModel(SentencesEnvironment env) {
		environment = env;
		model = environment.getWordgramAgentModel();
	}

	@Override
	public IResult processSentence(String sentenceId, String sentenceText) {
		environment.logDebug("SGModel.processSentence "+sentenceId);
		IResult r = model.processString(sentenceText, USER_ID, sentenceId);
		environment.logDebug("SGModel.processSentence+ "+r.getErrorString()
				+"\n"+r.getResultObject());
		return r;
	}


}
