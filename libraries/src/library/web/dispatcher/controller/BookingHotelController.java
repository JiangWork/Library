package library.web.dispatcher.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import library.web.dispatcher.annotation.Param;


/**
 * A simple controller to mimic booking hotel system.
 *  
 * @author Miller
 * @date Mar 21, 2016 8:00:31 PM
 */
public class BookingHotelController {
	
	private static final Logger logger = Logger.getLogger(BookingHotelController.class);
	
	public String getStar(@Param("hotelName") String hotelName) {
		logger.info("getStart request: " + hotelName);
		return "Star One:" + hotelName;
	}
	
	public double getPrice(@Param("hotelId") int id) {
		logger.info("getHotels request:" + id);
		return 100 + id;
	}
	
	public List<String> getHotelsAt(@Param("location") final String location, @Param("max") final int max) {
		return new ArrayList<String> () {
			private static final long serialVersionUID = 1L;
			{
				add("Location:" + location + " max:" + max);
				add("ShanghaiHotel");
				add("HangzhouHotel");
				add("JiangshuHotel");
			}
		};
	}
}
