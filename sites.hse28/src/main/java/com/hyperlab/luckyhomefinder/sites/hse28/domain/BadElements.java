package com.hyperlab.luckyhomefinder.sites.hse28.domain;

import java.util.Arrays;
import java.util.List;

/**
 * This class contain the bad elements need to be removed from property page
 * elements
 * @author Kareem EL-Shahawe
 */
public class BadElements {

    private static final String[] BADELEMENTS = new String[] {
            "price per feet",
            "price record",
            "Views",
            "Saved",
            "Address"
    };
    
    public static final List<String> getBadElements(){
       return Arrays.asList(BADELEMENTS);
    }
}
