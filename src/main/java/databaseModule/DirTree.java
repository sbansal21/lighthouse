package databaseModule;

import java.util.*;

/**
 * Represents a basic directory structure as a tree.
 * 
 * @author Sumeet Bansal
 * @version 1.0
 */
public class DirTree {

	private int nelems;
	private DirNode root;

	/**
	 * Creates Nodes for the tree.
	 * 
	 * @author Sumeet Bansal
	 * @version 1.0
	 */
	protected class DirNode {

		String name;
		Set<DirNode> children;
		boolean isDir;

		/**
		 * Constructor for class DirNode, initializes all fields.
		 * 
		 * @param name
		 *            DirNode name
		 * @param isDir
		 *            boolean indicating if the DirNode represents a directory
		 */
		public DirNode(String name, boolean isDir) {
			this.name = name;
			children = new LinkedHashSet<>();
			this.isDir = isDir;
		}

		/**
		 * Getter for DirNode name.
		 * 
		 * @return the DirNode name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter for DirNode children.
		 * 
		 * @return the DirNode children
		 */
		public Set<DirNode> getChildren() {
			return children;
		}

		/**
		 * Getter for DirNode type (i.e. either file or directory).
		 * 
		 * @return true if DirNode is a directory, false if a file
		 */
		public boolean isDirectory() {
			return isDir;
		}

		/**
		 * Adds a child to the DirNode.
		 * 
		 * @param child
		 *            the DirNode's new child
		 */
		public void addChild(DirNode child) {
			children.add(child);
		}

		/**
		 * Removes specified child node from DirNode's children.
		 * 
		 * @param child
		 *            child to be removed
		 * @return true in case of successful remove, else false
		 */
		public boolean removeChild(DirNode child) {
			return children.remove(child);
		}
	} // end of class DirNode

	/**
	 * Constructor for class DirTree, initializes empty tree with root DirNode.
	 */
	public DirTree() {
		this.nelems = 0;
		this.root = new DirNode("root", true);
	}

	/**
	 * Getter for the tree's root.
	 * 
	 * @return the the tree's root
	 */
	public DirNode getRoot() {
		if (nelems == 0) {
			return null;
		}
		return root;
	}

	/**
	 * Getter for the number of elements in the tree.
	 * 
	 * @return the number of elements in the tree
	 */
	public int getSize() {
		return nelems;
	}

	/**
	 * Inserts DirNode into the tree recursively.
	 * 
	 * @param path
	 *            the new path being inserted
	 * @throws NullPointerException
	 *             if the path is null
	 */
	public void insert(String path) throws NullPointerException {
		if (path == null) {
			throw new NullPointerException("The path is null.");
		}

		nelems++;
		add(root, path);
	}

	/**
	 * Determines if a certain path is in the tree.
	 * 
	 * @param path
	 *            the path of the DirNode to be found
	 * @return true if DirNode found, else false
	 * @throws NullPointerException
	 *             if the path is null
	 */
	public boolean hasKey(String path) throws NullPointerException {
		if (path == null) {
			throw new NullPointerException("The path is null.");
		}

		if (findNode(root, path) == null) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the children of the given path.
	 * 
	 * @param path
	 *            the path of the DirNode
	 * @return the children of the given DirNode
	 * @throws NullPointerException
	 *             if the path is null
	 * @throws IllegalArgumentException
	 *             if the path does not exist
	 */
	public Set<String> getChildren(String path) throws NullPointerException, IllegalArgumentException {
		if (path == null) {
			throw new NullPointerException("The path is null.");
		}
		if (findNode(root, path) == null) {
			throw new IllegalArgumentException("No such path.");
		}

		// returns names of children
		DirNode node = findNode(root, path);
		Set<String> children = new HashSet<>();
		if (node != null) {
			for (DirNode child : node.getChildren()) {
				children.add(child.getName());
			}
		}
		return children;
	}

	/**
	 * Helper method that finds a specific DirNode recursively.
	 * 
	 * @param curNode
	 *            the current DirNode being checked
	 * @param path
	 *            the path of the DirNode to be found
	 * @return the specified DirNode, or null
	 */
	private DirNode findNode(DirNode curNode, String path) {

		// shifts the key and path one level down
		String key = "";
		if (path.indexOf("/") != -1) {
			key = path.substring(0, path.indexOf("/"));
			path = path.substring(path.indexOf("/") + 1);
		} else {
			key = path;
			path = "";
		}

		// base case
		if (key.equals("")) {
			return curNode;
		}

		// recursive case
		for (DirNode child : curNode.getChildren()) {
			if (key.equals(child.getName())) {
				return findNode(child, path);
			}
		}

		// no matching node found
		return null;
	}

	/**
	 * Helper method that adds a DirNode to the tree recursively.
	 * 
	 * @param curNode
	 *            the current DirNode being checked
	 * @param path
	 *            the path of the DirNode to be added
	 */
	private void add(DirNode curNode, String path) {

		// base case
		if (path == null) {
			return;
		}

		// shifts the key and path one level down
		String key;
		boolean isDir;
		if (path.indexOf("/") != -1) {
			key = path.substring(0, path.indexOf("/"));
			path = path.substring(path.indexOf("/") + 1);
			isDir = true; // must be a directory
		} else {
			key = path;
			path = null;
			isDir = false; // must be a file
		}

		// finds correct child and goes down another level
		for (DirNode child : curNode.getChildren()) {
			if (child.getName().equals(key)) {
				add(child, path);
				return;
			}
		}

		// code block only reachable if no matching children
		DirNode child = new DirNode(key, isDir);
		curNode.addChild(child);
		add(child, path);
	}

	/**
	 * Prints a branch of the tree.
	 * @param path
	 *            the DirNode whose branch is being printed
	 * @param level
	 *            the number of levels to traverse
	 */
	public void print(String path, int level) {
		DirNode node = findNode(root, path);
		if (node == null) {
			System.err.println("[ERROR] Invalid path.");
			return;
		}

		// purely for aesthetic purposes
		String buffer = "";
		if (node != root) {
			System.out.println(node.getName());
			buffer = " | ";
		}

		// calls on the recursive function
		for (DirNode child : node.getChildren()) {
			prnt(child, buffer, level);
		}
	}

	/**
	 * Private helper method for in-order traversal.
	 * @param node
	 *            the current DirNode
	 * @param buffer
	 *            the String buffer (used to indicate levels)
	 * @param level
	 *            the number of levels to traverse
	 */
	private void prnt(DirNode node, String buffer, int level) {

		// base case
		if (level == 0) {
			return;
		}

		// recursive case
		System.out.println(buffer + node.getName());
		for (DirNode child : node.getChildren()) {
			prnt(child, buffer + " | ", level - 1);
		}
	}

	/**
	 * Counts the number of nodes at a certain depth (useful for checking how many nodes/files/etc.
	 * are currently in the database).
	 * @param node
	 *            the current dirNode in the traversal
	 * @param level
	 *            the number of levels remaining to traverse through
	 * @param dirsOnly
	 *            true to only report tree nodes representing directories, false to report files as
	 *            well
	 * @return the number of nodes at a certain depth
	 */
	public int countNodes(DirNode node, int level, boolean dirsOnly) {

		// base case
		if (level == 0) {
			if (dirsOnly && !node.isDirectory()) {
				return 0;
			}
			return 1;
		}

		// recursive case
		int nodes = 0;
		for (DirNode child : node.getChildren()) {
			nodes += countNodes(child, level - 1, dirsOnly);
		}
		return nodes;
	}
}
