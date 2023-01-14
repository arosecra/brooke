package org.github.arosecra.brooke.model.api;

import java.util.ArrayList;
import java.util.List;

public class CategoryApiModel {
	private BrookeOptionsApiModel brookeOptions;
	private List<ItemApiModel> items = new ArrayList<>();
	private String name;
	private VlcOptionsApiModel vlcOptions;
	public BrookeOptionsApiModel getBrookeOptions() {
		return brookeOptions;
	}
	public List<ItemApiModel> getItems() {
		return items;
	}
	public String getName() {
		return name;
	}
	public VlcOptionsApiModel getVlcOptions() {
		return vlcOptions;
	}
	public void setBrookeOptions(BrookeOptionsApiModel brookeOptions) {
		this.brookeOptions = brookeOptions;
	}
	public void setItems(List<ItemApiModel> items) {
		this.items = items;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setVlcOptions(VlcOptionsApiModel vlcOptions) {
		this.vlcOptions = vlcOptions;
	}
}