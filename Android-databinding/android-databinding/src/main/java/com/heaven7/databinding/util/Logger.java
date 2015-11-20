/*
 * Copyright (C) 2015 
 *            heaven7(donshine723@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.heaven7.databinding.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * {@link Logger#v(String, String, String)} i like
 * 
 * @author heaven7
 */
public class Logger {

	private static final String DEFAULT_LOG_PATH = android.os.Environment
			.getExternalStorageDirectory().getPath() + "/heaven7/log.txt";

	private static int sLOG_LEVEL = 6;// default all

	public static final int VERBOSE = 5; /* the lowest */
	public static final int DEBUG = 4;
	public static final int INFO = 3;
	public static final int WARNING = 2;
	public static final int ERROR = 1;
	/**default true(only called internal)*/
	public static boolean IsDebug = true;

	/** enable or disable debug */
	public static void setDebug(boolean debug) {
		IsDebug = debug;
		if (debug)
			setLevel(VERBOSE);
		else
			setLevel(INFO);
	}

	/**
	 */
	public static void setLevel(int lowestLevel) {
		switch (lowestLevel) {
		case VERBOSE:
			sLOG_LEVEL = 6;
			break;

		case DEBUG:
			sLOG_LEVEL = 5;
			break;

		case INFO:
			sLOG_LEVEL = 4;

			break;

		case WARNING:
		case ERROR:
			sLOG_LEVEL = 3;
			break;

		default:
			throw new IllegalArgumentException("caused by log lowestLevel="
					+ lowestLevel);
		}
	}

	// verbose
	public static void v(String tag, String msg) {
		if (sLOG_LEVEL > VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void v(String tag, String methodTag, String msg) {
		if (sLOG_LEVEL > VERBOSE) {
			Log.v(tag, "called [ " + methodTag + "() ]: " + msg);
		}
	}

	// debug
	public static void d(String tag, String msg) {
		if (sLOG_LEVEL > DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, String methodTag, String msg) {
		if (sLOG_LEVEL > DEBUG) {
			Log.d(tag, "called [ " + methodTag + "() ]: " + msg);
		}
	}

	// info
	public static void i(String tag, String msg) {
		if (sLOG_LEVEL > INFO) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String methodTag, String msg) {
		if (sLOG_LEVEL > INFO) {
			Log.i(tag, "called [ " + methodTag + "() ]: " + msg);
		}
	}

	// warning
	public static void w(String tag, String msg) {
		if (sLOG_LEVEL > WARNING) {
			Log.w(tag, msg);
		}
	}

	public static void w(String tag, String methodTag, String msg) {
		if (sLOG_LEVEL > WARNING) {
			Log.w(tag, "called [ " + methodTag + "() ]: " + msg);
		}
	}

	public static void w(String tag, Throwable throwable) {
		if (sLOG_LEVEL > WARNING) {
			Log.w(tag, throwable);
		}
	}

	// error
	public static void e(String tag, String msg) {
		if (sLOG_LEVEL > ERROR) {
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, String methodTag, String msg) {
		if (sLOG_LEVEL > ERROR) {
			Log.e(tag, "called [ " + methodTag + "() ]: " + msg);
		}
	}

	// =========== write sd =====================//
	public static void write2SD(String tag, String message, String filename,
			boolean append) {
		BufferedWriter bw = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("[ Time=" + FORMATER.format(new Date()) + " ] ：tag="
					+ tag + " ,message=" + message + " \r\n");
			bw = new BufferedWriter(new FileWriter(
					createFileIfNeed(filename), append));// append
			bw.append(sb.toString());
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
				}
		}
	}
	public static File createFileIfNeed(String filePath){
		File file=null;
		try {
			file = new File(filePath);
			File p = file.getParentFile();
			if(p!=null && !p.exists()){
				p.mkdirs();
			}
			if(!file.exists()){//need permission : mount_unmount_filesystem
				//permission.MOUNT_UNMOUNT_FILESYSTEMS
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	

	/** use default append */
	public static void write2SD(String tag, String message, String filename) {
		write2SD(tag, message, filename, true);
	}

	public static void write2SD(String tag, String message, boolean append) {
		write2SD(tag, message, DEFAULT_LOG_PATH, append);
	}

	/** use default file and append */
	public static void write2SD(String tag, String message) {
		write2SD(tag, message, DEFAULT_LOG_PATH, true);
	}

	public static void write2SD(String tag, Throwable t, String filename,
			boolean append) {
		write2SD(tag, toString(t), filename, append);
	}

	public static void write2SD(String tag, Throwable t, boolean append) {
		write2SD(tag, toString(t), DEFAULT_LOG_PATH, append);
	}

	/** use default append */
	public static void write2SD(String tag, Throwable t, String filename) {
		write2SD(tag, toString(t), filename, true);
	}

	/** use default file and append */
	public static void write2SD(String tag, Throwable t) {
		write2SD(tag, toString(t), DEFAULT_LOG_PATH, true);
	}

	public static String toString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		t.printStackTrace(pw);
		Throwable cause = t.getCause();
		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.flush();
		String data = sw.toString();
		pw.close();
		return data;
	}
	
	public static void deleteDefaultLogFile(){
		deleteLogFile(DEFAULT_LOG_PATH);
	}
	public static void deleteLogFile(String path){
		File file = new File(path);
		if(file.exists() && !file.delete()){
			Logger.w(null, "cannot delete log file!");
		}
	}
	public static String getDefaultLogInfo(){
		return getLogInfo(DEFAULT_LOG_PATH);
	}
	public static String getLogInfo(String path){
		File file = new File(path);
		if(file.exists()){
			BufferedReader br =null;
			try {
				StringBuilder sb = new StringBuilder();
				br = new BufferedReader(new FileReader(file));
				String line = null;
				while((line = br.readLine())!=null){
					sb.append(line);
				}
				return sb.toString();
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {
				return null;
			}finally{
				if(br!=null)
					try {
						br.close();
					} catch (IOException e) {
					}
			}
		}
		return null;
	}
	
	public static boolean isDefaultLogExist(){
		return new File(DEFAULT_LOG_PATH).exists();
	}
	

	public static final SimpleDateFormat FORMATER = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
}
