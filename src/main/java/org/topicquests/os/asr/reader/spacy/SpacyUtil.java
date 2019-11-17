/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy;

import java.util.Iterator;
import java.util.List;

import org.topicquests.os.asr.reader.sentences.SentencesEnvironment;
import org.topicquests.os.asr.reader.spacy.api.ISpacyConstants;
import org.topicquests.support.api.IEnvironment;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class SpacyUtil {
	private IEnvironment environment;

	/**
	 * 
	 */
	public SpacyUtil(IEnvironment env) {
		environment = env;
	}

	public JSONObject toPhrase(String pos, int start, List<JSONObject> nouns) {
		environment.logDebug("TOPHRASE "+pos+" "+start+"\n"+nouns);
		JSONObject result = new JSONObject();
		result.put("start", new Integer(start));
		result.put("pos", pos);
		JSONObject jo;
		StringBuilder buf = new StringBuilder();
		Iterator<JSONObject> itr = nouns.iterator();
		int width = 0;
		while (itr.hasNext()) {
			jo = itr.next();
			width++;
			buf = buf.append(jo.getAsString("text")+" ");
		}
		result.put("width", Integer.toString(width));
		result.put("text", buf.toString().trim());
		environment.logDebug("TOPHRASE+ "+result);		
		return result;
	}

	/**
	 * Return <code>true</code> if <code>index</code> points to a noun or pronoun
	 * @param index
	 * @param tokenMap
	 * @param nounPhraseMap
	 * @param entityNounMap
	 * @return
	 */
	public boolean parentPointsToNoun(Number index, JSONObject tokenMap, 
								//JSONObject nounPhraseMap,
								JSONObject entityNounMap) {
		if (index == null) // it doesn't have  parent
			return false;
		JSONObject jo = (JSONObject)tokenMap.get(index.toString());
		String pos = jo.getAsString("pos");
		boolean result = (pos.equals(ISpacyConstants.NOUN) || pos.equals(ISpacyConstants.PRON));
		if (!result) {
			//result = nounPhraseMap.get(index) != null;
			//if (!result)
				result = entityNounMap.get(index) != null;
		}
		return result;
	}
	
	public int locatePredicate(int offset, List<JSONObject> array) {
		int result = 0;
		JSONObject jo;
		int len = array.size();
		for (int i = offset; i<len; i++) {
			jo = array.get(i);
			if (jo.getAsString("pos").equals(ISpacyConstants.VERB))
				return i;
		}
		return result;
	}


}
