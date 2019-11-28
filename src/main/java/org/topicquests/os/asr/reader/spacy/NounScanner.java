/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.topicquests.os.asr.reader.spacy.api.IParagraphObjectFields;
import org.topicquests.os.asr.reader.spacy.api.ISpacyConstants;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IEnvironment;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class NounScanner {
	private IEnvironment environment;
	private SpacyUtil util;
	private List<String> detectorPatterns;
	/**
	 * 
	 */
	public NounScanner(IEnvironment env, SpacyUtil u) {
		environment = env;
		util = u;
		detectorPatterns = new ArrayList<String>();
		// gather NounPatterns from the sentence-agent-config.xml file
		List<List<String>> ptns = (List<List<String>>)environment.getProperties().get("NounPhrasePatterns");
		Iterator<List<String>>itr = ptns.iterator();
		List<String>l;
		while (itr.hasNext()) {
			l = itr.next();
			detectorPatterns.add(l.get(1));
		}
	}

	///////////////////////////
	// NounPhrase Scanning
	// Ripple through sentence tokens
	// Builds a nounPhraseMap for a given sentence
	//	MUST compare to what's in the entityNouns map
	///////////////////////////

	/**
	 * Fabricate a JSONObject key on start position with {@code List<JSONObject>} as the value
	 * @param sentenceObject
	 * @param paragraphObject
	 */
	public void scan4NounPhrases(List<JSONObject> masterTokens, JSONObject paragraphObject) {
		environment.logDebug("CNP\n"+masterTokens);
		//JSONObject paragraphTokenMap = (JSONObject)paragraphObject.get(ISpacyInterpreter.PARAGRAPH_TOKEN_MAP_KEY);
		int toklen = masterTokens.size();
		JSONObject tok;
		// noun phrases are just lists of tokens
		List<JSONObject> nouns = new ArrayList<JSONObject>();
		// we accumulate noun phrases as a map start/tokenlist
		JSONObject nounPhraseMap = new JSONObject();
		paragraphObject.put(IParagraphObjectFields.NOUN_PHRASES, nounPhraseMap);
/*		Number tStart;
		String pos;
		Number start = 0;
		boolean found = false;
		String dep;
		//JSONObject pix;
		int sint;
		//first, scan local tokens
		boolean tFound = false;
		for (int i = 0; i<toklen; i++) {
			//for every token in this sentence
			tok = masterTokens.get(i);
			if (found)
				environment.logDebug("NP "+tok+"\n"+nouns);
			tStart = tok.getAsNumber("start");
			sint = tStart.intValue();
			pos = tok.getAsString("pos");
			dep = tok.getAsString("dep");
			if (ISpacyConstants.NOUN.equalsIgnoreCase(pos)) {  // NOUN case
				tFound = found;
				found |= npNoun(i, found, tok, nouns, masterTokens);
				if (!tFound && found)
					start = tok.getAsNumber("start");
			} else if (!found && ISpacyConstants.VERB.equalsIgnoreCase(pos)) { // VERB case
				tFound = found;
				found |= npVerb(i, found, tok, nouns, masterTokens);
				if (!tFound && found)
					start = tok.getAsNumber("start");
			} else if (ISpacyConstants.ADJ.equalsIgnoreCase(pos)) { // ADJ case
				//anti-anflammatory agent GOOD
				//huge window  NOT GOOD
				// many something NOT GOOD
				if (isSafeADJ(tok.getAsString("text"))) {
					environment.logDebug("NPy "+tok+"\n"+nouns);
					tFound = found;
					found |= npAdjective(i, found, tok, nouns, masterTokens);
					if (!tFound && found)
						start = tok.getAsNumber("start");
				}
			//} else if (found && "ADP".equalsIgnoreCase(pos)) {
				//NOUN/nsubj:ADP/prep:NOUN/pobj
				//TODO disabled: made far too many messy noun phrases
				//npAdp(i, found, tok, nouns, tokens);
			} else if (found) { // STOPPING RULE
				// noun phrases end with a non-noun
				if (!ISpacyConstants.NOUN.equalsIgnoreCase(pos)) {
					environment.logDebug("NP+\n"+nouns);
					if (nouns.size() > 1) {
						nounPhraseMap.put(start.toString(), util.toPhrase(ISpacyConstants.NOUN, start.intValue(), nouns));
					}
					found = false;
					nouns = new ArrayList<JSONObject>();
				}
			}
		}
		String tTerm;
		JSONObject phrases = (JSONObject)paragraphObject.get(IParagraphObjectFields.VOCAB_NOUNS); //ENTITY_NOUNS);
		if (phrases != null && !phrases.isEmpty()) {
			String key;
			Iterator<String> keys = phrases.keySet().iterator();
			while (keys.hasNext()) {
				//for every entity in the entities list
				// note, they are JSONObjects, some with >1 word
				key = keys.next();
				tok = (JSONObject)phrases.get(key);
				tStart = tok.getAsNumber("start");
				sint = tStart.intValue();
					tTerm = tok.getAsString("text");
					found = termIsNounPhrase(tTerm, masterTokens);
					if (found) {
						// this might overwrite an existing list
						nounPhraseMap.put(tStart.toString(), tok);
						found = false;
					}
			}		
		}
*/
		this.spotPatternNounPhrases(masterTokens, paragraphObject.getAsString(IParagraphObjectFields.MASTER_PATTERNS), nounPhraseMap);
	}
	
	boolean isSafeADJ(String adj) {
		boolean result = true;
		result &= adj.equalsIgnoreCase("many");
		if (result)
			result &= adj.equalsIgnoreCase("some");
		else
			return result;
		if (result)
			result &= adj.equalsIgnoreCase("several");
		return result;
	}
	///////////////////////////
	// pattern case
	// ADJamod NOUNdobj ADPcase "on" NOUN  (on, into, ...)
	// "new insights on blah"
	//NOUNdobj ADPcase "on" NOUN  (on, into, ...)
	// "insights into blah"
	// the basic principles of voltage sensing and gating currents
	//////////////////////////
	void spotPatternNounPhrases(List<JSONObject> masterTokens, String masterPatterns, JSONObject nounPhraseMap) {
		//Run a group of subpatterns - a being longest nPattern_3
		Iterator<String> itr = this.detectorPatterns.iterator();
		String ptn;
		List<List<JSONObject>> aaa = null;
		List<List<JSONObject>> xxx = null;
		int counter = 0;
		while (itr.hasNext()) {
			ptn = itr.next();
			if (counter++ == 0)
				aaa = nPattern_0(masterTokens, masterPatterns, ptn);
			else {
				xxx = nPattern_0(masterTokens, masterPatterns, ptn);
				xxx = mergePatterns(aaa, xxx);
				counter = 0;
			}
				
		}
		environment.logDebug("NounScanner.spotPattrnNounPhrases\n"+xxx);
		toPhrases(xxx, nounPhraseMap, ISpacyConstants.NOUN);
	}

	void toPhrases(List<List<JSONObject>> newStuff, JSONObject phraseMap, String pos) {
		environment.logDebug("NounScanner.toPhrases\n"+newStuff+"\n"+phraseMap);
		if (newStuff.isEmpty()) return;
		List<JSONObject> it;
		Iterator<List<JSONObject>> itr = newStuff.iterator();
		Number start, end;
		int width, len;
		JSONObject tok;
		JSONObject newToken;
		String text = "";
		while (itr.hasNext()) {
			it = itr.next();
			width = it.size();
			tok = it.get(0);
			start = tok.getAsNumber("start");
			phraseMap.put(start.toString(), util.toPhrase(pos, start.intValue(), it));
			environment.logDebug("NounScanner.toPhrases-2\n"+phraseMap);
		}
	}
	
	List<List<JSONObject>> mergePatterns(List<List<JSONObject>> a, List<List<JSONObject>> b ) {
		List<List<JSONObject>> result = null;
		List<List<JSONObject>> x;
		int len1 = a.size(), len2 = b.size();
		if (len1 > len2) {
			result = a;
			x = b;
		} else {
			result = b;
			x = a;
		}
		len1 = x.size();
		environment.logDebug("NounScanner.mergePatterns\n"+result+"\n"+x);
		JSONObject tok;
		List<JSONObject> c;
		List<List<JSONObject>> adders = new ArrayList<List<JSONObject>>();
		for (int i=0; i<len1; i++) {
			c = result.get(i);
			if (!isSubset(c, result)) {
				adders.add(c);
			}
		}
		result.addAll(adders);
		return result;
	}
	
	boolean isSubset(List<JSONObject> a, List<List<JSONObject>> result) {
		boolean truth = result.contains(a);
		if (!truth) {
			int len = result.size();
			
			List<JSONObject> x;
			for (int i=0; i<len; i++) {
				x = result.get(i);
				truth &= _isSubset(a, x);
				if (truth)
					break;
			}
		}

		return truth;
	}
	
	boolean _isSubset(List<JSONObject> a, List<JSONObject> b) {
		boolean truth = false;
		int len1 = a.size(), len2 = b.size();
		List<JSONObject> x, y;
		if (len1 > len2) {
			x = a;
			y = b;
		} else {
			x = b;
			y = a;
		}
		len1 = y.size();
		for (int i=0; i<len1; i++) {
			truth &= x.contains(y);
			if (truth)
				break;
		}
		return truth;
	}
	
	/**
	 * Look for the pattern "ADJ NOUN ADP NOUN NOUN"
	 * @param masterTokens
	 * @param masterPatterns
	 * @return
	 */
	List<List<JSONObject>> nPattern_0(List<JSONObject> masterTokens, String masterPatterns, String pattern) {
		String [] myPattern = pattern.split(" ");
		String [] patterns = masterPatterns.split(" ");
		List<List<JSONObject>> col = new ArrayList<List<JSONObject>>();
		List<JSONObject> l = new ArrayList<JSONObject>();
		int pointer = 0;
		IResult r;
		while (l != null) {
			r = gatherPattern(pointer, myPattern, patterns, masterTokens);
			l = (List<JSONObject>)r.getResultObject();
			environment.logDebug("NounScanner.nPattern_0 "+l);
			if (l != null) {
				col.add(l);
				pointer += ((Integer)r.getResultObjectA()).intValue()+myPattern.length;
			}
		}
		return col;
	}

	/**
	 * 
	 * @param offset
	 * @param ptn		the pattern being tested
	 * @param allPatterns
	 * @param tokens
	 * @return
	 */
	IResult gatherPattern(int offset, String [] ptn, String [] allPatterns, List<JSONObject> tokens) {
		IResult r = new ResultPojo();
		List<JSONObject> result = null;
		int where = -1;
		int myLen = ptn.length;
		int allLen = allPatterns.length;
		int tokLen = tokens.size();
		String pstart, p1, p2;
		int temp = 0;
		boolean found = true;
		for (int i= offset; i< allLen; i++) {
			pstart = allPatterns[i];
			temp = i;
			for (int j=0; j<myLen; j++) {
				p1 = allPatterns[i+j];
				p2 = ptn[j];
				environment.logDebug("NounScanner.locatePattern "+i+" "+j+" "+found+" "+p1+" "+p2);
				if ((i+j) < allLen) {
					if (!p1.startsWith(p2)) {
						found = false;
						break;
					}
				} else {
					// ran out of room
					found = false;
					break;
				}
				
			}
			// we went through the entire pattern
			if (found) {
				where = temp;
				break;
			} else
				found = true;
			
		}
		boolean isValid = true;
		JSONObject tok = null;
		if (found && where > -1) {
			result = new ArrayList<JSONObject>();
			int start = where, lim = start+myLen;
			environment.logDebug("NounScanner.locatePattern-1 "+start+" "+lim+" "+tokLen);
			boolean isFirst = true;
			for (int i=start;i<lim; i++) {
				tok = tokens.get(i);
				isValid &= validateToken(isFirst, i, tok, tokens);
				if (isValid)
					result.add(tokens.get(i));
				else
					break;
				isFirst = false;
			}
		}
		environment.logDebug("NounScanner.locatePattern-2 "+isValid+"\n"+tok);
		if (isValid) {
			r.setResultObject(result);
			r.setResultObjectA(new Integer(where));
		}
		return r;
	}

	/**
	 * Returns {@code false} if this is a NOUN followed by a ")"
	 * because we don't want to include acronyms in triples, or <br/>
	 * NOUN followed by a "," or by a CCONJ hinting this might be a conjunction
	 * @param isFirst
	 * @param where
	 * @param token
	 * @param masterTokens
	 * @return
	 */
	boolean validateToken(boolean isFirst, int where, JSONObject token, List<JSONObject> masterTokens) {
		boolean result = true;
		if (isFirst) {
			int len = masterTokens.size();
			JSONObject jo;
			boolean x;
			if (token.getAsString("pos").equals(ISpacyConstants.NOUN)) {
				jo = masterTokens.get(where+1);
				x = !(jo.getAsString("pos").equals(ISpacyConstants.ADP) && jo.getAsString("text").equals("of"));
				environment.logDebug("NounScanner.validateToken "+x+"\n"+jo+"\n"+token);
				if (!x)
					return false;
				else {
					if ((where-1) > 0) {
						jo = masterTokens.get(where-1);
						x = !(jo.getAsString("pos").equals(ISpacyConstants.DET));
					}
				}
				/*result &= jo.getAsString("pos").equals(ISpacyConstants.PUNCT) && jo.getAsString("tag").equals("-RRB-");
				if (result) {
					result &= jo.getAsString("pos").equals(ISpacyConstants.PUNCT) && jo.getAsString("text").equals(",");
					if (result) {
						result &= jo.getAsString("pos").equals(ISpacyConstants.CCONJ);
			
					}
				}*/
			}
		}
		environment.logDebug("NounScanner.validateToken+ "+result+"\n"+token);
		
		return result;
	}
	///////////////////////////
	// These are cases in the quest for a nounPhrase
	///////////////////////////
	
	/**
	 * <p>{@code tok} is a NOUN</p>
	 * <ol><li>Nouns can come in pairs</li>
	 * <li>A Noun can follow a Verb/compound</li>
	 * <li>A Noun can follow an ADJ/amod</li>
	 * <li>???</li></ol>
	 * @param where
	 * @param tStart
	 * @param tok
	 * @param nouns
	 * @param tokens
	 * @return
	 */
	boolean npNoun(int where, boolean found, JSONObject tok, List<JSONObject> nouns, List<JSONObject> tokens) {
		String pos, dep;
		pos = tok.getAsString("pos");
		dep = tok.getAsString("dep");
		//TODO cannot find example of "npsubj"
		if (!found && !"npsubj".equalsIgnoreCase(dep)) {
			// 30 _years_ ago
			if (!"npadvmod".equalsIgnoreCase(dep)) {
				nouns.add(tok);
				if (!found) {
					return true;
				}
			}
		} 
			//already found
			nouns.add(tok);
		return found;
	}

	/**
	 * Looking for a NOUN/nsubj:ADP/prep:NOUN/pobj
	 * "window of opportunity"
	 * @param where
	 * @param found
	 * @param tok
	 * @param nouns
	 * @param tokens
	 */
	void npAdp(int where, boolean found, JSONObject tok, List<JSONObject> nouns, List<JSONObject> tokens) {
		String pos, dep;
		pos = tok.getAsString("pos");
		dep = tok.getAsString("dep");
		JSONObject tok1 = tokens.get(where+1); //Dangerous
		if (ISpacyConstants.NOUN.equalsIgnoreCase(tok1.getAsString("pos"))) {
			nouns.add(tok);
		}
	}
	/**
	 * {@code tok} is a VERB
	 * @param where
	 * @param tStart
	 * @param start
	 * @param found
	 * @param tok
	 * @param nouns
	 * @param tokens
	 * @return
	 */
	boolean npVerb(int where, boolean found, JSONObject tok, List<JSONObject> nouns, List<JSONObject> tokens) {
		String pos, dep;
		pos = tok.getAsString("pos");
		dep = tok.getAsString("dep");
		//Verb marked Compound followed by Noun
		// a _driving_ force
		// or Verb-amod followed by Noun --> NP
		//  of _activated_ microglia
		if ("compound".equalsIgnoreCase(dep)) { //||
			//"amod".equalsIgnoreCase(dep)) {
			environment.logDebug("NPx "+tok+"\n"+nouns);
			nouns.add(tok);
			if (!found) {
				return true;
			}
		} else if ("amod".equalsIgnoreCase(dep)) {
			 if (where > 0) {
				 //Verb/amod following an ADP
				 JSONObject tok1 = tokens.get(where-1);
				 if (ISpacyConstants.ADP.equalsIgnoreCase(tok1.getAsString("pos"))) {
					environment.logDebug("NPx-1 "+tok+"\n"+nouns);
					nouns.add(tok);
					if (!found) {
						return true;
					}
				 }
			 }
		}
		return found;
		
	}
	
/*
{
	"parent": 516,
	"pos": "ADJ",
	"start": 506,
	"text": "amyloid-β",
	"tag": "JJ",
	"sent": 465,
	"dep": "compound"
}, {
	"parent": 529,
	"pos": "NOUN",
	"tree_e_idx": 527,
	"start": 516,
	"tree_s_idx": 506,
	"text": "protein",
	"tag": "NN",
	"sent": 465,
	"dep": "nsubj"
} */
	/**
	 * <p>{@code tok} is an ADJ</p>
	 * <o><li>A Noun can follow an Adjective/amod</li>
	 * <li>An ADJ/amod can follow an ADJ/amod</li></ol>
	 * @param where
	 * @param found
	 * @param tok
	 * @param nouns
	 * @param tokens
	 * @return
	 */
	boolean npAdjective(int where, boolean found, JSONObject tok, List<JSONObject> nouns, List<JSONObject> tokens) {
		String pos, dep, pos2;
		pos = tok.getAsString("pos");
		dep = tok.getAsString("dep");
		Number ts = tok.getAsNumber("start");
		JSONObject tok2;
		//For now, this only processes an Adjective where dep == "amod"
		// or "compound"
		if ("amod".equalsIgnoreCase(dep) ||
			"compound".equalsIgnoreCase(dep)) {
			tok2 = tokens.get(where+1);
			pos2 = tok2.getAsString("pos");
			if (pos2.equalsIgnoreCase("NOUN") ||
				pos2.equalsIgnoreCase("ADJ")) {
				nouns.add(tok);
				if (!found) {
					return true;
				}
			}
		}
		return found;
	}
	
	/////////////////////
	// util
	/////////////////////
	
	boolean termIsNounPhrase(final String term, final List<JSONObject> tokens) {
		JSONObject tok;
		String tTerm, pos;
		String [] terms = term.split(" ");
		int len = terms.length;
		if (len == 1)
			return false; // it's not a phrase
		boolean found = true;
		for (int i=0; i<len; i++) {
			found &= isNounWord(terms[i], tokens);
			if (!found)
				return found;
		}
		return found;
	}

	boolean isNounWord(final String word, final List<JSONObject> tokens) {
		String term = word;
		//int where = term.indexOf("'");
		//Heuristic
		if ( term.endsWith("'s")) 
			term = term.substring(0, (term.length()-2));
		//environment.logDebug("ISNOUNWORD "+word+" "+term);
		JSONObject tok;
		String  pos, tTerm;
		Iterator<JSONObject>itr = tokens.iterator();
		boolean found = true;
		while (itr.hasNext()) {
			//for every token
			tok = itr.next();
			pos = tok.getAsString("pos");
			tTerm = tok.getAsString("text");
			if (ISpacyConstants.NOUN.equalsIgnoreCase(pos)) {
				found = term.equalsIgnoreCase(tTerm);
				//if (word.equals("Alzheimer's"))
				//	environment.logDebug("ISNOUNWORD "+word+" | "+term+" | "+found+"\n"+tok);
				if (found)
					return true;
			}
		}
		return false;
	}

	///////////////////////////
	// specialized NounDetection
	///////////////////////////
	
	/**
	 * Spotting nouns runs along the full token list for a sentence
	 * and makes any surgical switches from one POS to NOUN according to rules
	 * @param masterTokens  from the ParagraphObject
	 */
	public void spotNouns(List<JSONObject> masterTokens) {
		spotNounsA(masterTokens);
		spotNounsB(masterTokens);
	}
	
	///////////////////
	// Most DETs are followed by NOUN
	//	Their parent is that NOUN
	// DETs can be followed by ADJ
	//	parent should point ahead, not back
	// DETs can be followed by VERB
	//  parent points to the verb
	//		that verb could be a VerbPhrase
	// DETs have "dep"
	//	"det" which means it is a determiner
	//	"nsubj" which likely means it is a noun
	///////////////////
	
	/**
	 * DET followed by ADJ-nsubj followed by VERB --> Noun
	 * Must change POS of ADJ to NOUN
	 * @param tokens
	 */
	void spotNounsB(List<JSONObject> tokens) {
		int len = tokens.size();
		JSONObject tokA, tokB, tokC;
		environment.logDebug("SPOTNOUNB\n"+tokens);
		for (int i=0;i<len;i++) {
			tokA = tokens.get(i);
			if (tokA.getAsString("pos").equals(ISpacyConstants.DET)) {
				
				if (i+2 < len) {
					tokC = tokens.get(i+2);
					if (tokC.getAsString("pos").equals(ISpacyConstants.VERB)) {
						tokB = tokens.get(i+1);
						environment.logDebug("SPOTNOUNB-1 "+tokB+"\n"+tokA+" "+tokB);
						if (tokB.getAsString("dep").equals("nsubj"))
							tokB.put("pos", ISpacyConstants.NOUN);
					}
				}
			}
			
		}
	}
	
	/**
	 * A DET/nsubj followed by VERB/amod followed by NOUN/root
	 * @param tokens
	 */
	void spotNounsA(List<JSONObject> tokens) {
		environment.logDebug("SPOTNOUNA\n"+tokens);
		int len = tokens.size();
		JSONObject tokA, tokB, tokC;
		for (int i=0;i<len;i++) {
			tokA = tokens.get(i);
			if (tokA.getAsString("pos").equals("DET")) {
				if ("nsubj".equalsIgnoreCase(tokA.getAsString("dep"))) {
					//Most general form
					// Any DET/nsubj is a NOUN
					// We may have to specialize if this causes trouble
					tokA.put("pos", ISpacyConstants.NOUN);
					/*if (i+2 < len) {
						tokC = tokens.get(i+2);
						if (tokC.getAsString("pos").equals("VERB")) {
							tokB = tokens.get(i+1);
							environment.logDebug("SPOTNOUNA-1 "+tokB+"\n"+tokA+" "+tokB);
							if (tokB.getAsString("dep").equals("nsubj"))
								tokB.put("pos", "NOUN");
						}
					}*/
				}
			}
			
		}
		
	}
}
