/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.vocab.api;

/**
 * @author jackpark
 *
 */
public interface IServerConstants {
	public static final String
		PHRASE		= "phrase",	// carries phrase in during add
		TYPE		= "type",	// carries type codee in during add
		VERB		= "verb",
		ADD_VERB	= "addPhrase",
		LIST_VERB	= "listVerb",
		CARGO		= "cargo", // carries sentence to server during list
		NOUNS		= "nouns",	// returns list of nouns
		VERBS		= "verbs";	// returns list of verbs
}
