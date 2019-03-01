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
package de.alpharogroup.export.excel.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import lombok.experimental.UtilityClass;

/**
 * The class {@link ExportExcelExtensions} provides methods for export excel sheet {@link File}
 * objects
 */
@UtilityClass
public class ExportExcelExtensions
{

	/**
	 * Exports the given excel sheet {@link File} and return a two dimensonal array which holds the
	 * sheets and arrays of the rows
	 *
	 * @param excelSheet
	 *            the excel sheet {@link File}
	 * @return the a two dimensonal array which holds the sheets and arrays of the rows
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException
	 *             Signals that the file was not found
	 */
	public static List<String[][]> exportWorkbook(final File excelSheet)
		throws IOException, FileNotFoundException
	{
		final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelSheet));
		final HSSFWorkbook wb = new HSSFWorkbook(fs);

		final int numberOfSheets = wb.getNumberOfSheets();
		final List<String[][]> sheetList = new ArrayList<>();
		for (int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++)
		{
			HSSFSheet sheet = null;
			sheet = wb.getSheetAt(sheetNumber);
			final int rows = sheet.getLastRowNum();

			final int columns = sheet.getRow(0).getLastCellNum();
			String[][] excelSheetInTDArray = null;
			excelSheetInTDArray = new String[rows + 1][columns];
			for (int i = 0; i <= rows; i++)
			{
				final HSSFRow row = sheet.getRow(i);
				if (null != row)
				{
					for (int j = 0; j < columns; j++)
					{
						excelSheetInTDArray[i][j] = getCellValueAsString(row.getCell(j));
					}
				}
			}
			sheetList.add(excelSheetInTDArray);
		}
		wb.close();
		return sheetList;
	}

	/**
	 * Gets the cell value as String from the given {@link Cell} object
	 *
	 * @param cell
	 *            the cell
	 * @return the cell value
	 */
	public static String getCellValueAsString(Cell cell)
	{
		String result = null;
		if (null == cell)
		{
			return "";
		}
		CellType cellType = cell.getCellType();

		if (CellType.BLANK.equals(cellType))
		{
			result = "";
		}
		else if (CellType.BOOLEAN.equals(cellType))
		{
			result = Boolean.toString(cell.getBooleanCellValue());
		}
		else if (CellType.ERROR.equals(cellType))
		{
			result = "";
		}
		else if (CellType.FORMULA.equals(cellType))
		{
			result = cell.getCellFormula();
		}
		else if (CellType.NUMERIC.equals(cellType))
		{
			result = Double.toString(cell.getNumericCellValue());
		}
		else if (CellType.STRING.equals(cellType))
		{
			result = cell.getRichStringCellValue().getString();
		}
		return result;
	}

	/**
	 * Exports the given excel sheet {@link File} in a list of lists with the list of the sheets and
	 * lists of the rows.
	 *
	 * @param excelSheet
	 *            the excel sheet {@link File}
	 * @return the a list with the sheets and lists of the rows.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException
	 *             Signals that the file was not found
	 */
	public static List<List<List<String>>> exportWorkbookAsStringList(final File excelSheet)
		throws IOException, FileNotFoundException
	{
		final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelSheet));
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		final int numberOfSheets = wb.getNumberOfSheets();
		final List<List<List<String>>> sl = new ArrayList<>();
		for (int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++)
		{
			HSSFSheet sheet = null;
			sheet = wb.getSheetAt(sheetNumber);
			final int rows = sheet.getLastRowNum();
			final int columns = sheet.getRow(0).getLastCellNum();
			final List<List<String>> excelSheetList = new ArrayList<>();
			for (int i = 0; i <= rows; i++)
			{
				final HSSFRow row = sheet.getRow(i);
				if (null != row)
				{
					final List<String> reihe = new ArrayList<>();
					for (int j = 0; j < columns; j++)
					{
						reihe.add(getCellValueAsString(row.getCell(j)));
					}
					excelSheetList.add(reihe);
				}
			}
			sl.add(excelSheetList);
		}
		wb.close();
		return sl;
	}

	/**
	 * Replace null cells into empty cells.
	 *
	 * @param excelSheet
	 *            the excel sheet
	 * @return the HSSF workbook
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException
	 *             the file not found exception
	 */
	public static HSSFWorkbook replaceNullCellsIntoEmptyCells(final File excelSheet)
		throws IOException, FileNotFoundException
	{
		final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(excelSheet));
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		final int numberOfSheets = wb.getNumberOfSheets();
		for (int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++)
		{
			HSSFSheet sheet = null;
			sheet = wb.getSheetAt(sheetNumber);
			final int rows = sheet.getLastRowNum();
			final int columns = sheet.getRow(0).getLastCellNum();
			for (int i = 0; i <= rows; i++)
			{
				final HSSFRow row = sheet.getRow(i);
				if (null != row)
				{
					for (int j = 0; j < columns; j++)
					{
						HSSFCell cell = row.getCell(j);
						if (cell == null)
						{
							cell = row.createCell(j, CellType.BLANK);
						}
					}
				}
			}
		}
		return wb;
	}

}
