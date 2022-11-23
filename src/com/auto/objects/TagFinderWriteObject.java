package com.auto.objects;

public class TagFinderWriteObject {
	String initialTags;
	String removedTags;
	String addedTags;
	String tagsAfterUpdation;

	public TagFinderWriteObject(String initialTags, String removedTags, String addedTags, String tagAfterUpdation) {
		this.initialTags = initialTags;
		this.removedTags = removedTags;
		this.addedTags = addedTags;
		this.tagsAfterUpdation = tagAfterUpdation;
	}

}
