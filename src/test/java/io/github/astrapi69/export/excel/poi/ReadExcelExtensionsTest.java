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

import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.meanbean.factories.ObjectCreationException;
import org.meanbean.test.BeanTestException;
import org.meanbean.test.BeanTester;
import org.testng.annotations.Test;

import io.github.astrapi69.file.search.PathFinder;
import io.github.astrapi69.lang.ClassExtensions;

/**
 * The unit test class for the class {@link ReadExcelExtensions}
 */
public class ReadExcelExtensionsTest
{
	/**
	 * Test method for {@link ReadExcelExtensions#readXSSFWorkbook(File)}
	 */
	@Test
	public void testReadXSSFWorkbook() throws IOException
	{
		String excelFilePath = "addresses.xlsx";
		final File workbookFile = new File(PathFinder.getSrcTestResourcesDir(), excelFilePath);
		XSSFWorkbook xssfWorkbook = ReadExcelExtensions.readXSSFWorkbook(workbookFile);
		assertNotNull(xssfWorkbook);
	}

	/**
	 * Test method for {@link ReadExcelExtensions#readHSSFWorkbook(File)}
	 */
	@Test
	public void testReadHSSFWorkbook() throws URISyntaxException, IOException
	{
		final String filename = "test.xls";
		final URL url = ClassExtensions.getResource(filename);
		final File excelSheet = new File(url.toURI());
		HSSFWorkbook hssfWorkbook = ReadExcelExtensions.readHSSFWorkbook(excelSheet);
		assertNotNull(hssfWorkbook);
	}

	/**
	 * Test method for {@link ReadExcelExtensions}
	 */
	@Test(expectedExceptions = { BeanTestException.class, ObjectCreationException.class })
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(ReadExcelExtensions.class);
	}

}