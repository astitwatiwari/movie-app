package com.example.moviesapp.models.Places;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ResultsItem{

	@SerializedName("types")
	private List<String> types;

	@SerializedName("icon")
	private String icon;

	@SerializedName("rating")
	private double rating;

	@SerializedName("photos")
	private List<PhotosItem> photos;

	@SerializedName("reference")
	private String reference;

	@SerializedName("user_ratings_total")
	private int userRatingsTotal;

	@SerializedName("price_level")
	private int priceLevel;

	@SerializedName("scope")
	private String scope;

	@SerializedName("name")
	private String name;

	@SerializedName("opening_hours")
	private OpeningHours openingHours;

	@SerializedName("geometry")
	private Geometry geometry;

	@SerializedName("vicinity")
	private String vicinity;

	@SerializedName("id")
	private String id;

	@SerializedName("plus_code")
	private PlusCode plusCode;

	@SerializedName("place_id")
	private String placeId;

	public List<String> getTypes(){
		return types;
	}

	public String getIcon(){
		return icon;
	}

	public double getRating(){
		return rating;
	}

	public List<PhotosItem> getPhotos(){
		return photos;
	}

	public String getReference(){
		return reference;
	}

	public int getUserRatingsTotal(){
		return userRatingsTotal;
	}

	public int getPriceLevel(){
		return priceLevel;
	}

	public String getScope(){
		return scope;
	}

	public String getName(){
		return name;
	}

	public OpeningHours getOpeningHours(){
		return openingHours;
	}

	public Geometry getGeometry(){
		return geometry;
	}

	public String getVicinity(){
		return vicinity;
	}

	public String getId(){
		return id;
	}

	public PlusCode getPlusCode(){
		return plusCode;
	}

	public String getPlaceId(){
		return placeId;
	}

	@Override
 	public String toString(){
		return 
			"ResultsItem{" + 
			"types = '" + types + '\'' + 
			",icon = '" + icon + '\'' + 
			",rating = '" + rating + '\'' + 
			",photos = '" + photos + '\'' + 
			",reference = '" + reference + '\'' + 
			",user_ratings_total = '" + userRatingsTotal + '\'' + 
			",price_level = '" + priceLevel + '\'' + 
			",scope = '" + scope + '\'' + 
			",name = '" + name + '\'' + 
			",opening_hours = '" + openingHours + '\'' + 
			",geometry = '" + geometry + '\'' + 
			",vicinity = '" + vicinity + '\'' + 
			",id = '" + id + '\'' + 
			",plus_code = '" + plusCode + '\'' + 
			",place_id = '" + placeId + '\'' + 
			"}";
		}
}