/**
 * Copyright 2019, TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.reader.spacy.api;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public interface ICoreReader {

	/**
	 * <p>This expects a {@code spacyModel} which offers, at the very least:<br/>
	 *  * the tokens field <em>token_info</em> which includes <em>pos</em>, 
	 *  <em>start</em>, <em>text</em><br/>
	 *  * the sentences field <em>sentences</em> which includes <em>start</em>,
	 *  <em>end</em> and <em>text</em>
	 *  </p>
	 * <p>{@code paragraphObject} can be an empty {@code JSONObject}</p>
	 * <p>If several spacy models are exercised on the same paragraph, begin with this model
	 * since it provides all the necessary <em>pos</em> tokens, while other models may
	 * provide only the tokens for location reference.</p>
	 * @param paragraphObject
	 * @param spacyModel
	 */
	void interpretMainModel(JSONObject paragraphObject, JSONObject spacyModel);
}
