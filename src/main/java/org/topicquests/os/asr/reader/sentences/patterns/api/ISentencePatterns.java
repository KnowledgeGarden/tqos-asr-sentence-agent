/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.sentences.patterns.api;

/**
 * @author jackpark
 *
 */
public interface ISentencePatterns {
	public static final String
		NVN		= "NVN", 	// Noun Verb Noun (simple)
		NVNVN	= "NVNVN",	// Noun Verb Noun Verb Noun (nested?)
		DNofNCNVN	= "DNPNCNVN",	// Conjunctive Nouns and isA - "the X of A and B"
		NVNCPVN	= "NVNCPVN",	// two triples with anaphora "X pred Y and it pred M"
		NVNCPNVN	= "NVNCPNVN";	// two triples with anaphora "X pred Y and its Z pred M"
				// its Z --> X Z

}
