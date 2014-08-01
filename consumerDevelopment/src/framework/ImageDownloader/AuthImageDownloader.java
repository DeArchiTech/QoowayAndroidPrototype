package framework.ImageDownloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

import ui.MainScreenActivity;
import android.content.Context;

import com.qooway.consumerv01.R;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import data.DataStorageManager;


public class AuthImageDownloader extends BaseImageDownloader {
    public static final String TAG = AuthImageDownloader.class.getName();
	private String loginToken = "QoowayMember";

  

    public AuthImageDownloader(Context context, int connectTimeout, int readTimeout) {
        super(context, connectTimeout, readTimeout);
    }
    

	public AuthImageDownloader(Context context) {
		super(context);
	}

    
    protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {

        InputStream IS = null;
        
        try {
                      IS =this.downloadUrl(imageUri);
               } catch (KeyManagementException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
               } catch (NoSuchAlgorithmException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
               } catch (KeyStoreException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
               } catch (CertificateException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
               } 
        return IS;

     };
     
     
    @SuppressWarnings("unused")
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
	}
	conn.setReadTimeout(1000000 /* milliseconds */);
	conn.setConnectTimeout(1500000 /* milliseconds */);
	conn.setRequestMethod("GET");
	conn.setDoInput(true);
	conn.setRequestProperty("Content-Type", "Image");
	conn.setRequestProperty("User-Agent", "");
	
	userName = "david@qooway.com";
	passWord = "123456";
	
	if(dataStorageManager.loggedIn) {
		conn.setRequestProperty(
				"Authorization", dataStorageManager.loginToken);	
	} else {
	conn.setRequestProperty(
			"Authorization", loginToken);
	}	
		/*	"Basic "
					+   Base64.encodeToString(
							((userName + ":" + passWord).getBytes()),
							Base64.NO_WRAP)); */
	
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
	               return hv.verify(AuthImageDownloader.this.getHostName(), session);
	        }
	 };
	 
	 //DataStorageManager dataStorageManager = DataStorageManager.getSingletonInstance();
	 // Get an instance of the Bouncy Castle KeyStore format
	 KeyStore trusted = KeyStore.getInstance("BKS");
	 // Get the raw resource, which contains the keystore with
	 // your trusted certificates (root and any intermediate certs)
	 InputStream in = MainScreenActivity.tester.getApplicationContext().getResources()
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
	
	 @SuppressWarnings("unused")
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
