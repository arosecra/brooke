package org.github.arosecra.brooke.model.api;

import java.util.ArrayList;
import java.util.List;

public class CollectionApiModel {
	private boolean autoGenerateAlphaCategories;
	private List<CategoryApiModel> categories = new ArrayList<>();
	private List<String> excludeExtensions;
	private String itemExtension;
	private String localDirectory;
	private String name;
	private String openType;
	private List<String> pipelineSteps;
	private String remoteDirectory;
	public List<CategoryApiModel> getCategories() {
		if(categories == null)
			categories = new ArrayList<>();
		return categories;
	}
	public List<String> getExcludeExtensions() {
		return excludeExtensions;
	}
	public String getItemExtension() {
		return itemExtension;
	}
	public String getLocalDirectory() {
		return localDirectory;
	}
	public String getName() {
		return name;
	}
	public String getOpenType() {
		return openType;
	}
	public List<String> getPipelineSteps() {
		return pipelineSteps;
	}
	public String getRemoteDirectory() {
		return remoteDirectory;
	}
	public boolean isAutoGenerateAlphaCategories() {
		return autoGenerateAlphaCategories;
	}
	public void setAutoGenerateAlphaCategories(boolean autoGenerateAlphaCategories) {
		this.autoGenerateAlphaCategories = autoGenerateAlphaCategories;
	}
	public void setCategories(List<CategoryApiModel> categories) {
		this.categories = categories;
	}
	public void setExcludeExtensions(List<String> excludeExtensions) {
		this.excludeExtensions = excludeExtensions;
	}
	public void setItemExtension(String itemExtension) {
		this.itemExtension = itemExtension;
	}
	public void setLocalDirectory(String localDirectory) {
		this.localDirectory = localDirectory;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setOpenType(String openType) {
		this.openType = openType;
	}
	public void setPipelineSteps(List<String> pipelineSteps) {
		this.pipelineSteps = pipelineSteps;
	}
	public void setRemoteDirectory(String remoteDirectory) {
		this.remoteDirectory = remoteDirectory;
	}
}