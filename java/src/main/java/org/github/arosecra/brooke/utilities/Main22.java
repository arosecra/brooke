package org.github.arosecra.brooke.utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;


public class Main22 {
	public static void main(String[] args) throws Exception {
		// Mapper with default configuration
		ObjectMapper mapper = new YAMLMapper();
		Collection collection = mapper.readValue(new File("test.yaml"), Collection.class);
		
		System.out.println(collection.getName());
		for(Category category: collection.getCategories() ) {
			System.out.println("  " + category.getName());
			
			for(Item item : category.getItems()) {
				System.out.println("    " + item.getName());
			}
		}

		// Or using builder
//		ObjectMapper mapper = YAMLMapper.builder()
//		   .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
//		   .build();
		
		
	}
}

class BrookeOptions {
	private boolean reverseOrder;

	public boolean isReverseOrder() {
		return reverseOrder;
	}

	public void setReverseOrder(boolean reverseOrder) {
		this.reverseOrder = reverseOrder;
	}
	
}

class Category {
	private BrookeOptions brookeOptions;
	private List<Item> items = new ArrayList<>();
	private String name;
	private VlcOptions vlcOptions;
	public BrookeOptions getBrookeOptions() {
		return brookeOptions;
	}
	public List<Item> getItems() {
		return items;
	}
	public String getName() {
		return name;
	}
	public VlcOptions getVlcOptions() {
		return vlcOptions;
	}
	public void setBrookeOptions(BrookeOptions brookeOptions) {
		this.brookeOptions = brookeOptions;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setVlcOptions(VlcOptions vlcOptions) {
		this.vlcOptions = vlcOptions;
	}
}

class Collection {
	private boolean autoGenerateAlphaCategories;
	private List<Category> categories = new ArrayList<>();
	private List<String> excludeExtensions;
	private String itemExtension;
	private String localDirectory;
	private String name;
	private String openType;
	private List<String> pipelineSteps;
	private String remoteDirectory;
	public List<Category> getCategories() {
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
	public void setCategories(List<Category> categories) {
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

class Item {
	private BrookeOptions brookeOptions;
	private List<Item> childItems = new ArrayList<>();
	private String name;
	private boolean series;
	private VlcOptions vlcOptions;
	public BrookeOptions getBrookeOptions() {
		return brookeOptions;
	}
	public List<Item> getChildItems() {
		return childItems;
	}
	public String getName() {
		return name;
	}
	public VlcOptions getVlcOptions() {
		return vlcOptions;
	}
	public boolean isSeries() {
		return series;
	}
	public void setBrookeOptions(BrookeOptions brookeOptions) {
		this.brookeOptions = brookeOptions;
	}
	public void setChildItems(List<Item> childItems) {
		this.childItems = childItems;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSeries(boolean series) {
		this.series = series;
	}
	public void setVlcOptions(VlcOptions vlcOptions) {
		this.vlcOptions = vlcOptions;
	}
}

class VlcOptions {
	private int audioTrack;
	private int subtitleTrack;
	public int getAudioTrack() {
		return audioTrack;
	}
	public int getSubtitleTrack() {
		return subtitleTrack;
	}
	public void setAudioTrack(int audioTrack) {
		this.audioTrack = audioTrack;
	}
	public void setSubtitleTrack(int subtitleTrack) {
		this.subtitleTrack = subtitleTrack;
	}
}