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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import io.github.astrapi69.file.search.PathFinder;

public class ReadExcelAndWriteSql
{

	@Test
	public void read() throws Exception
	{
		String actual;
		String expected;
		String excelFilePath;
		StringBuilder sb;
		// read excel sheet
		excelFilePath = "addresses.xlsx";
		final File workbookFile = new File(PathFinder.getSrcTestResourcesDir(), excelFilePath);
		FileInputStream inputStream = new FileInputStream(workbookFile);

		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();
		// create a list for add entries from the excel sheet
		ArrayList<Address> list = new ArrayList<>();
		while (iterator.hasNext())
		{
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			final Address address = Address.builder().build();
			list.add(address);
			while (cellIterator.hasNext())
			{
				Cell nextCell = cellIterator.next();
				int columnIndex = nextCell.getColumnIndex();
				final String currentCellValue = ExportExcelExtensions
					.getCellValueAsString(nextCell);
				switch (columnIndex)
				{
					case 0 :
						address.setFirstname(currentCellValue);
						break;
					case 1 :
						address.setSurname(currentCellValue);
						break;
					case 2 :
						address.setStreet(currentCellValue);
						break;
					case 3 :
						address.setZip(currentCellValue);
						break;
					case 4 :
						address.setCity(currentCellValue);
					case 5 :
						address.setId(currentCellValue);
				}
			}
		}

		workbook.close();
		inputStream.close();
		// create an update sql statements
		sb = new StringBuilder();
		for (Address address : list)
		{
			sb.append("UPDATE ").append("ADDRESSES ").append("SET FIRST_NAME='")
				.append(address.getFirstname())
				.append("', SURNAME='").append(address.getSurname()).append("', STREET='")
				.append(address.getStreet()).append("', ZIP='").append(address.getZip())
				.append("', CITY='").append(address.getCity()).append("' WHERE ID='")
				.append(address.getId()).append("';").append("\n\n");
		}
		actual = sb.toString();
		expected = "UPDATE ADDRESSES SET FIRST_NAME='Firstname', SURNAME='Surname', STREET='Street', ZIP='zip', CITY='city' WHERE ID='id';\n"
			+ "\n"
			+ "UPDATE ADDRESSES SET FIRST_NAME='Henry', SURNAME='Miller', STREET='Seaside 5', ZIP='75345', CITY='Imaginationville' WHERE ID='1';\n"
			+ "\n";
		assertEquals(actual, expected);

		// create a insert statement instead
		sb = new StringBuilder();
		for (Address address : list)
		{
			sb.append("INSERT INTO ").append("ADDRESSES ").append("(").append("ID, ")
				.append("FIRST_NAME, ").append("SURNAME, ").append("STREET, ").append("ZIP, ")
				.append("CITY").append(") ").append("VALUES ").append("(").append("'")
				.append(address.getId()).append("'").append(", ").append("'")
				.append(address.getFirstname()).append("'").append(", ").append("'")
				.append(address.getSurname()).append("'").append(", ").append("'")
				.append(address.getStreet()).append("'").append(", ").append("'")
				.append(address.getZip()).append("'").append(", ").append("'")
				.append(address.getCity()).append("'").append(")").append(";").append("\n\n");
		}
		actual = sb.toString();
		expected = "INSERT INTO ADDRESSES (ID, FIRST_NAME, SURNAME, STREET, ZIP, CITY) VALUES ('id', 'Firstname', 'Surname', 'Street', 'zip', 'city');\n"
			+ "\n"
			+ "INSERT INTO ADDRESSES (ID, FIRST_NAME, SURNAME, STREET, ZIP, CITY) VALUES ('1', 'Henry', 'Miller', 'Seaside 5', '75345', 'Imaginationville');\n"
			+ "\n";
		assertEquals(actual, expected);
	}
}
