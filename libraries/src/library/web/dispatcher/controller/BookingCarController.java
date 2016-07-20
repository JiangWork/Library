package library.web.dispatcher.controller;

import java.util.Map;

import library.web.dispatcher.annotation.Url;
import library.web.dispatcher.core.ParameterAware;

/**
 * A simple controller to mimic car booking system.
 * 
 * @author Miller
 * @date Mar 21, 2016 8:23:37 PM
 */
@Url("/car/booking")
public class BookingCarController implements ParameterAware {

	private Map<String, String[]> requestParams;
	
	@Override
	public void setParameter(Map<String, String[]> parameter) {
		this.requestParams = parameter;
	}

	
	public String getBrand() {
		String[] params = requestParams.get("carName");
		return params == null? "carName is needed" : String.format("carName=%s", params[0]);
	}
	
	
	public double getPrice() {
		String[] params = requestParams.get("carName");
		return params == null? -1 : 100;
	
	}
	
}
