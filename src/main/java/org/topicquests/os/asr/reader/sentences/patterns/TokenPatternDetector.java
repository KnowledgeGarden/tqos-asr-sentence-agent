/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.sentences.patterns;

import java.util.Iterator;
import java.util.List;

import org.topicquests.os.asr.reader.sentences.patterns.api.ISentenceTokenPatterns;
import org.topicquests.support.api.IEnvironment;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class TokenPatternDetector {
	private IEnvironment environment;

	/**
	 * 
	 */
	public TokenPatternDetector(IEnvironment env) {
		environment = env;
	}

	/**
	 * Returns the sentence pattern for the given {@code sentenceTokens}
	 * @param sentenceTokens
	 * @return
	 */
	public String getSentencePattern(List<JSONObject> sentenceTokens) {
		String result = "";
		Iterator<JSONObject> itr = sentenceTokens.iterator();
		JSONObject tok;
		while (itr.hasNext()) {
			tok = itr.next();
			result += " "+getPattern(tok);
		}
		return result.trim();
	}
	
	public String getPattern(JSONObject token) {
		//environment.logDebug("GetPattern\n"+token);
		String pos = token.getAsString("pos");
		String dep = token.getAsString("dep");
		String pat = pos;
		if (dep != null)
			pat += dep;
		return validatePattern(pat);
	}
	
	/**
	 * validate pattern
	 * @param pattern
	 * NOTE; throws a RuntimeException if pattern not known
	 */
	public String validatePattern(String pattern) {
		
		String patn = pattern.replace(':', '_');
		//if (!findPattern(patn))
		//	throw new RuntimeException("NOPattern: "+patn);
		return patn;
	}

	boolean findPattern(String pattern) {
		return pattern.equals(ISentenceTokenPatterns.ADJacomp) ||
			pattern.equals(ISentenceTokenPatterns.ADJamod) ||
			pattern.equals(ISentenceTokenPatterns.ADJconj) ||
			pattern.equals(ISentenceTokenPatterns.ADPcase) ||
			pattern.equals(ISentenceTokenPatterns.ADPprep) ||
			pattern.equals(ISentenceTokenPatterns.ADVadvmod) ||
			pattern.equals(ISentenceTokenPatterns.CCONJcc) ||
			pattern.equals(ISentenceTokenPatterns.DETcc_preconj) ||
			pattern.equals(ISentenceTokenPatterns.DETdet) ||
			pattern.equals(ISentenceTokenPatterns.NOUN) ||
			pattern.equals(ISentenceTokenPatterns.NOUNappos) ||
			pattern.equals(ISentenceTokenPatterns.NOUNcompound) ||
			pattern.equals(ISentenceTokenPatterns.NOUNconj) ||
			pattern.equals(ISentenceTokenPatterns.NOUNdobj) ||
			pattern.equals(ISentenceTokenPatterns.NOUNnmod) ||
			pattern.equals(ISentenceTokenPatterns.NOUNnsubj) ||
			pattern.equals(ISentenceTokenPatterns.NOUNnsubjpass) ||
			pattern.equals(ISentenceTokenPatterns.NOUNpobj) ||
			pattern.equals(ISentenceTokenPatterns.NUMappos) ||
			pattern.equals(ISentenceTokenPatterns.NUMdep) ||
			pattern.equals(ISentenceTokenPatterns.NUMnummod) ||
			pattern.equals(ISentenceTokenPatterns.PARTcase) ||
			pattern.equals(ISentenceTokenPatterns.PARTprep) ||
			pattern.equals(ISentenceTokenPatterns.PRONnmod_poss) ||
			pattern.equals(ISentenceTokenPatterns.PRONnsubj) ||
			pattern.equals(ISentenceTokenPatterns.PRONnsubjpass) ||
			pattern.equals(ISentenceTokenPatterns.PRONposs) ||
			pattern.equals(ISentenceTokenPatterns.PROPNcompound) ||
			pattern.equals(ISentenceTokenPatterns.PROPNpobj) ||
			pattern.equals(ISentenceTokenPatterns.PUNCTpunct) ||
			pattern.equals(ISentenceTokenPatterns.VERB) ||
			pattern.equals(ISentenceTokenPatterns.VERBacl_relcl) ||
			pattern.equals(ISentenceTokenPatterns.VERBadvcl) ||
			pattern.equals(ISentenceTokenPatterns.VERBaux) ||
			pattern.equals(ISentenceTokenPatterns.VERBauxpass) ||
			pattern.equals(ISentenceTokenPatterns.VERBcompound) ||
			pattern.equals(ISentenceTokenPatterns.VERBconj) ||
			pattern.equals(ISentenceTokenPatterns.VERBROOT) ||
			pattern.equals(ISentenceTokenPatterns.VERBrelcl);

	}
}
