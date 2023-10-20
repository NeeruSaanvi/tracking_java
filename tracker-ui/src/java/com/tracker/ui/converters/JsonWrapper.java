package com.tracker.ui.converters;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString(includeFieldNames = true)
public class JsonWrapper<E> {

	private String message = "";

	private boolean success = true;
	private int total;
	private int recordsTotal;
	private int recordsFiltered;
	private List<E> data;

	public JsonWrapper() { 
	}

	public JsonWrapper(List<E> data, int total, int recordsTotal, int recordsFiltered) {
		this.data = data;
		this.total = total < 0 ? recordsTotal : total;
		this.recordsTotal = recordsTotal;
		this.recordsFiltered = recordsFiltered;
	}

}
