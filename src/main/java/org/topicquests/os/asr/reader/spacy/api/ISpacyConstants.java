/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy.api;

/**
 * @author jackpark
 * @see https://spacy.io/api/annotation
 * @see https://spacy.io/usage/linguistic-features
 * @see https://github.com/explosion/spaCy/blob/master/spacy/symbols.pxd
 */
public interface ISpacyConstants {
	public static final String
	   	ADJ					= "ADJ",
	   	ADP					= "ADP",
	   	ADV					= "ADV",
	   	AUX					= "AUX",
	    CONJ				= "CONJ",
	    CCONJ				= "CCONJ",
	    DET					= "DET",
	    INTJ				= "INTJ",
	    NOUN				= "NOUN",
	    NUM					= "NUM",
	    PART				= "PART",
	    PRON				= "PRON",
	    PROPN				= "PROPN",
	    PUNCT				= "PUNCT",
	    SCONJ				= "SCONJ",
	    SYM					= "SYM",
	    VERB				= "VERB",
	    X					= "X",
	    EOL					= "EOL",
	    SPACE				= "SPACE";
	
	public static final String
	
	    PERSON				= "PERSON",
	    NORP				= "NORP",
	    FACILITY			= "FACILITY",
	    ORG					= "ORG",
	    GPE					= "GPE",
	    LOC					= "LOC",
	    PRODUCT				= "PRDUCT",
	    EVENT				= "EVENT",
	    WORK_OF_ART			= "WORK_OF_ART",
	    LANGUAGE			= "LANGUAGE",
	    LAW					= "LAW",
	
	    DATE				= "DATE",
	    TIME				= "TIME",
	    PERCENT				= "PERCENT",
	    MONEY				= "MONEY",
	    QUANTITY			= "QUANTITY",
	    ORDINAL				= "ORDINAL",
	    CARDINAL			= "CARDINAL";

	public static final String
	
		ACOMP				= "acomp",
		ADVCL				= "advcl",
		ADVMOD				= "advmod",
		AGENT				= "agent",
		AMOD				= "amod",
		APPOS				= "appos",
		ATTR				= " attr",
		AUX_				= "aux",
		AUXPASS				= "auxpass",
		CC					= "cc",
		CCOMP				= "ccomp",
		COMPLM				= "complm",
		CONJ_				= "conj",
		COP					= "cop",
		CSUBJ				= "csubj",
		CSUBJPASS			= "csubjpass",
		DEP					= "dep",
		DET_				= "det",
		DOBJ				= "dobj",
		EXPL				= "expl",
		HMOD				= "hmod",
		HYPH				= "hyph",
		INFMOD				= "infmod",
		INTJ_				= "intj",
		IOBJ				= "iobj",
		MARK				= "mark",
		META				= "meta",
		NEG					= "neg",
		NMOD				= "nmod",
		NN					= "nn",
		NPADVMOD			= "npadvmod",
		NSUBJ				= "nsubj",
		NSUBJPASS			= "nsubjpass",
		NUM_				= "num",
		NUMBER				= "number",
		OPRD				= "oprd",
		OBJ					= "obj",
		OBL					= "obl",
		PARATAXIS			= "parataxis",
		PARTMOD				= "partmod",
		PCOMP				= "pcomp",
		POBJ				= "pobj",
		POSS				= "poss",
		POSSESSIVE			= "possessive",
		PRECONJ				= "preconj",
		PREP				= "prep",
		PRT					= "prt",
		PUNCT_				= "punct",
		QUANTMOD			= "quantmod",
		RECL				= "relcl",
		RCMOD				= "rcmod",
		ROOT				= "root",
		XCOMP				= "xcomp",

		ACL						= "acl";

}
