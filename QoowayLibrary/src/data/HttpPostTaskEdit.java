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

import framework.QoowayActivity;
import android.os.AsyncTask;

public class HttpPostTaskEdit extends AsyncTask<String, Void, String> {

	private QoowayActivity activity;
	private Boolean Xml = false;
	private String loginToken = "QoowayMember";
	private DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();

	public HttpPostTaskEdit(QoowayActivity activity, EnumData.request_mode rm) {
		this.activity = activity;
	}

	public HttpPostTaskEdit(QoowayActivity activity, Boolean xml) {
		this.activity = activity;
		this.Xml = true;
	}

	  @Override
	    protected void onPreExecute() {
		  	DataStorageManager.getSingletonInstance().incrementAsyncTask();
	    }
		@Override
		protected void onPostExecute(String result) {
			DataStorageManager.getSingletonInstance().decrementAsyncTask();
		}
	
	@SuppressWarnings("finally")
	@Override
	protected String doInBackground(String... urls) {
		{
			// params comes from the execute() call: params[0] is the url.
			String resultCode = null;
			try {
				
				if(urls.length>1 && urls[1]!= null)
					resultCode = sendJson(urls);
				else
					downloadUrl(urls[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultCode;
		}
	}
/*
	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		activity.cancelProgressDialog();

	}
	*/

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
		
		userName = "david@qooway.com";
		passWord = "123456";
		
		
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
		}
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("POST");
		conn.setDoInput(true);
		conn.setDoOutput(true);
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
	/*	conn.setRequestProperty(
				"Authorization",
		conn.setRequestProperty(
				"Authorization", "Go To World 8");
				
				/*
				"Basic "
						+ Base64.encodeToString(
								((userName + ":" + passWord).getBytes()),
								Base64.NO_WRAP));*/

		// Starts the query
		conn.connect();
		
		int code = conn.getResponseCode();
		String message = conn.getResponseMessage();
		dataStorageManager.lastCode = code;
		if(!message.isEmpty()){
			dataStorageManager.lastMessage = message;
		}

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
				return hv.verify(HttpPostTaskEdit.this.getHostName(), session);   // I5WIN2008R2
			}
		};

		// Get an instance of the Bouncy Castle KeyStore format
		KeyStore trusted = KeyStore.getInstance("BKS");
		// Get the raw resource, which contains the keystore with
		// your trusted certificates (root and any intermediate certs)
		InputStream in = activity.getApplicationContext().getResources()
				.openRawResource(this.getKeyStore());
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

	private String sendJson(String[] urls) throws IOException,
			URISyntaxException {
		/*
		JSONObject jsonObjSend = null;

		try {
			jsonObjSend = new JSONObject(urls[1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
		String userName = dataStorageManager.userName;
		String passWord = dataStorageManager.password;
		
		String resultt = urls[1];
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
		conn.setRequestProperty("Accept", "application/JSON");
		conn.setRequestProperty("Content-Type", "application/JSON");
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
		
		urls[1] = "\"" + urls[1] + "\"";
		
		byte[] outputBytes = urls[1].getBytes("UTF-8");
		OutputStream os = conn.getOutputStream();
		os.write(outputBytes);
		os.close();

		int code = conn.getResponseCode();
		String message = conn.getResponseMessage();
		dataStorageManager.lastCode = code;
		if(!message.isEmpty()){
			dataStorageManager.lastMessage = message;
		}		
		return conn.getResponseMessage();	
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
		 conn.setRequestProperty(
				"Authorization", loginToken);
		/*
				"Basic "
						+ Base64.encodeToString(
								((userName + ":" + passWord).getBytes()),
								Base64.NO_WRAP));
								*/
		conn.connect();
			
		return conn.getResponseMessage();
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
