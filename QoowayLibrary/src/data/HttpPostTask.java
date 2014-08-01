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

public class HttpPostTask extends AsyncTask<String, Void, String> {

	private QoowayActivity activity;
	private Boolean Xml = false;
	private String loginToken = "QoowayMember";
	private DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
	private int responseCode = -1;
	private String resultFromPost ;
	public HttpPostTask(QoowayActivity activity, EnumData.request_mode rm) {
		this.activity = activity;
	}

	public HttpPostTask(QoowayActivity activity, Boolean xml) {
		this.activity = activity;
		this.Xml = true;
	}

	  @Override
	    protected void onPreExecute() {
		  	DataStorageManager.getSingletonInstance().incrementAsyncTask();
		  	 if(DataStorageManager.getSingletonInstance().currentActivity != null)
		  		this.activity =DataStorageManager.getSingletonInstance().currentActivity ;
	    }
		@Override
		protected void onPostExecute(String result) {
			DataStorageManager.getSingletonInstance().decrementAsyncTask();
			displayResponseCodeError(result);

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
		this.setResponseCode(conn.getResponseCode());
		dataStorageManager.lastCode = conn.getResponseCode();
		dataStorageManager.lastMessage = conn.getResponseMessage();
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
				return hv.verify(HttpPostTask.this.getHostName(), session);   // I5WIN2008R2
			}
		};

		// Get an instance of the Bouncy Castle KeyStore format
		KeyStore trusted = KeyStore.getInstance("BKS");
		// Get the raw resource, which contains the keystore with
		// your trusted certificates (root and any intermediate certs)
		InputStream in = activity.getApplicationContext().getResources()
				.openRawResource(	 this.getKeyStore());
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
		JSONObject jsonObjSend = null;

		try {
			jsonObjSend = new JSONObject(urls[1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
		String userName = dataStorageManager.userName;
		String passWord = dataStorageManager.password;
		
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
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setDoOutput(true);
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

		byte[] outputBytes = jsonObjSend.toString().getBytes("UTF-8");
		OutputStream os = conn.getOutputStream();
		os.write(outputBytes);
		conn.connect();
		os.close();
		this.setResponseCode(conn.getResponseCode());
		dataStorageManager.lastCode = conn.getResponseCode();
		dataStorageManager.lastMessage = conn.getResponseMessage();
		resultFromPost = inputStreamToString(conn.getInputStream());
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

	 private void PromptFailureMessage(String input)
	 {
		 if(DataStorageManager.getSingletonInstance().currentActivity != null)
		 if(this.activity!=null && input!=null)
		 {
			 this.activity.promptDialog("Error", input, "Close");
		 }
	 }
	 
	 private void setResponseCode(int code)
	 {
		 this.responseCode = code;
	 }

	 private int getResponseCode()
	 {
		return this.responseCode;
	 }
	 
	 private Boolean displayResponseCodeError(String result)
	 {
		 //Right now result is responseCode not actural return json
		 //Refractor later
		Boolean booleanResult = false;
		if(this.getResponseCode()== 202 ||this.getResponseCode()==500  )
			this.PromptFailureMessage(resultFromPost);
		return booleanResult;
	 }
}