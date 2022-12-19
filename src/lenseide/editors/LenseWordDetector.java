package lenseide.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class LenseWordDetector implements IWordDetector {

	@Override
	public boolean isWordStart(char c) {
		return Character.isLetterOrDigit(c);
	}

	@Override
	public boolean isWordPart(char c) {
		return Character.isLetterOrDigit(c);
	}

}
