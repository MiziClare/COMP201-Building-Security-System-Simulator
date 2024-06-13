package test;
import uk.ac.liv.comp201.ResponseCode;

public class TestCase {
	public TestCase(ResponseCode[] expected, Object[] arguments) {
		super();
		this.expected = expected;
		this.arguments = arguments;
	}
	private ResponseCode expected[]; // possible expected codes
	private Object arguments[]; // argument list
	public ResponseCode[] getExpected() {
		return expected;
	}
	public Object[] getArguments() {
		return arguments;
	}
	

}
