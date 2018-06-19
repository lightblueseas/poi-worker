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
package de.alpharogroup.file.zip;

import java.io.IOException;
import java.io.InputStream;

// from 'http://blog.alutam.com/2009/10/31/reading-password-protected-zip-files-in-java/'
/**
 * The class ZipDecryptInputStream is from the blog from Martin's Weekend Coding from
 * 'http://blog.alutam.com/2009/10/31/reading-password-protected-zip-files-in-java/'.
 * 
 * @deprecated use instead
 *             {@code Zip4jExtensions#extract(net.lingala.zip4j.core.ZipFile, java.io.File, String)}.
 *             <br>
 * 			<br>
 *             Note: will be removed in the next minor version.
 */
@Deprecated
public class ZipDecryptInputStream extends InputStream
{

	/** The Constant CRC_TABLE. */
	private static final int[] CRC_TABLE = new int[256];

	// compute the table
	// (could also have it pre-computed - see http://snippets.dzone.com/tag/crc32)
	static
	{
		for (int i = 0; i < 256; i++)
		{
			int r = i;
			for (int j = 0; j < 8; j++)
			{
				if ((r & 1) == 1)
				{
					r = r >>> 1 ^ 0xedb88320;
				}
				else
				{
					r >>>= 1;
				}
			}
			CRC_TABLE[i] = r;
		}
	}

	/** The Constant DECRYPT_HEADER_SIZE. */
	private static final int DECRYPT_HEADER_SIZE = 12;

	/** The Constant LFH_SIGNATURE. */
	private static final int[] LFH_SIGNATURE = { 0x50, 0x4b, 0x03, 0x04 };

	/** The delegate. */
	private final InputStream delegate;

	/** The password. */
	private final String password;

	/** The keys. */
	private final int keys[] = new int[3];

	/** The state. */
	private ZipState state = ZipState.SIGNATURE;

	/** The skip bytes. */
	private int skipBytes;

	/** The compressed size. */
	private int compressedSize;

	/** The value. */
	private int value;

	/** The value pos. */
	private int valuePos;

	/** The value inc. */
	private int valueInc;

	/**
	 * Instantiates a new zip decrypt input stream.
	 *
	 * @param stream
	 *            the stream
	 * @param password
	 *            the password
	 */
	public ZipDecryptInputStream(final InputStream stream, final String password)
	{
		this.delegate = stream;
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#close()
	 */
	@Override
	public void close() throws IOException
	{
		delegate.close();
		super.close();
	}

	/**
	 * Crc32.
	 *
	 * @param oldCrc
	 *            the old crc
	 * @param charAt
	 *            the char at
	 * @return the int
	 */
	private int crc32(final int oldCrc, final byte charAt)
	{
		return oldCrc >>> 8 ^ CRC_TABLE[(oldCrc ^ charAt) & 0xff];
	}

	/**
	 * Decrypt byte.
	 *
	 * @return the byte
	 */
	private byte decryptByte()
	{
		final int temp = keys[2] | 2;
		return (byte)(temp * (temp ^ 1) >>> 8);
	}

	/**
	 * Inits the keys.
	 *
	 * @param password
	 *            the password
	 */
	private void initKeys(final String password)
	{
		keys[0] = 305419896;
		keys[1] = 591751049;
		keys[2] = 878082192;
		for (int i = 0; i < password.length(); i++)
		{
			updateKeys((byte)(password.charAt(i) & 0xff));
		}
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		int result = delegate.read(b, off, len);
		return readlocal(result);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException
	{
		int result = delegate.read();
		return readlocal(result);
	}

	private int readlocal(int result) throws IOException
	{
		if (skipBytes == 0)
		{
			switch (state)
			{
				case SIGNATURE :
					if (result != LFH_SIGNATURE[valuePos])
					{
						state = ZipState.TAIL;
					}
					else
					{
						valuePos++;
						if (valuePos >= LFH_SIGNATURE.length)
						{
							skipBytes = 2;
							state = ZipState.FLAGS;
						}
					}
					break;
				case FLAGS :
					if ((result & 1) == 0)
					{
						throw new IllegalStateException("ZIP not password protected.");
					}
					if ((result & 64) == 64)
					{
						throw new IllegalStateException("Strong encryption used.");
					}
					if ((result & 8) == 8)
					{
						throw new IllegalStateException("Unsupported ZIP format.");
					}
					result -= 1;
					compressedSize = 0;
					valuePos = 0;
					valueInc = DECRYPT_HEADER_SIZE;
					state = ZipState.COMPRESSED_SIZE;
					skipBytes = 11;
					break;
				case COMPRESSED_SIZE :
					compressedSize += result << 8 * valuePos;
					result -= valueInc;
					if (result < 0)
					{
						valueInc = 1;
						result += 256;
					}
					else
					{
						valueInc = 0;
					}
					valuePos++;
					if (valuePos > 3)
					{
						valuePos = 0;
						value = 0;
						state = ZipState.FN_LENGTH;
						skipBytes = 4;
					}
					break;
				case FN_LENGTH :
				case EF_LENGTH :
					value += result << 8 * valuePos;
					if (valuePos == 1)
					{
						valuePos = 0;
						if (state == ZipState.FN_LENGTH)
						{
							state = ZipState.EF_LENGTH;
						}
						else
						{
							state = ZipState.HEADER;
							skipBytes = value;
						}
					}
					else
					{
						valuePos = 1;
					}
					break;
				case HEADER :
					initKeys(password);
					for (int i = 0; i < DECRYPT_HEADER_SIZE; i++)
					{
						updateKeys((byte)(result ^ decryptByte()));
						result = delegate.read();
					}
					compressedSize -= DECRYPT_HEADER_SIZE;
					state = ZipState.DATA;
					// intentionally no break
				case DATA :
					result = (result ^ decryptByte()) & 0xff;
					updateKeys((byte)result);
					compressedSize--;
					if (compressedSize == 0)
					{
						valuePos = 0;
						state = ZipState.SIGNATURE;
					}
					break;
				case TAIL :
					// do nothing
			}
		}
		else
		{
			skipBytes--;
		}
		return result;
	}

	/**
	 * Update keys.
	 *
	 * @param charAt
	 *            the char at
	 */
	private void updateKeys(final byte charAt)
	{
		keys[0] = crc32(keys[0], charAt);
		keys[1] += keys[0] & 0xff;
		keys[1] = keys[1] * 134775813 + 1;
		keys[2] = crc32(keys[2], (byte)(keys[1] >> 24));
	}
}
