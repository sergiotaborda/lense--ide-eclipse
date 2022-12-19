package lenseide.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.*;

public class LensePartitionScanner extends RuleBasedPartitionScanner {
	
	public final static String SINGLELINE_COMMENT = "__lense_single_comment";
	public final static String MULTILINE_COMMENT = "__lense_multiline_comment";
	public final static String STRING = "__lense_string";

	public LensePartitionScanner() {

		IToken multilinecomment= new Token(MULTILINE_COMMENT);
		IToken singlelinecomment= new Token(SINGLELINE_COMMENT);
		IToken string = new Token(STRING);
		
		List<IPredicateRule> rules= new ArrayList<>();
		
		// Add rule for single line comments.
		rules.add(new EndOfLineRule("//", singlelinecomment));
		
		// Add rule for strings and character constants.
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("'", "'", string, '\\'));
		
		// Add rules for multi-line comments and javadoc.
		rules.add(new MultiLineRule("/{", "}/", multilinecomment, (char) 0, true));
		
		IPredicateRule[] result= new IPredicateRule[rules.size()];
		
		rules.toArray(result);
		
		setPredicateRules(result);
		
	}
}
