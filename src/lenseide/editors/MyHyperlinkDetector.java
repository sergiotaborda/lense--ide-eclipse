package lenseide.editors;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.ui.editors.text.TextEditor;

public class MyHyperlinkDetector implements IHyperlinkDetector {
	
	public IHyperlink[] detectHyperlinks(ITextViewer document, IRegion region, boolean canShowMultipleHyperlinks) {
		
//		IRegion lineInfo; 
//		String line; 
//		try { 
//			lineInfo= document.getLineInformationOfOffset(offset); 
//			line= document.get(lineInfo.getOffset(), lineInfo.getLength()); 
//		} catch (BadLocationException ex) {
//			return null; 
//		}
//		
//		int begin= line.indexOf('<'); 
//		int end = line.indexOf('>');
//	
//		if(end<0 || begin<0 || end==begin+1)
//			return null; 
//		
//		String text = line.substring(begin+1,end+1); 
//		
//		IRegion nregion = new Region(lineInfo.getOffset()+begin+1,text.length()); 
//		
		return new IHyperlink[] {
			//new MyHyperlink(nregion,text)
		};
	}
	
	
	
}

class MyHyperlink implements IHyperlink { 
	
	private String location; 
	private IRegion region; 
	
	public MyHyperlink(IRegion region, String text) { 
		this.region= region;
		this.location = text; 
	}
	
	public IRegion getHyperlinkRegion() { return region; } 
	
	public void open() { 
		// TODO 
//		if(location!=null) { 
//			int offset=MyAST.get().getOffset(location);
//			TextEditor editor=getActiveEditor(); 
//			editor.selectAndReveal(offset,0); editor.setFocus(); 
//		}
	} 
	
	public String getTypeLabel() { return null; } 
	
	public String getHyperlinkText() { return null; }}