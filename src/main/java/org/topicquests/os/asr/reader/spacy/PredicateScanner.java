/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.topicquests.os.asr.reader.spacy.api.ISpacyConstants;
import org.topicquests.os.asr.reader.spacy.api.IParagraphObjectFields;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IEnvironment;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class PredicateScanner {
	private IEnvironment environment;
	private SpacyUtil util;
	private List<String> detectorPatterns;

	/**
	 * 
	 */
	public PredicateScanner(IEnvironment env, SpacyUtil u) {
		environment = env;
		util = u;
		detectorPatterns = new ArrayList<String>();
		// gather VerbPatterns from the sentence-agent-config.xml file
		List<List<String>> ptns = (List<List<String>>)environment.getProperties().get("VerbPhrasePatterns");
		Iterator<List<String>>itr = ptns.iterator();
		List<String>l;
		while (itr.hasNext()) {
			l = itr.next();
			detectorPatterns.add(l.get(1));
		}
	}
	
	///////////////////////////
	// VerbPhrase Scanning
	///////////////////////////

	/**
	 * Scans along sentenceTokens for predicate phrases
	 * @param tokens
	 * @param paragraphObject
	 * @param sentencePattern
	 */
	public void scan4VerbPhrases(List<JSONObject> sentenceTokens, 
								 JSONObject paragraphObject,
								 String sentencePattern) {
		environment.logDebug("PredicateScanner.scan4VerbPhrases\n"+sentenceTokens);
		JSONObject verbPhraseMap = (JSONObject)paragraphObject.get(IParagraphObjectFields.PREDICATE_PHRASES);

		spotPatternVerbPhrases(sentenceTokens, sentencePattern, verbPhraseMap);
		environment.logDebug("PredicateScanner.scan4VerbPhrases+\n"+verbPhraseMap);
	}
	
	void spotPatternVerbPhrases(List<JSONObject> sentenceTokens, String sentencePatterns, JSONObject verbPhraseMap) {
		//Run a group of subpatterns - a being longest nPattern_3
		Iterator<String> itr = this.detectorPatterns.iterator();
		String ptn;
		List<List<JSONObject>> master = new ArrayList<List<JSONObject>>();
		List<List<JSONObject>> xxx = null;		
		while (itr.hasNext()) {
			ptn = itr.next();
			xxx = nPattern_0(sentenceTokens, sentencePatterns, ptn);
			if (xxx != null) {
				master.addAll(xxx);
			}
		}
		this.mergePatterns(master);
		environment.logDebug("PredicateScanner.spotPattrnNounPhrases\n"+master);
		toPhrases(master, verbPhraseMap, ISpacyConstants.VERB);

	}
	
	void toPhrases(List<List<JSONObject>> newStuff, JSONObject phraseMap, String pos) {
		environment.logDebug("PredicateScanner.toPhrases\n"+newStuff+"\n"+phraseMap);
		if (newStuff.isEmpty()) return;
		List<JSONObject> it;
		Iterator<List<JSONObject>> itr = newStuff.iterator();
		Number start;
		int width;
		JSONObject tok;
		while (itr.hasNext()) {
			it = itr.next();
			width = it.size();
			tok = it.get(0);
			start = tok.getAsNumber("start");
			phraseMap.put(start.toString(), util.toPhrase(pos, start.intValue(), it));
			environment.logDebug("PredicateScanner.toPhrases-2\n"+phraseMap);
		}
	}
	
	List<List<JSONObject>> mergePatterns(List<List<JSONObject>> a) {
		List<List<JSONObject>> longest = a;

		int len1 = longest.size();
		environment.logDebug("PredicateScannermergePatterns\n"+longest);
		List<JSONObject> c;
		List<List<JSONObject>> toRemove = new ArrayList<List<JSONObject>>();
		List<JSONObject> dropper;
		//now burp the gas
		for (int i=0; i<len1; i++) {
			c = longest.get(i);
			dropper = compare(c, longest);
			if (dropper != null)
				toRemove.add(dropper);
		}
		environment.logDebug("PredicateScannermergePatterns-2\n"+toRemove);
		if (!toRemove.isEmpty()) {
			len1 = toRemove.size();
			for (int i=0; i<len1; i++)
				longest.remove(toRemove.get(i));
		}
		environment.logDebug("PredicateScannermergePatterns+\n"+longest);
		return longest;
	}
	
	List<JSONObject> compare(List<JSONObject> target, List<List<JSONObject>>longest)  {
		List<JSONObject> result = null;
		int len = longest.size();
		int lenA = target.size(), lenB;
		JSONObject targ = target.get(0);
		int tStart = targ.getAsNumber("start").intValue();
		List<JSONObject>l;
		Number n;
		JSONObject tok;
		String t1, t2;
		boolean yes;
		for (int i=0; i<len; i++) {
			l = longest.get(i);
			lenB = l.size();
			tok = l.get(0);
			if (tok.getAsNumber("start").intValue() == tStart) {
				if (lenB < lenA)
					result = l;
				else if (lenB > lenA)
					result = target;
			} else {
				n = tok.getAsNumber("end");
				if (n != null) {
					if (n.intValue() > tStart) {
						//it's inside
						return result;
					}
				} else {
					// no choice but to compare text
					t1 = targ.getAsString("text");
					t2 = tok.getAsString("text");
					if (t1.length() > t2.length())
						yes = t1.contains(t2);
					else
						yes = t2.contains(t1);
					if (yes)
						return result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Look for the pattern "ADJ NOUN ADP NOUN NOUN"
	 * @param sentenceTokens
	 * @param sentencePatterns
	 * @return
	 */
	List<List<JSONObject>> nPattern_0(List<JSONObject> sentenceTokens, String sentencePatterns, String pattern) {
		environment.logDebug("PredicateScanner.nPattern_0 "+pattern+"\n"+sentencePatterns);
		String [] myPattern = pattern.split(" ");
		String [] patterns = sentencePatterns.split(" ");
		List<List<JSONObject>> col = new ArrayList<List<JSONObject>>();
		List<JSONObject> l = new ArrayList<JSONObject>();
		String firstPattern = myPattern[0];
		String blockPattern = firstPattern;
		if (Character.isLowerCase(firstPattern.charAt(0))) {
			firstPattern.toUpperCase();
			myPattern[0] = firstPattern;
			blockPattern = firstPattern;
		} else {
			blockPattern = null;
		}
		int pointer = 0;
		IResult r;
		while (l != null) {
			r = gatherPattern(pointer, myPattern, patterns, sentenceTokens, blockPattern);
			l = (List<JSONObject>)r.getResultObject();
			environment.logDebug("PredicateScanner.nPattern_0-1 "+l);
			if (l != null) {
				pointer = ((Integer)r.getResultObjectA()).intValue();
				col.add(l);
				pointer += myPattern.length;
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
	 * @param blockPattern can be {@code null}
	 * @return
	 */
	IResult gatherPattern(int offset, 
						  String [] ptn, 
						  String [] allPatterns, 
						  List<JSONObject> tokens,
						  String blockPattern) {
		environment.logDebug("PredicateScanner.gatherPattern "+offset+" "+blockPattern);
		IResult r = new ResultPojo();
		List<JSONObject> result = null;
		int where = -1;
		int myLen = ptn.length;
		int allLen = allPatterns.length;
		int tokLen = tokens.size();
		String p1, p2;
		int temp = 0;
		boolean found = true;
		boolean did = false;
		for (int i= offset; i< allLen; i++) {
			if (found && !did) {
				// for each pattern in allPatterns
				p1 = allPatterns[i];
				temp = i;
				for (int j=0; j<myLen; j++) {
					// for each following allPattern
					p2 = ptn[j];
					// is this it?
					environment.logDebug("PredicateScanner.gatherPattern-0 "+i+" "+j+" "+found+" "+p1+" "+p2);
					if (p1.startsWith(p2)) {
						//possible hit
						if ((i+j+1) < allLen) {
							p1 = allPatterns[i+j+1];
							if ((j+1)<myLen) {
								p2 = ptn[j+1];
								environment.logDebug("PredicateScanner.gatherPattern-1 "+(i+j+1)+" "+(j+1)+" "+found+" "+p1+" "+p2);
								if (!p1.startsWith(p2)) {
									environment.logDebug("PredicateScanner.gatherPattern-a");
									break;
								}
							} else {
								environment.logDebug("PredicateScanner.gatherPattern-did");
								did = true;
								where = temp;
								break;
							}
						} else {
							environment.logDebug("PredicateScanner.gatherPattern-b?");
							//ran out of room ??????
							found = false;
							break;
						}
					} else {
						environment.logDebug("PredicateScanner.gatherPattern-c");
						break; // endif - could b here because not hit
					}
				}
			}
		}
		boolean isValid = false;
		JSONObject tok = null;
		if (found && where > -1) {
			isValid = true;
			result = new ArrayList<JSONObject>();
			int start = where, lim = start+myLen;
			environment.logDebug("PredicateScanner.gatherPattern-2 "+start+" "+lim+" "+tokLen);
			boolean isFirst = true;
			boolean block = false;
			for (int i=start;i<lim; i++) {
				tok = tokens.get(i);
				if (blockPattern != null && isFirst) {
					//first token
					if (tok.getAsString("pos").startsWith(blockPattern))
						block = true;
				}
				if (!block) {
					isValid &= validateToken(isFirst, i, tok, tokens);
					if (isValid)
						result.add(tokens.get(i));
					else
						break;
				} else
					block = false;
				isFirst = false;
			}
		}
		environment.logDebug("NounScanner.gatherPattern-3 "+isValid+"\n"+result);
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
	 * @param sentenceTokens
	 * @return
	 */
	boolean validateToken(boolean isFirst, int where, JSONObject token, List<JSONObject> sentenceTokens) {
		boolean result = true;
		/*if (isFirst) {
			int len = sentenceTokens.size();
			JSONObject jo;
			boolean x;
			if (token.getAsString("pos").equals(ISpacyConstants.NOUN)) {
				jo = sentenceTokens.get(where+1);
				x = !(jo.getAsString("pos").equals(ISpacyConstants.ADP) && jo.getAsString("text").equals("of"));
				environment.logDebug("PredicateScanner.validateToken "+x+"\n"+jo+"\n"+token);
				if (!x)
					return false;
				else {
					if ((where-1) > 0) {
						jo = sentenceTokens.get(where-1);
						x = !(jo.getAsString("pos").equals(ISpacyConstants.DET));
					}
				}
			}
		}*/

		environment.logDebug("PredicateScanner.validateToken+ "+result+"\n"+token);
		
		return result;
	}
}
