package com.soen343.idmappers;

import java.util.HashMap;
import java.util.Map;

import com.soen343.core.DomainObject;

public class IdentityMap {

	protected Map<Long, DomainObject> identityMap;

	public IdentityMap() {
		this.identityMap = new HashMap<Long, DomainObject>();
	}

	public void add(DomainObject o) {
		identityMap.put(o.getId(), o);
	}

	public void delete(long id) {
		identityMap.remove(id);
	}

	public DomainObject get(long id) {
		return identityMap.get(id);
	}
}