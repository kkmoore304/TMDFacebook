/**
 * 
 */
package com.tellmedoctor.tmdfacebook.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author kmoore
 * 
 */
public class ImageUtils {

	private static final String TAG = ImageUtils.class.getSimpleName();

	/**
	 * 
	 */
	public ImageUtils() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void go(String imageURL, String fileName) {
		DownloadImage task = new DownloadImage();
		task.execute(new String[] { imageURL, fileName });
	}

	public static void DownloadFile(String imageURL, String fileName)
			throws IOException {

		URL url = new URL(imageURL);
		File file = new File(fileName);

		long startTime = System.currentTimeMillis();
		Log.i(TAG, "Begin Download URL: " + url + " Filename: " + fileName);
		URLConnection ucon = url.openConnection();
		ucon.setDoInput(true);
		ucon.connect();
		InputStream is = ucon.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayBuffer baf = new ByteArrayBuffer(5000);
		int current = 0;
		while ((current = bis.read()) != -1)
			baf.append((byte) current);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(baf.toByteArray());
		fos.close();
		Log.i(TAG, "File was downloaded in: "
				+ ((System.currentTimeMillis() - startTime) / 1000) + "s");

	}

	public class DownloadImage extends AsyncTask<String, Integer, Drawable> {

		@Override
		protected Drawable doInBackground(String... params) {

			try {
				DownloadFile(params[0], params[1]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// This is done in a background thread
			return null;
		}

	}

	/**
	 * convert Bitmap to byte array
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap b) {
		if (b == null) {
			return null;
		}

		ByteArrayOutputStream o = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 100, o);
		return o.toByteArray();
	}

	/**
	 * convert byte array to Bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] b) {
		return (b == null || b.length == 0) ? null : BitmapFactory
				.decodeByteArray(b, 0, b.length);
	}

	/**
	 * convert Drawable to Bitmap
	 * 
	 * @param d
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable d) {
		return d == null ? null : ((BitmapDrawable) d).getBitmap();
	}

	/**
	 * convert Bitmap to Drawable
	 * 
	 * @param b
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap b) {
		return b == null ? null : new BitmapDrawable(b);
	}

	/**
	 * convert Drawable to byte array
	 * 
	 * @param d
	 * @return
	 */
	public static byte[] drawableToByte(Drawable d) {
		return bitmapToByte(drawableToBitmap(d));
	}

	/**
	 * convert byte array to Drawable
	 * 
	 * @param b
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] b) {
		return bitmapToDrawable(byteToBitmap(b));
	}

	/**
	 * get input stream from network by imageurl, you need to close inputStream
	 * yourself
	 * 
	 * @param imageUrl
	 * @param readTimeOutMillis
	 * @return
	 */
	public static InputStream getInputStreamFromUrl(String imageUrl,
			int readTimeOutMillis) {
		return getInputStreamFromUrl(imageUrl, readTimeOutMillis, null);
	}

	/**
	 * get input stream from network by imageurl, you need to close inputStream
	 * yourself
	 * 
	 * @param imageUrl
	 * @param readTimeOutMillis
	 *            read time out, if less than 0, not set, in mills
	 * @param requestProperties
	 *            http request properties
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static InputStream getInputStreamFromUrl(String imageUrl,
			int readTimeOutMillis, Map<String, String> requestProperties) {
		InputStream stream = null;
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			// HttpUtils.setURLConnection(requestProperties, con);
			if (readTimeOutMillis > 0) {
				con.setReadTimeout(readTimeOutMillis);
			}
			stream = con.getInputStream();
		} catch (MalformedURLException e) {
			closeInputStream(stream);
			throw new RuntimeException("MalformedURLException occurred. ", e);
		} catch (IOException e) {
			closeInputStream(stream);
			throw new RuntimeException("IOException occurred. ", e);
		}
		return stream;
	}

	/**
	 * get drawable by imageUrl
	 * 
	 * @param imageUrl
	 * @param readTimeOutMillis
	 * @return
	 */
	public static Drawable getDrawableFromUrl(String imageUrl,
			int readTimeOutMillis) {
		return getDrawableFromUrl(imageUrl, readTimeOutMillis, null);
	}

	/**
	 * get drawable by imageUrl
	 * 
	 * @param imageUrl
	 * @param readTimeOutMillis
	 *            read time out, if less than 0, not set, in mills
	 * @param requestProperties
	 *            http request properties
	 * @return
	 */
	public static Drawable getDrawableFromUrl(String imageUrl,
			int readTimeOutMillis, Map<String, String> requestProperties) {
		InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOutMillis,
				requestProperties);
		Drawable d = Drawable.createFromStream(stream, "src");
		closeInputStream(stream);
		return d;
	}

	/**
	 * get Bitmap by imageUrl
	 * 
	 * @param imageUrl
	 * @param readTimeOut
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut) {
		return getBitmapFromUrl(imageUrl, readTimeOut, null);
	}

	/**
	 * get Bitmap by imageUrl
	 * 
	 * @param imageUrl
	 * @param requestProperties
	 *            http request properties
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String imageUrl, int readTimeOut,
			Map<String, String> requestProperties) {
		InputStream stream = getInputStreamFromUrl(imageUrl, readTimeOut,
				requestProperties);
		Bitmap b = BitmapFactory.decodeStream(stream);
		closeInputStream(stream);
		return b;
	}

	/**
	 * scale image
	 * 
	 * @param org
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
		return scaleImage(org, (float) newWidth / org.getWidth(),
				(float) newHeight / org.getHeight());
	}

	/**
	 * scale image
	 * 
	 * @param org
	 * @param scaleWidth
	 *            sacle of width
	 * @param scaleHeight
	 *            scale of height
	 * @return
	 */
	public static Bitmap scaleImage(Bitmap org, float scaleWidth,
			float scaleHeight) {
		if (org == null) {
			return null;
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(),
				matrix, true);
	}

	/**
	 * close inputStream
	 * 
	 * @param s
	 */
	private static void closeInputStream(InputStream s) {
		if (s == null) {
			return;
		}

		try {
			s.close();
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		}
	}

	/***
	 * Attach an image to the View passed in
	 * 
	 * @param _context
	 * @param uri
	 * @param authuser
	 */
	public static void setImageView(Context _context, String uri,
			ImageView authuser) {
		int width = 0;
		int height = 0;
		if (width == 0) {
			width = 240;
			height = 222;
		} else {
			width = authuser.getWidth();
			height = authuser.getHeight();
		}
		BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
		factoryOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(uri, factoryOptions);

		int imageWitdth = factoryOptions.outWidth;
		int imageHeight = factoryOptions.outHeight;
		int scalefactor = Math.min(imageWitdth / width, imageHeight / height);

		// Decode the image file into a Bitmap sized to fill the View
		factoryOptions.inJustDecodeBounds = false;
		factoryOptions.inSampleSize = scalefactor;
		factoryOptions.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(uri, factoryOptions);

		if (bitmap == null)
			return;

		//PrefUtils.setProfileImage(_context, uri);
		//PrefUtils.setProfileImageFromGallery(_context, null);
		authuser.setImageBitmap(bitmap);

	}

	/***
	 * Attach an image to the view pass in from the gallery
	 * 
	 * @param _context
	 * @param profileImageFromGallery
	 * @param authuser
	 */
	public static void setImageViewFromGallery(Context _context,
			String profileImageFromGallery, ImageView authuser)
			throws IOException {

		try {
			if (Build.VERSION.SDK_INT < 19) {
				Bitmap bitmap = BitmapFactory
						.decodeFile(profileImageFromGallery);
				authuser.setImageBitmap(bitmap);

			} else {

				Uri c = Uri.parse(profileImageFromGallery);
				ParcelFileDescriptor parcelFileDescriptor = _context
						.getContentResolver().openFileDescriptor(c, "r");
				FileDescriptor fileDescriptor = parcelFileDescriptor
						.getFileDescriptor();
				Bitmap image = BitmapFactory
						.decodeFileDescriptor(fileDescriptor);
				parcelFileDescriptor.close();
				authuser.setImageBitmap(image);

			}
		} catch (Exception ex) {
			Log.d(TAG, ex.getMessage());
		}

	}

	/***
	 * 
	 * @param inContext
	 * @param inImage
	 * @return
	 */
	public static Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	/***
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getRealPathFromURI(Context context, Uri uri,
			String data) {

		String[] largeFileProjection = { Images.ImageColumns._ID,
				Images.ImageColumns.DATA };

		String largeFileSort = Images.ImageColumns._ID + " DESC";
		Cursor myCursor = context.getContentResolver().query(
				Images.Media.EXTERNAL_CONTENT_URI,
				largeFileProjection, null, null, largeFileSort);
		String largeImagePath = "";
		try {
			myCursor.moveToFirst();
			largeImagePath = myCursor
					.getString(myCursor
							.getColumnIndexOrThrow(Images.ImageColumns.DATA));
		} finally {
			myCursor.close();
		}

		return largeImagePath;
	}

	public static String random(int MAX_LENGTH) {
		Random generator = new Random();
		StringBuilder randomStringBuilder = new StringBuilder();
		;
		int randomLength = generator.nextInt(MAX_LENGTH);
		char tempChar;
		for (int i = 0; i < randomLength; i++) {
			tempChar = (char) (generator.nextInt(96) + 32);
			randomStringBuilder.append(tempChar);
		}
		return randomStringBuilder.toString();
	}

	/***
	 * Get MAP static image based on Lat Long and for a specific size with X
	 * marking the spot
	 * 
	 * @param lati
	 * @param longi
	 * @param w
	 * @param h
	 * @param X
	 * @return
	 */
	public static Bitmap getGoogleMapThumbnail(double lati, double longi,
			int w, int h, String X, int zoom) {
		String URL = "http://maps.google.com/maps/api/staticmap?center=" + lati
				+ "," + longi + "&zoom=" + zoom + "&size=" + w + "x" + h
				+ "&sensor=false&markers=color:blue%7Clabel:" + X + "%7C"
				+ lati + "," + longi + "";
		Log.d(TAG, URL);
		Bitmap bmp = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet(URL);

		InputStream in = null;
		try {
			in = httpclient.execute(request).getEntity().getContent();
			bmp = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bmp;
	}

	/***
	 * get the Screen size
	 * 
	 * @param context
	 * @return
	 */
	public static String GeScreenSize(Context context) {
		int screenSize = context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;

		String toastMsg;
		switch (screenSize) {
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			toastMsg = "Large screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			toastMsg = "Normal screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			toastMsg = "Small screen";
			break;
		default:
			toastMsg = "Screen size is neither large, normal or small";
		}

		return toastMsg;
	}

	/***
	 * Save the image from streem
	 * 
	 * @return
	 */
	public static String storeImage(Context context, Bitmap image) {
		File pictureFile = getOutputMediaFile(context);
		if (pictureFile == null) {
			Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
			return null;
		}
		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			image.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Error accessing file: " + e.getMessage());
		}

		return pictureFile.getAbsolutePath();
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(Context context) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory() + "/Android/data/"
						+ context.getPackageName() + "/Files");

		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm")
				.format(new Date());
		File mediaFile;
		String mImageName = "MI_" + timeStamp + ".jpg";
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ mImageName);
		return mediaFile;
	}

	/***
	 * Less Resolution
	 * @param filePath
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap lessResolution(String filePath, int width, int height) {
		int reqHeight = width;
		int reqWidth = height;
		BitmapFactory.Options options = new BitmapFactory.Options();

		// First decode with inJustDecodeBounds=true to check dimensions
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/***
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
}
