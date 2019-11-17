/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.topicquests.os.asr.reader.sentences.SentencesEnvironment;
import org.topicquests.os.asr.reader.spacy.api.IParagraphObjectFields;
import org.topicquests.os.asr.reader.spacy.api.ISpacyConstants;
import org.topicquests.support.api.IEnvironment;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class NounScanner {
	private IEnvironment environment;
	private SpacyUtil util;
	/**
	 * 
	 */
	public NounScanner(IEnvironment env, SpacyUtil u) {
		environment = env;
		util = u;
	}
/** Strange
 * Got this
{
	"pos": "vp",
	"start": 1575,
	"text": "has now become"
}, {
	"parent": 1598,
	"pos": "DET",
	"start": 1590,
	"text": "a",
	"tag": "DT",
	"sent": 1515,
	"dep": "det"
}, {
	"pos": "np",
	"start": 1592,
	"text": "years major field" <<<< "years" does not belong here
}, { <<<<<<<<<<<<<<<<<<<<<dropped "of"
	"start": 1607,
	"end": 1621,
	"text": "brain research",
	"label": "ENTITY",
	"sent": 1515
}
 * From this
{
		"parent": 1561,
		"pos": "ADJ",
		"start": 1551,
		"text": "more",
		"tag": "JJR",
		"sent": 1515,
		"dep": "advmod"
	}, {
		"parent": 1561,
		"pos": "ADP",
		"start": 1556,
		"text": "than",
		"tag": "IN",
		"sent": 1515,
		"dep": "quantmod"
	}, {
		"parent": 1564,
		"pos": "NUM",
		"tree_e_idx": 1561,
		"start": 1561,
		"tree_s_idx": 1551,
		"text": "30",
		"tag": "CD",
		"sent": 1515,
		"dep": "nummod"
	}, {
		"parent": 1570,
		"pos": "NOUN",
		"tree_e_idx": 1564,
		"start": 1564,
		"lemma": "year",
		"tree_s_idx": 1551,
		"text": "years",
		"tag": "NNS",
		"sent": 1515,
		"dep": "npadvmod"
	}, {
		"parent": 1534,
		"pos": "ADV",
		"tree_e_idx": 1570,
		"start": 1570,
		"tree_s_idx": 1551,
		"text": "ago",
		"tag": "RB",
		"sent": 1515,
		"dep": "advmod"
	}, {
		"parent": 1583,
		"pos": "PUNCT",
		"start": 1573,
		"text": ",",
		"tag": ",",
		"sent": 1515,
		"dep": "punct"
	}, {
		"parent": 1583,
		"pos": "VERB",
		"start": 1575,
		"lemma": "have",
		"text": "has",
		"tag": "VBZ",
		"sent": 1515,
		"dep": "aux"
	}, {
		"parent": 1583,
		"pos": "ADV",
		"start": 1579,
		"text": "now",
		"tag": "RB",
		"sent": 1515,
		"dep": "advmod"
	}, {
		"pos": "VERB",
		"tree_e_idx": 1627,
		"start": 1583,
		"tree_s_idx": 1515,
		"text": "become",
		"tag": "VBN",
		"sent": 1515,
		"dep": "ROOT"
	}, {
		"parent": 1598,
		"pos": "DET",
		"start": 1590,
		"text": "a",
		"tag": "DT",
		"sent": 1515,
		"dep": "det"
	}, { <<<<<<<<<<< "major field of brain research" would be the NP
			except that we dropped "of" so this should be two NPs
			major field   and brain research
		"parent": 1598,  <<< start noun phrase
		"pos": "ADJ",
		"start": 1592,
		"text": "major",
		"tag": "JJ",
		"sent": 1515,
		"dep": "amod"
	}, {
		"parent": 1583,
		"pos": "NOUN",
		"tree_e_idx": 1622,
		"start": 1598,
		"tree_s_idx": 1590,
		"text": "field",
		"tag": "NN",
		"sent": 1515,
		"dep": "attr"
	}, {
		"parent": 1598,
		"pos": "ADP",
		"tree_e_idx": 1613,
		"start": 1604,
		"tree_s_idx": 1604,
		"text": "of",
		"tag": "IN",
		"sent": 1515,
		"dep": "prep"
	}, {
		"parent": 1613,
		"pos": "NOUN",
		"start": 1607,
		"text": "brain",
		"tag": "NN",
		"sent": 1515,
		"dep": "compound"
	}, {
		"parent": 1604,
		"pos": "NOUN",
		"tree_e_idx": 1613,
		"start": 1613,
		"tree_s_idx": 1607,
		"text": "research",
		"tag": "NN",
		"sent": 1515,
		"dep": "pobj"  <<<<<< that should stop this
	}, {
		"parent": 1598,
		"pos": "NOUN",
		"start": 1622,
		"text": "today",
		"tag": "NN",
		"sent": 1515,
		"dep": "npadvmod"
	} 
 */
/** Possible noun phrase
 * HAD TO KILL THIS ONE
 * NOUN/nsubj:ADP/prep:NOUN/pobj
{
	"pos": "np",
	"start": 1210,
	"text": "huge window" << ADJ/amod:NOUN/nsubm should not make a phrase
}, 

{
			"parent": 1215,
			"pos": "ADJ",  << must ignore this
			"start": 1210,
			"text": "huge",
			"tag": "JJ",
			"sent": 1166,
			"dep": "amod"
		}, {
			"parent": 1237,
			"pos": "NOUN",
			"tree_e_idx": 1225,
			"start": 1215,
			"tree_s_idx": 1208,
			"text": "window",
			"tag": "NN",
			"sent": 1166,
			"dep": "nsubj"
		}
{
	"parent": 1215,
	"pos": "ADP",  << must allow this if followed by NOUN/pobj or psubj
	"tree_e_idx": 1225,
	"start": 1222,
	"tree_s_idx": 1222,
	"text": "of",
	"tag": "IN",
	"sent": 1166,
	"dep": "prep"
}, {
	"parent": 1222,
	"pos": "NOUN",
	"start": 1225,
	"text": "opportunity",
	"tag": "NN",
	"sent": 1166,
	"dep": "pobj"
}
 */
	
/**
{
			"parent": 567,   <<<<< Refers back to a NounPhrase
			"pos": "DET",
			"start": 562,
			"lemma": "this",
			"text": "This", <<<< NOUN
			"tag": "DT",
			"sent": 562,
			"dep": "nsubj"
		}, {
			"parent": 575,   <<<<<<Start VerbPhrase NOT A NOUN PHRASE
			"pos": "VERB",
			"tree_e_idx": 567,
			"start": 567,
			"lemma": "focus",
			"tree_s_idx": 562,
			"text": "focused",
			"tag": "VBD",
			"sent": 562,
			"dep": "amod"
		}, {
			"pos": "NOUN",
			"tree_e_idx": 644,
			"start": 575,
			"tree_s_idx": 562,
			"text": "attention",
			"tag": "NN",
			"sent": 562,
			"dep": "ROOT"	<<<<<<<<End VerbPhrase
		}, {
			"parent": 588,
			"pos": "ADP",
			"start": 585,
			"text": "on",
			"tag": "IN",
			"sent": 562,
			"dep": "case"
		}, {
			"parent": 575,
			"pos": "NOUN",
			"tree_e_idx": 588,
			"start": 588,
			"lemma": "anti-inflammatorie",
			"tree_s_idx": 585,
			"text": "anti-inflammatories",
			"tag": "NNS",
			"sent": 562,
			"dep": "nmod"
		}
 */
/* bad
 * failed to pick up both as one
 * looks like missing adj -- ADJ/amod:ADJ/amod:NOUN
 {
	"parent": 753,
	"pos": "ADJ",
	"start": 721,
	"text": "non-steroidal",
	"tag": "JJ",
	"sent": 646,
	"dep": "amod"
}, {
	"pos": "np",
	"start": 735,
	"text": "anti-inflammatory drugs"
}
 */
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
	public void scan4NounPhrases(List<JSONObject> tokens, JSONObject paragraphObject) {
		environment.logDebug("CNP\n"+tokens);
		//JSONObject paragraphTokenMap = (JSONObject)paragraphObject.get(ISpacyInterpreter.PARAGRAPH_TOKEN_MAP_KEY);
		int toklen = tokens.size();
		JSONObject tok;
		// noun phrases are just lists of tokens
		List<JSONObject> nouns = new ArrayList<JSONObject>();
		// we accumulate noun phrases as a map start/tokenlist
		JSONObject nounPhraseMap = new JSONObject();
		paragraphObject.put(IParagraphObjectFields.NOUN_PHRASES, nounPhraseMap);
		Number tStart;
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
			tok = tokens.get(i);
			if (found)
				environment.logDebug("NP "+tok+"\n"+nouns);
			tStart = tok.getAsNumber("start");
			sint = tStart.intValue();
			pos = tok.getAsString("pos");
			dep = tok.getAsString("dep");
			if ("NOUN".equalsIgnoreCase(pos)) {  // NOUN case
				tFound = found;
				found |= npNoun(i, found, tok, nouns, tokens);
				if (!tFound && found)
					start = tok.getAsNumber("start");
			} else if (!found && "VERB".equalsIgnoreCase(pos)) { // VERB case
				tFound = found;
				found |= npVerb(i, found, tok, nouns, tokens);
				if (!tFound && found)
					start = tok.getAsNumber("start");
			} else if ("ADJ".equalsIgnoreCase(pos)) { // ADJ case
				//anti-anflammatory agent GOOD
				//huge window  NOT GOOD
				environment.logDebug("NPy "+tok+"\n"+nouns);
				tFound = found;
				found |= npAdjective(i, found, tok, nouns, tokens);
				if (!tFound && found)
					start = tok.getAsNumber("start");
			//} else if (found && "ADP".equalsIgnoreCase(pos)) {
				//NOUN/nsubj:ADP/prep:NOUN/pobj
				//TODO disabled: made far too many messy noun phrases
				//npAdp(i, found, tok, nouns, tokens);
			} else if (found) { // STOPPING RULE
				// noun phrases end with a non-noun
				if (!"NOUN".equalsIgnoreCase(pos)) {
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
					found = termIsNounPhrase(tTerm, tokens);
					if (found) {
						// this might overwrite an existing list
						nounPhraseMap.put(tStart.toString(), tok);
						found = false;
					}
			}		
		}
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
		if ("NOUN".equalsIgnoreCase(tok1.getAsString("pos"))) {
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
				 if ("ADP".equalsIgnoreCase(tok1.getAsString("pos"))) {
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
			if ("NOUN".equalsIgnoreCase(pos)) {
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
	 * @param tokens
	 */
	public void spotNouns(List<JSONObject> tokens) {
		spotNounsA(tokens);
		spotNounsB(tokens);
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
		environment.logDebug("SPOTNOUNA\n"+tokens);
		for (int i=0;i<len;i++) {
			tokA = tokens.get(i);
			if (tokA.getAsString("pos").equals("DET")) {
				
				if (i+2 < len) {
					tokC = tokens.get(i+2);
					if (tokC.getAsString("pos").equals("VERB")) {
						tokB = tokens.get(i+1);
						environment.logDebug("SPOTNOUNA-1 "+tokB+"\n"+tokA+" "+tokB);
						if (tokB.getAsString("dep").equals("nsubj"))
							tokB.put("pos", "NOUN");
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
		int len = tokens.size();
		JSONObject tokA, tokB, tokC;
		for (int i=0;i<len;i++) {
			tokA = tokens.get(i);
			if (tokA.getAsString("pos").equals("DET")) {
				if ("nsubj".equalsIgnoreCase(tokA.getAsString("dep"))) {
					//Most general form
					// Any DET/nsubj is a NOUN
					// We may have to specialize if this causes trouble
					tokA.put("pos", "NOUN");
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
