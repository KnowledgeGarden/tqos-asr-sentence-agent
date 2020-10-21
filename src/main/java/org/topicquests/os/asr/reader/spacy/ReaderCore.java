/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.topicquests.os.asr.reader.spacy.api.ICoreReader;
import org.topicquests.os.asr.reader.spacy.api.IParagraphObjectFields;
import org.topicquests.support.api.IEnvironment;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class ReaderCore implements ICoreReader {
	private IEnvironment environment;

	/**
	 * @param env
	 */
	public ReaderCore(IEnvironment env) {
		environment = env;
	}

	@Override
	public void interpretMainModel(JSONObject paragraphObject, JSONObject spacyModel) {
		//environment.logDebug("SpacyInterpreter.interpretMainModel\n"+spacyModel);
		
		// create a list for sentenceObjects
		List<JSONObject> sentenceObjects = new ArrayList<JSONObject>();
		// inject it into the paragraphObject
		paragraphObject.put(IParagraphObjectFields.SENTENCE_OBJECT_KEY, sentenceObjects);
		// fetch the sentences returned by the model
		List<JSONObject> sentences = (List<JSONObject>)spacyModel.get(IParagraphObjectFields.SENTENCES_KEY);
		// fetch the raw tokens
		List<JSONObject> tokens = (List<JSONObject>)spacyModel.get("tok_info");
		paragraphObject.put(IParagraphObjectFields.PARAGRAPH_RAW_TOKENS, tokens);
		
		// fetch the NounChunks
		List<JSONObject> nounChunks = (List<JSONObject>)spacyModel.get("noun_chunks");
		paragraphObject.put(IParagraphObjectFields.NOUN_CHUNKS, nounChunks);

		// MasterTokens
		List<JSONObject> masterTokens = new ArrayList<JSONObject>();
		paragraphObject.put(IParagraphObjectFields.MASTER_TOKENS, masterTokens);
		// ParagraphRawTokens
		List<JSONObject> rawTokens = new ArrayList<JSONObject>();
		paragraphObject.put(IParagraphObjectFields.PARAGRAPH_RAW_TOKENS, rawTokens);

		// paragraph token map
		// A map of tokens in the paragraph indexed on their start position
		JSONObject paragraphTokenMap = new JSONObject();
		paragraphObject.put(IParagraphObjectFields.PARAGRAPH_TOKEN_MAP_KEY, paragraphTokenMap);

		//Populate masterTokens and ParagraphTokenMap
		Iterator<JSONObject> titr = tokens.iterator();
		JSONObject jo;
		Number st;
		while (titr.hasNext()) {
			//for every token in the model
			jo = titr.next();
			st = jo.getAsNumber("start");
			paragraphTokenMap.put(st.toString(), jo);
			masterTokens.add(jo);
			rawTokens.add(jo);
		}
		//environment.logDebug("PARAGRAPHTOKENS\n"+paragraphTokenMap);
		// create a list for DBpedia hits
		List<JSONObject>dbPediaObjects = new ArrayList<JSONObject>();
		// inject it into the paragraphObject
		paragraphObject.put(IParagraphObjectFields.DBPEDIA_OBJECT_KEY, dbPediaObjects);
		
		//create a list for MainEntities
		List<JSONObject> entities = (List<JSONObject>)spacyModel.get("entities");
		paragraphObject.put(IParagraphObjectFields.MAIN_ENTITIES, entities);

		//prepare to process each sentence
		//DBpedia
		List<JSONObject> dbps;
		List<JSONObject> allDbps = new ArrayList<JSONObject>();


		// text for each sentence
		String theSentence;
				
		// the SentenceObject for each sentence	
		JSONObject sentenceObject;
		JSONObject sx;
		int sstart, send;
		Number  nFirst = null, nLast = null;

		Iterator<JSONObject> sitr = sentences.iterator();
		while (sitr.hasNext()) {
			//for each sentence
			///////////////////
			// {
		    //  "start": 0,
		    //  "end": 6,
		    //  "text": " This is a sentence."
		    // }
			////////////////////
			sx = sitr.next();
			//Start the sentenceObject
			theSentence = sx.getAsString("text");
			sentenceObject = new JSONObject();
			sentenceObjects.add(sentenceObject);
			nFirst = sx.getAsNumber("start");
			nLast = sx.getAsNumber("end");
			sentenceObject.put(IParagraphObjectFields.SENTENCE_TOKEN_START, nFirst);
			sentenceObject.put(IParagraphObjectFields.SENTENCE_TOKEN_END, nLast);
			sentenceObject.put(IParagraphObjectFields.SENTENCE_TEXT, theSentence);
			//houskeeping
			sstart = nFirst.intValue();
			send = nLast.intValue();
			
			//DBpedia
			// Add any DBpedia hits to sentence and accumulate for paragraph
			dbps = null; // TODO processDBpedia(sentenceObject.getAsString("text"));
			if (dbps != null && !dbps.isEmpty()) {
				sentenceObject.put(IParagraphObjectFields.DBPEDIA_OBJECT_KEY, dbps);
				allDbps.addAll(dbps);
			}
			// put all of them into the paragraphObject for later nouns. -- might be empty
			paragraphObject.put(IParagraphObjectFields.DBPEDIA_OBJECT_KEY, allDbps);
		}
		paragraphObject.put(IParagraphObjectFields.SENTENCE_OBJECT_KEY, sentenceObjects);
	}


}
