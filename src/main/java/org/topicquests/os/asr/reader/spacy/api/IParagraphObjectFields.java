/**
 * 
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
		SENTENCE_PATTERNS		= "sentencePattern",
		BEFORE_NOUN_TOKENS		= "beforeNouns",
		AFTER_NOUN_TOKENS		= "afterNouns",
		//these are paragraph level
		PARAGRAPH_TOKEN_MAP_KEY	=	"paragraphTokens",
		PARAGRAPH_ID			= "paragraphId",
		DOCUMENT_ID				= "documentId",
		MASTER_TOKENS			= "masterTokens", // all tokens with nouns updated
		POS_TOKENS				= "posTokens",
		NOUN_CHUNKS				= "nounChunks",
		NOUN_PHRASES			= "nounPhrases",
		MAIN_ENTITIES			= "mainEntities", // from the main model
		ENTITY_NOUNS			= "entityNouns",
		PREDICATE_PHRASES		= "predPhrases",
		VOCAB_NOUNS				= "vocabNouns",
		VOCAB_VERBS				= "vocabVerbs";
}
