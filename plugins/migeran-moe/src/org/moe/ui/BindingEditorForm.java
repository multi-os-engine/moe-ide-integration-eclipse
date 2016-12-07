/*
 * Copyright (C) 2016 Migeran
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.moe.ui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.moe.tools.natjgen.AbstractBinding;
import org.moe.tools.natjgen.Bindings;
import org.moe.tools.natjgen.FrameworkBinding;
import org.moe.tools.natjgen.HeaderBinding;
import org.moe.utils.logger.LoggerFactory;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;

import java.io.File;
import java.io.IOException;

import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Group;

public class BindingEditorForm extends FormPage {
	
	private static final Logger LOG = LoggerFactory.getLogger(BindingEditorForm.class);
	
	private Text outputText;
	private File configurationFile;
	private List list;
	private Bindings bindings;
	private Combo platformCombo;
	private IManagedForm managedForm;
	private Group editorGroup;
	private Button newBindingButton;
	private Button removeBindingButton;
	private Button upButton;
	private Button downButton;
	private Button generateBindingsButton;
	
	private FrameworkBinding frameworkBinding;
	private FrameworkComposite frameworkComposite;
	private HeaderComposite headerComposite;
	protected boolean dirty = false;
	private boolean inited = false;

	/**
	 * Create the form page.
	 * @param id
	 * @param title
	 */
	public BindingEditorForm(String id, String title) {
		super(id, title);
	}

	/**
	 * Create the form page.
	 * @param editor
	 * @param id
	 * @param title
	 * @wbp.parser.constructor
	 * @wbp.eval.method.parameter id "Some id"
	 * @wbp.eval.method.parameter title "Some title"
	 */
	public BindingEditorForm(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		this.managedForm = managedForm;
		managedForm.getForm().getBody().setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		FormToolkit toolkit = managedForm.getToolkit();
		toolkit.setBackground(null);
		ScrolledForm form = managedForm.getForm();
		form.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		Composite body = form.getBody();
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		managedForm.getForm().getBody().setLayout(null);
		
		list = new List(managedForm.getForm().getBody(), SWT.BORDER);
		list.setBounds(10, 58, 229, 393);
		managedForm.getToolkit().adapt(list, true, true);
		
		Label lblNewLabel = new Label(managedForm.getForm().getBody(), SWT.NONE);
		lblNewLabel.setBounds(10, 10, 102, 14);
		managedForm.getToolkit().adapt(lblNewLabel, true, true);
		lblNewLabel.setText("Output directory:");
		
		outputText = new Text(managedForm.getForm().getBody(), SWT.BORDER);
		outputText.setBounds(118, 7, 121, 19);
		managedForm.getToolkit().adapt(outputText, true, true);
		
		platformCombo = new Combo(managedForm.getForm().getBody(), SWT.READ_ONLY);
		platformCombo.setBounds(118, 30, 121, 22);
		managedForm.getToolkit().adapt(platformCombo);
		managedForm.getToolkit().paintBordersFor(platformCombo);
		
		Label lblNewLabel_1 = new Label(managedForm.getForm().getBody(), SWT.NONE);
		lblNewLabel_1.setBounds(10, 30, 59, 14);
		managedForm.getToolkit().adapt(lblNewLabel_1, true, true);
		lblNewLabel_1.setText("Platform:");
		
		newBindingButton = new Button(managedForm.getForm().getBody(), SWT.NONE);
		newBindingButton.setBounds(245, 58, 141, 28);
		managedForm.getToolkit().adapt(newBindingButton, true, true);
		newBindingButton.setText("+");
		
		removeBindingButton = new Button(managedForm.getForm().getBody(), SWT.NONE);
		removeBindingButton.setBounds(245, 92, 141, 28);
		managedForm.getToolkit().adapt(removeBindingButton, true, true);
		removeBindingButton.setText("-");
		
		upButton = new Button(managedForm.getForm().getBody(), SWT.NONE);
		upButton.setBounds(245, 126, 141, 28);
		managedForm.getToolkit().adapt(upButton, true, true);
		upButton.setText("Up");
		
		downButton = new Button(managedForm.getForm().getBody(), SWT.NONE);
		downButton.setBounds(245, 160, 141, 28);
		managedForm.getToolkit().adapt(downButton, true, true);
		downButton.setText("Down");
		
		generateBindingsButton = new Button(managedForm.getForm().getBody(), SWT.NONE);
		generateBindingsButton.setBounds(245, 423, 141, 28);
		managedForm.getToolkit().adapt(generateBindingsButton, true, true);
		generateBindingsButton.setText("Generate Bindings");
		
		Button btnNewButton_1 = new Button(managedForm.getForm().getBody(), SWT.NONE);
		btnNewButton_1.setBounds(245, 389, 141, 28);
		managedForm.getToolkit().adapt(btnNewButton_1, true, true);
		btnNewButton_1.setText("Test All");
		
		Button btnNewButton_2 = new Button(managedForm.getForm().getBody(), SWT.NONE);
		btnNewButton_2.setBounds(245, 352, 141, 28);
		managedForm.getToolkit().adapt(btnNewButton_2, true, true);
		btnNewButton_2.setText("Test Selected");
		
		editorGroup = new Group(managedForm.getForm().getBody(), SWT.NONE);
		editorGroup.setBounds(392, 48, 504, 535);
		managedForm.getToolkit().adapt(editorGroup);
		managedForm.getToolkit().paintBordersFor(editorGroup);
		
		setDefaultValuesAndListeners();
		init();
	}
	
	@Override
	public final boolean isDirty() {
		return dirty;
	}
	
	public void documentChanged() {
		this.dirty = true;
		getEditor().editorDirtyStateChanged();
	}
	
	private void setDefaultValuesAndListeners() {
		
		list.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int idx = list.getSelectionIndex();
				if (idx >= 0) {
					AbstractBinding selected = bindings.get(idx);
					updateEditor(selected);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}
		});
		
		platformCombo.add(Bindings.PLATFORM_IOS);
		platformCombo.add(Bindings.PLATFORM_OSX);
		
		platformCombo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				bindings.setPlatform(platformCombo.getText());
				documentChanged();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		outputText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				if (inited) {
					bindings.setOutputDirectory(outputText.getText().trim());
					documentChanged();
				}
			}
		});
		
		newBindingButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				showNewBindingDialog();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		removeBindingButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int idx = list.getSelectionIndex();
				if (idx >= 0) {
					bindings.remove(idx);
					updateList();
					documentChanged();
					if (idx > 0) {
						idx--;
					}
					if (!bindings.isEmpty()) {
						list.setSelection(idx);
						AbstractBinding binding = bindings.get(idx);
						updateEditor(binding);
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		upButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int idx = list.getSelectionIndex();
				if (idx > 0) {
					AbstractBinding binding = bindings.remove(idx);
					idx--;
					bindings.add(idx, binding);
					updateList();
					documentChanged();
					list.setSelection(idx);
					updateEditor(binding);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		downButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int idx = list.getSelectionIndex();
				if (idx < bindings.size() - 1) {
					AbstractBinding binding = bindings.remove(idx);
					idx++;
					bindings.add(idx, binding);
					documentChanged();
					updateList();
					list.setSelection(idx);
					updateEditor(binding);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		generateBindingsButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				doSave(null);
				generateBindings();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void init() {
		inited = false;
		this.bindings = new Bindings();
		try {
			bindings.load(configurationFile);
		} catch (IOException e) {
			LOG.error("Unable load binding configuration", e);
		}
		
		updateList();
		
		String outputFolder = bindings.getOutputDirectory();
		outputFolder = outputFolder == null || outputFolder.isEmpty() ? "src/main/java" : outputFolder;
		outputText.setText(outputFolder);
		
		String platform = bindings.getPlatform();
		platform = platform == null || platform.isEmpty() ? Bindings.PLATFORM_IOS : platform;
		platformCombo.select(getSelectedPlatformIndex(platform));
		
		inited = true;
	}

	public File getConfigurationFile() {
		return configurationFile;
	}

	public void setConfigurationFile(File configurationFile) {
		this.configurationFile = configurationFile;
	}
	
	private int getSelectedPlatformIndex(String s) {
		if (s.equals(Bindings.PLATFORM_IOS)) {
			return 0;
		}
		return 1;
	}
	
	private void updateList() {
		int idx = list.getSelectionIndex();
		idx = idx < 0 ? 0 : idx;
		list.removeAll();
		for (AbstractBinding binding : bindings) {
			list.add(binding.getName());
		}
		
		if (list.getItemCount() > 0) {
			updateEditor(bindings.get(0));
		} else {
			updateEditor(null);
		}
		
		list.select(idx);
	}
	
	private void updateEditor(AbstractBinding selected) {
		if (selected == null) {
			if (headerComposite != null) {
				headerComposite.dispose();
				headerComposite = null;
			}
			if (frameworkComposite != null) {
				frameworkComposite.dispose();
				frameworkComposite = null;
			}
			return;
		}
		if (selected.getType().equals(FrameworkBinding.TYPE)) {
			if (headerComposite != null) {
				headerComposite.dispose();
				headerComposite = null;
			}
			frameworkBinding = (FrameworkBinding) selected;
			if (frameworkComposite == null) {
				frameworkComposite = new FrameworkComposite(editorGroup, SWT.NONE);
				frameworkComposite.setBindingEditorForm(this);
				Rectangle retangle = editorGroup.getBounds();
				frameworkComposite.setBounds(10, 10, retangle.width, retangle.height);
				managedForm.getToolkit().adapt(frameworkComposite);
				managedForm.getToolkit().paintBordersFor(frameworkComposite);
			}
			frameworkComposite.initWithFrameworkBinding(frameworkBinding);
		} else {
			if (frameworkComposite != null) {
				frameworkComposite.dispose();
				frameworkComposite = null;
			}
			HeaderBinding headerBinding = (HeaderBinding) selected;
			if (headerComposite == null) {
				headerComposite = new HeaderComposite(editorGroup, SWT.NONE);
				headerComposite.setBindingEditorForm(this);
				Rectangle retangle = editorGroup.getBounds();
				headerComposite.setBounds(10, 10, retangle.width, retangle.height);
				managedForm.getToolkit().adapt(headerComposite);
				managedForm.getToolkit().paintBordersFor(headerComposite);
			}
			headerComposite.initWithHeaderBinding(headerBinding);
		}
	}
	
	private void showNewBindingDialog() {
		Shell shell = Display.getDefault().getActiveShell();
		NewBindingDialog dialog = new NewBindingDialog(shell);
		if (dialog.open() == Window.OK) {
			String type = dialog.getType();
			String name = dialog.getName();
			
			if (name != null && !dialog.getName().isEmpty()) {
				AbstractBinding binding = null;
				if (type.equals(FrameworkBinding.TYPE)) {
					binding = new FrameworkBinding();
				} else {
					binding = new HeaderBinding();
				}
				binding.setName(name);
				bindings.add(binding);
				updateList();
				list.setSelection(list.getItemCount() - 1);
				updateEditor(binding);
				documentChanged();
			}
		}
	}
	
	@Override
	public void doSave(IProgressMonitor arg0) {
		try {
			bindings.save(configurationFile);
			this.dirty = false;
			getEditor().editorDirtyStateChanged();
		} catch (IOException e) {
			LOG.error("Unable save binding configuration", e);;
		}
	}
	
	private void generateBindings() {}
}
