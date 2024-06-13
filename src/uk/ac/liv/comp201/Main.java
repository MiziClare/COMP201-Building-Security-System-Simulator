package uk.ac.liv.comp201;

public class Main {

	public static void main(String[] args) {
		String cardName="coopesabc";
		Card card;
		// TODO Auto-generated method stub
		try {
			Card.createNewCard(cardName);
			card = Card.loadCard(cardName);
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			card = Card.loadCard(cardName);
			// two valid codes
			card.setCodes("abcdefghij","0123456789");
			Authenticator authenticator=new Authenticator(card);
			// first do a look at invalid codes
			ResponseCode response=authenticator.checkFireCode("abcdefg");
			System.out.println("Response is "+response.toString());
			response=authenticator.checkFireCode("abcdefghij");
			System.out.println("Response is "+response.toString());
			response=authenticator.checkFireCode("abcdefghij1");
			System.out.println("Response is "+response.toString());
			response=authenticator.checkFireCode("abcdefghij1");
			System.out.println("Response is "+response.toString());
			response=authenticator.checkFireCode("abcdefghij1");
			System.out.println("Response is "+response.toString());
			response=authenticator.checkFireCode("abcdefghij1");
			System.out.println("Response is "+response.toString());
			
			
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

}
