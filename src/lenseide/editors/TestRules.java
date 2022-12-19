package lenseide.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.Token;
import org.junit.jupiter.api.Test;

class TestRules {

	@Test
	void test() {
		LenseScanner.LenseWordRules rule = new LenseScanner.LenseWordRules(new LenseWordDetector(), Token.UNDEFINED , Token.UNDEFINED);
		
		TestCharacterScanner scanner= new TestCharacterScanner("value class Matrix");
		

			rule.evaluate(scanner);
	
		
	}

	
	public static class TestCharacterScanner implements ICharacterScanner {

		int current =0;
		private String text;
		
		public TestCharacterScanner (String text) {
			this.text = text;
		}
		
		public boolean canRead() {
			return current < text.length();
		}

		@Override
		public char[][] getLegalLineDelimiters() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getColumn() {
			return current;
		}

		@Override
		public int read() {
			 if (current > text.length()) {
				 return -1;
			 }
			 return text.charAt(current++);
		}

		@Override
		public void unread() {
			current--;
		}
		
	}
}
