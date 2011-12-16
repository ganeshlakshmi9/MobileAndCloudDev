package opgave2;

import javax.xml.ws.WebServiceRef;

import opgave1.HelloWorldService;

public class CalculatorTest {

	@WebServiceRef(wsdlLocation = "http://localhost:5496/WebServices/services/HelloWorldService")
	static HelloWorldService service;
	
	public static void main(String[] args) {
		try{
			service = new HelloWorldService();
			System.out.println(service.hello("HEEEEJ"));
//			double a = 1.55;
//			double b = 5.45;
//			
//			System.out.println(service.add(a, b));
//			System.out.println(service.substract(a, b));
//			System.out.println(service.multiply(a, b));
//			System.out.println(service.divide(a, b));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
