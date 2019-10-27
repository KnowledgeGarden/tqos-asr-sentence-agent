/**
 * 
 */
package org.topicquests.os.asr.reader.sentences;

import java.util.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.topicquests.asr.general.document.api.IDocumentClient;
import org.topicquests.asr.sentence.api.ISentenceClient;
import org.topicquests.hyperbrane.api.ISentence;
import org.topicquests.hyperbrane.api.IWordGram;
import org.topicquests.ks.kafka.KafkaConsumer;
import org.topicquests.ks.kafka.KafkaProducer;
import org.topicquests.os.asr.api.IASRCoreModel;
import org.topicquests.os.asr.dbpedia.SpotlightClient;
import org.topicquests.os.asr.dbpedia.SpotlightUtil;
import org.topicquests.os.asr.reader.sentences.api.ISentenceAgent;
import org.topicquests.os.asr.wordgram.api.IWordGramAgentModel;
import org.topicquests.support.ResultPojo;
import org.topicquests.support.api.IResult;

import net.minidev.json.JSONObject;

/**
 * @author jackpark
 *
 */
public class SentenceAgent implements ISentenceAgent {
	private SentencesEnvironment environment;
	private SpotlightClient scl;
	private IDocumentClient documentDatabase;
	private ISentenceClient sentenceDatabase;
	private IWordGramAgentModel gramolizer;
	private IASRCoreModel asrModel;


	private KafkaConsumer kConsumer;
	private KafkaProducer kProducer;
	private final String
		MODEL_1		= "en_core_sci_lg", // sentences, entities, noun_chunks, POS
		MODEL_2		= "en_ner_jnlpba_md",
		MODEL_3		= "en_ner_bc5cdr_md",
		MODEL_4		= "en_ner_bionlp13cg_md",
		MODEL_5		= "en_ner_craft_md",
		USER_ID		= "SystemUser";

	/**
	 * @param env
	 */
	public SentenceAgent(SentencesEnvironment env) {
		environment = env;
		scl = new SpotlightClient(environment);
		documentDatabase = environment.getDocumentDatabase();
		sentenceDatabase = environment.getSentenceDatabase();
		asrModel = environment.getCoreModel();
		gramolizer = environment.getWordgramAgentModel();
//TODO		kConsumer = new KafkaConsumer(env, ISentenceAgent.KAFKA_CONSUMER_NAME+Long.toString(System.currentTimeMillis()), this);
//TODO		kProducer = new KafkaProducer(env, ISentenceAgent.KAFKA_PRODUCER_NAME+Long.toString(System.currentTimeMillis()));
	}

	/* (non-Javadoc)
	 * @see org.topicquests.backside.kafka.consumer.api.IMessageConsumerListener#acceptRecord(org.apache.kafka.clients.consumer.ConsumerRecord)
	 */
	@Override
	public boolean acceptRecord(ConsumerRecord record) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.topicquests.os.asr.reader.sentences.api.ISentenceAgent#shutDown()
	 */
	@Override
	public void shutDown() {
		kProducer.close();
	}

	@Override
	public IResult acceptSpacyJSON(JSONObject spacy) {
		IResult result = new ResultPojo();
		IResult r;
		System.out.println("A "+spacy);
		if (MODEL_1.contentEquals(spacy.getAsString("Model")))
			r = processEnCoreSciLgModel(spacy);
		// TODO Auto-generated method stub
		return result;
	}
	
	/**
	 * Creates <em>sentence objects</em> which include:
	 * <ol><li>docId</li>
	 * <li>paraId</li>
	 * <li>text - the sentence</li>
	 * <li>entities list</li>
	 * <li>nounchunks list</li>
	 * <li>tokens list </list></ol>
	 * @param spacy
	 * @return
	 */
	
	IResult processEnCoreSciLgModel(JSONObject spacy) {
		IResult result = new ResultPojo();
		IResult r;
		List<JSONObject> sentences = (List<JSONObject>)spacy.get("sentences");
		System.out.println("S "+sentences);
		List<JSONObject> tokens = (List<JSONObject>)spacy.get("tok_info");
		List<JSONObject> entities = (List<JSONObject>)spacy.get("entities");
		List<JSONObject> nounChunks = (List<JSONObject>)spacy.get("noun_chunks");
		JSONObject jo;
		JSONObject sx;
		JSONObject tx;
		String documentId = spacy.getAsString("doc_id");
		String paragraphId = spacy.getAsString("para_id");
		List<JSONObject>toks;
		int sstart, send, tstart;
		Iterator<JSONObject> sitr = sentences.iterator();
		while (sitr.hasNext()) {
			jo = new JSONObject();
			jo.put("docId", documentId);
			jo.put("paraId", paragraphId);
			toks = new ArrayList<JSONObject>();
			jo.put("tokens", toks);
			sx = sitr.next();
			jo.put("text", sx.getAsString("text"));
			sstart = sx.getAsNumber("start").intValue();
			send = sx.getAsNumber("end").intValue();
			Iterator<JSONObject>titr = tokens.iterator();
			while (titr.hasNext()) {
				tx = titr.next();
				tstart = tx.getAsNumber("start").intValue();
				if  (tstart >= sstart && tstart < send)
					toks.add(tx);
			}
			if (entities != null  && !entities.isEmpty()) {
				titr = entities.iterator();
				toks = new ArrayList<JSONObject>();
				jo.put("entities", toks);
				while (titr.hasNext()) {
					tx = titr.next();
					tstart = tx.getAsNumber("start").intValue();
					if  (tstart >= sstart && tstart < send)
						toks.add(tx);
				}
			}
			if (nounChunks != null  && !nounChunks.isEmpty()) {
				titr = nounChunks.iterator();
				toks = new ArrayList<JSONObject>();
				jo.put("nounchunks", toks);
				while (titr.hasNext()) {
					tx = titr.next();
					tstart = tx.getAsNumber("start").intValue();
					if  (tstart >= sstart && tstart < send)
						toks.add(tx);
				}
			}
			r = processSentence(jo);
			if (r.hasError())
				result.addErrorString(r.getErrorString());
		}
		
		
		
		return result;
	}
/**
 * Example DBpedia hit
 * The URI resolves to http://dbpedia.org/page/Amyloid_beta
 * which is going to be an important page to fetch and harvest
 *   perhaps through a DBpedia API https://wiki.dbpedia.org/rest-api
{"@percentageOfSecondRank":"0.0",
"@URI":"http:\/\/dbpedia.org\/resource\/Amyloid_beta",
"@support":"430",
"@surfaceForm":"amyloid-β",
"@offset":"64","@similarityScore":"1.0",
"@types":"Wikidata:Q8054,Wikidata:Q206229,DBpedia:Protein,DBpedia:Biomolecule"} 
*/
	
	/**
	 * <p>This is the primary workhorse based on the large science scispacy model</p>
	 * <p> It gets a sentence text, tokens, entities and nounchunks.</p>
	 * <p> Its job is to fabricate an ISentence</p>
	 * @param sentenceObject
	 */
	IResult processSentence(JSONObject sentenceObject) {
		IResult result = new ResultPojo();
		environment.logDebug("JS\n"+sentenceObject);
		// Get DBpedia hits for this sentence
		String theSentence = sentenceObject.getAsString("text");
		List<JSONObject> dbpds = processDBpedia(theSentence);
		environment.logDebug("JSDBP\n"+dbpds);
		// DBpedia hits can include types, etc.
		ISentence s = startISentence(sentenceObject, result);
		//TODO entities are most likely to have dbpedia entries
		processEntities(s, sentenceObject, result);
		processTokens(s, sentenceObject, result);
		updateISentence(s, sentenceObject, result);
		//TODO -- dbpedia 
		return result;
	}
	
	//////////////////////////////
	// When you have a sentenceobject and any DBpedia stuff, steps are:
	// 1: Create an ISentence in order to get a sentenceId
	// 2: Process the Entities and nounChunks to make major Wordgrams
	// 3: Process tokens to fillin the rest of the sentence WordGrams
	// 4: Update ISentence and IDocument as necessary
	///////////////////////////////
	// TODO
	// What is missing in this code is fetching the IDocument for updating
	// Need to create devTests which will include crafting an IDocument
	///////////////////////////////
	
	ISentence startISentence(JSONObject sentenceObject, IResult r) {
		
		ISentence theSentence = asrModel.newSentence(sentenceObject.getAsString("docId"),
					sentenceObject.getAsString("text"), USER_ID);
		return theSentence;
	}
	
	/**
	 * Process all the entities for this sentence, including nounChunks
	 * @param sentenceObject
	 * @param r
	 */
	void processEntities(ISentence s, JSONObject sentenceObject, IResult r) {
		String sentenceId = s.getID();
		List<JSONObject> entities = (List<JSONObject>)sentenceObject.get("entities");
		List<JSONObject> nounChunks = (List<JSONObject>)sentenceObject.get("nounchunks");
		environment.logDebug("SentenceAgent.processEntities-\n"+entities+"\n"+nounChunks);

		JSONObject jo;
		String term;
		List<String> gids;
		Iterator<String> sitr;
		Iterator<JSONObject> itr;
		IResult res;
		if (entities != null &&  !entities.isEmpty()) {
			itr = entities.iterator();
			while (itr.hasNext()) {
				jo = itr.next();
				environment.logDebug("SentenceAgent.processEntities "+jo);
				term = jo.getAsString("text");
				//returns a List<String> of wordGram IDs
				res = gramolizer.processString(term, USER_ID, sentenceId);
				gids = (List<String>)res.getResultObject();
				environment.logDebug("SentenceAgent.processEntities-1 "+gids);
				/////////////////////////////
				//TODO
				// CHANGE THIS to use a PostgresConnection to speed it up
				// That would mean IWordGram needs an API addition to allow that
				/////////////////////////////
				if (gids != null && !gids.isEmpty()) {
					sitr = gids.iterator();
					while (sitr.hasNext())
						s.addWordGramId(sitr.next());
				} //TODO else terrible error condition
			}
		}
		/////////////////////////////////////
		//TODO
		// THIS IS WHERE we look at nounChunks.
		// They can include stop words
		// But, our plan is to ripple through them and compare them to
		// Entities; those for which entities are a component or whole become noun/noun phrases
		// But, if the entity is already a noun, no change is made.
		// This entails a lot of fetching of WordGrams; fortunately, they exist in a large cache
		// to reduce database round trips
		// EXAMPLE nounchunk
		//{
	    //  "start": 1272,
	    //  "end": 1302,
	    //  "text": "other anti-inflammatory agents",
	    //  "label": "NP",
	    //  "sent": 1166
	    //}
		// EXAMPLE entity
		//{
	    //  "start": 1278,
	    //  "end": 1302,
	    //  "text": "anti-inflammatory agents",
	    //  "label": "ENTITY",
	    //  "sent": 1166
	    //}
		// NOTE:
		//	The same entity text may appear in > 1 nounchunk
		//	so we must detect loops
		//
		/////////////////////////////////////
		if (nounChunks != null &&  !nounChunks.isEmpty() &&
			entities != null && !entities.isEmpty()) {
			List<String> detected = new ArrayList<String>();
			IWordGram g;
			String ncterm;
			Iterator<JSONObject> ncitr = nounChunks.iterator();
			JSONObject nc;
			while (ncitr.hasNext()) {
				nc = ncitr.next();
				ncterm = nc.getAsString("text");
				itr = entities.iterator();
				while (itr.hasNext()) {
					term = itr.next().getAsString("text");
					environment.logDebug("SentenceAgent.processEntities-2 "+ncterm+" | "+term);
					if (!detected.contains(term)) {
						if (ncterm.contains(term)) {
							detected.add(term);
							g = gramolizer.getThisWordGramByWords(term);
							if (!g.isNoun()) {
								g.addIsNounType();
								environment.logDebug("SentenceAgent.processEntities-3 "+g.getID());
							}
						}
					}
				}
				
			}
		}
		environment.logDebug("SentenceAgent.processEntities+");

	}
	
	/**
	 * See Appendix Javadoc below
	 * @param s
	 * @param sentenceObject
	 * @param r
	 */
	void processTokens(ISentence s, JSONObject sentenceObject, IResult r) {
		environment.logDebug("SentenceAgent.processTokens-");
		String sentenceId = sentenceObject.getAsString("id");
		List<JSONObject> tokens = (List<JSONObject>)sentenceObject.get("tokens");
		//List<JSONObject> entities = (List<JSONObject>)sentenceObject.get("entities");
		List<JSONObject> nounChunks = (List<JSONObject>)sentenceObject.get("nounchunks");
		JSONObject triple = new JSONObject();
		if (tokens != null  && !tokens.isEmpty()) {
			if (nounChunks != null && !nounChunks.isEmpty()) {
				doTokensNounChunks(triple, sentenceObject, tokens, nounChunks);
			}
			//TODO Otherwise
		}
		//TODO when triples are formed, the predicates must be turned into wordgrams
		environment.logDebug("SentenceAgent.processTokens\n"+sentenceObject+"\n"+triple);
	}
	
	/**
	 * Look for subject, predicate, and object in sentence - a simple NPN check
	 * @param triple
	 * @param sentenceObject
	 * @param tokens
	 * @param nounChunks
	 */
	void doTokensNounChunks(JSONObject triple, JSONObject sentenceObject, List<JSONObject> tokens, List<JSONObject> nounChunks) {
		environment.logDebug("SentenceAgent.doTokensNounChunks-\n"+sentenceObject+"\n"+triple);
		int where = findSubject(triple, sentenceObject, tokens, nounChunks);
		environment.logDebug("SentenceAgent.doTokensNounChunks\n"+sentenceObject+"\n"+triple);
		where = findPredicate(where, triple, sentenceObject, tokens);
		environment.logDebug("SentenceAgent.doTokensNounChunks-1\n"+sentenceObject+"\n"+triple);
		where = findObject(where, triple, sentenceObject, tokens, nounChunks);
	}
	
	int findSubject(JSONObject triple, JSONObject sentenceObject, List<JSONObject> tokens, List<JSONObject> nounChunks) {
		int toklen = tokens.size();
		int nclen = nounChunks.size();
		int tStart, nStart;
		JSONObject t, n;
		int tokenCursor, nounCursor, where=0;
		boolean found = false;
		for (tokenCursor=0; tokenCursor< toklen; tokenCursor++) {
			t = tokens.get(tokenCursor);
			tStart = t.getAsNumber("start").intValue();
			for (nounCursor = 0; nounCursor< nclen; nounCursor++) {
				n = nounChunks.get(nounCursor);
				nStart = n.getAsNumber("start").intValue();
				if (tStart == nStart) {
					//we have a hit
					triple.put("subject", n);
					where = tokenCursor;
					found = true;
					break;
				}
				if (found)
					break;
			}
		}
		return where;
	}
	
	int findPredicate(int tokenOffset, JSONObject triple, JSONObject sentenceObject, List<JSONObject> tokens) {
		int toklen = tokens.size();
		environment.logDebug("SentenceAgent.findPredicate- "+tokenOffset+" "+toklen);
		int tStart;
		JSONObject t;
		int tokenCursor;
		int wherePredicate = 0;
		String pos;
		boolean found = false;
		for (tokenCursor=tokenOffset; tokenCursor< toklen; tokenCursor++) {
			t = tokens.get(tokenCursor);
			environment.logDebug("SentenceAgent.findPredicate "+t);
			tStart = t.getAsNumber("start").intValue();
			pos = t.getAsString("pos");
			//watch for predicates
			if ("VERB".equalsIgnoreCase(pos)) {
				addPredToTriple(triple, t);
				wherePredicate = tokenCursor;
				found = true;
			} else if ("ADV".equalsIgnoreCase(pos)) {
				if (tStart < t.getAsNumber("parent").intValue())
					addPredToTriple(triple, t);
			} else if (found && "NOUN".equalsIgnoreCase(pos)) {
				break;
			}
			
		}
		return wherePredicate;	
	}
	int findObject(int tokenOffset, JSONObject triple, JSONObject sentenceObject, List<JSONObject> tokens, List<JSONObject> nounChunks) {
		int toklen = tokens.size();
		environment.logDebug("SentenceAgent.findObject- "+tokenOffset+" "+toklen);
		int nclen = nounChunks.size();
		int tStart, nStart;
		JSONObject t, n;
		int tokenCursor, nounCursor;
		for (tokenCursor=tokenOffset; tokenCursor< toklen; tokenCursor++) {
			t = tokens.get(tokenCursor);
			tStart = t.getAsNumber("start").intValue();
			for (nounCursor = 0; nounCursor< nclen; nounCursor++) {
				n = nounChunks.get(nounCursor);
				nStart = n.getAsNumber("start").intValue();
				if (tStart == nStart) {
					//we have a hit
					triple.put("object", n);
					break;
				}
			}
		}
		return tokenCursor;	
	}
	
	void addPredToTriple(JSONObject triple, JSONObject pred) {
		List<JSONObject>preds = (List<JSONObject>)triple.get("predicate");
		if (preds == null) preds = new ArrayList<JSONObject>();
		preds.add(pred);
		triple.put("predicate", preds);
	}
	
	void updateISentence(ISentence s, JSONObject sentenceObject, IResult r) {
		//TODO this is where you add stuff to the ISentence
		//store it
		IResult rx = asrModel.putSentence(s);
		if (rx.hasError())
			r.addErrorString(rx.getErrorString());
	}
	
	/**
	 * DBpedia
	 * @param sentence
	 * @return
	 */
	List<JSONObject> processDBpedia(String sentence) {
		IResult r =  scl.annotateSentence(sentence);
		JSONObject jo =(JSONObject)r.getResultObject();
		if (jo != null) {
			SpotlightUtil scu = new SpotlightUtil(jo);
			return scu.listResources();
		}
		return null;
	}

}

/** APPENDIX
 Two basic discoveries spurred research into inflammation as a driving force in the pathogenesis of Alzheimer's disease (AD).
 NOTES A: Simple SubjectPhrase, Predicate, Object
 SubjectPhrase
 {
      "start": 0,
      "text": "Two",
      "parent": 10,
      "pos": "NUM",
      "tag": "CD",
      "dep": "nummod",
      "lemma": "two",
      "sent": 0
    },
    {
      "start": 4,
      "text": "basic",
      "parent": 10,
      "pos": "ADJ",
      "tag": "JJ",
      "dep": "amod",
      "sent": 0
    },
    {
      "start": 10,
      "text": "discoveries",
      "parent": 22,
      "tree_s_idx": 0,
      "tree_e_idx": 10,
      "pos": "NOUN",
      "tag": "NNS",
      "dep": "nsubj",
      "lemma": "discovery",
      "sent": 0
    },
    Predicate
    {
      "start": 22,
      "text": "spurred",  <-- a kind of cause
      "tree_s_idx": 0,
      "tree_e_idx": 123,
      "pos": "VERB",
      "tag": "VBD",
      "dep": "ROOT",
      "lemma": "spur",
      "sent": 0
    },
	Object
	{
      "start": 30,
      "text": "research",
      "parent": 22,
      "pos": "NOUN",
      "tag": "NN",
      "dep": "dobj",
      "sent": 0
    }
WE HAVE THESE NOUN Chunks
	{
      "start": 0,
      "end": 21,
      "text": "Two basic discoveries",
      "label": "NP",
      "sent": 0
    },
    {
      "start": 30,
      "end": 38,
      "text": "research",
      "label": "NP",
      "sent": 0
    },
THOSE take care of the core subject and object for the given predicate.
We could make a JSONObject triple:
	1: walk the Tokens left to right
	2: For each Token, is there a NounChunk at this position?
	3: If so: insert that nounchunk into triple as subject
	4: Move cursor to nounchunk end
	5: Walk Tokens to Predicate or precate phrase
	6: insert that predicate into triple as predicate
	7: Move cursor to end of predicate
	8: Walk tokens left to right
	9: For each Token, is there a NounChunk at this position?
	10:If so: insert that nounchunk into triple as object
	NOTE: if sentence is simple NPN, this would terminate
	BUT: if sentence is nested, e.g. NPNPN { triple, pred, object }
		OR NPNPN ( subject, pred, triple }, we have more work to do
	11: Move cursor to end of nounchunk
	12: Walk Tokens left to right
	13: we are looking now to look for higher-order structures

 */
/* Unusual Sentence an edge case we shall ignore for the time being
{
	"paraId": "0",
	"nounchunks": [{
		"start": 1640,
		"end": 1642,
		"text": "it",
		"label": "NP",
		"sent": 1629
	}, {
		"start": 1650,
		"end": 1657,
		"text": "the key",
		"label": "NP",
		"sent": 1629
	}],
	"entities": [{
		"start": 1629,
		"end": 1639,
		"text": "Inhibiting",
		"label": "ENTITY",
		"sent": 1629
	}, {
		"start": 1672,
		"end": 1681,
		"text": "treatment",
		"label": "ENTITY",
		"sent": 1629
	}, {
		"start": 1690,
		"end": 1697,
		"text": "chronic",
		"label": "ENTITY",
		"sent": 1629
	}, {
		"start": 1698,
		"end": 1720,
		"text": "neurological disorders",
		"label": "ENTITY",
		"sent": 1629
	}],
	"docId": "0",
	"tokens": [{
		"parent": 1647,
		"pos": "VERB",
		"start": 1629,
		"lemma": "inhibit",
		"text": "Inhibiting",
		"tag": "VBG",
		"sent": 1629,
		"dep": "csubj"
	}, {
		"parent": 1647,
		"pos": "PRON",
		"start": 1640,
		"lemma": "-PRON-",
		"text": "it",
		"tag": "PRP",
		"sent": 1629,
		"dep": "nsubj"
	}, {
		"parent": 1647,
		"pos": "AUX",
		"start": 1643,
		"text": "may",
		"tag": "MD",
		"sent": 1629,
		"dep": "aux"
	}, {
		"pos": "VERB",
		"tree_e_idx": 1720,
		"start": 1647,
		"tree_s_idx": 1629,
		"text": "be",
		"tag": "VB",
		"sent": 1629,
		"dep": "ROOT"
	}, {
		"parent": 1654,
		"pos": "DET",
		"start": 1650,
		"text": "the",
		"tag": "DT",
		"sent": 1629,
		"dep": "det"
	}, {
		"parent": 1647,
		"pos": "NOUN",
		"tree_e_idx": 1711,
		"start": 1654,
		"tree_s_idx": 1650,
		"text": "key",
		"tag": "NN",
		"sent": 1629,
		"dep": "attr"
	}, {
		"parent": 1672,
		"pos": "PART",
		"start": 1658,
		"text": "to",
		"tag": "TO",
		"sent": 1629,
		"dep": "case"
	}, {
		"parent": 1672,
		"pos": "ADJ",
		"start": 1661,
		"text": "successful",
		"tag": "JJ",
		"sent": 1629,
		"dep": "amod"
	}, {
		"parent": 1654,
		"pos": "NOUN",
		"tree_e_idx": 1711,
		"start": 1672,
		"tree_s_idx": 1658,
		"text": "treatment",
		"tag": "NN",
		"sent": 1629,
		"dep": "nmod"
	}, {
		"parent": 1711,
		"pos": "ADP",
		"start": 1682,
		"text": "of",
		"tag": "IN",
		"sent": 1629,
		"dep": "case"
	}, {
		"parent": 1711,
		"pos": "ADJ",
		"start": 1685,
		"text": "many",
		"tag": "JJ",
		"sent": 1629,
		"dep": "amod"
	}, {
		"parent": 1711,
		"pos": "ADJ",
		"start": 1690,
		"text": "chronic",
		"tag": "JJ",
		"sent": 1629,
		"dep": "amod"
	}, {
		"parent": 1711,
		"pos": "ADJ",
		"start": 1698,
		"text": "neurological",
		"tag": "JJ",
		"sent": 1629,
		"dep": "amod"
	}, {
		"parent": 1672,
		"pos": "NOUN",
		"tree_e_idx": 1711,
		"start": 1711,
		"lemma": "disorder",
		"tree_s_idx": 1682,
		"text": "disorders",
		"tag": "NNS",
		"sent": 1629,
		"dep": "nmod"
	}, {
		"parent": 1647,
		"pos": "PUNCT",
		"start": 1720,
		"text": ".",
		"tag": ".",
		"sent": 1629,
		"dep": "punct"
	}],
	"text": "Inhibiting it may be the key to successful treatment of many chronic neurological disorders."
} 
 */ 
