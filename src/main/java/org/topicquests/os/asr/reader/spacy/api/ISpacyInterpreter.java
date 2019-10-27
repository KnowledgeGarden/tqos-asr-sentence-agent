/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy.api;

import org.topicquests.hyperbrane.api.ISentence;
import org.topicquests.support.api.IResult;

/**
 * @author jackpark
 *
 */
public interface ISpacyInterpreter {

	
	IResult processSentence(ISentence sentence);
}
