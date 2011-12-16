package opgave1;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class HelloWorldService {
	
	@WebMethod
	public String hello(String str){
		return str;
	}
	
}
