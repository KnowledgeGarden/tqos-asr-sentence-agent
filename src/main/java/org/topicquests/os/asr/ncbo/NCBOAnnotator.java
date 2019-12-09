/**
 * 
 */
package org.topicquests.os.asr.ncbo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.topicquests.support.api.IEnvironment;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author jackpark
 * @see https://github.com/ncbo/ncbo_rest_sample_code/blob/master/java/src/AnnotateText.java
 */
public class NCBOAnnotator {
    private final String REST_URL = "http://data.bioontology.org/";
	private IEnvironment environment;
	private AnnotationHandler handler;

	/**
	 * 
	 */
	public NCBOAnnotator(IEnvironment env) {
		environment = env;
		handler = new AnnotationHandler(environment);
	}

	public JSONArray annotate(String text, String apiKey) throws Exception {
		JSONArray result = null;
        String textToAnnotate = URLEncoder.encode(text,"utf-8");
        String urlParameters = "annotator?text=" + textToAnnotate+"&display_context=false";
		String json = get(REST_URL+urlParameters, apiKey);
		if (json != null) {
			environment.logDebug("NCBOAnnotator "+json);
			JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			result = (JSONArray)p.parse(json);
		}
		return result;
	}

	String get(String urlToGet, String apiKey) {
		environment.logDebug("XXX\n"+urlToGet+"\n"+apiKey);
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToGet);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "apikey token=" + apiKey);
            conn.setRequestProperty("Accept", "application/json");
            rd = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
        	environment.logError(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }
}
