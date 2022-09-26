/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */

package com.redxun.common.utils;

import com.redxun.common.tool.Exceptions;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.Charsets;
import org.springframework.lang.Nullable;

import java.io.*;
import java.nio.charset.Charset;

/**
 * IO读写工具类
 *
 * @author L.cm
 */
@UtilityClass
public class IoUtil extends org.springframework.util.StreamUtils {

	/**
	 * closeQuietly
	 *
	 * @param closeable 自动关闭
	 */
	public static void closeQuietly(@Nullable Closeable closeable) {
		if (closeable == null) {
			return;
		}
		if (closeable instanceof Flushable) {
			try {
				((Flushable) closeable).flush();
			} catch (IOException ignored) {
				// ignore
			}
		}
		try {
			closeable.close();
		} catch (IOException ignored) {
			// ignore
		}
	}

	/**
	 * InputStream to String utf-8
	 *
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested String
	 */
	public static String readToString(InputStream input) {
		return readToString(input, Charsets.UTF_8);
	}

	/**
	 * InputStream to String
	 *
	 * @param input   the <code>InputStream</code> to read from
	 * @param charset the <code>Charset</code>
	 * @return the requested String
	 */
	public static String readToString(@Nullable InputStream input, Charset charset) {
		try {
			return IoUtil.copyToString(input, charset);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		} finally {
			IoUtil.closeQuietly(input);
		}
	}

	public static byte[] readToByteArray(@Nullable InputStream input) {
		try {
			return IoUtil.copyToByteArray(input);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		} finally {
			IoUtil.closeQuietly(input);
		}
	}


}
