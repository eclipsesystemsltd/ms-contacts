package uk.co.meridenspares.web.form;

public class Message {

	private String type;
	
	private String message;
	
	public Message() {
	}
	
	public Message(String type, String message) {
		this.type = type;
		this.message = message;
	}

	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @return the message
	 */
	public final String getMessage() {
		return message;
	}
}
