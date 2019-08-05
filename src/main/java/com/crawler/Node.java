package com.crawler;

import java.util.HashSet;
import java.util.Set;

public class Node {
	private String data;
	private Node parent;
	private int status;
	private Set<Node> children = new HashSet<Node>();

	public Node() {
		data = null;
		parent = null;
	}

	public Node(String _data, Node _parent) {
		data = _data;
		parent = _parent;
	}

	public HashSet<Node> getChildren() {
		return (HashSet<Node>) children;
	}
 
	public String getData() {
		return data;
	}

	public void setData(String _data) {
		data = _data;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node _parent) {
		if (!(_parent == null)) {
			_parent.insertChild(data);
			parent = _parent;
		}

	}

	public void setStatus(int _status) {
		this.status = _status;
	}

	public int getStatus() {
		return this.status;
	}

	public void insertChild(String _data) {
		Node child = new Node(_data, this);
		children.add(child);
	}

	public void addChild(Node _child) {
		// Node child = new Node(_data, this);
		if (children.isEmpty()) {
			children = new HashSet<Node>();
		}
		children.add(_child);
	}

	public boolean isRoot() {
		return (parent == null);
	}
}
