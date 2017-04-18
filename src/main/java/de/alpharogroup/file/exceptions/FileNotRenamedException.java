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
package de.alpharogroup.file.exceptions;

/**
 * Exception if the file could not be renamed.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public class FileNotRenamedException extends Exception
{

	/**
	 * The default serialVersionUID.
	 */
	private static final long serialVersionUID = 6852760469710242067L;

	/**
	 * Default constructor.
	 */
	public FileNotRenamedException()
	{
		super();
	}

	/**
	 * Constructs a new FileNotRenamedException with the specified detail message.
	 * 
	 * @param message
	 *            The message.
	 */
	public FileNotRenamedException(final String message)
	{
		super(message);
	}

	/**
	 * Constructs a new FileNotRenamedException with the specified detail message and the Throwable.
	 * 
	 * @param message
	 *            The message.
	 * @param cause
	 *            The Throwable.
	 */
	public FileNotRenamedException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Constructs a FileNotRenamedException with the Throwable.
	 * 
	 * @param cause
	 *            The Throwable.
	 */
	public FileNotRenamedException(final Throwable cause)
	{
		super(cause);
	}

}
