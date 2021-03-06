package com.example.raqib.instadate;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/*
 * Helper class for downloading a file.
 */
public class Downloader {

	//Handler msg that represents we are posting a progress update.
	static final int POST_PROGRESS = 1;

	/************************************************
	 * Download a file from the Internet and store it locally
	 * 
	 * @param URL - the url of the file to download
	 * @param fos - a FileOutputStream to save the downloaded file to.
	 ************************************************/
	public static void DownloadFromUrl(String URL, FileOutputStream fos) {  //this is the downloader method
		try {

			URL url = new URL(URL); //URL of the file
			
			//keep the start time so we can display how long it took to the Log.
			long startTime = System.currentTimeMillis();
			Log.d("NewsContents", "DOWNLOAD STARTED");
			
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();
			
			// this will be useful so that you can show a tipical 0-100% progress bar
            //int lengthOfFile = ucon.getContentLength();

//			Log.i(myTag, "OPENED CONNECTION");
			
			/************************************************
			 * Define InputStreams to read from the URLConnection.
			 ************************************************/
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
//			Log.i(myTag, "GOT INPUT STREAM AND BUFFERED INPUT STREAM");
			
			/************************************************
			 * Define OutputStreams to write to our file.
			 ************************************************/
			
			BufferedOutputStream bos = new BufferedOutputStream(fos);
//			Log.i(myTag, "GOT FILE OUTPUT STREAM AND BUFFERED OUTPUT STREAM");

			/************************************************
			 * Start reading the and writing our file.
			 ************************************************/
			byte data[] = new byte[1024];
            //long total = 0;
            int count;
            //loop and read the current chunk
            while ((count = bis.read(data)) != -1) {            	
            	//keep track of size for progress.
                //total += count;
            	
            	//write this chunk
                bos.write(data, 0, count);
            }
			//Have to call flush or the  file can get corrupted.
			bos.flush();
			bos.close();
			
			Log.d("NewsContents", "DOWNLOAD READY IN"
					+ ((System.currentTimeMillis() - startTime))
					+ " MILLI SECONDS");
		} catch (IOException e) {
			Log.d("NewsContents", "ERROR: " + e);
		}
	}
}