package com.hyperlab.luckyhomefinder.sites.gohome.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class TestSuitableDataStructure {

	@Test
	public void testDs(){
		List<String> values = new ArrayList<String>();
		values.add("A");
		values.add("B");
		values.add("C");
		values.add("D");
		values.add("E");
		
		List<String> target = new ArrayList<String>();
		target.add("A");
		target.add("B");
		target.add("D");
		target.add("S");
		target.add("Y");
		target.removeAll(values);
		values.addAll(target);
		Collections.reverse(values);
		
		
		
	}
}
