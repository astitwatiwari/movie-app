package com.example.moviesapp.models.Places;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class OpeningHours{

	@SerializedName("open_now")
	private boolean openNow;

	public boolean isOpenNow(){
		return openNow;
	}

	@Override
 	public String toString(){
		return 
			"OpeningHours{" + 
			"open_now = '" + openNow + '\'' + 
			"}";
		}
}