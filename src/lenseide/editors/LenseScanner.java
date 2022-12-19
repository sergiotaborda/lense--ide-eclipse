package lenseide.editors;

import org.eclipse.jface.text.rules.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.*;

public class LenseScanner extends RuleBasedScanner {

	private static String[] fgKeywords = { 
			"abstract",	"as",	"annotation"	,"break",	"case",	"catch",	"class",
			"continue",	"constructor",	"default",	"do",	"else",	"enhancement",	"enum",
			"export",	"extends",	"finally",	"for",	"if",	"implements",	"implicit",
			"import",	"interface",	"in",	"inv",	"is",	"module",	"native",
			"new",	"null",	"none",	"out",	"object",	"package",	"public",
			"private",	"protected",	"return",	"super",	"switch",	"this",	"throw",
			"trait",	"try",	"let",	"var",	"while",	"true",	"false", "get", "set", "override" 
	};
	
	public LenseScanner(ColorManager manager) {
		

		IToken keyword= new Token(new TextAttribute(manager.getColor(ILenseColorConstants.KEYWORD)));
		
		IToken string= new Token(new TextAttribute(manager.getColor(ILenseColorConstants.STRING)));
		IToken comment= new Token(new TextAttribute(manager.getColor(ILenseColorConstants.COMMENT)));
		IToken numbers= new Token(new TextAttribute(manager.getColor(ILenseColorConstants.NUMBER)));
		
		IToken other= new Token(new TextAttribute(manager.getColor(ILenseColorConstants.DEFAULT)));
		
		List<IRule> rules= new ArrayList<>();
		
		// Add rule for single line comments.
		rules.add(new EndOfLineRule("//", comment));
		// Add rule for strings.
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("'", "'", string, '\\'));
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new LenseWhitespaceDetector()));
		// Add generic number rule.
		rules.add(new NumberRule(numbers));
		
		// Add word rule for keywords.
		LenseWordRules wordRule= new LenseWordRules(new LenseWordDetector(), other , keyword);
		
//		WordRule wordRule = new WordRule(new LenseWordDetector(), other);
		
		for (int i= 0; i < fgKeywords.length; i++) {
			wordRule.addWord(fgKeywords[i], keyword);
		}

		rules.add(wordRule);
		

		IRule[] result= new IRule[rules.size()];
		rules.toArray(result);
		
		setRules(result);
				
				
	}
	
	public static class LenseWordRules implements IRule {

		private IToken keyword;
		
	
		/** Internal setting for the un-initialized column constraint. */
		protected static final int UNDEFINED= -1;

		/** The word detector used by this rule. */
		protected IWordDetector fDetector;
		/** The default token to be returned on success and if nothing else has been specified. */
		protected IToken fDefaultToken;
		/** The column constraint. */
		protected int fColumn= UNDEFINED;
		/** The table of predefined words and token for this rule. */
		protected Map<String, IToken> fWords= new HashMap<>();
		/** Buffer used for pattern detection. */
		private StringBuffer fBuffer= new StringBuffer();
		/**
		 * Tells whether this rule is case sensitive.
		 * @since 3.3
		 */
		private boolean fIgnoreCase= false;

		public LenseWordRules(IWordDetector fDetector, IToken fDefaultToken, IToken keyword) {
			this.fDetector = fDetector;
			this.fDefaultToken = fDefaultToken;
			this.keyword = keyword;
		}

		/**
		 * Adds a word and the token to be returned if it is detected.
		 *
		 * @param word the word this rule will search for, may not be <code>null</code>
		 * @param token the token to be returned if the word has been found, may not be <code>null</code>
		 */
		public void addWord(String word, IToken token) {
			Assert.isNotNull(word);
			Assert.isNotNull(token);

			// If case-insensitive, convert to lower case before adding to the map
			if (fIgnoreCase)
				word= word.toLowerCase();
			fWords.put(word, token);
		}

		/**
		 * Sets a column constraint for this rule. If set, the rule's token
		 * will only be returned if the pattern is detected starting at the
		 * specified column. If the column is smaller then 0, the column
		 * constraint is considered removed.
		 *
		 * @param column the column in which the pattern starts
		 */
		public void setColumnConstraint(int column) {
			if (column < 0)
				column= UNDEFINED;
			fColumn= column;
		}

		@Override
		public IToken evaluate(ICharacterScanner scanner) {
			int c= scanner.read();
			if (c != ICharacterScanner.EOF && fDetector.isWordStart((char) c)) {
				if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1)) {

					fBuffer.setLength(0);
					do {
						fBuffer.append((char) c);
						c= scanner.read();
					} while (c != ICharacterScanner.EOF && fDetector.isWordPart((char) c));
					scanner.unread();

					String buffer= fBuffer.toString();
					// If case-insensitive, convert to lower case before accessing the map
					if (fIgnoreCase)
						buffer= buffer.toLowerCase();

					IToken token= fWords.get(buffer);

					if (token != null) {
						return token;
					} else if (buffer.toString().equals("value")) {
						scanner.read();
						StringBuilder nextWord = new StringBuilder();
						// detect the next word is class 
						c= scanner.read();
						do {
							nextWord.append((char) c);
							c= scanner.read();
						} while (c != ICharacterScanner.EOF && fDetector.isWordPart((char) c));
						
						for (int i= nextWord.length() - 1; i >= 0; i--) { 
							scanner.unread();
						}
						scanner.unread();
						if (nextWord.toString().equals("class")) {
							return keyword;
						}
					
					}
						

					if (fDefaultToken.isUndefined()) {
						unreadBuffer(scanner);
					}

					return fDefaultToken;
				}
			}

			scanner.unread();
			return Token.UNDEFINED;
		}

		/**
		 * Returns the characters in the buffer to the scanner.
		 *
		 * @param scanner the scanner to be used
		 */
		protected void unreadBuffer(ICharacterScanner scanner) {
			for (int i= fBuffer.length() - 1; i >= 0; i--)
				scanner.unread();
		}
		
	}
}
