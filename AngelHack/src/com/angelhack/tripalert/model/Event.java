package com.angelhack.tripalert.model;

public class Event {

	private String eventId;
	private TravelPoint travelPoint;
	private String msg;
	private int sev;
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public TravelPoint getTravelPoint() {
		return travelPoint;
	}
	public void setTravelPoint(TravelPoint travelPoint) {
		this.travelPoint = travelPoint;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getSev() {
		return sev;
	}
	public void setSev(int sev) {
		this.sev = sev;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (eventId == null) {
			if (other.eventId != null)
				return false;
		} else if (!eventId.equals(other.eventId))
			return false;
		return true;
	}
	
	
}
