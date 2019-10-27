/*
 * Copyright 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.dbpedia;

import org.topicquests.os.asr.reader.sentences.SentencesEnvironment;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class SpotlightClient {
	private SentencesEnvironment environment;
	private DBpediaSpotlightClient client;

	/**
	 * 
	 */
	public SpotlightClient(SentencesEnvironment env) {
		environment = env;
		client = new DBpediaSpotlightClient(environment);
	}
	
	public IResult annotateSentence(String sentence) {
		IResult result = new ResultPojo();
		try {
			JSONObject jo = client.extract(sentence);
			result.setResultObject(jo);
		} catch (Exception e) {
			environment.logError(e.getMessage(), e);
			//result.addErrorString(e.getMessage());
			//e.printStackTrace();
		}
		return result;
	}

}
