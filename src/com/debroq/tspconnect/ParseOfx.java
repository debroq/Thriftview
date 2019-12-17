package com.debroq.tspconnect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
//import android.provider.DocumentsContract.Document;
//import android.sax.Element;
import android.util.Log;

public class ParseOfx {
	private final static String TAG = "ParseOfx";
	String loginResult = "System Error";
	Document doc;
	DocumentBuilder dBuilder;
	static Fund L2050, L2040, L2030, L2020, LIncome, GFund, FFund, CFund, SFund, IFund;
	static Fund L2050T, L2040T, L2030T, L2020T, LIncomeT, GFundT, FFundT, CFundT, SFundT, IFundT;
	// Initialize all of these in case they are not read in
	static String ytdTrad="0", ytdAAuto="0", ytdAMatch="0", ytdTradCatchup="0", ytdTaxExempt="0", ytdRoth="0", ytdRothCatchup="0";
	Context context;
	String SALT_PW1 = "733hC617Hs";
	String acctnbr, passwd;
    SimpleDateFormat longDateFormat = 
            new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat shortDateFormat = 
            new SimpleDateFormat("yyyyMMdd");
    static String balanceDate;
    static boolean queryComplete=false;
    Date asOfDate;
    static boolean memoContrib=false;
    static boolean memoBalance=false;
    static ParseOfx poxInstance;


    public static ParseOfx getInstance() {
    	return poxInstance;
    }

    public void setNewDate(Date asOf) {
    	Log.i(TAG, "setNewDate:" + asOf);
    	this.asOfDate = asOf;
		String newdate = shortDateFormat.format(asOfDate);
    }

    public ParseOfx(Context context, String nbr, String pwd, Date asOf) {
		this.context = context;
		this.acctnbr = nbr;
		this.passwd = pwd;
		this.asOfDate = asOf;
		poxInstance = this;
	}

	@SuppressLint("DefaultLocale")
	public Fund getTrans(String nam) {
		if (nam.contains("2050")) {
			L2050T = new Fund();
			L2050T.setName("L 2050");
			return L2050T;
		}
		else if (nam.contains("2040")) {
			L2040T = new Fund();
			L2040T.setName("L 2040");
			return L2040T;
		}
		else if (nam.contains("2030")) {
			L2030T = new Fund();
			L2030T.setName("L 2030");
			return L2030T;
		}
		else if (nam.contains("2020")) {
			L2020T = new Fund();
			L2020T.setName("L 2020");
			return L2020T;
		}
		else if (nam.toUpperCase().contains("INCOME")) {
			LIncomeT = new Fund();
			LIncomeT.setName("L Income");
			return LIncomeT;
		}
		else if (nam.toUpperCase().contains("G FUND")) {
			GFundT = new Fund();
			GFundT.setName("G Fund");
			return GFundT;
		}
		else if (nam.toUpperCase().contains("F FUND")) {
			FFundT = new Fund();
			FFundT.setName("F Fund");
			return FFundT;
		}
		else if (nam.toUpperCase().contains("C FUND")) {
			CFundT = new Fund();
			CFundT.setName("C Fund");
			return CFundT;
		}
		else if (nam.toUpperCase().contains("S FUND")) {
			SFundT = new Fund();
			SFundT.setName("S Fund");
			return SFundT;
		}
		else if (nam.toUpperCase().contains("I FUND")) {
			IFundT = new Fund();
			IFundT.setName("I Fund");
			return IFundT;
		}
		else
			return null;
	}

	@SuppressLint("DefaultLocale")
	public Fund getFund(String nam) {
		if (nam.contains("2050")) {
			L2050 = new Fund();
			L2050.setName("L 2050");
			return L2050;
		}
		else if (nam.contains("2040")) {
			L2040 = new Fund();
			L2040.setName("L 2040");
			return L2040;
		}
		else if (nam.contains("2030")) {
			L2030 = new Fund();
			L2030.setName("L 2030");
			return L2030;
		}
		else if (nam.contains("2020")) {
			L2020 = new Fund();
			L2020.setName("L 2020");
			return L2020;
		}
		else if (nam.toUpperCase().contains("INCOME")) {
			LIncome = new Fund();
			LIncome.setName("L Income");
			return LIncome;
		}
		else if (nam.toUpperCase().contains("G FUND")) {
			GFund = new Fund();
			GFund.setName("G Fund");
			return GFund;
		}
		else if (nam.toUpperCase().contains("F FUND")) {
			FFund = new Fund();
			FFund.setName("F Fund");
			return FFund;
		}
		else if (nam.toUpperCase().contains("C FUND")) {
			CFund = new Fund();
			CFund.setName("C Fund");
			return CFund;
		}
		else if (nam.toUpperCase().contains("S FUND")) {
			SFund = new Fund();
			SFund.setName("S Fund");
			return SFund;
		}
		else if (nam.toUpperCase().contains("I FUND")) {
			IFund = new Fund();
			IFund.setName("I Fund");
			return IFund;
		}
		else
			return null;
	}

	public void ParseOfxSample() {
		try {
			String sample = readEncryptedFile("out.enc");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			// org.w3c.dom.Document doc = dBuilder.parse(input);
			InputStream is = new ByteArrayInputStream(sample.getBytes("UTF-8"));
			doc = dBuilder.parse(is);
//			doc = dBuilder.parse(context.getAssets().open("SampleOfxResponse.xml"));
			ParseElements();
		} catch (Exception e) {
			loginResult = "ERROR";
		}
	}

	public void parseOfxFromUrl() {
		downloadFromURL();
//		new downloadFromURL().execute("");
		if (queryComplete==true)
			ParseElements();
		else
			loginResult="Error";
	}

//	private class downloadFromURL extends AsyncTask<String, Void, String> {
//
//		@Override
//		protected String doInBackground(String... params) {
	public void downloadFromURL() {
	try {
				queryComplete=false;
//				BufferedReader in = new BufferedReader(new InputStreamReader(context.getAssets().open("in.ofx")));
//				StringBuffer strbuf = new StringBuffer();
//				String inline;
//				while ((inline = in.readLine()) != null) {
//					strbuf.append(inline);
//				}
//				String input = strbuf.toString();
				
				String input = readEncryptedFile("in.enc");
				
				input = input.replace("NNNN", acctnbr); // login
				input = input.replace("PPPP", passwd); // password
				String longdate = longDateFormat.format(asOfDate);
				input = input.replace("DDDD", longdate);
				String shortdate = shortDateFormat.format(asOfDate);
				input = input.replace("EEEE", shortdate);
//				in.close();
				Log.i(TAG, input);

				CookieHandler.setDefault(new CookieManager());

				Log.i(TAG, "Opening:" + context.getString(R.string.tsp_url));

				URL url = new URL(context.getString(R.string.tsp_url));
				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
				SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				connection.setSSLSocketFactory(sslsocketfactory);
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches (false);
				connection.setDefaultUseCaches (false);
				connection.setRequestProperty("content-type", "application/x-ofx");
				connection.setRequestProperty("Accept", "text/xml");
				connection.setRequestProperty("Expect", "100-continue");

				// connection.setReadTimeout(15*1000);
				Log.i(TAG, "Connecting");
				connection.connect();
				Log.i(TAG, "After Connecting");

				Log.i(TAG, "Opening OutputStreamWriter to send input");
				Log.i(TAG, "******input.length=" + input.length() + "*******");
				OutputStreamWriter oStream = new OutputStreamWriter(connection.getOutputStream());
				BufferedWriter inStream = new BufferedWriter(oStream);
				inStream.write(input);
				inStream.flush();
				inStream.close();

				Log.i(TAG, "Opening BufferedReader to read output");
				BufferedReader outStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuffer inputBuffer = new StringBuffer();
				String inputLine;
				while ((inputLine = outStream.readLine()) != null) {
					Log.i(TAG, ">" + inputLine + "<");
					inputBuffer.append(inputLine);
				}
				outStream.close();
				connection.disconnect();
				String response = inputBuffer.toString();
				Log.i(TAG, "******response.length=" + response.length() + "*******");
				Log.i(TAG, "===========" + response + "===========");

				InputStream is = new ByteArrayInputStream(response.getBytes());
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				dBuilder = dbFactory.newDocumentBuilder();
				doc = dBuilder.parse(is);
				ParseElements();
				// } catch (MalformedURLException e) {
			} catch (IOException e) {
				Log.i(TAG, "IOException:" + e.getMessage());
			} catch (SAXException e) {
				Log.i(TAG, "SAXException:" + e.getMessage());
			} catch (ParserConfigurationException e) {
				Log.i(TAG, "ParserConfigurationException:" + e.getMessage());
			} catch (Exception e) {
				Log.i(TAG, "Exception:" + e.getMessage());
				e.printStackTrace();
			}
			queryComplete=true;
//			return null;
		}

//		@Override
//		protected void onPostExecute(String result) {
//			ParseElements();
//		}
//	}
	
	public void ParseElements() {
		Log.i(TAG, "parseElements");
		Element element = doc.getDocumentElement();
		element.normalize();

		NodeList nList = doc.getElementsByTagName("STATUS");

		for (int i = 0; i < nList.getLength(); i++) {

			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element2 = (Element) node;
				loginResult = getValue("MESSAGE", element2);
				Log.i(TAG, "loginResult from getValue=" + loginResult);
				if (!loginResult.equalsIgnoreCase("SUCCESS"))
					return;
			}
		} // end of for loop


		// Get Ytd Contributions
		nList = doc.getElementsByTagName("INVSTMTTRNRS");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element2 = (Element) node;
				try {
					String memo = getValue("MEMO", element2);
					Log.i(TAG, "MEMO=" + memo);
					memoContrib=true;
					parseContrib(memo);
				} catch(Exception e) {
					Log.i(TAG, "MEMO not found for ytd contributions");
					memoContrib=false;
				}
			}
		}
				
				
		// Get current transactions
		nList = doc.getElementsByTagName("INVBUY");
		Log.i(TAG, "Elements in INVTRAN:" + nList.getLength());
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element2 = (Element) node;
				String secid = getValue("UNIQUEID", element2);
				Log.i(TAG, "UNIQUEID=" + secid);
				Fund trans = getTrans(secid);
				trans.setShares(getValue("UNITS", element2));
				trans.setSharePrice(getValue("UNITPRICE", element2));
				trans.setBalance(getValue("TOTAL", element2));
			}
		}


		// Get current positions
		nList = doc.getElementsByTagName("INVPOS");
		Log.i(TAG, "Elements in INVPOS:" + nList.getLength());
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element2 = (Element) node;
				String secid = getValue("UNIQUEID", element2);
				Log.i(TAG, "UNIQUEID=" + secid);
				Fund fname = getFund(secid);
				fname.setShares(getValue("UNITS", element2));
				fname.setSharePrice(getValue("UNITPRICE", element2));
				fname.setBalance(getValue("MKTVAL", element2));
				balanceDate = getValue("DTPRICEASOF", element2);
				try {
					String memo = getValue("MEMO", element2);
					if (memo!=null && memo.length() > 0)
					{
						parseBalances(memo, fname);
						memoBalance=true;
					}
				} catch (Exception e) {
					Log.i(TAG, "MEMO not found for balances");
					memoBalance=false;
				}
			}
		}
	}	// ParseElements

	private static String getValue(String tag, Element element) {
		NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodeList.item(0);
		return node.getNodeValue();
	}

	  public void parseContrib(String memo) {
		    Log.i(TAG,"parseContrib: " + memo);
		    String[] val = memo.split("~");
		    String[] val2;
		    for (int knt = 0; knt < val.length; knt++) {
		      Log.i(TAG, val[knt]);
		      val2 = val[knt].split("=");
		      if (val2[0].toUpperCase().equalsIgnoreCase("YTDTRADITIONAL")) {
		        ytdTrad = val2[1];
		      } else if (val2[0].toUpperCase().contains("AUTO")) {
		        ytdAAuto = val2[1];
		      } else if (val2[0].toUpperCase().contains("MATCH")) {
		        ytdAMatch = val2[1];
		      } else if (val2[0].toUpperCase().contains("ALCATCHUP")) {
		        ytdTradCatchup = val2[1];
		      } else if (val2[0].toUpperCase().contains("TAXE")) {
		        ytdTaxExempt = val2[1];
		      } else if (val2[0].toUpperCase().equalsIgnoreCase("YTDROTH")) {
		        ytdRoth = val2[1];
		      } else if (val2[0].toUpperCase().contains("ROTHCATCHUP")) {
		        ytdRothCatchup = val2[1];
		      }
		    }
		  }

		  public void parseBalances(String memo, Fund fname) {
			Log.i(TAG,"parseBalance: " + memo);
		    String[] val = memo.split("~");
		    String[] val2;
		    for (int knt = 0; knt < val.length; knt++) {
		      val2 = val[knt].split("=");
		      if (val2[0].toUpperCase().contains("CONTRIB")) {
			fname.setContAlloc(val2[1]);
		       } else if (val2[0].toUpperCase().contains("TRAD")) {
			fname.setTraditional(val2[1]);
		      } else if (val2[0].toUpperCase().contains("TAXE")) {
		        fname.setTaxExempt(val2[1]);
		      } else if (val2[0].toUpperCase().contains("ROTH")) {
		        fname.setRoth(val2[1]);
		      } else if (val2[0].toUpperCase().contains("AUTO")) {
		        fname.setAgencyAuto(val2[1]);
		      } else if (val2[0].toUpperCase().contains("MATCH")) {
		        fname.setAgencyMatch(val2[1]);
		      }
		    }
		  }
		    
	public String getLoginMessage() {
		Log.i(TAG, "loginResult from getLoginMessage=" + loginResult);
		return loginResult;
	}

	public String readEncryptedFile(String fname) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(context.getAssets().open(fname)));
		StringBuffer strbuf = new StringBuffer();
		String inline;
		while ((inline = in.readLine()) != null) {
			strbuf.append(inline);
		}
		in.close();

		String input = strbuf.toString();
		String SALT_PW2 = "7Af06Sl01d";

		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(SALT_PW1 + SALT_PW2);
		String enc = encryptor.decrypt(input);
		return enc;
	}
}
