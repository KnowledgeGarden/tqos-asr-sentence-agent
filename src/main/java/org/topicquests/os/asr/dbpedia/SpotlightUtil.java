/*
 * Copyright 2018 TopicQuests Foundation
 *  This source code is available under the terms of the Affero General Public License v3.
 *  Please see LICENSE.txt for full license terms, including the availability of proprietary exceptions.
 */
package org.topicquests.os.asr.dbpedia;

import java.util.*;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class SpotlightUtil {
	private JSONObject data;
	/**
	 * 
	 */
	public SpotlightUtil(JSONObject jo) {
		setJSON(jo);
	}

	public void setJSON(JSONObject jo) {
		data = jo;
	}
	
	public String getURI(JSONObject jo) {
		return (String)jo.get("@URI");
	}
	
	public String getTerm(JSONObject jo) {
		return jo.getAsString("@surfaceForm");
	}
	
	/**
	 * Can return a string or a list
	 * @param jo
	 * @return
	 */
	public Object getType(JSONObject jo) {
		return jo.get("@types");
	}
	
	/**
	 * Can throw an exception
	 * @param jo
	 * @return
	 */
	public double getSimilarityScore(JSONObject jo) {
		return Double.parseDouble(jo.getAsString("@similarityScore"));
	}
	
	/**
	 * Can return <code>null</code>
	 * @return
	 */
	public List<JSONObject> listResources() {
		return (List<JSONObject>)data.get("Resources");
	}
	
	/**
	 * Can return <code>null</code>
	 * @param term
	 * @return
	 */
	public JSONObject findTerm(String term) {
		List<JSONObject> ll = listResources();
		if (ll != null) {
			JSONObject jo;
			Iterator<JSONObject>itr = ll.iterator();
			while (itr.hasNext()) {
				jo = itr.next();
				if (getTerm(jo).equals(term))
					return jo;
			}
		}
		return null;
	}
}
