package test;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import uk.ac.liv.comp201.Authenticator;
import uk.ac.liv.comp201.Card;
import uk.ac.liv.comp201.CardException;
import uk.ac.liv.comp201.ResponseCode;

public final class Test {
	
	
	private FileWriter writer=null;
	
	public Test() {
		try {
			writer=new FileWriter("feedback.text");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * check set codes in Card.java
	 */
	private TestCase setCodesCases[]= {

			new TestCase( new ResponseCode[] {ResponseCode.INVALID_FIRE_CODE}, new String[] {"abcdefgh","0123456789"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.INVALID_FIRE_CODE}, new String[] {"abcdefghfgfgfffffff","0123456789"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.INVALID_FIRE_CODE}, new String[] {"!&*##$$%%^","0123456789"}  ),
						
			new TestCase( new ResponseCode[] {ResponseCode.INVALID_BURGLARY_CODE}, new String[] {"abcdefghij","012345"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.INVALID_BURGLARY_CODE}, new String[] {"abcdefghij","012345612133446575656"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.INVALID_BURGLARY_CODE}, new String[] {"abcdefghij","abcdefghij"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.OK}, new String[] {"abcdefghij","1234567890"}  ),
						
						
						
						
			
	};
	
	private TestCase checkFireCodeCases[]= {
			new TestCase( new ResponseCode[] {ResponseCode.INVALID_FIRE_CODE}, new String[] {"abcdefg"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.OK}, new String[] {"abcdefghij"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.BAD_FIRE_CODE}, new String[] {"abcdefghij1"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.BAD_FIRE_CODE}, new String[] {"abcdefghij1"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.BAD_FIRE_CODE,ResponseCode.CARD_LOCKED,ResponseCode.CARD_LOCKED_FIRE }, new String[] {"abcdefghij1"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.CARD_LOCKED,ResponseCode.CARD_LOCKED_FIRE }, new String[] {"abcdefghij1"}  )						
	};

	
	private TestCase checkBurglaryCodeCases[]= {
			new TestCase( new ResponseCode[] {ResponseCode.INVALID_BURGLARY_CODE}, new String[] {"1234567"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.INVALID_BURGLARY_CODE}, new String[] {"abcdefghi"}  ),
						
			new TestCase( new ResponseCode[] {ResponseCode.OK}, new String[] {"1234567890"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.BAD_BURGLARY_CODE}, new String[] {"1234567895"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.BAD_BURGLARY_CODE}, new String[] {"1234567895"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.BAD_BURGLARY_CODE,ResponseCode.CARD_LOCKED,ResponseCode.BAD_BURGLARY_CODE }, new String[] {"1234567895"}  ),
			new TestCase( new ResponseCode[] {ResponseCode.CARD_LOCKED,ResponseCode.CARD_LOCKED_BURGLAR_ALARM }, new String[] {"1234567895"}  )						
	};


	
	private Authenticator authenticator;
	private int score=0;
	private int totalScorePossible=0;

	private Card card;
	
	/*
	 * Converts a list of response codes to their string equivalent
	 */
	private String dump(ResponseCode codes[]) {
		StringBuffer sb=new StringBuffer();
		for (int idx=0;idx<codes.length;idx++) {
			if (idx>0) {
				sb.append(" or ");
			}
			sb.append(""+codes[idx]);
		}
		return(sb.toString());
	}

	private ResponseCode runMethod(String name,Object object,String args[]) {
		ResponseCode returnValue=ResponseCode.OK;
		Class classId=object.getClass();
		Class argTypes[]=new Class[args.length];
		for (int idx=0;idx<args.length;idx++) {
			argTypes[idx]=String.class;
		}
		try {
			Method method=classId.getMethod(name, argTypes);
			try {
			switch (args.length) {
			case 1 :
				
					returnValue=(ResponseCode)method.invoke(object,args[0]);
				break;
			case 2 :
				returnValue=(ResponseCode)method.invoke(object,args[0],args[1]);
				break;
			}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				if (e.getCause().getClass().equals(CardException.class)) {
					CardException cexp=(CardException)e.getCause();
					returnValue=cexp.getResponseCode();				
				}				
			} 
			
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		if (returnValue==null) {
			returnValue=ResponseCode.OK;
		}
		return(returnValue);
	}
	
	private void runCheckSetCodes(TestCase testCase) {
		totalScorePossible++;
		// first fetch the test data
		String fireCode=(String)(testCase.getArguments()[0]); // get fire code
		String burglarCode=(String)(testCase.getArguments()[1]); // get burglar code
				
		ResponseCode expectedResponses[]=testCase.getExpected();

		// Now run the test
		ResponseCode response=ResponseCode.OK;
		response=runMethod("setCodes",card,new String[] { fireCode,burglarCode } );
		boolean ok=false;
		for (int idx=0;idx<expectedResponses.length;idx++) {
			if (expectedResponses[idx]==response) {
				ok=true;
			}
		}
		if (ok) {
			score++;
			addToLog("Set codes with fire("+fireCode+") burglar("+burglarCode+") Expected "+dump(expectedResponses)+" actual "+response+" test passed score is "+score);
		} else {
			addToLog("Set codes with fire("+fireCode+") burglar("+burglarCode+")  Expected "+dump(expectedResponses)+" actual "+response+" test failed score is "+score);
		}
	}

	
	private void runCheckFireCode(TestCase testCase) {
		totalScorePossible++;
		// first fetch the test data
		String code=(String)(testCase.getArguments()[0]); // single string argument
		ResponseCode expectedResponses[]=testCase.getExpected();

		// Now run the test
		ResponseCode response=ResponseCode.OK;
		response=runMethod("checkFireCode",authenticator,new String[] {code});
		boolean ok=false;
		for (int idx=0;idx<expectedResponses.length;idx++) {
			if (expectedResponses[idx]==response) {
				ok=true;
			}
		}
		if (ok) {
			score++;
			addToLog("Check fire code with code ="+code+" Expected "+dump(expectedResponses)+" actual "+response+" test passed score is "+score);
		} else {
			addToLog("Check fire code with code ="+code+" Expected "+dump(expectedResponses)+" actual "+response+" test failed score is "+score);
		}
	}
	private void runCheckBurglarCode(TestCase testCase) {
		totalScorePossible++;
		// first fetch the test data
		String code=(String)(testCase.getArguments()[0]); // single string argument
		ResponseCode expectedResponses[]=testCase.getExpected();

		// Now run the test
		ResponseCode response=ResponseCode.OK;
		response=runMethod("checkBurglaryCode",authenticator,new String[] {code});
		boolean ok=false;
		for (int idx=0;idx<expectedResponses.length;idx++) {
			if (expectedResponses[idx]==response) {
				ok=true;
			}
		}
		if (ok) {
			score++;
			addToLog("Check burglar code with code ="+code+" Expected "+dump(expectedResponses)+" actual "+response+" test passed score is "+score);
		} else {
			addToLog("Check burglar code with code ="+code+" Expected "+dump(expectedResponses)+" actual "+response+" test failed score is "+score);
		}
		
	}

	
	private void addToLog(String string) {
		System.out.println(string+" out of "+totalScorePossible);
		try {
			writer.write(string+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Test test=new Test();
		test.runTests();
		

	}

	private void runTests() {
		String cardName="coopesabc";
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
			// test setting of codes in Card
			for (int idx=0;idx<setCodesCases.length;idx++) {
				TestCase testCase=setCodesCases[idx];
				runCheckSetCodes(testCase);
			}
			authenticator=new Authenticator(card);
			// Now do a series of check fire code cases
			for (int idx=0;idx<checkFireCodeCases.length;idx++) {
				TestCase testCase=checkFireCodeCases[idx];
				runCheckFireCode(testCase);
			}
			// Now do a series of check burglar code cases
			for (int idx=0;idx<checkBurglaryCodeCases.length;idx++) {
				TestCase testCase=checkBurglaryCodeCases[idx];
				runCheckBurglarCode(testCase);
			}
			addToLog("Total is "+score);
			addToLog("Out of "+this.totalScorePossible);

		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
