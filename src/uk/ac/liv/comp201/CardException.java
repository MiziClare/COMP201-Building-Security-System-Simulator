package uk.ac.liv.comp201;

public class CardException extends Exception {

	private ResponseCode responseCode;
	public CardException(ResponseCode responseCode,String message) {
		super(responseCode.name()+" "+message);
		this.responseCode=responseCode;
	}
	
	/**
	 * returns cause of card exception
	 * @return
	 */
	public ResponseCode getResponseCode() {
		return(responseCode);
	}
	
	public CardException(ResponseCode responseCode) {
		super(responseCode.name()); // provide name for Exception reason
		this.responseCode=responseCode;		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
