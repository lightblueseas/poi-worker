/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.export.excel.poi;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.astrapi69.file.delete.DeleteFileExtensions;
import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.io.StreamExtensions;
import io.github.astrapi69.lang.ClassExtensions;

/**
 * The unit test class for the class {@link ExportExcelExtensions}.
 */
public class ExportExcelExtensionsTest
{
	final String twoDimArray[][] = { { "1", "a", "!" }, { "2", "b", "?" }, { "3", "c", "%" } };
	final String[][] twoDimArrayDouble = { { "1", "a", "!" }, { "2", "b", "?" },
			{ "3", "c", "%" } };
	File emptyWorkbook;
	Workbook workbook;


	/**
	 * Creates the workbook with content.
	 *
	 * @return the file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private File createWorkbookWithContent() throws IOException
	{
		final File emptyWorkbook = new File(PathFinder.getSrcTestResourcesDir(),
			"emptyWorkbook.xls");
		final Workbook workbook = ExcelPoiFactory.newHSSFWorkbook(emptyWorkbook);
		final Sheet sheet = ExcelPoiFactory.newSheet(workbook, "first sheet");
		int rownum = 0;
		Row row = sheet.createRow(rownum);
		Cell cell0 = row.createCell(0);
		cell0.setCellValue("1");
		Cell cell1 = row.createCell(1);
		cell1.setCellValue("a");
		Cell cell2 = row.createCell(2);
		cell2.setCellValue("!");
		rownum++;
		row = sheet.createRow(rownum);
		cell0 = row.createCell(0);
		cell0.setCellValue("2");
		cell1 = row.createCell(1);
		cell1.setCellValue("b");
		cell2 = row.createCell(2);
		cell2.setCellValue("?");
		rownum++;
		row = sheet.createRow(rownum);
		cell0 = row.createCell(0);
		cell0.setCellValue("3");
		cell1 = row.createCell(1);
		cell1.setCellValue("c");
		cell2 = row.createCell(2);
		cell2.setCellValue("%");

		try
		{
			final OutputStream outputStream = StreamExtensions.getOutputStream(emptyWorkbook);
			workbook.write(outputStream);
			outputStream.close();
		}
		catch (final IOException e)
		{
			throw e;
		}
		return emptyWorkbook;
	}

	/**
	 * Sets up method will be invoked before every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@BeforeMethod
	protected void setUp() throws Exception
	{
		emptyWorkbook = new File(PathFinder.getSrcTestResourcesDir(), "emptyWorkbook.xls");
		workbook = ExcelPoiFactory.newHSSFWorkbook(emptyWorkbook);
	}

	/**
	 * Tear down method will be invoked after every unit test method
	 *
	 * @throws Exception
	 *             is thrown if an exception occurs
	 */
	@AfterMethod
	protected void tearDown() throws Exception
	{
		emptyWorkbook.deleteOnExit();
	}

	/**
	 * Test method for {@link ExportExcelExtensions#isEmpty(Row)}
	 */
	@Test
	public void testIsEmpty()
	{
		// TODO implement...
	}

	/**
	 * Test method for {@link ExportExcelExtensions#getCellValue(Cell)}
	 */
	@Test
	public void testGetCellValue()
	{
		// TODO implement...
	}

	/**
	 * Test method for {@link ExportExcelExtensions#getCellValueAsString(Cell)}
	 */
	@Test
	public void testGetCellValueAsString()
	{
		// TODO implement...
	}

	/**
	 * Test method for {@link ExportExcelExtensions#exportWorkbook(File)}
	 */
	@Test
	public void testExportWorkbook() throws URISyntaxException, IOException
	{
		final File emptyWorkbook = createWorkbookWithContent();
		List<String[][]> sheetList = ExportExcelExtensions.exportWorkbook(emptyWorkbook);
		for (int i = 0; i < sheetList.size(); i++)
		{
			final String[][] sheetEntry = sheetList.get(i);

			for (int j = 0; j < sheetEntry.length; j++)
			{
				for (int y = 0; y < sheetEntry[j].length; y++)
				{
					String expected = twoDimArray[j][y];
					String actual = sheetEntry[j][y];
					assertEquals(expected, actual);
				}
			}
		}
		DeleteFileExtensions.delete(emptyWorkbook);

		final String filename = "test.xls";
		final URL url = ClassExtensions.getResource(filename);
		final File excelSheet = new File(url.toURI());
		assertTrue(excelSheet.exists());
		sheetList = ExportExcelExtensions.exportWorkbook(excelSheet);
		for (int i = 0; i < sheetList.size(); i++)
		{
			final String[][] sheetEntry = sheetList.get(i);

			for (int j = 0; j < sheetEntry.length; j++)
			{
				for (int y = 0; y < sheetEntry[j].length; y++)
				{
					String expected = twoDimArrayDouble[j][y];
					String actual = sheetEntry[j][y];
					assertEquals(expected, actual);
				}
			}
		}
	}

	/**
	 * Test method for {@link ExportExcelExtensions#exportWorkbookAsStringList(File)}
	 */
	@Test
	public void testExportWorkbookAsStringList() throws IOException, URISyntaxException
	{
		final File emptyWorkbook = createWorkbookWithContent();
		final List<List<List<String>>> sheetList = ExportExcelExtensions
			.exportWorkbookAsStringList(emptyWorkbook);
		for (int i = 0; i < sheetList.size(); i++)
		{
			final List<List<String>> sheetEntry = sheetList.get(i);

			for (int j = 0; j < sheetEntry.size(); j++)
			{
				final List<String> list = sheetEntry.get(j);
				for (int y = 0; y < list.size(); y++)
				{
					assertEquals(twoDimArray[j][y], list.get(y));
				}
			}
		}
		DeleteFileExtensions.delete(emptyWorkbook);

		final String filename = "test.xls";
		final URL url = ClassExtensions.getResource(filename);
		final File excelSheet = new File(url.toURI());
		final List<List<List<String>>> excelSheetList = ExportExcelExtensions
			.exportWorkbookAsStringList(excelSheet);
		for (int i = 0; i < excelSheetList.size(); i++)
		{
			final List<List<String>> sheetEntry = excelSheetList.get(i);

			for (int j = 0; j < sheetEntry.size(); j++)
			{
				final List<String> list = sheetEntry.get(j);
				for (int y = 0; y < list.size(); y++)
				{
					String expected = twoDimArrayDouble[j][y];
					String actual = list.get(y);
					assertEquals(expected, actual);
				}
			}
		}
	}

	/**
	 * Test method for {@link ExportExcelExtensions#replaceNullCellsIntoEmptyCells(File)}
	 *
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testReplaceNullCellsIntoEmptyCells() throws FileNotFoundException, IOException
	{
		Sheet sheet = workbook.createSheet("Foo");

		// create a header font styling
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short)12);
		headerFont.setColor(IndexedColors.BLUE.getIndex());

		// create a CellStyle with the created font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		// create the header row
		Row headerRow = sheet.createRow(0);

		// create some header cells
		for (int i = 0; i < twoDimArray.length; i++)
		{
			Cell cell = headerRow.createCell(i);
			String header = twoDimArray[i][0];
			cell.setCellValue(header);
			cell.setCellStyle(headerCellStyle);
		}

		// create some cells with null values
		for (int i = 0; i < twoDimArray.length; i++)
		{
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue((String)null);
			cell = row.createCell(1);
			cell.setCellValue((String)null);
			cell = row.createCell(2);
			cell.setCellValue((String)null);
		}

		// now write it to the output file
		try (FileOutputStream fileOut = new FileOutputStream(emptyWorkbook))
		{
			workbook.write(fileOut);
			workbook.close();
		}

		HSSFWorkbook hssfWorkbook = ExportExcelExtensions
			.replaceNullCellsIntoEmptyCells(emptyWorkbook);

		sheet = hssfWorkbook.getSheet("Foo");

		// Create cells with null values
		for (int i = 0; i < twoDimArray.length; i++)
		{
			Row row = sheet.getRow(i + 1);
			Cell cell = row.getCell(0);
			assertEquals(cell.getStringCellValue(), "");
			cell = row.getCell(1);
			assertEquals(cell.getStringCellValue(), "");
			cell = row.getCell(2);
			assertEquals(cell.getStringCellValue(), "");
		}
	}

	/**
	 * Test method for {@link ExportExcelExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ExportExcelExtensions.class);
	}

}
