package violanessInvoiceCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;

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

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

public class MainShell extends Shell {

	@SuppressWarnings("unused")
	private LocalResourceManager localResourceManager;

	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	private Table table;

	private Text textPayeeName;
	private Text textPayeeSortCode;
	private Text textInvoiceOutputFolder;
	private Text textPayeeAccountNumber;
	private Text textPayeeBankName;
	private Text textCustomLogo;
	private Text textTelephoneNumber;
	private Text textEmailAddress;
	private Text textWebAddress;
	private Text textInstrumentHireRate;

	private String invoiceOutputFolder;
	private String payeeName;
	private String payeeSortCode;
	private String payeeAccountNumber;
	private String payeeBankName;
	private String customLogo;
	private String telephoneNumber;
	private String emailAddress;
	private String webAddress;
	private String instrumentHireRate;

	// Data Defaults
	private static final String DEFAULT_LESSONS = "0";
	private static final String DEFAULT_PRICE = "£0.00";
	private static final String DEFAULT_DATE = "--/--/----";
	private static final String DEFAULT_TEXT = "-----";
	private static final String DEFAULT_NAME = "Name";

	// Configuration Labels
	private final static String INVOICE_OUTPUT_FOLDER = "Invoice Output Folder";
	private final static String CUSTOM_LOGO = "Custom Logo";
	private final static String PAYEE_NAME = "Payee Name";
	private final static String PAYEE_SORT_CODE = "Payee Sort Code";
	private final static String PAYEE_ACCOUNT_NUMBER = "Payee Account Number";
	private final static String PAYEE_BANK_NAME = "Payee Bank Name";
	private final static String TELEPHONE_NUMBER = "Telephone Number";
	private final static String EMAIL_ADDRESS = "Email Address";
	private final static String WEB_ADDRESS = "Website Address";
	private final static String INSTRUMENT_HIRE_RATE = "Instrument Hire Rate (£)";

	// Delimiter
	private final static String COMMA_DELIMITER = ",";

	// GUI Instrument Constants
	private static final String VLN_GUI_TEXT = "Violin";
	private static final String VLN_HIRED_GUI_TEXT = "Violin (Hired)";
	private static final String VLA_GUI_TEXT = "Viola";
	private static final String VLA_HIRED_GUI_TEXT = "Viola (Hired)";
	private static final String VLN_VLA_GUI_TEXT = "Violin/Viola";
	private static final String VLN_VLA_HIRED_GUI_TEXT = "Violin/Viola (Hired)";

	// PDF Instrument Constants
	private static final String VLN_PDF_TEXT = "Violin";
	private static final String VLN_HIRED_PDF_TEXT = "Violin (Hired)";
	private static final String VLA_PDF_TEXT = "Viola";
	private static final String VLA_HIRED_PDF_TEXT = "Viola (Hired)";
	private static final String VLN_VLA_PDF_TEXT = "Violin/Viola";
	private static final String VLN_VLA_HIRED_PDF_TEXT = "Violin/Viola (Hired)";

	// Hash Map for the Instruments
	private static final HashMap<String, String> INSTRUMENTS = new HashMap<String, String>();

	// GUI Term Constants
	private static final String SPRING_GUI = "Spring";
	private static final String SPRING_GUI_1 = "Spring (1st)";
	private static final String SPRING_GUI_2 = "Spring (2nd)";
	private static final String SUMMER_GUI = "Summer";
	private static final String SUMMER_GUI_1 = "Summer (1st)";
	private static final String SUMMER_GUI_2 = "Summer (2nd)";
	private static final String AUTUMN_GUI = "Autumn";
	private static final String AUTUMN_GUI_1 = "Autumn (1st)";
	private static final String AUTUMN_GUI_2 = "Autumn (2nd)";

	// PDF Term Constants
	private static final String SPRING_PDF = "Spring Term";
	private static final String SPRING_PDF_1 = "Spring Term (1st Half)";
	private static final String SPRING_PDF_2 = "Spring Term (2nd Half)";
	private static final String SUMMER_PDF = "Summer Term";
	private static final String SUMMER_PDF_1 = "Summer Term (1st Half)";
	private static final String SUMMER_PDF_2 = "Summer Term (2nd Half)";
	private static final String AUTUMN_PDF = "Autumn Term";
	private static final String AUTUMN_PDF_1 = "Autumn Term (1st Half)";
	private static final String AUTUMN_PDF_2 = "Autumn Term (2nd Half)";

	// Hash Map for the Terms
	private static final HashMap<String, String> TERMS = new HashMap<String, String>();

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

		INSTRUMENTS.put(VLN_GUI_TEXT, VLN_PDF_TEXT);
		INSTRUMENTS.put(VLN_HIRED_GUI_TEXT, VLN_HIRED_PDF_TEXT);
		INSTRUMENTS.put(VLA_GUI_TEXT, VLA_PDF_TEXT);
		INSTRUMENTS.put(VLA_HIRED_GUI_TEXT, VLA_HIRED_PDF_TEXT);
		INSTRUMENTS.put(VLN_VLA_GUI_TEXT, VLN_VLA_PDF_TEXT);
		INSTRUMENTS.put(VLN_VLA_HIRED_GUI_TEXT, VLN_VLA_HIRED_PDF_TEXT);

		TERMS.put(SPRING_GUI, SPRING_PDF);
		TERMS.put(SPRING_GUI_1, SPRING_PDF_1);
		TERMS.put(SPRING_GUI_2, SPRING_PDF_2);
		TERMS.put(SUMMER_GUI, SUMMER_PDF);
		TERMS.put(SUMMER_GUI_1, SUMMER_PDF_1);
		TERMS.put(SUMMER_GUI_2, SUMMER_PDF_2);
		TERMS.put(AUTUMN_GUI, AUTUMN_PDF);
		TERMS.put(AUTUMN_GUI_1, AUTUMN_PDF_1);
		TERMS.put(AUTUMN_GUI_2, AUTUMN_PDF_2);

		TabFolder tabFolder = new TabFolder(this, SWT.NONE);

		payeeName = " ";
		payeeSortCode = " ";
		payeeAccountNumber = " ";
		payeeBankName = " ";
		invoiceOutputFolder = " ";
		customLogo = " ";
		telephoneNumber = " ";
		emailAddress = " ";
		webAddress = " ";
		instrumentHireRate = " ";

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
							case PAYEE_BANK_NAME:
								payeeBankName = values[1];
								break;
							case INVOICE_OUTPUT_FOLDER:
								invoiceOutputFolder = values[1];
								break;
							case CUSTOM_LOGO:
								customLogo = values[1];
								break;
							case TELEPHONE_NUMBER:
								telephoneNumber = values[1];
								break;
							case EMAIL_ADDRESS:
								emailAddress = values[1];
								break;
							case WEB_ADDRESS:
								webAddress = values[1];
								break;
							case INSTRUMENT_HIRE_RATE:
								instrumentHireRate = values[1];
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
		fileWriter.write(PAYEE_BANK_NAME + "," + payeeBankName + "\n");
		fileWriter.write(INVOICE_OUTPUT_FOLDER + "," + invoiceOutputFolder + "\n");
		fileWriter.write(CUSTOM_LOGO + "," + customLogo + "\n");
		fileWriter.write(TELEPHONE_NUMBER + "," + telephoneNumber + "\n");
		fileWriter.write(EMAIL_ADDRESS + "," + emailAddress + "\n");
		fileWriter.write(WEB_ADDRESS + "," + webAddress + "\n");
		fileWriter.write(INSTRUMENT_HIRE_RATE + "," + instrumentHireRate + "\n");
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

		// -- Save Button -- //

		Button btnSave = new Button(studentsComposite, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell(), SWT.SAVE);
				fileDialog.setFilterExtensions(new String[] { "*.students" });
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

		// -- Load Button -- //

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

		// -- Generate Button -- //

		Button btnGenerate = new Button(studentsComposite, SWT.NONE);
		btnGenerate.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		btnGenerate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				writePDFs();
			}

			private void writePDFs() {

				for (TableItem student : table.getItems()) {

					org.eclipse.swt.graphics.Color swtRed = new org.eclipse.swt.graphics.Color(138, 0, 0);
					org.eclipse.swt.graphics.Color swtAmber = new org.eclipse.swt.graphics.Color(212, 89, 13);
					org.eclipse.swt.graphics.Color swtGreen = new org.eclipse.swt.graphics.Color(23, 138, 0);

					if (student.getData() != null && (Boolean) student.getData() == true) {
						continue;
					}

					student.setForeground(swtAmber);

					String studentName = student.getText(0);
					if (studentName.contains(DEFAULT_NAME)) {
						student.setForeground(swtRed);
						student.setData(false);
						continue;
					}

					String instrument = student.getText(1);
					if (instrument.contains(DEFAULT_TEXT)) {
						student.setForeground(swtRed);
						student.setData(false);
						continue;
					}

					String term = student.getText(2);
					if (term.contains(DEFAULT_TEXT)) {
						student.setForeground(swtRed);
						student.setData(false);
						continue;
					}

					String numberOfLessons = student.getText(3);
					final int numberOfLessonsInt = Integer.valueOf(numberOfLessons);

					String rate = student.getText(4);
					final double rateDouble = Double.valueOf(rate.replace("£", ""));

					String dateText = student.getText(5);
					if (dateText.contains(DEFAULT_DATE)) {
						student.setForeground(swtRed);
						student.setData(false);
						continue;
					}

					String dateArray[] = dateText.split("/");
					LocalDate date = LocalDate.of(Integer.valueOf(dateArray[2]), Integer.valueOf(dateArray[1]) - 1,
							Integer.valueOf(dateArray[0]));

					String extraOne = student.getText(6);
					String extraOnePrice = student.getText(7);
					final double extraOnePriceDouble = Double.valueOf(extraOnePrice.replace("£", ""));

					String extraTwo = student.getText(8);
					String extraTwoPrice = student.getText(9);
					final double extraTwoPriceDouble = Double.valueOf(extraTwoPrice.replace("£", ""));

					int invoiceNumber = (int) (Math.random() * 10000000);

					String fileNameTerm = term.replace("(1st)", "1").replace("(2nd)", "2");

					String path = invoiceOutputFolder + "/" + date.getYear() + " - " + fileNameTerm + " - "
							+ studentName + " - " + invoiceNumber + ".pdf";

					float[] blueColourValues = { (float) 0.7, (float) 0.43, (float) 0.0, (float) 0.44 };
					Color blueColour = Color.createColorWithColorSpace(blueColourValues);
					
					double finalTotal = 0;

					try {
						PdfWriter pdfWriter = new PdfWriter(path);
						PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER);

						PdfDocument pdfDocument = new PdfDocument(pdfWriter);
						pdfDocument.addNewPage();
						pdfDocument.setDefaultPageSize(PageSize.A4);

						Document document = new Document(pdfDocument);

						/**
						 * Add the Image to the PDF
						 */
						if (customLogo.endsWith(".jpg") || customLogo.endsWith(".png")) {
							String imageFile = customLogo;

							ImageData imageData = ImageDataFactory.create(imageFile);

							Image image = new Image(imageData);

							image.setHorizontalAlignment(HorizontalAlignment.CENTER);
							image.setTextAlignment(TextAlignment.CENTER);
							image.setHeight(80);
							image.setMarginTop(50);

							document.add(image);
						}

						/**
						 * Add the Title
						 */
						{
							Paragraph pTitleName = new Paragraph();
							pTitleName.add(payeeName);
							pTitleName.setTextAlignment(TextAlignment.CENTER);
							pTitleName.setFontSize(13);
							pTitleName.setFont(font);
							pTitleName.setFontColor(blueColour);
							pTitleName.setMarginBottom(0);
							pTitleName.setMarginTop(0);

							document.add(pTitleName);
						}

						/**
						 * Add the Telephone Number
						 */
						{
							Paragraph pTelephoneNumber = new Paragraph();
							pTelephoneNumber.add(telephoneNumber);
							pTelephoneNumber.setTextAlignment(TextAlignment.CENTER);
							pTelephoneNumber.setFontSize(13);
							pTelephoneNumber.setFont(font);
							pTelephoneNumber.setFontColor(blueColour);
							pTelephoneNumber.setMarginBottom(0);
							pTelephoneNumber.setMarginTop(0);

							document.add(pTelephoneNumber);
						}

						/**
						 * Add the E-Mail Address
						 */
						{
							Paragraph pEmailAddress = new Paragraph();
							pEmailAddress.add(emailAddress);
							pEmailAddress.setTextAlignment(TextAlignment.CENTER);
							pEmailAddress.setFontSize(11);
							pEmailAddress.setFont(font);
							pEmailAddress.setFontColor(blueColour);
							pEmailAddress.setMarginBottom(0);
							pEmailAddress.setMarginTop(0);

							document.add(pEmailAddress);
						}

						/**
						 * Add the Web Address
						 */
						{
							Paragraph pWebAddress = new Paragraph();
							pWebAddress.add(webAddress);
							pWebAddress.setTextAlignment(TextAlignment.CENTER);
							pWebAddress.setFontSize(11);
							pWebAddress.setFont(font);
							pWebAddress.setFontColor(blueColour);
							pWebAddress.setMarginBottom(0);
							pWebAddress.setMarginTop(0);

							document.add(pWebAddress);
						}

						/**
						 * Add the Title
						 */
						{
							Paragraph pTitle = new Paragraph();

							String titleInstrument = "";

							if (instrument.contains("Violin/Viola")) {
								titleInstrument = "Violin/Viola";
							} else if (instrument.contains("Violin")) {
								titleInstrument = "Violin";
							} else if (instrument.contains("Viola")) {
								titleInstrument = "Viola";
							} else {
								titleInstrument = "";
							}

							String titleText = titleInstrument + " Lessons - Invoice";

							pTitle.add(titleText);
							pTitle.setTextAlignment(TextAlignment.CENTER);
							pTitle.setFontSize(20);
							pTitle.setFont(font);
							pTitle.setFontColor(blueColour);
							pTitle.setMarginBottom(0);
							pTitle.setMarginTop(30);

							document.add(pTitle);
						}

						/**
						 * Add the Invoice Information
						 */
						{
							Paragraph pInvoiceInfoTitle = new Paragraph();

							String invoiceInfoTitle = "Tuition Fees for " + TERMS.get(term) + " - " + date.getYear();

							pInvoiceInfoTitle.add(invoiceInfoTitle);
							pInvoiceInfoTitle.setTextAlignment(TextAlignment.LEFT);
							pInvoiceInfoTitle.setFontSize(11);
							pInvoiceInfoTitle.setFont(font);
							pInvoiceInfoTitle.setMarginBottom(0);
							pInvoiceInfoTitle.setMarginTop(30);
							pInvoiceInfoTitle.setUnderline();
							pInvoiceInfoTitle.setMarginLeft(25);

							document.add(pInvoiceInfoTitle);

							Paragraph pInvoiceInfo = new Paragraph();

							String invoiceInfo = "";
							invoiceInfo += "Invoice Number:  " + invoiceNumber + "\n";
							invoiceInfo += "Invoice Date:    " + dateText + "\n";
							invoiceInfo += "Pupil's Name:    " + studentName + "\n";

							pInvoiceInfo.add(invoiceInfo);
							pInvoiceInfo.setTextAlignment(TextAlignment.LEFT);
							pInvoiceInfo.setFontSize(11);
							pInvoiceInfo.setFont(font);
							pInvoiceInfo.setMarginBottom(0);
							pInvoiceInfo.setMarginTop(10);
							pInvoiceInfo.setMarginLeft(25);

							document.add(pInvoiceInfo);
						}

						/**
						 * Add the Table of Costs
						 */
						{
							Paragraph pFeesTitle = new Paragraph();

							pFeesTitle.add("Fees:");
							pFeesTitle.setTextAlignment(TextAlignment.LEFT);
							pFeesTitle.setFontSize(11);
							pFeesTitle.setFont(font);
							pFeesTitle.setMarginBottom(0);
							pFeesTitle.setMarginTop(30);
							pFeesTitle.setUnderline();
							pFeesTitle.setMarginLeft(25);

							document.add(pFeesTitle);

							com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(2);

							// -- Tuition Cell -- //
							Cell tuitionCell = new Cell();

							String tableInstrument = "";

							if (instrument.contains("Violin/Viola")) {
								tableInstrument = " Violin/Viola";
							} else if (instrument.contains("Violin")) {
								tableInstrument = " Violin";
							} else if (instrument.contains("Viola")) {
								tableInstrument = " Viola";
							} else {
								tableInstrument = "";
							}

							Paragraph pTuition = new Paragraph();
							pTuition.add(numberOfLessons + " weeks" + tableInstrument + " tuition @ " + rate);
							pTuition.setFontSize(11);
							pTuition.setFont(font);
							pTuition.setMarginRight(10);
							pTuition.setTextAlignment(TextAlignment.RIGHT);

							tuitionCell.add(pTuition);
							tuitionCell.setBorder(Border.NO_BORDER);

							table.addCell(tuitionCell);

							// -- Rate Cell -- //
							Cell rateCell = new Cell();

							double finalRate = rateDouble * numberOfLessonsInt;
							finalTotal += finalRate;

							Paragraph pRate = new Paragraph();
							pRate.add("£" + String.format("%.2f", finalRate));
							pRate.setFontSize(11);
							pRate.setFont(font);
							pRate.setMarginLeft(10);

							rateCell.add(pRate);
							rateCell.setBorder(Border.NO_BORDER);
							rateCell.setBorderLeft(new SolidBorder(0.5f));

							table.addCell(rateCell);

							if (instrument.contains("Hired")) {

								// -- Hired Instrument Info Cell -- //

								Cell hiredInstrumentInfoCell = new Cell();

								Paragraph pHiredInstrument = new Paragraph();
								pHiredInstrument.add("Hired" + tableInstrument);
								pHiredInstrument.setFontSize(11);
								pHiredInstrument.setFont(font);
								pHiredInstrument.setMarginRight(10);
								pHiredInstrument.setTextAlignment(TextAlignment.RIGHT);

								hiredInstrumentInfoCell.add(pHiredInstrument);
								hiredInstrumentInfoCell.setBorder(Border.NO_BORDER);

								table.addCell(hiredInstrumentInfoCell);

								// -- Hired Instrument Price Cell -- //
								Cell hiredInstrumentPriceCell = new Cell();
								

								double instrumentHireRateDouble = Double.valueOf(instrumentHireRate.replace(" ", ""));
								
								if (!(term.contains("1st") || term.contains("2nd"))) {
									instrumentHireRateDouble *= 2; 
								}
										
								finalTotal += instrumentHireRateDouble;

								Paragraph pHiredInstrumentPrice = new Paragraph();
								pHiredInstrumentPrice.add("£" + String.format("%.2f", instrumentHireRateDouble));
								pHiredInstrumentPrice.setFontSize(11);
								pHiredInstrumentPrice.setFont(font);
								pHiredInstrumentPrice.setMarginLeft(10);

								hiredInstrumentPriceCell.add(pHiredInstrumentPrice);
								hiredInstrumentPriceCell.setBorder(Border.NO_BORDER);
								hiredInstrumentPriceCell.setBorderLeft(new SolidBorder(0.5f));

								table.addCell(hiredInstrumentPriceCell);
							}

							if (!extraOne.contains(DEFAULT_TEXT)) {

								// -- Extra One Info Cell -- //

								Cell extraOneCell = new Cell();

								Paragraph pExtraOne = new Paragraph();
								pExtraOne.add(extraOne);
								pExtraOne.setFontSize(11);
								pExtraOne.setFont(font);
								pExtraOne.setMarginRight(10);
								pExtraOne.setTextAlignment(TextAlignment.RIGHT);

								extraOneCell.add(pExtraOne);
								extraOneCell.setBorder(Border.NO_BORDER);

								table.addCell(extraOneCell);

								// -- Extra One Price Cell -- //
								Cell extraOnePriceCell = new Cell();

								Paragraph pExtraOnePrice = new Paragraph();
								pExtraOnePrice.add(extraOnePrice);
								pExtraOnePrice.setFontSize(11);
								pExtraOnePrice.setFont(font);
								pExtraOnePrice.setMarginLeft(10);

								extraOnePriceCell.add(pExtraOnePrice);
								extraOnePriceCell.setBorder(Border.NO_BORDER);
								extraOnePriceCell.setBorderLeft(new SolidBorder(0.5f));

								table.addCell(extraOnePriceCell);
							
								finalTotal += extraOnePriceDouble;
							}

							if (!extraTwo.contains(DEFAULT_TEXT)) {

								// -- Extra Two Info Cell -- //

								Cell extraTwoCell = new Cell();

								Paragraph pExtraTwo = new Paragraph();
								pExtraTwo.add(extraTwo);
								pExtraTwo.setFontSize(11);
								pExtraTwo.setFont(font);
								pExtraTwo.setMarginRight(10);
								pExtraTwo.setTextAlignment(TextAlignment.RIGHT);

								extraTwoCell.add(pExtraTwo);
								extraTwoCell.setBorder(Border.NO_BORDER);

								table.addCell(extraTwoCell);

								// -- Extra Two Price Cell -- //
								Cell extraTwoPriceCell = new Cell();

								Paragraph pExtraTwoPrice = new Paragraph();
								pExtraTwoPrice.add(extraTwoPrice);
								pExtraTwoPrice.setFontSize(11);
								pExtraTwoPrice.setFont(font);
								pExtraTwoPrice.setMarginLeft(10);

								extraTwoPriceCell.add(pExtraTwoPrice);
								extraTwoPriceCell.setBorder(Border.NO_BORDER);
								extraTwoPriceCell.setBorderLeft(new SolidBorder(0.5f));

								table.addCell(extraTwoPriceCell);
								
								finalTotal += extraTwoPriceDouble;
							}

							// -- Filler Cells -- //

							addFillerRows(table);

							// -- Total Cells -- //
							Cell totalCell = new Cell();

							Paragraph ptotal = new Paragraph();
							ptotal.add("Total");
							ptotal.setFontSize(11);
							ptotal.setFont(font);
							ptotal.setMarginRight(10);
							ptotal.setTextAlignment(TextAlignment.RIGHT);

							totalCell.add(ptotal);
							totalCell.setBorder(Border.NO_BORDER);

							table.addCell(totalCell);

							Cell totalPriceCell = new Cell();

							Paragraph pTotalPrice = new Paragraph();
							pTotalPrice.add("£" + String.format("%.2f", finalTotal));
							pTotalPrice.setFontSize(11);
							pTotalPrice.setFont(font);
							pTotalPrice.setMarginLeft(10);

							totalPriceCell.add(pTotalPrice);
							totalPriceCell.setBorder(Border.NO_BORDER);
							totalPriceCell.setBorderLeft(new SolidBorder(0.5f));

							table.addCell(totalPriceCell);

							// -- Final Table Setup -- //

							table.setHorizontalAlignment(HorizontalAlignment.CENTER);

							document.add(table);
						}

						/**
						 * Add the Payment Information
						 */
						{
							Paragraph pPaymentInfoTitle = new Paragraph();

							String paymentInfoTitle = "Payment Terms:";

							pPaymentInfoTitle.add(paymentInfoTitle);
							pPaymentInfoTitle.setTextAlignment(TextAlignment.LEFT);
							pPaymentInfoTitle.setFontSize(10);
							pPaymentInfoTitle.setFont(font);
							pPaymentInfoTitle.setMarginBottom(0);
							pPaymentInfoTitle.setMarginTop(30);
							pPaymentInfoTitle.setUnderline();
							pPaymentInfoTitle.setMarginLeft(25);

							document.add(pPaymentInfoTitle);

							Paragraph pPaymentInfo = new Paragraph();

							String paymentInfo = "";
							paymentInfo += "Payment, if made by bank transfer: \n";
							paymentInfo += "Payee Name: " + payeeName + "\n";
							paymentInfo += "Payee Sort Code: " + payeeSortCode + "\n";
							paymentInfo += "Account: " + payeeBankName + "\n";
							paymentInfo += "Payee Account Number: " + payeeAccountNumber + "\n";

							String payeeReference = "";

							if (instrument.contains("Violin/Viola")) {
								payeeReference = "VLNVLA";
							} else if (instrument.contains("Violin")) {
								payeeReference = "VLN";
							} else if (instrument.contains("Viola")) {
								payeeReference = "VLA";
							} else {
								payeeReference = "VLN";
							}

							paymentInfo += "Payee Reference: " + payeeReference + " " + studentName;

							pPaymentInfo.add(paymentInfo);
							pPaymentInfo.setTextAlignment(TextAlignment.LEFT);
							pPaymentInfo.setFontSize(10);
							pPaymentInfo.setFont(font);
							pPaymentInfo.setMarginBottom(0);
							pPaymentInfo.setMarginTop(10);
							pPaymentInfo.setMarginLeft(25);

							document.add(pPaymentInfo);
						}

						document.close();

						student.setForeground(swtGreen);
						student.setData(true);

					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}

			}

			private void addFillerRows(com.itextpdf.layout.element.Table table) {
				Cell fillerCellLeft = new Cell();
				fillerCellLeft.setHeight(50);
				fillerCellLeft.setBorder(Border.NO_BORDER);

				Cell fillerCellRight = new Cell();
				fillerCellRight.setHeight(50);
				fillerCellRight.setBorder(Border.NO_BORDER);
				fillerCellRight.setBorderLeft(new SolidBorder(0.5f));

				// Filler Row 1
				table.addCell(fillerCellLeft);
				table.addCell(fillerCellRight);
			}
		});
		btnGenerate.setText("Generate");

		// -- Add Button -- //

		Button btnAdd = new Button(studentsComposite, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem newStudent = new TableItem(table, SWT.NONE);
				newStudent.setText(0, DEFAULT_NAME);
				newStudent.setText(1, DEFAULT_TEXT);
				newStudent.setText(2, DEFAULT_TEXT);
				newStudent.setText(3, DEFAULT_LESSONS);
				newStudent.setText(4, DEFAULT_PRICE);
				newStudent.setText(5, DEFAULT_DATE);
				newStudent.setText(6, DEFAULT_TEXT);
				newStudent.setText(7, DEFAULT_PRICE);
				newStudent.setText(8, DEFAULT_TEXT);
				newStudent.setText(9, DEFAULT_PRICE);
			}
		});
		btnAdd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnAdd.setText("Add Student");

		// -- Remove Button -- //

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

					instrumentCombo.add(VLN_GUI_TEXT);
					instrumentCombo.add(VLN_HIRED_GUI_TEXT);
					instrumentCombo.add(VLA_GUI_TEXT);
					instrumentCombo.add(VLA_HIRED_GUI_TEXT);
					instrumentCombo.add(VLN_VLA_GUI_TEXT);
					instrumentCombo.add(VLN_VLA_HIRED_GUI_TEXT);

					instrumentCombo.addModifyListener(new ModifyListener() {

						@Override
						public void modifyText(ModifyEvent arg0) {
							int currentColumn = tableCursor.getColumn();
							for (TableItem row : table.getSelection()) {
								row.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
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
					termCombo.add(SPRING_GUI);
					termCombo.add(SPRING_GUI_1);
					termCombo.add(SPRING_GUI_2);
					termCombo.add(SUMMER_GUI);
					termCombo.add(SUMMER_GUI_1);
					termCombo.add(SUMMER_GUI_2);
					termCombo.add(AUTUMN_GUI);
					termCombo.add(AUTUMN_GUI_1);
					termCombo.add(AUTUMN_GUI_2);

					termCombo.addModifyListener(new ModifyListener() {

						@Override
						public void modifyText(ModifyEvent arg0) {
							int currentColumn = tableCursor.getColumn();
							for (TableItem row : table.getSelection()) {
								row.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
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

					if (cursorRow.getText(column) == DEFAULT_LESSONS) {
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
									lessonsText.setText(DEFAULT_LESSONS);
								}

								for (TableItem row : table.getSelection()) {
									row.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
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

					if (cursorRow.getText(column) == DEFAULT_NAME || cursorRow.getText(column) == DEFAULT_TEXT) {
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
									row.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
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
									moneyText.setText(DEFAULT_LESSONS + moneyText.getText());
								}
								for (TableItem row : table.getSelection()) {
									row.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
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

									row.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
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

		// --- Payee Name --- //

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

		// -- Payee Sort Code --- //

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

		// -- Payee Account Number -- //

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

		// -- Payee Bank Name -- //

		Label lblPayeeBankName = new Label(configComposite, SWT.NONE);
		lblPayeeBankName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblPayeeBankName, true, true);
		lblPayeeBankName.setText(PAYEE_BANK_NAME);

		textPayeeBankName = new Text(configComposite, SWT.BORDER);
		textPayeeBankName.setText(payeeBankName);
		textPayeeBankName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				payeeBankName = textPayeeBankName.getText();
				try {
					writeSettingsToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridData gd_textPayeeBankName = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textPayeeBankName.widthHint = 300;
		textPayeeBankName.setLayoutData(gd_textPayeeBankName);
		formToolkit.adapt(textPayeeBankName, true, true);
		new Label(configComposite, SWT.NONE);

		// -- Telephone Number -- //

		Label lblTelephoneNumber = new Label(configComposite, SWT.NONE);
		lblTelephoneNumber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblTelephoneNumber, true, true);
		lblTelephoneNumber.setText(TELEPHONE_NUMBER);

		textTelephoneNumber = new Text(configComposite, SWT.BORDER);
		textTelephoneNumber.setText(telephoneNumber);
		textTelephoneNumber.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				telephoneNumber = textTelephoneNumber.getText();
				try {
					writeSettingsToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridData gd_textTelephoneNumber = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textTelephoneNumber.widthHint = 300;
		textTelephoneNumber.setLayoutData(gd_textTelephoneNumber);
		formToolkit.adapt(textTelephoneNumber, true, true);
		new Label(configComposite, SWT.NONE);

		// -- Email Address -- //

		Label lblEmailAddress = new Label(configComposite, SWT.NONE);
		lblEmailAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblEmailAddress, true, true);
		lblEmailAddress.setText(EMAIL_ADDRESS);

		textEmailAddress = new Text(configComposite, SWT.BORDER);
		textEmailAddress.setText(emailAddress);
		textEmailAddress.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				emailAddress = textEmailAddress.getText();
				try {
					writeSettingsToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridData gd_textEmailAddress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textEmailAddress.widthHint = 300;
		textEmailAddress.setLayoutData(gd_textEmailAddress);
		formToolkit.adapt(textEmailAddress, true, true);
		new Label(configComposite, SWT.NONE);

		// -- Web Address -- //

		Label lblWebAddress = new Label(configComposite, SWT.NONE);
		lblWebAddress.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblWebAddress, true, true);
		lblWebAddress.setText(WEB_ADDRESS);

		textWebAddress = new Text(configComposite, SWT.BORDER);
		textWebAddress.setText(webAddress);
		textWebAddress.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				webAddress = textWebAddress.getText();
				try {
					writeSettingsToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridData gd_textWebAddress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textWebAddress.widthHint = 300;
		textWebAddress.setLayoutData(gd_textWebAddress);
		formToolkit.adapt(textWebAddress, true, true);
		new Label(configComposite, SWT.NONE);

		// -- Instrument Hire Rate -- //

		Label lblInstrumentHireRate = new Label(configComposite, SWT.NONE);
		lblInstrumentHireRate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblInstrumentHireRate, true, true);
		lblInstrumentHireRate.setText(INSTRUMENT_HIRE_RATE);

		textInstrumentHireRate = new Text(configComposite, SWT.BORDER);
		textInstrumentHireRate.setText(instrumentHireRate);

		// The verify listener to enforce price format
		textInstrumentHireRate.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent arg0) {
				String textToVerify = textInstrumentHireRate.getText().substring(0, arg0.start) + arg0.text
						+ textInstrumentHireRate.getText().substring(arg0.end);

				if (textToVerify.matches("^(\\d{1,}|(\\d{1,})?(\\.(\\d{0,2}))?)$")) {
					arg0.doit = true;
				} else
					arg0.doit = false;
			}
		});

		textInstrumentHireRate.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				instrumentHireRate = textInstrumentHireRate.getText();
				try {
					writeSettingsToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridData gd_textInstrumentHireRate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textInstrumentHireRate.widthHint = 300;
		textInstrumentHireRate.setLayoutData(gd_textInstrumentHireRate);
		formToolkit.adapt(textInstrumentHireRate, true, true);
		new Label(configComposite, SWT.NONE);

		// -- Custom Logo -- //

		Label lblCustomLogo = new Label(configComposite, SWT.NONE);
		lblCustomLogo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblCustomLogo, true, true);
		lblCustomLogo.setText("Custom Logo");

		textCustomLogo = new Text(configComposite, SWT.BORDER);
		textCustomLogo.setText(customLogo);
		textCustomLogo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				customLogo = textCustomLogo.getText();
				try {
					writeSettingsToFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		GridData gd_textCustomLogo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_textCustomLogo.widthHint = 300;
		textCustomLogo.setLayoutData(gd_textCustomLogo);
		formToolkit.adapt(textCustomLogo, true, true);

		Button btnChooseCustomLogo = new Button(configComposite, SWT.NONE);
		GridData gd_btnChooseCustomLogo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnChooseCustomLogo.widthHint = 100;
		btnChooseCustomLogo.setLayoutData(gd_btnChooseCustomLogo);
		btnChooseCustomLogo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell());
				customLogo = fileDialog.open();

				if (customLogo != null) {
					textCustomLogo.setText(customLogo);
				}
			}
		});
		formToolkit.adapt(btnChooseCustomLogo, true, true);
		btnChooseCustomLogo.setText("Choose File");

		// -- Invoice Output Folder -- //

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
		GridData gd_btnChooseFolder = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_btnChooseFolder.widthHint = 100;
		btnChooseFolder.setLayoutData(gd_btnChooseFolder);
		btnChooseFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
				invoiceOutputFolder = directoryDialog.open();

				if (invoiceOutputFolder != null) {
					textInvoiceOutputFolder.setText(invoiceOutputFolder);
				}
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
