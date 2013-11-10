package com.angelhack.tripalert.model;

import java.util.Date;

public class TravelPoint implements Comparable<TravelPoint> {

	Date travelTime;
	Location location;
	
	public Date getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(Date travelTime) {
		this.travelTime = travelTime;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	@Override
	public int compareTo(TravelPoint another) {
		return travelTime.compareTo(another.getTravelTime());
	}
	
	
}
