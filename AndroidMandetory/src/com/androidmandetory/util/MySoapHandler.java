package com.androidmandetory.util;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class MySoapHandler {
	private static final String NAMESPACE = "http://www.webserviceX.NET/";
	private static String URL = "http://www.webservicex.net/CurrencyConvertor.asmx?WSDL";
	private static final String METHOD_NAME = "ConversionRate";
	private static final String SOAP_ACTION = "http://www.webserviceX.NET/ConversionRate";

	/**
	 * The method sends a soap request with two currencies 
	 * to a web-service which returns the exchange rate. 
	 * 
	 * @param from
	 * @param to
	 * @return exchange rate or -1 on failure.
	 */
	public static double convertCurrency(String from, String to){
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("FromCurrency", from);
		request.addProperty("ToCurrency", to);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.setOutputSoapObject(request);
		envelope.dotNet = true;
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope
					.getResponse();
			Double exchangeRate = Double.valueOf(resultsRequestSOAP.toString());
			return exchangeRate;
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (XmlPullParserException e) {
			e.printStackTrace();
		} 
		
		return -1;
	}
}
