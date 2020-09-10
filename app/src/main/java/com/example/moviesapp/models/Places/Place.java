package com.example.moviesapp.models.Places;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Place{

	@SerializedName("html_attributions")
	private List<Object> htmlAttributions;

	@SerializedName("results")
	private List<ResultsItem> results;

	@SerializedName("status")
	private String status;

	public List<Object> getHtmlAttributions(){
		return htmlAttributions;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Place{" + 
			"html_attributions = '" + htmlAttributions + '\'' + 
			",results = '" + results + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}