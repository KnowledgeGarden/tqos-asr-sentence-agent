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
		String x;
		while (itr.hasNext()) {
			l = itr.next();
			x = l.get(1);
			if (x != null)
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
	 * @param sentencePattern
	 */
	public void scan4NounPhrases(List<JSONObject> sentenceTokens, 
								 JSONObject paragraphObject,
								 String sentencePattern) {
		environment.logDebug("CNP\n"+sentenceTokens);
		JSONObject nounPhraseMap = (JSONObject)paragraphObject.get(IParagraphObjectFields.NOUN_PHRASES);
		this.spotPatternNounPhrases(sentenceTokens, sentencePattern, nounPhraseMap);
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

	void spotPatternNounPhrases(List<JSONObject> sentenceTokens, String sentencePatterns, JSONObject nounPhraseMap) {
		//Run a group of subpatterns - a being longest nPattern_3
		environment.logDebug("NounScanner.spotPatternNounPhrases\n"+detectorPatterns);
		Iterator<String> itr = this.detectorPatterns.iterator();
		String ptn;
		List<List<JSONObject>> master = new ArrayList<List<JSONObject>>();
		List<List<JSONObject>> xxx = null;
		int counter = 0;
		
		while (itr.hasNext()) {
			ptn = itr.next();
			xxx = nPattern_0(sentenceTokens, sentencePatterns, ptn);
			if (xxx != null) {
				master.addAll(xxx);
			}
		}
		this.mergePatterns(master);
		environment.logDebug("NounScanner.spotPattrnNounPhrases\n"+master);
		toPhrases(master, nounPhraseMap, ISpacyConstants.NOUN);
	}

	void toPhrases(List<List<JSONObject>> newStuff, JSONObject phraseMap, String pos) {
		environment.logDebug("NounScanner.toPhrases\n"+newStuff+"\n"+phraseMap);
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
			environment.logDebug("NounScanner.toPhrases-2\n"+phraseMap);
		}
	}
	
	List<List<JSONObject>> mergePatterns(List<List<JSONObject>> a) {
		List<List<JSONObject>> longest = a;

		int len1 = longest.size();
		environment.logDebug("NounScanner.mergePatterns\n"+longest);
		JSONObject tok;
		List<JSONObject> c;
		List<List<JSONObject>> toRemove = new ArrayList<List<JSONObject>>();
		List<List<JSONObject>> dropper;
		//now burp the gas
		for (int i=0; i<len1; i++) {
			c = longest.get(i);
			dropper = compare(c, longest);
			if (!dropper.isEmpty())
				toRemove.addAll(dropper);
		}
		environment.logDebug("NounScanner.mergePatterns-2\n"+toRemove);
		if (!toRemove.isEmpty()) {
			len1 = toRemove.size();
			for (int i=0; i<len1; i++)
				longest.remove(toRemove.get(i));
		}
		environment.logDebug("NounScanner.mergePatterns+\n"+longest);
		return longest;
	}
	
	/**
	 * Return {@code null} if {@code target} is inside any member of {@code longest}
	 * @param target
	 * @param longest
	 * @return
	 */
	List<List<JSONObject>> compare(List<JSONObject> target, List<List<JSONObject>>longest)  {
		List<List<JSONObject>> result = new ArrayList<List<JSONObject>>();
		int len = longest.size();
		int lenA = target.size(), lenB;
		JSONObject targ = target.get(0);
		environment.logDebug("C1 \n"+targ);
		JSONObject tok = target.get(lenA-1);
		int tStart = targ.getAsNumber("start").intValue();
		int tEnd = tok.getAsNumber("start").intValue();
		int fStart;
		List<JSONObject>l;
		for (int i=0; i<len; i++) {
			l = longest.get(i);
			lenB = l.size();
			tok = l.get(0);
			fStart = tok.getAsNumber("start").intValue();
			environment.logDebug("C1-1 "+tStart+" "+tEnd+" "+fStart+"\n"+target+"\n"+l);
			if ( fStart == tStart) {
				if (lenB < lenA)
					result.add(l);
				else if (lenB > lenA)
					result.add(target);
				//otherwise, they are one and the same
				//TODO try equals
			} else if (fStart >= tStart && fStart <= tEnd) {
				result.add(l);
			} 
		}
		environment.logDebug("C1+\n"+target+"\n"+result);
		return result;
	}
	
	/**
	 * Look for the pattern "ADJ NOUN ADP NOUN NOUN"
	 * @param sentenceTokens
	 * @param sentencePatterns
	 * @return
	 */
	List<List<JSONObject>> nPattern_0(List<JSONObject> sentenceTokens, String sentencePatterns, String pattern) {
		environment.logDebug("NounScanner.nPattern_0 "+pattern+"\n"+sentencePatterns);
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
		//try to find as many hits on this pattern as possible
		while (l != null) {
			r = gatherPattern(pointer, myPattern, patterns, sentenceTokens, blockPattern);
			l = (List<JSONObject>)r.getResultObject();
			environment.logDebug("NounScanner.nPattern_0-2\n"+l);
			if (l != null) {
				pointer = ((Integer)r.getResultObjectA()).intValue();
				col.add(l);
				pointer += myPattern.length;
				environment.logDebug("NounScanner.nPattern_0-3 "+pointer);
			}
		}
		environment.logDebug("NounScanner.nPattern_0+\n"+col);
		return col;
	}
	/////////////////////////////////
	// GatherPatterns
	//	GIVEN
	//		A pattern
	//			e.g. punct VERB NOUN
	//  WHERE
	//		A lowercase pattern at the beginning is a kind of marker
	//		to begin the pattern, but which is ignored once the pattern is detected
	//	RETURNS
	//		A list of the tokens which satisfy the pattern
	//		The offset into the allPatterns array where the pattern started
	/////////////////////////////////

	/**
	 * 
	 * @param offset
	 * @param ptn		the pattern being tested
	 * @param allPatterns
	 * @param tokens
	 * @parm blockPattern can be {@code  null}
	 * @return
	 */
	IResult gatherPattern(int offset,
						  String [] ptn, 
						  String [] allPatterns,
						  List<JSONObject> tokens,
						  String blockPattern) {
		environment.logDebug("NounScanner.gatherPattern "+offset+" "+blockPattern);
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
					environment.logDebug("NounScanner.gatherPattern-0 "+i+" "+j+" "+found+" "+p1+" "+p2);
					if (p1.startsWith(p2)) {
						//possible hit
						if ((i+j+1) < allLen) {
							p1 = allPatterns[i+j+1];
							if ((j+1)<myLen) {
								p2 = ptn[j+1];
								environment.logDebug("NounScanner.gatherPattern-1 "+(i+j+1)+" "+(j+1)+" "+found+" "+p1+" "+p2);
								if (!p1.startsWith(p2)) {
									environment.logDebug("PredicateScanner.gatherPattern-a");
									break;
								}							} else {
								environment.logDebug("NounScanner.gatherPattern-did");
								did = true;
								where = temp;
								break;
							}
						} else {
							environment.logDebug("NounScanner.gatherPattern-b?");
							//ran out of room
							found = false;
							break;
						}
					} else {
						environment.logDebug("NounScanner.gatherPattern-c");
						break; // endif - could b here because not hit
					}
					environment.logDebug("NounScanner.gatherPattern-d");
				}				
			}
		}
		boolean isValid = false;
		JSONObject tok = null;
		if (found && where > -1) {
			isValid = true;
			result = new ArrayList<JSONObject>();
			int start = where, lim = start+myLen;
			environment.logDebug("NounScanner.gatherPattern-2 "+start+" "+lim+" "+tokLen);
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

	////////////////////////////
	// Designed to block e.g. "The pandemic of obesity..."
	// checks for leading DET to allow "and maintenance of healthy..."
	///////////////////////////
	///////////////////////////
	// interesting pattern ADJ NOUN ADP ADJ NOUN
	//  "Slow waves in neural activity"
	
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
		if (isFirst) {
			int len = sentenceTokens.size();
			JSONObject jo;
			boolean x;
			if (token.getAsString("pos").equals(ISpacyConstants.NOUN)) {
				jo = sentenceTokens.get(where+1);
				//test for specific ADP: "of"
				x = (jo.getAsString("pos").equals(ISpacyConstants.ADP) && jo.getAsString("text").equals("of"));
				environment.logDebug("NounScanner.validateToken "+x+"\n"+jo+"\n"+token);
				if (x) {
					// an "of" preceded by a DET -- the X of y must be blocked
					if ((where-1) >= 0) {
						jo = sentenceTokens.get(where-1);
						result &= !(jo.getAsString("pos").equals(ISpacyConstants.DET));
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
	 * @param sentenceTokens  from the ParagraphObject
	 */
	public void spotNouns(List<JSONObject> sentenceTokens) {
		spotNounsA(sentenceTokens);
		spotNounsB(sentenceTokens);
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
