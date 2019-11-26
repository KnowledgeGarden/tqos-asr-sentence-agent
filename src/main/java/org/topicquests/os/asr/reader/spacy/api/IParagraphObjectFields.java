/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy.api;

/**
 * @author jackpark
 *
 */
public interface IParagraphObjectFields {
	public final String
		USER_ID		= "SystemUser",
		SENTENCE_OBJECT_KEY 	=	"sentenceObjects",
		I_SENTENCE_OBJECT_KEY	= 	"iSentenceObjects",
		DBPEDIA_OBJECT_KEY		=	"dbPediaObjects",
		SENTENCES_KEY			= "sentences", // sentenceObjects
		SENTENCE_ARRAY_LIST_KEY		= "sentenceArray", // possibly many arrays
		/** SentenceTokens are drawn from a paragraphs MASTER_TOKENS after all processing is finished */
		SENTENCE_TOKEN_LIST		= "sentenceTokens",
		SENTENCE_TOKEN_PATTERNS		= "sentenceTokenPattern", // patterns of individual tokens
		SENTENCE_PATTERN		= "sentencePattern",	// pattern of entire sentence, e.g. NVN
		BEFORE_NOUN_TOKENS		= "beforeNouns",
		AFTER_NOUN_TOKENS		= "afterNouns",
		SENTENCE_TOKEN_START	= "tokenStart",
		SENTENCE_TOKEN_END		= "tokenEnd",
		SENTENCE_TEXT			= "text",
		//these are paragraph level
		PARAGRAPH_TOKEN_MAP_KEY	=	"paragraphTokens",
		PARAGRAPH_ID			= "paragraphId",
		DOCUMENT_ID				= "documentId",
		MASTER_TOKENS			= "masterTokens", // all tokens with nouns updated
		MASTER_PATTERNS			= "masterPatterns", // String of patterns for masterTokens
		PARAGRAPH_RAW_TOKENS	= "rawTokens",
		NOUN_CHUNKS				= "nounChunks",
		NOUN_PHRASES			= "nounPhrases",
		MAIN_ENTITIES			= "mainEntities", // from the main model
		ENTITY_NOUNS			= "entityNouns",
		PREDICATE_PHRASES		= "predPhrases",
		TRAILING_PHRASES		= "trailingPhrases",
		VOCAB_NOUNS				= "vocabNouns",
		VOCAB_VERBS				= "vocabVerbs";
}
