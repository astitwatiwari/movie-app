package com.example.moviesapp.models.Places;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Geometry{

	@SerializedName("viewport")
	private Viewport viewport;

	@SerializedName("location")
	private Location location;

	public Viewport getViewport(){
		return viewport;
	}

	public Location getLocation(){
		return location;
	}

	@Override
 	public String toString(){
		return 
			"Geometry{" + 
			"viewport = '" + viewport + '\'' + 
			",location = '" + location + '\'' + 
			"}";
		}
}