package lenseide.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class LenseEditor extends TextEditor {

	private ColorManager colorManager;

	public LenseEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new LenseEditorConfiguration(colorManager));
		setDocumentProvider(new LenseDocumentProvider());
	}
	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
