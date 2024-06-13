package uk.ac.liv.comp201;

import static uk.ac.liv.comp201.ResponseCode.*;

public class Authenticator {
	private Card card;	// this is the Card this is being checked
	private String passcodeFire;
	private String passcodeBurglary;
	
	public Authenticator(Card card) {
		this.card=card;
	}
	
	public ResponseCode checkFireCode(String passCodeFire) throws CardException {
		ResponseCode returnValue=OK; // default is ok, all other responses are returned as Exceptions from Card.java
		try {
			// functionality pushed into Card.java to reduce coupling
			// full marks were also given if method throws return
			// value as CardException
			card.checkFireCode(passCodeFire);
		} catch (CardException e) {
			returnValue=e.getResponseCode();
		}
		return(returnValue);
	}
	
	public ResponseCode checkBurglaryCode(String passCodeBurglary) throws CardException {
		ResponseCode returnValue=OK; // default is ok, all other responses are returned as Exceptions from Card.java
//		try {
			// functionality pushed into Card.java to reduce coupling
			// full marks were also given if method throws return
			// value as CardException
			card.checkBurglaryCode(passCodeBurglary);
		//} catch (CardException e) {
//			returnValue=e.getResponseCode();
//			e.printStackTrace();
		//}
		return(returnValue);
	}
	
	

}
