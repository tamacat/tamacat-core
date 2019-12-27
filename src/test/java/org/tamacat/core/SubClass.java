package org.tamacat.core;

public class SubClass extends ParentClass {

	public void add(SubClass c) {
		name = c.getName();
	}
}
