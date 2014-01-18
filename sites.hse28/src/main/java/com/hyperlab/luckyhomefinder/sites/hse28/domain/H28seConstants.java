package com.hyperlab.luckyhomefinder.sites.hse28.domain;

/**
 * H28Constants contains all constant strings needed for various operations,
 * link main links search and request parameter values;
 * */
public enum H28seConstants {

	MAINSEARCH("http://eng.28hse.com/utf8/search2_ajax.php"), ALLDATAVALUE(
			"s_order=1&s_order_direction=0&s_type=0&s_sellrent=0&s_sellrange=0&s_sellrange_l=0&s_sellrange_h=0&s_rentrange=0&s_rentrange_l=0&s_rentrange_h=0&s_source=0&s_roomno=0&s_area=0&s_area_l=0&s_area_h=0&s_cached_fav=0&s_stored_fav=0&s_cat_child=0&s_restore_search_codition=0&s_page=0&s_myrelated=0&s_viewmode=0&s_global_tag=0&url=&search_location=0&search_district_dummy=0&search_district_1=0&search_district_2=0&search_district_3=0&search_district_170=0&search_district_180=0&s_keywords=&input_low=&input_high=&input_low=&input_high=&s_area_buildact=1&input_low=&input_high="), ALLDATAKEY(
			"the_alldata"), ACTIONKEY("action"), ACTIONVALUE("200");

	private String data;

	private H28seConstants(String data) {
		this.data = data;
	}

	public final String value() {
		return data;
	}
}
