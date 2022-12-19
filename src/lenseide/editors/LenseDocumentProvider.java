package lenseide.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class LenseDocumentProvider extends FileDocumentProvider {

	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		
		IDocument document = super.createDocument(element);
		
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(
					new LensePartitionScanner(),
					new String[] { 
							LensePartitionScanner.MULTILINE_COMMENT, 
							LensePartitionScanner.SINGLELINE_COMMENT , 
							LensePartitionScanner.STRING 
					}
			);
			
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		
		return document;
	}
}