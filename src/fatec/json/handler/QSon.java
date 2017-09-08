package fatec.json.handler;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fatec.json.model.QSonElement;

public class QSon {

	public JsonElement stringToJson(String str) {
		return new JsonParser().parse(str);
	}

	public JsonElement fileToJson(FileReader path) throws IOException {
		return new JsonParser().parse(path);
	}

	public Map<String, Map<String, String>> jsonToMap(JsonElement element) {
		Map<String, Map<String, String>> jsonMap = new TreeMap<String, Map<String, String>>();

		JsonObject obj = (JsonObject) element;

		obj.entrySet().forEach(entry -> {
			JsonObject objects = (JsonObject) elementToJson(entry.getValue());
			Map<String, String> childs = getChildNodes(objects.entrySet());

			jsonMap.put(entry.getKey(), childs);
		});

		return jsonMap;
	}

	public JsonElement merge(QSonElement... elements) {
		JsonElement[] verifiedJsons = prepareElements(elements);
		JsonElement[] jsons = elementsToJsonArray(verifiedJsons);
		
		JsonObject result = (JsonObject) jsons[0];

		int length = jsons.length;

		for (int index = 1; length > 1; index++, length--) {
			JsonObject obj = (JsonObject) jsons[index];

			result = (JsonObject) recursivelyMerge(result, obj);
		}

		return result;
	}

	public JsonElement jsonToAttribute(String key, JsonElement element) {
		JsonElement json = element;

		if (json.isJsonNull()) {
			return element;
		}

		String str = json.toString();
		String attr = String.format("{ \"%s\": ", key);

		attr = String.format("{ \"%s\": ", key);
		attr = attr.concat(str).concat("}");

		return stringToJson(attr);
	}

	private JsonElement elementToJson(JsonElement json) {
		return json;
	}

	private JsonElement[] elementsToJsonArray(JsonElement... str) {
		JsonElement[] elements = new JsonElement[str.length];

		for (int i = 0; i < elements.length; i++) {
			elements[i] = str[i];
		}

		return elements;
	}

	private Map<String, String> getChildNodes(Set<Entry<String, JsonElement>> entries) {
		Map<String, String> jsonChildsMap = new TreeMap<String, String>();

		entries.forEach(entry -> {
			jsonChildsMap.put(entry.getKey(), entry.getValue().getAsString());
		});

		return jsonChildsMap;
	}

	private JsonElement[] prepareElements(QSonElement[] elements) {
		JsonElement[] result = new JsonElement[elements.length];

		for (int i = 0; i < elements.length; i++) {
			JsonElement json = elements[i].getJson();

			if (json.isJsonArray()) {
				elements[i].setJson(jsonToAttribute(elements[i].getKey(), json));
			}

			result[i] = elements[i].getJson();
		}

		return result;
	}

	private JsonElement recursivelyMerge(JsonObject target, JsonObject source) {
		source.entrySet().forEach(entry -> {
			if (!target.has(entry.getKey())) {
				target.add(entry.getKey(), entry.getValue());
			}
		});

		return target;
	}
	
}
