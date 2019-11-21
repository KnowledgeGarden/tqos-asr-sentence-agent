/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy;

import java.util.ArrayList;
import java.util.List;

import org.topicquests.hyperbrane.api.ILexTypes;
import org.topicquests.os.asr.reader.sentences.SentencesEnvironment;
import org.topicquests.os.asr.reader.spacy.api.ISpacyConstants;
import org.topicquests.os.asr.reader.spacy.api.IParagraphObjectFields;
import org.topicquests.support.api.IEnvironment;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class PredicateScanner {
	private IEnvironment environment;
	private SpacyUtil util;

	/**
	 * 
	 */
	public PredicateScanner(IEnvironment env, SpacyUtil u) {
		environment = env;
		util = u;
	}
	/**
	{
				"parent": 567,   <<<<< Refers forward to a NounPhrase
				"pos": "DET",
				"start": 562,
				"lemma": "this",  
				"text": "This",  <<<< NOUN
				"tag": "DT",
				"sent": 562,
				"dep": "nsubj"
			}, {
				"parent": 575,    <<<<<<Start VerbPhrase
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
				"dep": "ROOT"      <<<<<<<<End VerbPhrase
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
	
	/** bug
	 * It can be anticipated that this principle will apply
	 * predPhrase: can be anticipated will apply
	 * conjecture: missing stopping rule
	 * fixed by stopping on !VERB
{
			"pos": "VERB",
			"tree_e_idx": 1513,
			"start": 1422,
			"lemma": "anticipate",
			"tree_s_idx": 1412,
			"text": "anticipated",
			"tag": "VBN",
			"sent": 1412,
			"dep": "ROOT"
		}, {
			"parent": 1459,
			"pos": "ADP",
			"start": 1434,
			"text": "that",
			"tag": "IN",
			"sent": 1412,
			"dep": "mark"
		},
	 
	 */
	/** bug
	 * Such biomarker studies have revealed that a huge window of opportunity exists when application
	 * predPhrase:exists when could arrest
	 * suggests that the stopping rule happens too late because ADV is trapped earler
{
			"parent": 1194,
			"pos": "VERB",
			"tree_e_idx": 1397,
			"start": 1237,
			"lemma": "exist",
			"tree_s_idx": 1203,
			"text": "exists",
			"tag": "VBZ",
			"sent": 1166,
			"dep": "ccomp"
		}, {
			"parent": 1345,
			"pos": "ADV",
			"start": 1244,
			"text": "when",
			"tag": "WRB",
			"sent": 1166,
			"dep": "advmod"
		}
	 */
	
	/** bug
	 * VP not picked up
	 * Verb/aux:ADP:Verb/ROOT
{
	"parent": 694,
	"pos": "VERB",
	"start": 683,
	"text": "have",
	"tag": "VBP",
	"sent": 646,
	"dep": "aux"
}, {
	"parent": 694,
	"pos": "ADP",
	"start": 688,
	"text": "since",
	"tag": "IN",
	"sent": 646,
	"dep": "mark"
}, {
	"pos": "VERB",
	"tree_e_idx": 773,
	"start": 694,
	"lemma": "show",
	"tree_s_idx": 646,
	"text": "showed",
	"tag": "VBN",
	"sent": 646,
	"dep": "ROOT"
}
The one above works
This one below fails
{
	"parent": 924,
	"pos": "VERB",
	"start": 914,
	"lemma": "have",
	"text": "has",
	"tag": "VBZ",
	"sent": 903,
	"dep": "aux"
}, {
	"parent": 924,
	"pos": "ADP",
	"start": 918,
	"text": "since",
	"tag": "IN",
	"sent": 903,
	"dep": "advmod"
}, {
	"pos": "VERB",
	"tree_e_idx": 1040,
	"start": 924,
	"lemma": "emerge",
	"tree_s_idx": 903,
	"text": "emerged",
	"tag": "VBN",
	"sent": 903,
	"dep": "ROOT"
}
	 */
	///////////////////////////
	// VerbPhrase Scanning
	///////////////////////////

	/**
	 * Scans along MasterTokens for predicate phrases
	 * @param tokens
	 * @param paragraphObject
	 */
	public void scan4VerbPhrases(List<JSONObject> tokens, JSONObject paragraphObject) {
		environment.logDebug("AVB\n"+tokens);
		//List<JSONObject> tokens = (List<JSONObject>)sentenceObject.get("tokens");
		JSONObject paragraphTokenMap = (JSONObject)paragraphObject.get(IParagraphObjectFields.PARAGRAPH_TOKEN_MAP_KEY);
		JSONObject entityNounMap = (JSONObject)paragraphObject.get(IParagraphObjectFields.ENTITY_NOUNS); //NOUN_PHRASES);
		//JSONObject nounPhraseMap = (JSONObject)paragraphObject.get(IParagraphObjectFields.VOCAB_NOUNS); //ENTITY_NOUNS);
		int toklen = tokens.size();
		JSONObject tok, tok1;
		// A list to accumulate predicate phrases
		// It gets emptied after each predicate phrase is detected
		List<JSONObject> preds = new ArrayList<JSONObject>();
		// a fresh predPhrases List for this paragraph
		JSONObject verbPhraseMap = new JSONObject();
		paragraphObject.put(IParagraphObjectFields.PREDICATE_PHRASES, verbPhraseMap);
		int tStart;
		String pos, dep;
		Number start = 0;
		boolean found = false;
		String tid;
		JSONObject pix;
		boolean tFound = false;
		boolean haveVERB = false;
		for (int i = 0; i<toklen; i++) {
			// for each token
			tok = tokens.get(i);
			tStart = tok.getAsNumber("start").intValue();
			environment.logDebug("AA "+found+" "+tok);
			pos = tok.getAsString("pos");
			dep = tok.getAsString("dep");
			if (found)
				environment.logDebug("XXX "+preds.size()+" "+tok+"\n"+preds);
			// see if tok is a VERB and it is not a ROOT verb
			// but a ROOT can exist if the preds list has some adverbs, etc
			if (ISpacyConstants.VERB.equalsIgnoreCase(pos)) {
				tFound = found;
				boolean vFound = vpVerb(i, found, tok, preds, paragraphTokenMap, /*nounPhraseMap,*/ entityNounMap, tokens);
				if (!tFound && vFound) {
					start = tok.getAsNumber("start");
					found = true;
					//Verb/aux:ADP:Verb/ROOT
					// we wish to allow an ADP as the next term
					// but we don't want an ADP in otherwise, so we hold haveVERB off
					if (!dep.equalsIgnoreCase(ISpacyConstants.AUX))
						haveVERB = true;
				} else if (tFound && !vFound) {
					//trailing VERB
					if (!dep.equalsIgnoreCase(ISpacyConstants.AUX))
						haveVERB = true;
				}
			} else if (found && ISpacyConstants.NOUN.equalsIgnoreCase(pos)) { 
				//for the case where a noun follows a verb
				// we must trap for a noun here first,
				// since this particular NOUN might follow a particular VERB
				found = vpNoun(i, found, tok, preds);
			// if it is an AUX which has a VERB as the next word, take it
			} else if (!haveVERB && (ISpacyConstants.AUX.equalsIgnoreCase(pos))) { 
				tFound = found;
				found = vpAux(i, found, tok, preds, tokens);
				if (!tFound && found)
					start = tok.getAsNumber("start");
			// if it is an ADV which has a parent later in the tokens, take it
			// may need to study this one
			} else if (!haveVERB && ISpacyConstants.ADV.equalsIgnoreCase(pos)) {
				tFound = found;
				found = vpAdv(i, found, tok, preds);
				if (!tFound && found)
					start = tok.getAsNumber("start");
			// if a VERB is found, and this is an ADP, take it
			} else if (!haveVERB && found && ISpacyConstants.ADP.equalsIgnoreCase(pos)) {
				vpAdp(i, found, tok, preds, tokens);
			// NOUN or PRON will stop this
			} else if (found && (!ISpacyConstants.VERB.equalsIgnoreCase(pos))
					/*"NOUN".equalsIgnoreCase(pos) || 
						"PRON".equalsIgnoreCase(pos) ||
						"ADJ".equalsIgnoreCase(pos))*/) {
				if (preds.size() > 1) {
					environment.logDebug("B "+preds);
					verbPhraseMap.put(start.toString(), util.toPhrase(ISpacyConstants.VERB, start.intValue(), preds));
				}
				preds = new ArrayList<JSONObject>();
				found = false;
			}
			
		}
		
	}
	
	boolean vpVerb(int where, boolean found, JSONObject tok, List<JSONObject> preds, JSONObject paragraphTokenMap,
			/*JSONObject nounPhraseMap,*/ JSONObject entityNounMap, List<JSONObject> tokens) {
		environment.logDebug("VPVERB "+tok+"\n"+preds);
		String dep = tok.getAsString("dep");
		// if this verb is already in a noun phrase, ignore it
		//if (nounPhraseMap.containsKey(tok.getAsNumber("start").toString()))
		//	return false;
		if ("amod".equalsIgnoreCase(dep)) {
			JSONObject tok1 = tokens.get(where+1); //TODO DANGER or array out of bounds
			if (ISpacyConstants.NOUN.equalsIgnoreCase(tok1.getAsString("pos"))) {
				preds.add(tok);
				if (!found) {
					return true;
				}				
			}
		} else if (preds.size() > 0 || !dep.equalsIgnoreCase(ISpacyConstants.ROOT)) {
			if (preds.size() > 0 || !util.parentPointsToNoun(tok.getAsNumber("parent"), paragraphTokenMap, /*nounPhraseMap,*/ entityNounMap)) {
				preds.add(tok);
				if (!found) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Here for AUX
	 * @param where
	 * @param found
	 * @param tok
	 * @param preds
	 * @param tokens
	 * @return
	 */
	boolean vpAux(int where, boolean found, JSONObject tok, List<JSONObject> preds, List<JSONObject>tokens) {
		JSONObject tok1 = tokens.get(where+1); //TODO DANGER or array out of bounds
		if (ISpacyConstants.VERB.equalsIgnoreCase(tok1.getAsString("pos"))) {
			preds.add(tok);
			return true;
		}
		return false;
	}
	
	void vpAdp(int where, boolean found, JSONObject tok, List<JSONObject> preds, List<JSONObject>tokens) {
		//must filter out if follows a verb, or test if is followed by a verb
		JSONObject tok1 = tokens.get(where+1); //TODO DANGER or array out of bounds
		if (ISpacyConstants.VERB.equalsIgnoreCase(tok1.getAsString("pos")))
			preds.add(tok);
	}
	
	/**
	 * possible cases where Noun follows Verb
	 * @param where
	 * @param found
	 * @param tok
	 * @param preds
	 * @return
	 */
	boolean vpNoun(int where, boolean found, JSONObject tok, List<JSONObject> preds) {
		// case 1: preds has just one verb and that verb is an amod
		JSONObject tok1 = preds.get(0);
		if (ISpacyConstants.VERB.equalsIgnoreCase(tok1.getAsString("pos")) &&
			ISpacyConstants.AMOD.equalsIgnoreCase(tok1.getAsString("dep"))) {
			preds.add(tok);
			return true;		
		}
		return false;
	}
	
	/**
	 * Adverb with parent > start
	 * @param where
	 * @param found
	 * @param tok
	 * @param preds
	 * @return
	 */
	boolean vpAdv(int where, boolean found, JSONObject tok, List<JSONObject> preds) {
		int tStart = tok.getAsNumber("start").intValue();
		if (tStart < tok.getAsNumber("parent").intValue()) {
			preds.add(tok);
			return true;
		}
		return false;
	}
	
	//////////////////////////
	// VERB/amod followed by NOUN/root --> VerbPhrase
	//////////////////////////
	
}
