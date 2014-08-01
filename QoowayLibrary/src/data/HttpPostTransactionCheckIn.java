package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.qooway.qoowaylibrary.R;

import data.DataStorageManager;
import framework.QoowayActivity;
import android.os.AsyncTask;



public class HttpPostTransactionCheckIn extends AsyncTask<String, Void, String> {

	private QoowayActivity activity;
	private Boolean Xml = false;
	private String loginToken = "QoowayMember";
	private DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();

	private EnumData.request_mode rm;
	private status stat= status.notLoggedIn;

	public HttpPostTransactionCheckIn(QoowayActivity activity, EnumData.request_mode rm) {
		this.activity = activity;
	
		this.rm = rm;
	}

	public HttpPostTransactionCheckIn(QoowayActivity activity, Boolean xml) {
		this.activity = activity;
		this.Xml = true;
		
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
		if(this.rm == EnumData.request_mode.LogIN )
		{
			if (this.stat== status.loggedIn) {
				dataStorageManager.loggedIn = true;
			} else {
				dataStorageManager.loggedIn = false;
			}
		}
		//activity.cancelProgressDialog();

	}
	
	
	@SuppressWarnings("finally")
	@Override
	protected String doInBackground(String... urls) {
		// params comes from the execute() call: params[0] is the url.
		String resultCode = null;
		InputStream stream = null;
		if(urls[1] != null)
		{
			try {
				stream = sendJson(urls);
				resultCode = inputStreamToString(stream);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultCode;
		}
		
		else   // Delete from database .... may have to changed in future .. logic may be wrong
		{
			try {
				resultCode = sendDelete(urls[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resultCode;
		}
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		
	}
	

	@SuppressWarnings("unchecked")
	private String loadResult(String urlString) throws XmlPullParserException,
			IOException {
		InputStream stream = null;
		String result = null;
		try {
			stream = downloadUrl(urlString);
			result = inputStreamToString(stream);
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (stream != null) {
			stream.close();
		}

		return result;

	}

	private InputStream downloadUrl(String urlString) throws IOException,
			KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, CertificateException {
		DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
		String userName = dataStorageManager.userName;
		String passWord = dataStorageManager.password;
		HttpURLConnection conn = null;
		if (urlString.startsWith("https")) {
			try {
				conn = (HttpURLConnection) httpsConnection(urlString);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			//
		}
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		if (Xml) {
			conn.setRequestProperty("Accept", "text/xml");
		}
		if(dataStorageManager.loggedIn) {
			conn.setRequestProperty(
					"Authorization", dataStorageManager.loginToken);	
		} else {
		conn.setRequestProperty(
				"Authorization", loginToken);
		}	
				
				/*
				"Basic "
						+  Base64.encodeToString(
								((userName + ":" + passWord).getBytes()),
								Base64.NO_WRAP));   */

		// Starts the query
		conn.connect();
		return conn.getInputStream();
	}

	private HttpURLConnection httpsConnection(String urlString)
			throws NoSuchAlgorithmException, KeyManagementException,
			KeyStoreException, IOException, CertificateException {
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				HostnameVerifier hv = HttpsURLConnection
						.getDefaultHostnameVerifier();
				return hv.verify(HttpPostTransactionCheckIn.this.getHostName(), session);   // I5WIN2008R2
			}
		};

		// Get an instance of the Bouncy Castle KeyStore format
		KeyStore trusted = KeyStore.getInstance("BKS");
		// Get the raw resource, which contains the keystore with
		// your trusted certificates (root and any intermediate certs)
		InputStream in = activity.getApplicationContext().getResources()
				.openRawResource(this.getKeyStore());  // took away 2
		try {
			// Initialize the keystore with the provided trusted
			// certificates
			// Also provide the password of the keystore
			trusted.load(in, this.getKeyStorePassWord().toCharArray());
		} finally {
			in.close();
		}
		String algorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
		tmf.init(trusted);

		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, tmf.getTrustManagers(), null);

		URL url = new URL(urlString);
		HttpsURLConnection urlConnection = (HttpsURLConnection) url
				.openConnection();
		urlConnection.setSSLSocketFactory(context.getSocketFactory());
		urlConnection.setHostnameVerifier(hostnameVerifier);
		return urlConnection;
	}

	private String inputStreamToString(InputStream is) throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}

		// Return full string
		return total.toString();
	}

	private InputStream sendJson(String[] urls) throws IOException,
			URISyntaxException {
		JSONObject jsonObjSend = null;

		try {
			jsonObjSend = new JSONObject(urls[1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String userName = "david@qooway.com";
		String passWord = "123456";
		String resultt = jsonObjSend.toString();
		HttpURLConnection conn = null;
		 if (urls[0].startsWith("https")) 
		 {
            try {
                            conn = (HttpURLConnection) httpsConnection(urls[0]);
            } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
            }
		 } 
		 else 
		 {
            URL url = new URL(urls[0]);
            conn = (HttpURLConnection) url.openConnection();
		 }
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestMethod("POST");
		if(dataStorageManager.loggedIn) {
			conn.setRequestProperty(
					"Authorization", dataStorageManager.loginToken);	
		} else {
		conn.setRequestProperty(
				"Authorization", loginToken);
		}	
				
				/*
				"Basic "
						+ Base64.encodeToString(
								((userName + ":" + passWord).getBytes()),
								Base64.NO_WRAP));
								*/

		conn.connect();
		byte[] outputBytes = jsonObjSend.toString().getBytes("UTF-8");
		OutputStream os = conn.getOutputStream();
		os.write(outputBytes);
		os.close();
		
		
		if(conn.getResponseMessage().equals("OK") && this.rm == EnumData.request_mode.LogIN)
		{
			this.stat = status.loggedIn;
		}
		return conn.getInputStream();

		
	}

	
	
	// used for: delete favorites
	private String sendDelete(String urlString) throws IOException,
			URISyntaxException {
		
		String userName = "david@qooway.com";
		String passWord = "123456";
		HttpURLConnection conn = null;
		 if (urlString.startsWith("https")) 
		 {
             try {
                             conn = (HttpURLConnection) httpsConnection(urlString);
             } catch (Exception e) {
                             // TODO Auto-generated catch block
                             e.printStackTrace();
             }
		 } 
		 else 
		 {
             URL url = new URL(urlString);
             conn = (HttpURLConnection) url.openConnection();
		 }
		 conn.setRequestProperty("Accept", "application/json");
		 conn.setRequestMethod("DELETE");
			if(dataStorageManager.loggedIn) {
				conn.setRequestProperty(
						"Authorization", dataStorageManager.loginToken);	
			} else {
			conn.setRequestProperty(
					"Authorization", loginToken);
			}	
		/*
				"Basic "
						+ Base64.encodeToString(
								((userName + ":" + passWord).getBytes()),
								Base64.NO_WRAP));
								*/
		conn.connect();
		return conn.getResponseMessage();
}
	
	private enum status
	{
		loggedIn , notLoggedIn
	}

	 private int getKeyStore()
	 {
		 return DataStorageManager.getSingletonInstance().getApiKeyStore();
	 }

	 private String getHostName()
	 {
		 return DataStorageManager.getSingletonInstance().getApiHostNameVerfier();
	 }
	 
	 private String getKeyStorePassWord()
	 {
		 return DataStorageManager.getSingletonInstance().getKeyStorePassword();
	 }
}

