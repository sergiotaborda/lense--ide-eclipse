package lenseide.editors;

import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class LenseEditorConfiguration extends SourceViewerConfiguration {
	private LenseDoubleClickStrategy doubleClickStrategy;

	private LenseScanner scanner;
	private ColorManager colorManager;

	public LenseEditorConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			LensePartitionScanner.SINGLELINE_COMMENT,
			LensePartitionScanner.MULTILINE_COMMENT,
			LensePartitionScanner.STRING
		};
	}

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		
		if (doubleClickStrategy == null) {
			doubleClickStrategy = new LenseDoubleClickStrategy();
		}
			
		return doubleClickStrategy;
	}

	protected LenseScanner getLenseScanner() {
		if (scanner == null) {
			scanner = new LenseScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(ILenseColorConstants.DEFAULT))));
		}
		return scanner;
	}
	
	

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getLenseScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(ILenseColorConstants.COMMENT)));
		reconciler.setDamager(ndr, LensePartitionScanner.MULTILINE_COMMENT);
		reconciler.setRepairer(ndr, LensePartitionScanner.MULTILINE_COMMENT);

		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(ILenseColorConstants.COMMENT)));
		reconciler.setDamager(ndr, LensePartitionScanner.SINGLELINE_COMMENT);
		reconciler.setRepairer(ndr, LensePartitionScanner.SINGLELINE_COMMENT);
		
		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager.getColor(ILenseColorConstants.STRING)));
		reconciler.setDamager(ndr, LensePartitionScanner.STRING);
		reconciler.setRepairer(ndr, LensePartitionScanner.STRING);
		
		return reconciler;
	}

	
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
			IAutoEditStrategy strategy= IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)
				? new MyAutoEditStrategy() 
				: new DefaultIndentLineAutoEditStrategy()
			;
			
			return new IAutoEditStrategy[] { strategy };
	}
	
	@Override 
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) { 
		ContentAssistant assistant= new ContentAssistant();
		
		assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
		assistant.setContentAssistProcessor(new MyContentAssistProcessor(), IDocument.DEFAULT_CONTENT_TYPE); 
		assistant.setAutoActivationDelay(0); assistant.enableAutoActivation(true); 
		assistant.setProposalSelectorBackground(Display.getDefault(). getSystemColor(SWT.COLOR_WHITE)); 
		
		return assistant;
	}
	
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) { 
		return new IHyperlinkDetector[] { 
				new MyHyperlinkDetector()
		};
	}
}