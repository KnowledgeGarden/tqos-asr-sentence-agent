/**
 * Copyright 2011 Pablo Mendes, Max Jakob
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.topicquests.os.asr.dbpedia;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.mime.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.topicquests.os.asr.reader.sentences.SentencesEnvironment;
import org.topicquests.support.api.IEnvironment;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

import java.io.*;
import java.net.URLEncoder;

/**
 * Simple web service-based annotation client for DBpedia Spotlight.
 *
 * @author pablomendes, Joachim Daiber
 * @author park -- stripped down for local use
 */

public class DBpediaSpotlightClient  {
	private IEnvironment environment;
//@see https://github.com/dbpedia-spotlight/dbpedia-spotlight-model
    private final String API_URL;// = "http://api.dbpedia-spotlight.org/en/annotate?"; //"http://spotlight.dbpedia.org/";
	private final double CONFIDENCE = 0.5;
	private final int SUPPORT = 0;
	private final Object syncObj = new Object();

	public DBpediaSpotlightClient(IEnvironment env) {
		environment = env;
	    API_URL = environment.getStringProperty("SpotlightURL")+"/annotate?";
	}

	public JSONObject extract(String text) throws Exception {
        environment.logDebug("DBpediaSpotlight.extract "+text);
 		String spotlightResponse = null;
		CloseableHttpClient httpclient = null;
		try {
			synchronized(syncObj) {
				syncObj.wait(2000);
			}
			httpclient = HttpClients.custom()
			        .setRetryHandler(new DefaultHttpRequestRetryHandler(3, false))
			        .build();
			String query = API_URL +
					//URLEncoder.encode("text="+text, "utf-8") +
					//"--data confidence=0.35"); 
					"confidence=" + CONFIDENCE
					+ "&support=" + SUPPORT
					+ "&text=" + URLEncoder.encode(text, "utf-8");
	        environment.logDebug("DBpediaSpotlight.extract-1 "+query);
		
			HttpGet httpGet = new HttpGet(query);
			httpGet.addHeader("Accept", "application/json");
			HttpResponse response = httpclient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			environment.logDebug("RESPONSECODE : " + statusCode);
			if (statusCode == 200) {
				BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
	
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				spotlightResponse = result.toString();
				System.out.println("DBSC-2 "+spotlightResponse);
			}
		
		} finally {
			if (httpclient != null)
				httpclient.close();
		}

		JSONObject resultJSON = null;
		if (spotlightResponse != null) {
			try {
				JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
				resultJSON = (JSONObject)p.parse(spotlightResponse);
			} catch (Exception e) {
				throw new Exception("Received invalid response from DBpedia Spotlight API.");
			}
		}
		return resultJSON;
	}


}
