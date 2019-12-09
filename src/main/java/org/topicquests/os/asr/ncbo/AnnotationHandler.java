/**
 * 
 */
package org.topicquests.os.asr.ncbo;

import org.topicquests.support.api.IEnvironment;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class AnnotationHandler {
	private IEnvironment environment;

	/**
	 * 
	 */
	public AnnotationHandler(IEnvironment env) {
		environment = env;
	}

	public JSONObject annotate(JSONArray annotations) {
		JSONObject result = new JSONObject();
		
		return result;
	}

}
