package com.underhilllabs.birdmate;

public class BirdSighting {
    	String com_name;
    	String sci_name;
    	String date;
    	String locId;
    	Double latitude;
    	Double longitude;
    	int how_many;
    	BirdSighting(String com_name,String sci_name, String date, String loc_id, Double lat, Double lng) {
    		this.com_name=com_name;
    		this.date = date;
    		this.sci_name = sci_name;
    		this.latitude = lat;
    		this.longitude = lng;
    		this.locId = loc_id;
    		
    	}
    	public String toString() {
			return com_name + " spotted on " + date + " at " + locId;
			
    	}
}
