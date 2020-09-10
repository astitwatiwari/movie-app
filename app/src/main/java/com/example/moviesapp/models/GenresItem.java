package com.example.moviesapp.models;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GenresItem{

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

}