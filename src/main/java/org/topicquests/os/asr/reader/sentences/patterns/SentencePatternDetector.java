/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.sentences.patterns;

import org.topicquests.os.asr.reader.spacy.api.ISpacyConstants;

/**
 * @author jackpark
 *
 */
public class SentencePatternDetector {


	/**
	 * Returns a particular Sentence Pattern, e.g. "NVN"
	 * @param sentenceTokenPattern
	 * @return
	 */
	public static String getSentencePattern(String sentenceTokenPattern) {
		String result = "";
		String [] patterns = sentenceTokenPattern.split(" ");
		int len = patterns.length;
		String p;
		for (int i=0; i<len; i++) {
			p = patterns[i];
			if (p.startsWith(ISpacyConstants.DET) && i == 0)
				result += "D";
			else if (p.startsWith(ISpacyConstants.NOUN))
				result += "N";
			else if (p.startsWith(ISpacyConstants.VERB))
				result += "V";
			else if (p.startsWith(ISpacyConstants.CCONJ))
				result += "C";
			else if (p.startsWith(ISpacyConstants.PRON))
				result += "P";
		}
		return result;
	}
}
