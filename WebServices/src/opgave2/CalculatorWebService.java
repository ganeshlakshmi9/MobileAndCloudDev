package opgave2;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class CalculatorWebService {

	@WebMethod
	public double add(double a, double b){
		return a+b;
	}
	
	@WebMethod
	public double substract(double a, double b){
		return a-b;
	}
	
	@WebMethod
	public double multiply(double a, double b){
		return a*b;
	}
	
	@WebMethod
	public double divide(double a, double b){
		return a/b;
	}
	
}
