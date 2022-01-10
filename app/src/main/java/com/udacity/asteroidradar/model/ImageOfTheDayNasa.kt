package com.udacity.asteroidradar.model

import com.squareup.moshi.Json

data class ImageOfTheDayNasa (

	@field:Json(name = "copyright") val copyright : String,
	@field:Json(name = "date") val date : String,
	@field:Json(name = "explanation") val explanation : String,
	@field:Json(name = "hdurl") val hdurl : String,
	@field:Json(name = "media_type") val media_type : String,
	@field:Json(name = "service_version") val service_version : String,
	@field:Json(name = "title") val title : String,
	@field:Json(name = "url") val url : String
)