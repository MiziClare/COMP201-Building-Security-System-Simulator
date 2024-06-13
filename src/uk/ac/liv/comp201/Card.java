package uk.ac.liv.comp201;

import static uk.ac.liv.comp201.ResponseCode.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Card {
	// TO DO 
	// Needs to add in bad code count for fire code
	// and burglar alarm
	// You need to modify loadCard and saveCard
	// As soon as the card has a valid burglar code
	// or fire code change its status from NEW
	// to OK
	private static final int CARD_ID_LENGTH=9;
	private static final int CARD_OK=1;
	private static final int CARD_BLOCKED=2;
	/*
	 * CARD_NEW is a card with no fire code or burglar code
	 */
	private static final int CARD_NEW=3;
	private static final int MIN_FIRE_CODE_LEN = 10;
	private static final int MAX_FIRE_CODE_LEN = 14;
	private static final int MIN_BURGLAR_CODE_LEN = 8;
	private static final int MAX_BURGLAR_CODE_LEN = 10;
	private static final int FIRE_RETRY_LIMIT = 3;
	private static final int BURGLAR_RETRY_LIMIT = 3;
	
	
/**
 * The fire code must be between 10 and 14 characters
 * It is made up of alphanumeric characters only 	
 */
	private String cardFireCode="";
/**
  * The burglary code must be between 8 and 10 characters
  * It is made up of numeric digits only 0 to 9 	
*/
	private String cardBurglaryCode="";
/* If a CARD_STATUS = CARD_NEW   doing any form of authentication
 * (checkFireCode or checkBurglaryCode)
 * will throw the CardException exception, with  CARD_STATUS_BAD 
 */
	private int cardStatus=CARD_NEW;
	
	
	/**
	 * Count of consecutive bad fire codes
	 */
	private int badFireCodeCount=0;
	
	/**
	 * Count of consecutive bad burglar codes
	 */
	private int badBurglarCodeCount=0;
	
	
	
	
	/**
	 * User of card, this is a alpha string 9 characters long
	 * The id is case insensivite so SEBCOOPET = sebcoopet
	 */
	private String cardUsername="";
	
	public Card(String cardUsername) throws CardException {
		checkCardName(cardUsername);
		this.cardUsername=cardUsername;
	}
	
	 



	private void checkCardName(String cardUsername) throws CardException {
		if (cardUsername.length()!=CARD_ID_LENGTH) {
			throw new CardException(ResponseCode.INVALID_CARD_ID_LENGTH);			
		}
		if (!cardUserNameValid(cardUsername)) {
			throw new CardException(ResponseCode.INVALID_CARD_ID);			
		}
	}
	
	
	
	public static void createNewCard(String cardUsername) throws CardException {
		Card card=new Card(cardUsername);
		card.saveCard();
	}
		
	private boolean fireCodeValid(String code) {
		if (code==null) return(false);
		if ((code.length()<MIN_FIRE_CODE_LEN) || (code.length()>MAX_FIRE_CODE_LEN)) {
			return(false);
		}
		boolean ok=true;	// true of the fire code format is ok
		for (int idx=0;idx<code.length();idx++) {
			if  ( (!Character.isAlphabetic(code.charAt(idx))) && (!Character.isDigit(code.charAt(idx))) ) {
				ok=false;break;	// oops bad character
			}
		}		
		return(ok);
	}
	
	private boolean burglarCodeValid(String code) {
		if (code==null) return(false);
		if ((code.length()<MIN_BURGLAR_CODE_LEN) || (code.length()>MAX_BURGLAR_CODE_LEN)) {
			return(false);
		}
		boolean ok=true;	// true of the fire code format is ok
		for (int idx=0;idx<code.length();idx++) {
			if  (!Character.isDigit(code.charAt(idx))) {
				ok=false;break;	// oops bad character
			}
		}		
		return(ok);
	}
	
	private boolean cardUserNameValid(String cardUsername) {
		boolean returnValue=true; // default to card is ok
		for (int idx=0;idx<cardUsername.length();idx++) {
			if (!Character.isAlphabetic(cardUsername.charAt(idx))) {
				returnValue=false;break;	// oops bad character
			}
		}
		return(returnValue);
	}
	
	
	private void saveCard() {
		try {
		      FileWriter fileWriter = new FileWriter(cardUsername);
		      fileWriter.write(cardFireCode+"\n");
		      fileWriter.write(cardBurglaryCode+"\n");
		      fileWriter.write(""+cardStatus+"\n");
		      fileWriter.write(""+badBurglarCodeCount);
		      fileWriter.write(""+badFireCodeCount);
		      fileWriter.close();
		    } catch (IOException e) {
		 }
	}
	
	public static Card loadCard(String cardUsername) throws CardException {
		Card card=new Card(cardUsername);
		try {
		      File file = new File(cardUsername);
		      Scanner myReader = new Scanner(file);
		      if (myReader.hasNextLine()) {
		         card.cardFireCode = myReader.nextLine();		        
		      }
		      if (myReader.hasNextLine()) {
			     card.cardBurglaryCode = myReader.nextLine();		        
			  }
		      if (myReader.hasNextLine()) {
				 card.cardStatus =Integer.parseInt(myReader.nextLine());		        
		      }				    
		      if (myReader.hasNextLine()) {
					 card.badBurglarCodeCount =Integer.parseInt(myReader.nextLine());		        
			  }				    
		      if (myReader.hasNextLine()) {
					 card.badFireCodeCount =Integer.parseInt(myReader.nextLine());		        
			  }				    			      
		      myReader.close();
		    } catch (FileNotFoundException e) {
		    	throw new CardException(CARD_NOT_FOUND,cardUsername);
		    }
			return(card);
		  }



	public String getCardFireCode() {
		return cardFireCode;
	}



	private void setCardFireCode(String cardFireCode) throws CardException {
		boolean ok=fireCodeValid(cardFireCode);
		if (!ok) {
			throw new CardException(ResponseCode.INVALID_FIRE_CODE);
		}
		this.cardFireCode = cardFireCode;
	}



	public String getCardBurglaryCode() {
		return cardBurglaryCode;
	}



	private void setCardBurglaryCode(String cardBurglaryCode) throws CardException {
		boolean ok=this.burglarCodeValid(cardBurglaryCode);
		if (!ok) {
			throw new CardException(ResponseCode.INVALID_BURGLARY_CODE);
		}		
		this.cardBurglaryCode = cardBurglaryCode;
	}
	
	public void setCodes(String cardFireCode,String cardBurglaryCode) throws CardException {
		setCardFireCode(cardFireCode);
		setCardBurglaryCode(cardBurglaryCode);
		cardStatus=Card.CARD_OK;
		saveCard();
	}

/**
 * Checks if user input fire code is authenticated ok
 * Any problems are indicated via CardExceptions
 * @param code
 * @throws CardException
 */
	public void checkFireCode(String code) throws CardException {
		if (cardStatus==CARD_NEW) {
			throw new CardException(ResponseCode.CARD_STATUS_BAD);
		}
		if (badFireCodeCount>=FIRE_RETRY_LIMIT) {
           throw new CardException(ResponseCode.CARD_LOCKED);			
		}
		// Now see if fire code valid
		boolean ok=fireCodeValid(code);
		if (!ok) {
			throw new CardException(ResponseCode.INVALID_FIRE_CODE);
		}
		ok=cardFireCode.equals(code);
		if (!ok) {
			// if wrong code, then increment lock count and save
			badFireCodeCount++;saveCard();
			throw new CardException(ResponseCode.BAD_FIRE_CODE);
		}
	}

	/**
	 * Checks if user input fire code is authenticated ok
	 * Any problems are indicated via CardExceptions
	 * @param code  Burglar code to authenticate
	 * @throws CardException
	 */
	public void checkBurglaryCode(String code) throws CardException {
		if (cardStatus==CARD_NEW) {
			throw new CardException(ResponseCode.CARD_STATUS_BAD);
		}
		if (badBurglarCodeCount>=BURGLAR_RETRY_LIMIT) {
           throw new CardException(ResponseCode.CARD_LOCKED);			
		}
		// Now see if burglar code valid
		boolean ok=burglarCodeValid(code);
		if (!ok) {
			throw new CardException(ResponseCode.INVALID_BURGLARY_CODE);
		}
		ok=cardBurglaryCode.equals(code);
		if (!ok) {
			// if wrong code, then increment lock count and save
			badBurglarCodeCount++;saveCard();
			throw new CardException(ResponseCode.BAD_BURGLARY_CODE);
		}
	}

	
	public int getCardStatus() {
		return cardStatus;
	}



	public void setCardStatus(int cardStatus) {
		this.cardStatus = cardStatus;
	}
}
	


