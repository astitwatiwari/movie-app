package com.example.moviesapp.models.Places;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Location{

	@SerializedName("lng")
	private double lng;

	@SerializedName("lat")
	private double lat;

	public double getLng(){
		return lng;
	}

	public double getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"Location{" + 
			"lng = '" + lng + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}
}