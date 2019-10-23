/**
 * 
 */
package devtests;

import org.topicquests.support.api.IResult;

import java.io.*;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

/**
 * @author jackpark
 * This test simply exercises the system
 * No computation performed
 */
public class FirstTest extends TestRoot {
	private final String // Spacy data
		D1 	= "data/S1.txt",
		D2 	= "data/S2.txt",
		D3 	= "data/S3.txt",
		D4 	= "data/S4.txt",
		D5 	= "data/S5.txt";


	/**
	 * 
	 */
	public FirstTest() {
		IResult r;
		JSONObject jo = toJO(D1);
		if (jo != null)
			r = agent.acceptSpacyJSON(jo);
		jo = toJO(D2);
		if (jo != null)
			r = agent.acceptSpacyJSON(jo);
		jo = toJO(D3);
		if (jo != null)
			r = agent.acceptSpacyJSON(jo);
		jo = toJO(D4);
		if (jo != null)
			r = agent.acceptSpacyJSON(jo);
		jo = toJO(D5);
		if (jo != null)
			r = agent.acceptSpacyJSON(jo);
		environment.shutDown();
		System.exit(0);
	}
	
	JSONObject toJO(String path) {
		JSONObject result = null;
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			JSONParser p = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			result = (JSONObject)p.parse(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}

/*
ABC SentenceAgentConsumerName1571863541489
Consumer started
A {"tok_info":[{"parent":4,"pos":"DET","start":0,"lemma":"the","text":"The","tag":"DT","sent":0,"dep":"det"},{"parent":122,"pos":"NOUN","tree_e_idx":100,"start":4,"tree_s_idx":0,"text":"pandemic","tag":"NN","sent":0,"dep":"nsubjpass"},{"parent":4,"pos":"ADP","tree_e_idx":16,"start":13,"tree_s_idx":13,"text":"of","tag":"IN","sent":0,"dep":"prep"},{"parent":13,"pos":"NOUN","start":16,"text":"obesity","tag":"NN","sent":0,"dep":"pobj"},{"parent":4,"pos":"PUNCT","start":23,"text":",","tag":",","sent":0,"dep":"punct"},{"parent":41,"pos":"NOUN","start":25,"text":"type","tag":"NN","sent":0,"dep":"compound"},{"parent":41,"pos":"NUM","start":30,"text":"2","tag":"CD","sent":0,"dep":"nummod"},{"parent":41,"pos":"NOUN","start":32,"text":"diabetes","tag":"NN","sent":0,"dep":"compound"},{"parent":4,"pos":"NOUN","tree_e_idx":100,"start":41,"tree_s_idx":25,"text":"mellitus","tag":"NN","sent":0,"conjuncts":[86],"dep":"appos"},{"parent":51,"pos":"PUNCT","start":50,"text":"(","tag":"-LRB-","sent":0,"dep":"punct"},{"parent":41,"pos":"NOUN","tree_e_idx":55,"start":51,"lemma":"t2dm","tree_s_idx":50,"text":"T2DM","tag":"NN","sent":0,"dep":"appos"},{"parent":51,"pos":"PUNCT","start":55,"text":")","tag":"-RRB-","sent":0,"dep":"punct"},{"parent":41,"pos":"CCONJ","start":57,"text":"and","tag":"CC","sent":0,"dep":"cc"},{"parent":86,"pos":"ADJ","start":61,"text":"nonalcoholic","tag":"JJ","sent":0,"dep":"amod"},{"parent":86,"pos":"ADJ","start":74,"text":"fatty","tag":"JJ","sent":0,"dep":"amod"},{"parent":86,"pos":"NOUN","start":80,"text":"liver","tag":"NN","sent":0,"dep":"compound"},{"parent":41,"pos":"NOUN","tree_e_idx":100,"start":86,"tree_s_idx":61,"text":"disease","tag":"NN","sent":0,"conjuncts":[41],"dep":"conj"},{"parent":95,"pos":"PUNCT","start":94,"text":"(","tag":"-LRB-","sent":0,"dep":"punct"},{"parent":86,"pos":"NOUN","tree_e_idx":100,"start":95,"lemma":"nafld","tree_s_idx":94,"text":"NAFLD","tag":"NN","sent":0,"dep":"appos"},{"parent":95,"pos":"PUNCT","start":100,"text":")","tag":"-RRB-","sent":0,"dep":"punct"},{"parent":122,"pos":"VERB","start":102,"lemma":"have","text":"has","tag":"VBZ","sent":0,"dep":"aux"},{"parent":122,"pos":"ADV","start":106,"text":"frequently","tag":"RB","sent":0,"dep":"advmod"},{"parent":122,"pos":"VERB","start":117,"lemma":"be","text":"been","tag":"VBN","sent":0,"dep":"auxpass"},{"pos":"VERB","tree_e_idx":222,"start":122,"lemma":"associate","tree_s_idx":0,"text":"associated","tag":"VBN","sent":0,"conjuncts":[210],"dep":"ROOT"},{"parent":146,"pos":"ADP","start":133,"text":"with","tag":"IN","sent":0,"dep":"case"},{"parent":146,"pos":"ADJ","start":138,"text":"dietary","tag":"JJ","sent":0,"dep":"amod"},{"parent":122,"pos":"NOUN","tree_e_idx":166,"start":146,"tree_s_idx":133,"text":"intake","tag":"NN","sent":0,"dep":"nmod"},{"parent":166,"pos":"ADP","start":153,"text":"of","tag":"IN","sent":0,"dep":"case"},{"parent":166,"pos":"ADJ","start":156,"text":"saturated","tag":"JJ","sent":0,"dep":"amod"},{"parent":146,"pos":"NOUN","tree_e_idx":166,"start":166,"lemma":"fat","tree_s_idx":153,"text":"fats","tag":"NNS","sent":0,"dep":"nmod"},{"parent":172,"pos":"PUNCT","start":171,"text":"(","tag":"-LRB-","sent":0,"dep":"punct"},{"parent":122,"pos":"NUM","tree_e_idx":173,"start":172,"tree_s_idx":171,"text":"1","tag":"CD","sent":0,"dep":"dep"},{"parent":172,"pos":"PUNCT","start":173,"text":")","tag":"-RRB-","sent":0,"dep":"punct"},{"parent":122,"pos":"CCONJ","start":175,"text":"and","tag":"CC","sent":0,"dep":"cc"},{"parent":210,"pos":"ADV","start":179,"text":"specifically","tag":"RB","sent":0,"dep":"advmod"},{"parent":210,"pos":"ADP","start":192,"text":"with","tag":"IN","sent":0,"dep":"case"},{"parent":210,"pos":"ADJ","start":197,"text":"dietary","tag":"JJ","sent":0,"dep":"amod"},{"parent":210,"pos":"NOUN","start":205,"text":"palm","tag":"NN","sent":0,"dep":"compound"},{"parent":122,"pos":"NOUN","tree_e_idx":221,"start":210,"tree_s_idx":179,"text":"oil","tag":"NN","sent":0,"conjuncts":[122],"dep":"conj"},{"parent":215,"pos":"PUNCT","start":214,"text":"(","tag":"-LRB-","sent":0,"dep":"punct"},{"parent":210,"pos":"NOUN","tree_e_idx":217,"start":215,"lemma":"po","tree_s_idx":214,"text":"PO","tag":"NN","sent":0,"dep":"appos"},{"parent":215,"pos":"PUNCT","start":217,"text":")","tag":"-RRB-","sent":0,"dep":"punct"},{"parent":220,"pos":"PUNCT","start":219,"text":"(","tag":"-LRB-","sent":0,"dep":"punct"},{"parent":210,"pos":"NUM","tree_e_idx":221,"start":220,"tree_s_idx":219,"text":"2","tag":"CD","sent":0,"dep":"appos"},{"parent":220,"pos":"PUNCT","start":221,"text":")","tag":"-RRB-","sent":0,"dep":"punct"},{"parent":122,"pos":"PUNCT","start":222,"text":".","tag":".","sent":0,"dep":"punct"}],"entities":[{"start":4,"end":12,"text":"pandemic","label":"ENTITY","sent":0},{"start":16,"end":23,"text":"obesity","label":"ENTITY","sent":0},{"start":25,"end":49,"text":"type 2 diabetes mellitus","label":"ENTITY","sent":0},{"start":51,"end":55,"text":"T2DM","label":"ENTITY","sent":0},{"start":61,"end":93,"text":"nonalcoholic fatty liver disease","label":"ENTITY","sent":0},{"start":95,"end":100,"text":"NAFLD","label":"ENTITY","sent":0},{"start":122,"end":137,"text":"associated with","label":"ENTITY","sent":0},{"start":138,"end":152,"text":"dietary intake","label":"ENTITY","sent":0},{"start":156,"end":170,"text":"saturated fats","label":"ENTITY","sent":0},{"start":197,"end":213,"text":"dietary palm oil","label":"ENTITY","sent":0},{"start":215,"end":217,"text":"PO","label":"ENTITY","sent":0}],"analyzer":{"name":"core_sci_lg","lang":"en","version":"0.2.3"},"sentences":[{"start":0,"end":223,"text":"The pandemic of obesity, type 2 diabetes mellitus (T2DM) and nonalcoholic fatty liver disease (NAFLD) has frequently been associated with dietary intake of saturated fats (1) and specifically with dietary palm oil (PO) (2)."}],"Model":"en_core_sci_lg","time":"2019-10-12T10:22:12.505492","doc_id":0,"para_id":0,"noun_chunks":[{"start":0,"end":12,"text":"The pandemic","label":"NP","sent":0},{"start":16,"end":23,"text":"obesity","label":"NP","sent":0},{"start":25,"end":49,"text":"type 2 diabetes mellitus","label":"NP","sent":0},{"start":50,"end":55,"text":"(T2DM","label":"NP","sent":0},{"start":61,"end":93,"text":"nonalcoholic fatty liver disease","label":"NP","sent":0},{"start":94,"end":100,"text":"(NAFLD","label":"NP","sent":0},{"start":179,"end":213,"text":"specifically with dietary palm oil","label":"NP","sent":0},{"start":214,"end":217,"text":"(PO","label":"NP","sent":0}]}
S [{"start":0,"end":223,"text":"The pandemic of obesity, type 2 diabetes mellitus (T2DM) and nonalcoholic fatty liver disease (NAFLD) has frequently been associated with dietary intake of saturated fats (1) and specifically with dietary palm oil (PO) (2)."}]
A {"tok_info":[{"start":0,"text":"The"},{"start":4,"text":"pandemic"},{"start":13,"text":"of"},{"start":16,"text":"obesity"},{"start":23,"text":","},{"start":25,"text":"type"},{"start":30,"text":"2"},{"start":32,"text":"diabetes"},{"start":41,"text":"mellitus"},{"start":50,"text":"("},{"start":51,"text":"T2DM"},{"start":55,"text":")"},{"start":57,"text":"and"},{"start":61,"text":"nonalcoholic"},{"start":74,"text":"fatty"},{"start":80,"text":"liver"},{"start":86,"text":"disease"},{"start":94,"text":"("},{"start":95,"text":"NAFLD"},{"start":100,"text":")"},{"start":102,"text":"has"},{"start":106,"text":"frequently"},{"start":117,"text":"been"},{"start":122,"text":"associated"},{"start":133,"text":"with"},{"start":138,"text":"dietary"},{"start":146,"text":"intake"},{"start":153,"text":"of"},{"start":156,"text":"saturated"},{"start":166,"text":"fats"},{"start":171,"text":"("},{"start":172,"text":"1"},{"start":173,"text":")"},{"start":175,"text":"and"},{"start":179,"text":"specifically"},{"start":192,"text":"with"},{"start":197,"text":"dietary"},{"start":205,"text":"palm"},{"start":210,"text":"oil"},{"start":214,"text":"("},{"start":215,"text":"PO"},{"start":217,"text":")"},{"start":219,"text":"("},{"start":220,"text":"2"},{"start":221,"text":")"},{"start":222,"text":"."}],"entities":[],"analyzer":{"name":"ner_jnlpba_md","lang":"en","version":"0.2.3"},"Model":"en_ner_jnlpba_md","time":"2019-10-12T10:40:33.952793","doc_id":0,"para_id":0}
A {"tok_info":[{"start":0,"text":"The"},{"start":4,"text":"pandemic"},{"start":13,"text":"of"},{"start":16,"text":"obesity"},{"start":23,"text":","},{"start":25,"text":"type"},{"start":30,"text":"2"},{"start":32,"text":"diabetes"},{"start":41,"text":"mellitus"},{"start":50,"text":"("},{"start":51,"text":"T2DM"},{"start":55,"text":")"},{"start":57,"text":"and"},{"start":61,"text":"nonalcoholic"},{"start":74,"text":"fatty"},{"start":80,"text":"liver"},{"start":86,"text":"disease"},{"start":94,"text":"("},{"start":95,"text":"NAFLD"},{"start":100,"text":")"},{"start":102,"text":"has"},{"start":106,"text":"frequently"},{"start":117,"text":"been"},{"start":122,"text":"associated"},{"start":133,"text":"with"},{"start":138,"text":"dietary"},{"start":146,"text":"intake"},{"start":153,"text":"of"},{"start":156,"text":"saturated"},{"start":166,"text":"fats"},{"start":171,"text":"("},{"start":172,"text":"1"},{"start":173,"text":")"},{"start":175,"text":"and"},{"start":179,"text":"specifically"},{"start":192,"text":"with"},{"start":197,"text":"dietary"},{"start":205,"text":"palm"},{"start":210,"text":"oil"},{"start":214,"text":"("},{"start":215,"text":"PO"},{"start":217,"text":")"},{"start":219,"text":"("},{"start":220,"text":"2"},{"start":221,"text":")"},{"start":222,"text":"."}],"entities":[{"start":16,"end":23,"text":"obesity","label":"DISEASE"},{"start":32,"end":49,"text":"diabetes mellitus","label":"DISEASE"},{"start":51,"end":55,"text":"T2DM","label":"DISEASE"},{"start":74,"end":93,"text":"fatty liver disease","label":"DISEASE"},{"start":95,"end":100,"text":"NAFLD","label":"DISEASE"}],"analyzer":{"name":"ner_bc5cdr_md","lang":"en","version":"0.2.3"},"Model":"en_ner_bc5cdr_md","time":"2019-10-12T10:46:10.765532","doc_id":0,"para_id":0}
A {"tok_info":[{"start":0,"text":"The"},{"start":4,"text":"pandemic"},{"start":13,"text":"of"},{"start":16,"text":"obesity"},{"start":23,"text":","},{"start":25,"text":"type"},{"start":30,"text":"2"},{"start":32,"text":"diabetes"},{"start":41,"text":"mellitus"},{"start":50,"text":"("},{"start":51,"text":"T2DM"},{"start":55,"text":")"},{"start":57,"text":"and"},{"start":61,"text":"nonalcoholic"},{"start":74,"text":"fatty"},{"start":80,"text":"liver"},{"start":86,"text":"disease"},{"start":94,"text":"("},{"start":95,"text":"NAFLD"},{"start":100,"text":")"},{"start":102,"text":"has"},{"start":106,"text":"frequently"},{"start":117,"text":"been"},{"start":122,"text":"associated"},{"start":133,"text":"with"},{"start":138,"text":"dietary"},{"start":146,"text":"intake"},{"start":153,"text":"of"},{"start":156,"text":"saturated"},{"start":166,"text":"fats"},{"start":171,"text":"("},{"start":172,"text":"1"},{"start":173,"text":")"},{"start":175,"text":"and"},{"start":179,"text":"specifically"},{"start":192,"text":"with"},{"start":197,"text":"dietary"},{"start":205,"text":"palm"},{"start":210,"text":"oil"},{"start":214,"text":"("},{"start":215,"text":"PO"},{"start":217,"text":")"},{"start":219,"text":"("},{"start":220,"text":"2"},{"start":221,"text":")"},{"start":222,"text":"."}],"entities":[{"start":80,"end":85,"text":"liver","label":"ORGAN"},{"start":205,"end":213,"text":"palm oil","label":"ORGANISM_SUBSTANCE"},{"start":215,"end":217,"text":"PO","label":"SIMPLE_CHEMICAL"}],"analyzer":{"name":"ner_bionlp13cg_md","lang":"en","version":"0.2.3"},"Model":"en_ner_bionlp13cg_md","time":"2019-10-12T10:48:46.779400","doc_id":0,"para_id":0}
A {"tok_info":[{"start":0,"text":"The"},{"start":4,"text":"pandemic"},{"start":13,"text":"of"},{"start":16,"text":"obesity"},{"start":23,"text":","},{"start":25,"text":"type"},{"start":30,"text":"2"},{"start":32,"text":"diabetes"},{"start":41,"text":"mellitus"},{"start":50,"text":"("},{"start":51,"text":"T2DM"},{"start":55,"text":")"},{"start":57,"text":"and"},{"start":61,"text":"nonalcoholic"},{"start":74,"text":"fatty"},{"start":80,"text":"liver"},{"start":86,"text":"disease"},{"start":94,"text":"("},{"start":95,"text":"NAFLD"},{"start":100,"text":")"},{"start":102,"text":"has"},{"start":106,"text":"frequently"},{"start":117,"text":"been"},{"start":122,"text":"associated"},{"start":133,"text":"with"},{"start":138,"text":"dietary"},{"start":146,"text":"intake"},{"start":153,"text":"of"},{"start":156,"text":"saturated"},{"start":166,"text":"fats"},{"start":171,"text":"("},{"start":172,"text":"1"},{"start":173,"text":")"},{"start":175,"text":"and"},{"start":179,"text":"specifically"},{"start":192,"text":"with"},{"start":197,"text":"dietary"},{"start":205,"text":"palm"},{"start":210,"text":"oil"},{"start":214,"text":"("},{"start":215,"text":"PO"},{"start":217,"text":")"},{"start":219,"text":"("},{"start":220,"text":"2"},{"start":221,"text":")"},{"start":222,"text":"."}],"entities":[{"start":51,"end":55,"text":"T2DM","label":"GGP"},{"start":74,"end":79,"text":"fatty","label":"CHEBI"},{"start":166,"end":170,"text":"fats","label":"CHEBI"}],"analyzer":{"name":"ner_craft_md","lang":"en","version":"0.2.3"},"Model":"en_ner_craft_md","time":"2019-10-12T11:12:00.603648","doc_id":0,"para_id":0}
AsrCoreEnvironment shutting down
Environment shutting down
WordGramThread shutting down
AsrCoreEnvironment shutting down
Environment shutting down
WordGramThread shutting down
Environment shutting down
WordGramThread shutting down
 */
