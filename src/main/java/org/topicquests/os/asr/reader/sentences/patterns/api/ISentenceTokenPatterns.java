/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.sentences.patterns.api;

/**
 * @author jackpark
 * @see https://spacy.io/api/annotation
 * @see https://universaldependencies.org/u/pos/all.html
 */
public interface ISentenceTokenPatterns {
	public static final String
		//adjective
		ADJacomp		= "ADJacomp",
		ADJamod			= "ADJamod",
		ADJconj			= "ADJconj",
		//adposition
		//Adposition is a cover term for prepositions and postpositions. 
		//Adpositions belong to a closed set of items that occur before 
		//(preposition) or after (postposition) a complement composed of a 
		//noun phrase, noun, pronoun, or clause that functions as a noun phrase, 
		//and that form a single structure with the complement to express its 
		//grammatical and semantic relation to another unit within a clause.
		ADPcase			= "ADPcase",
		ADPprep			= "ADPprep",
		//adverb
		ADVadvmod		= "ADVadvmod",
		//aux
		//An auxiliary is a function word that accompanies the lexical verb 
		//of a verb phrase and expresses grammatical distinctions not carried 
		//by the lexical verb, such as person, number, tense, mood, aspect, voice 
		//or evidentiality. It is often a verb (which may have non-auxiliary uses 
		//as well) but many languages have nonverbal TAME markers and these should
		//also be tagged AUX. The class AUX also include copulas (in the narrow 
		//sense of pure linking words for nonverbal predication).
		//TODO find some examples
		
		//coordinating conjunction
		//A coordinating conjunction is a word that links words or larger constituents without 
		//syntactically subordinating one to the other and expresses a semantic 
		//relationship between them
		CCONJcc			= "CCONJcc",		// e.g. and, or, but
		//determiner
		//Determiners are words that modify nouns or noun phrases and express the reference of 
		//the noun phrase in context. That is, a determiner may indicate whether the 
		//noun is referring to a definite or indefinite element of a class, to a closer 
		//or more distant element, to an element belonging to a specified person or thing, 
		//to a particular number or quantity, etc.
		DETdet			= "DETdet",
		DETcc_preconj	= "DETcc_preconj",
		//interjection
		//An interjection is a word that is used most often as an exclamation or 
		//part of an exclamation. It typically expresses an emotional reaction, is 
		//not syntactically related to other accompanying expressions, and may 
		//include a combination of sounds not otherwise found in the language.
		//TODO find some examples

		//noun
		NOUN			= "NOUN",  			// case where there were no other annotations
		NOUNappos		= "NOUNappos",		// e.g. AD as an accronym for Alzheimer's Disease
		NOUNcompound	= "NOUNcompound",
		NOUNconj		= "NOUNconj",
		NOUNdobj		= "NOUNdobj",		//direct object?
		NOUNnmod		= "NOUNnmod",
		NOUNnmod_poss	= "NOUNnmod_poss",	// Possessive Noun, e.g. Alzheimer followed by "'s" << PARTcase
		NOUNnsubj		= "NOUNnsubj",
		NOUNnsubjpass	= "NOUNnsubjpass",
		NOUNpobj		= "NOUNpobj",		// object of preposition?
		//numeral
		NUMappos		= "NUMappos",
		NUMdep			= "NUMdep",
		NUMnummod		= "NUMnummod",
		//particle
		//Particles are function words that must be associated with another word or phrase 
		//to impart meaning and that do not satisfy definitions of other universal
		//parts of speech (e.g. adpositions, coordinating conjunctions, subordinating
		//conjunctions or auxiliary verbs). Particles may encode grammatical 
		//categories such as negation, mood, tense etc. Particles are normally not 
		//inflected, although exceptions may occur.
		PARTcase		= "PARTcase",		// e.g. "'s" following "Alzheimer"
		PARTprep		= "PARTprep",
		//pronoun
		PRONnmod_poss	= "PRONnmod_poss",
		PRONnsubj 		= "PRONnsubj", 
		PRONnsubjpass	= "PRONnsubjpass",
		PRONposs		= "PRONposs",
		//proper noun
		PROPNcompound	= "PROPNcompound",
		PROPNpobj		= "PROPNpobj",
		
		//punctuation
		PUNCTpunct		= "PUNCTpunct",
		//SCONJ subordinating conjunction
		//A subordinating conjunction is a conjunction that links constructions by making one of them 
		//a constituent of the other. The subordinating conjunction typically marks the 
		//incorporated constituent which has the status of a (subordinate) clause.
		// e.g. "I believe that he will come", "if", "while"
		//TODO
		
		//SYM: symbol
		//A symbol is a word-like entity that differs from ordinary words by form, 
		//function, or both.
		//Many symbols are or contain special non-alphanumeric characters, similarly
		//to punctuation. What makes them different from punctuation is that they can be 
		//substituted by normal words. This involves all currency symbols, 
		//e.g. $ 75 is identical to seventy-five dollars.
		//TODO
		
		//verb
		VERB			= "VERB",			// case where no other annotations
		VERBacl_relcl	= "VERBacl_relcl",
		VERBadvcl		= "VERBadvcl",
		VERBaux			= "VERBaux",
		VERBauxpass		= "VERBauxpass",
		VERBcompound	= "VERBcompound",	// might be following by a Noun? "driving"
		VERBconj		= "VERBconj",
		VERBROOT		= "VERBROOT",
		VERBrelcl		= "VERBrelcl";
		//X: other
		//The tag X is used for words that for some reason cannot be assigned a real part-of-speech category. 
		//It should be used very restrictively.
		//TODO
		
		
		
		

}
