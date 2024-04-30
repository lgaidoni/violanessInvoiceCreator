package violanessInvoiceCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class MainShell extends Shell {
	private Table table;
	private LocalResourceManager localResourceManager;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text textPayeeName;
	private Text textPayeeSortCode;
	private Text textInvoiceOutputFolder;
	private Text textPayeeAccountNumber;

	private String invoiceOutputFolder;
	private String payeeName;
	private String payeeSortCode;
	private String payeeAccountNumber;

	private final static String INVOICE_OUTPUT_FOLDER = "Invoice Output Folder";
	private final static String PAYEE_NAME = "Payee Name";
	private final static String PAYEE_SORT_CODE = "Payee Sort Code";
	private final static String PAYEE_ACCOUNT_NUMBER = "Payee Account Number";

	private final static String COMMA_DELIMITER = ",";

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			MainShell shell = new MainShell(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public MainShell(Display display) {
		super(display, SWT.SHELL_TRIM);
		createResourceManager();
		setLayout(new FillLayout(SWT.HORIZONTAL));

		TabFolder tabFolder = new TabFolder(this, SWT.NONE);

		payeeName = " ";
		payeeSortCode = " ";
		payeeAccountNumber = " ";
		invoiceOutputFolder = " ";

		try {
			File settingsFile = new File("settings.config");
			if (settingsFile.createNewFile()) {
				writeSettingsToFile();
			} else {
				// Settings File Already Exists
				try (BufferedReader br = new BufferedReader(new FileReader("settings.config"))) {
					String line;
					while ((line = br.readLine()) != null) {
						String[] values = line.split(COMMA_DELIMITER);

						if (values.length == 2) {
							switch (values[0]) {
							case PAYEE_NAME:
								payeeName = values[1];
								break;
							case PAYEE_SORT_CODE:
								payeeSortCode = values[1];
								break;
							case PAYEE_ACCOUNT_NUMBER:
								payeeAccountNumber = values[1];
								break;
							case INVOICE_OUTPUT_FOLDER:
								invoiceOutputFolder = values[1];
								break;
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		createStudentsTab(tabFolder);

		createConfigTab(tabFolder);

		createContents();
	}

	private void writeSettingsToFile() throws IOException {
		FileWriter fileWriter = new FileWriter("settings.config");
		fileWriter.write(PAYEE_NAME + "," + payeeName + "\n");
		fileWriter.write(PAYEE_SORT_CODE + "," + payeeSortCode + "\n");
		fileWriter.write(PAYEE_ACCOUNT_NUMBER + "," + payeeAccountNumber + "\n");
		fileWriter.write(INVOICE_OUTPUT_FOLDER + "," + invoiceOutputFolder + "\n");
		fileWriter.close();
	}

	private void createStudentsTab(TabFolder tabFolder) {
		TabItem tbtmStudents = new TabItem(tabFolder, SWT.NONE);
		tbtmStudents.setText("Students");

		Composite studentsCcomposite = new Composite(tabFolder, SWT.NONE);
		tbtmStudents.setControl(studentsCcomposite);
		studentsCcomposite.setLayout(new GridLayout(5, false));

		table = new Table(studentsCcomposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		createColumns();

		TableCursor tableCursor = createStudentsTabCursor();

		createStudentsTabButtons(studentsCcomposite, tableCursor);
	}

	private void createStudentsTabButtons(Composite studentsComposite, TableCursor tableCursor) {
		Button btnSave = new Button(studentsComposite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setFilterExtensions(new String[] { "*.students" });
				fileDialog.setFileName(".students");
				String fileToSave = fileDialog.open();

				if (fileToSave == null)
					return;

				if (!fileToSave.endsWith(".students")) {
					fileToSave += ".students";
				}

				try {
					FileWriter fileWriter = new FileWriter(fileToSave);

					for (TableItem item : table.getItems()) {

						String lineToWrite = "";
						lineToWrite += item.getText(0);
						lineToWrite += "," + item.getText(1);
						lineToWrite += "," + item.getText(2);
						lineToWrite += "," + item.getText(3);
						lineToWrite += "," + item.getText(4);
						lineToWrite += "," + item.getText(5);
						lineToWrite += "," + item.getText(6);
						lineToWrite += "," + item.getText(7);
						lineToWrite += "," + item.getText(8);
						lineToWrite += "," + item.getText(9) + "\n";

						fileWriter.write(lineToWrite);
					}

					fileWriter.close();

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnSave.setText("Save");

		Button btnLoad = new Button(studentsComposite, SWT.NONE);
		btnLoad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell());
				String fileToLoad = fileDialog.open();

				if (fileToLoad != null) {

					try (BufferedReader br = new BufferedReader(new FileReader(fileToLoad))) {
						String line;

						while ((line = br.readLine()) != null) {
							String[] values = line.split(COMMA_DELIMITER);

							TableItem newTableItem = new TableItem(table, SWT.NONE);
							newTableItem.setText(0, values[0]);
							newTableItem.setText(1, values[1]);
							newTableItem.setText(2, values[2]);
							newTableItem.setText(3, values[3]);
							newTableItem.setText(4, values[4]);
							newTableItem.setText(5, values[5]);
							newTableItem.setText(6, values[6]);
							newTableItem.setText(7, values[7]);
							newTableItem.setText(8, values[8]);
							newTableItem.setText(9, values[9]);
						}
					} catch (FileNotFoundException e1) {
						System.out.println("File Not Found");
						e1.printStackTrace();
					} catch (IOException e1) {
						System.out.println("IO Exception");
						e1.printStackTrace();
					}
				} else {
					System.out.println("File String is Null");
				}
			}
		});
		btnLoad.setText("Load");

		Button btnGenerate = new Button(studentsComposite, SWT.NONE);
		btnGenerate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		btnGenerate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

			}
		});
		btnGenerate.setText("Generate");

		Button btnAdd = new Button(studentsComposite, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem newStudent = new TableItem(table, SWT.NONE);
				newStudent.setText(0, "Name");
				newStudent.setText(1, "-----");
				newStudent.setText(2, "-----");
				newStudent.setText(3, "0");
				newStudent.setText(4, "£0.00");
				newStudent.setText(5, "--/--/----");
				newStudent.setText(6, "-----");
				newStudent.setText(7, "£0.00");
				newStudent.setText(8, "-----");
				newStudent.setText(9, "£0.00");
			}
		});
		btnAdd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnAdd.setText("Add Student");

		Button btnRemove = new Button(studentsComposite, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (table.getSelection().length != 0) {
					table.remove(table.getSelectionIndices());
				}
			}
		});
		btnRemove.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnRemove.setText("Remove Student");
	}

	private TableCursor createStudentsTabCursor() {
		TableCursor tableCursor = new TableCursor(table, SWT.BORDER);

		formToolkit.adapt(tableCursor);
		formToolkit.paintBordersFor(tableCursor);

		final ControlEditor editor = new ControlEditor(tableCursor);
		editor.grabHorizontal = true;
		editor.grabVertical = true;

		tableCursor.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				tableSelectionFunctionality(tableCursor, editor, e);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				tableSelectionFunctionality(tableCursor, editor, e);
			}

			private void tableSelectionFunctionality(TableCursor tableCursor, final ControlEditor editor,
					SelectionEvent e) {
				// Dispose of the previous editor
				if (editor.getEditor() != null)
					editor.getEditor().dispose();

				TableItem cursorRow = tableCursor.getRow();
				int column = tableCursor.getColumn();

				switch (tableCursor.getColumn()) {

				// Instrument Editor
				case 1:
					final Combo instrumentCombo = new Combo(tableCursor, SWT.NONE);
					instrumentCombo.add("Violin");
					instrumentCombo.add("Violin (Hired)");
					instrumentCombo.add("Viola");
					instrumentCombo.add("Viola (Hired)");
					instrumentCombo.add("Violin/Viola");
					instrumentCombo.add("Violin/Viola (Hired)");

					instrumentCombo.addModifyListener(new ModifyListener() {

						@Override
						public void modifyText(ModifyEvent arg0) {
							int currentColumn = tableCursor.getColumn();
							for (TableItem row : table.getSelection()) {
								row.setText(currentColumn, instrumentCombo.getText());
							}

						}
					});

					editor.setEditor(instrumentCombo);
					instrumentCombo.setFocus();

					break;

				// Term Editor
				case 2:
					final Combo termCombo = new Combo(tableCursor, SWT.NONE);
					termCombo.add("Spring");
					termCombo.add("Spring (1st)");
					termCombo.add("Spring (2nd)");
					termCombo.add("Summer");
					termCombo.add("Summer (1st)");
					termCombo.add("Summer (2nd)");
					termCombo.add("Autumn");
					termCombo.add("Autumn (1st)");
					termCombo.add("Autumn (2nd)");

					termCombo.addModifyListener(new ModifyListener() {

						@Override
						public void modifyText(ModifyEvent arg0) {
							int currentColumn = tableCursor.getColumn();
							for (TableItem row : table.getSelection()) {
								row.setText(currentColumn, termCombo.getText());
							}
						}
					});

					editor.setEditor(termCombo);
					termCombo.setFocus();

					break;

				// Number of Lessons Editor
				case 3:

					// The Editor for the Selected Cell
					final Text lessonsText = new Text(tableCursor, SWT.NONE);

					if (cursorRow.getText(column) == "0") {
						lessonsText.setText("");
					} else {
						lessonsText.setText(cursorRow.getText(column));
					}

					// The verify listener to enforce price format
					lessonsText.addVerifyListener(new VerifyListener() {

						@Override
						public void verifyText(VerifyEvent arg0) {
							String textToVerify = lessonsText.getText().substring(0, arg0.start) + arg0.text
									+ lessonsText.getText().substring(arg0.end);

							if (textToVerify.matches("^(\\d{1,2})?$")) {
								arg0.doit = true;
							} else
								arg0.doit = false;
						}
					});

					// The key listener to finalise the entered data
					lessonsText.addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent e) {
							// close the text editor and copy the data over
							// when the user hits "ENTER"
							if (e.character == SWT.CR) {
								int currentColumn = tableCursor.getColumn();

								if (lessonsText.getText() == "") {
									lessonsText.setText("0");
								}

								for (TableItem row : table.getSelection()) {
									row.setText(currentColumn, lessonsText.getText());
								}
								lessonsText.dispose();
							}
							// close the text editor when the user hits "ESC"
							if (e.character == SWT.ESC) {
								lessonsText.dispose();
							}
						}
					});
					editor.setEditor(lessonsText);
					lessonsText.setFocus();

					break;

				// Simple Text Editors
				case 0: // Name
				case 6: // Extra 1
				case 8: // Extra 2

					// The Editor for the Selected Cell
					final Text simpleText = new Text(tableCursor, SWT.NONE);

					if (cursorRow.getText(column) == "Name" || cursorRow.getText(column) == "-----") {
						simpleText.setText("");
					} else {
						simpleText.setText(cursorRow.getText(column));
					}

					// The key listener to finalise the entered data
					simpleText.addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent e) {
							// close the text editor and copy the data over
							// when the user hits "ENTER"
							if (e.character == SWT.CR) {
								int currentColumn = tableCursor.getColumn();
								for (TableItem row : table.getSelection()) {
									row.setText(currentColumn, simpleText.getText());
								}
								simpleText.dispose();
							}
							// close the text editor when the user hits "ESC"
							if (e.character == SWT.ESC) {
								simpleText.dispose();
							}
						}
					});
					editor.setEditor(simpleText);
					simpleText.setFocus();

					break;

				// Money Editors
				case 4: // Rate
				case 7: // Extra 1 (Cost Column)
				case 9: // Extra 2 (Cost Column)

					// The Editor for the Selected Cell
					final Text moneyText = new Text(tableCursor, SWT.NONE);
					moneyText.setText("");

					// The verify listener to enforce price format
					moneyText.addVerifyListener(new VerifyListener() {

						@Override
						public void verifyText(VerifyEvent arg0) {
							String textToVerify = moneyText.getText().substring(0, arg0.start) + arg0.text
									+ moneyText.getText().substring(arg0.end);

							if (textToVerify.matches("^(\\d{1,}|(\\d{1,})?(\\.(\\d{0,2}))?)$")) {
								arg0.doit = true;
							} else
								arg0.doit = false;
						}
					});

					// The key listener to finalise the entered data
					moneyText.addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent e) {
							// close the text editor and copy the data over
							// when the user hits "ENTER"
							if (e.character == SWT.CR) {
								int currentColumn = tableCursor.getColumn();

								if (moneyText.getText() == "") {
									moneyText.setText("0.00");
								} else if (!moneyText.getText().contains(".")) {
									moneyText.setText(moneyText.getText() + ".00");
								} else if (moneyText.getText().startsWith(".")) {
									moneyText.setText("0" + moneyText.getText());
								}
								for (TableItem row : table.getSelection()) {
									row.setText(currentColumn, "£" + moneyText.getText());
								}
								moneyText.dispose();
							}
							// close the text editor when the user hits "ESC"
							if (e.character == SWT.ESC) {
								moneyText.dispose();
							}
						}
					});
					editor.setEditor(moneyText);
					moneyText.setFocus();

					break;

				case 5:
					final DateTime dateTime = new DateTime(tableCursor, SWT.NONE);

					dateTime.setDate(Calendar.DAY_OF_MONTH, Calendar.MONTH, Calendar.YEAR);

					// The key listener to finalise the entered data
					dateTime.addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent e) {
							// close the text editor and copy the data over
							// when the user hits "ENTER"
							if (e.character == SWT.CR) {
								int currentColumn = tableCursor.getColumn();
								for (TableItem row : table.getSelection()) {

									String dateString = dateTime.getDay() + "/" + (dateTime.getMonth() + 1) + "/"
											+ dateTime.getYear();

									row.setText(currentColumn, dateString);
								}
								dateTime.dispose();
							}
							// close the text editor when the user hits "ESC"
							if (e.character == SWT.ESC) {
								dateTime.dispose();
							}
						}
					});

					editor.setEditor(dateTime);
					dateTime.setFocus();

					break;
				}
			}
		});

		return tableCursor;
	}

	private void createConfigTab(TabFolder tabFolder) {
		TabItem tbtmConfig = new TabItem(tabFolder, SWT.NONE);
		tbtmConfig.setText("Config");

		Composite configComposite = new Composite(tabFolder, SWT.NONE);
		tbtmConfig.setControl(configComposite);
		formToolkit.paintBordersFor(configComposite);
		configComposite.setLayout(new GridLayout(3, false));
		new Label(configComposite, SWT.NONE);
		new Label(configComposite, SWT.NONE);
		new Label(configComposite, SWT.NONE);

		Label lblPayeeName = new Label(configComposite, SWT.NONE);
		GridData gd_lblPayeeName = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPayeeName.horizontalIndent = 100;
		lblPayeeName.setLayoutData(gd_lblPayeeName);
		formToolkit.adapt(lblPayeeName, true, true);
		lblPayeeName.setText(PAYEE_NAME);

		textPayeeName = new Text(configComposite, SWT.BORDER);
		textPayeeName.setText(payeeName);
		textPayeeName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				payeeName = textPayeeName.getText();
				try {
					writeSettingsToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridData gd_textPayeeName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textPayeeName.widthHint = 300;
		textPayeeName.setLayoutData(gd_textPayeeName);
		formToolkit.adapt(textPayeeName, true, true);
		new Label(configComposite, SWT.NONE);

		Label lblPayeeSortCode = new Label(configComposite, SWT.NONE);
		GridData gd_lblPayeeSortCode = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPayeeSortCode.horizontalIndent = 100;
		lblPayeeSortCode.setLayoutData(gd_lblPayeeSortCode);
		formToolkit.adapt(lblPayeeSortCode, true, true);
		lblPayeeSortCode.setText(PAYEE_SORT_CODE);

		textPayeeSortCode = new Text(configComposite, SWT.BORDER);
		textPayeeSortCode.setText(payeeSortCode);
		textPayeeSortCode.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				payeeSortCode = textPayeeSortCode.getText();
				try {
					writeSettingsToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridData gd_textPayeeSortCode = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textPayeeSortCode.widthHint = 300;
		textPayeeSortCode.setLayoutData(gd_textPayeeSortCode);
		formToolkit.adapt(textPayeeSortCode, true, true);
		new Label(configComposite, SWT.NONE);

		Label lblPayeeAccountNumber = new Label(configComposite, SWT.NONE);
		GridData gd_lblPayeeAccountNumber = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPayeeAccountNumber.horizontalIndent = 100;
		lblPayeeAccountNumber.setLayoutData(gd_lblPayeeAccountNumber);
		formToolkit.adapt(lblPayeeAccountNumber, true, true);
		lblPayeeAccountNumber.setText(PAYEE_ACCOUNT_NUMBER);

		textPayeeAccountNumber = new Text(configComposite, SWT.BORDER);
		textPayeeAccountNumber.setText(payeeAccountNumber);
		textPayeeAccountNumber.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				payeeAccountNumber = textPayeeAccountNumber.getText();
				try {
					writeSettingsToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridData gd_textPayeeAccountNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textPayeeAccountNumber.widthHint = 300;
		textPayeeAccountNumber.setLayoutData(gd_textPayeeAccountNumber);
		formToolkit.adapt(textPayeeAccountNumber, true, true);
		new Label(configComposite, SWT.NONE);

		Label lblInvoiceOutputFolder = new Label(configComposite, SWT.NONE);
		GridData gd_lblInvoiceOutputFolder = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblInvoiceOutputFolder.horizontalIndent = 100;
		lblInvoiceOutputFolder.setLayoutData(gd_lblInvoiceOutputFolder);
		formToolkit.adapt(lblInvoiceOutputFolder, true, true);
		lblInvoiceOutputFolder.setText(INVOICE_OUTPUT_FOLDER);

		textInvoiceOutputFolder = new Text(configComposite, SWT.BORDER);
		textInvoiceOutputFolder.setText(invoiceOutputFolder);
		textInvoiceOutputFolder.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				invoiceOutputFolder = textInvoiceOutputFolder.getText();
				try {
					writeSettingsToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridData gd_textInvoiceOutputFolder = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textInvoiceOutputFolder.widthHint = 300;
		textInvoiceOutputFolder.setLayoutData(gd_textInvoiceOutputFolder);
		formToolkit.adapt(textInvoiceOutputFolder, true, true);

		Button btnChooseFolder = new Button(configComposite, SWT.NONE);
		btnChooseFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
				invoiceOutputFolder = directoryDialog.open();
				textInvoiceOutputFolder.setText(invoiceOutputFolder);
			}
		});
		formToolkit.adapt(btnChooseFolder, true, true);
		btnChooseFolder.setText("Choose Folder");
	}

	private void createColumns() {
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");

		TableColumn tblclmnInstrument = new TableColumn(table, SWT.NONE);
		tblclmnInstrument.setWidth(109);
		tblclmnInstrument.setText("Instrument");

		TableColumn tblclmnTErm = new TableColumn(table, SWT.NONE);
		tblclmnTErm.setWidth(100);
		tblclmnTErm.setText("Term");

		TableColumn tblclmnNoLessons = new TableColumn(table, SWT.NONE);
		tblclmnNoLessons.setWidth(78);
		tblclmnNoLessons.setText("No. Lessons");

		TableColumn tblclmnRate = new TableColumn(table, SWT.NONE);
		tblclmnRate.setWidth(57);
		tblclmnRate.setText("Rate (£)");

		TableColumn tblclmnDate = new TableColumn(table, SWT.NONE);
		tblclmnDate.setWidth(100);
		tblclmnDate.setText("Date");

		TableColumn tblclmnExtra1 = new TableColumn(table, SWT.NONE);
		tblclmnExtra1.setWidth(100);
		tblclmnExtra1.setText("Extra 1");

		TableColumn tblclmnExtra1Price = new TableColumn(table, SWT.NONE);
		tblclmnExtra1Price.setWidth(57);
		tblclmnExtra1Price.setText("Price (£)");

		TableColumn tblclmnExtra2 = new TableColumn(table, SWT.NONE);
		tblclmnExtra2.setWidth(100);
		tblclmnExtra2.setText("Extra 2");

		TableColumn tblclmnExtra2Price = new TableColumn(table, SWT.NONE);
		tblclmnExtra2Price.setWidth(59);
		tblclmnExtra2Price.setText("Price (£)");
	}

	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(), this);
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Violaness Invoice Creator");
		setSize(900, 600);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
