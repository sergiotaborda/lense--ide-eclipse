package lenseide.editors;


import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;

public class MyAutoEditStrategy implements IAutoEditStrategy {
	
	public void customizeDocumentCommand(IDocument document, DocumentCommand command)
	{
		if(command.text.equals("\""))
		{
			command.text = "\"\"";
			configureCommand(command);
		}
		else if(command.text.equals("'"))
		{
			command.text = "''";
			configureCommand(command);
		} 
		else if (command.text.equals("{"))
		{
			
			
			try {
				int line = document.getLineOfOffset(command.offset);
				String indent = getIndentOfLine(document, line);

				String beforeContent = getBeforeContent(document, line);
				
				if (beforeContent.equals("/")) {
					command.text = "{"  + "\r\n" + indent + "}/";
				} else {
					command.text = "{"  + "\r\n" + indent + "}";
				}
					
				
			
				configureCommand(command);
				
			} catch (BadLocationException e) {
				// no-op
			}
	
		}
	}
	
	private String getBeforeContent(IDocument document, int line) throws BadLocationException {
		
		if (line > -1) {
			int start = document.getLineOffset(line);
			
			if (start -1 >= 0) {
				return document.get(start, 1);
			}
			
		} 
			
		return "XXX";
		
		
	}

	private String getAfterContent(IDocument document, int line) throws BadLocationException {
		if (line > -1) {
			int start = document.getLineOffset(line);
			int end = start + document.getLineLength(line) - 1;
			
			return document.get(start,end).trim();
		} else {
			return "";
		}
	}

	private void configureCommand(DocumentCommand command)
	{
		//puts the caret between both the quotes
		command.caretOffset = command.offset + 1;
		command.shiftsCaret = false;
	}
	
	private  String getIndentOfLine(IDocument document, int line) throws BadLocationException {
			
		if (line > -1) {
			int start = document.getLineOffset(line);
			int end = start + document.getLineLength(line) - 1;
			int whiteend = findEndOfWhiteSpace(document, start, end);
			return document.get(start, whiteend - start);
		} else {
			return "";
		}
	}
	
	public  int findEndOfWhiteSpace(IDocument document, int offset, int end) throws BadLocationException {
			while (offset < end) {
				char c= document.getChar(offset);
				if (c != ' ' & c != '\t') {
					return offset;
				}
				offset++;
			}
			return end;
	}
}

