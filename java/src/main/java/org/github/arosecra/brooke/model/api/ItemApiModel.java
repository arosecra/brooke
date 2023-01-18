package org.github.arosecra.brooke.model.api;

import java.util.ArrayList;
import java.util.List;

public class ItemApiModel {
	private BrookeOptionsApiModel brookeOptions;
	private List<ItemApiModel> childItems = new ArrayList<>();
	private String name;
	private boolean series;
	private VlcOptionsApiModel vlcOptions;
	public BrookeOptionsApiModel getBrookeOptions() {
		return brookeOptions;
	}
	public List<ItemApiModel> getChildItems() {
		if(childItems == null)
			childItems = new ArrayList<>();
		return childItems;
	}
	public String getName() {
		return name;
	}
	public VlcOptionsApiModel getVlcOptions() {
		return vlcOptions;
	}
	public boolean isSeries() {
		return series;
	}
	public void setBrookeOptions(BrookeOptionsApiModel brookeOptions) {
		this.brookeOptions = brookeOptions;
	}
	public void setChildItems(List<ItemApiModel> childItems) {
		this.childItems = childItems;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSeries(boolean series) {
		this.series = series;
	}
	public void setVlcOptions(VlcOptionsApiModel vlcOptions) {
		this.vlcOptions = vlcOptions;
	}
}