package fatec.json.model;

import com.google.gson.JsonElement;

public class QSonElement {

	private String key;
	private JsonElement json;

	public QSonElement(String key, JsonElement json) {
		this.key = key;
		this.json = json;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public JsonElement getJson() {
		return json;
	}

	public void setJson(JsonElement json) {
		this.json = json;
	}

}
