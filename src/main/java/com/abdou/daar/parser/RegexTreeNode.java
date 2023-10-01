package com.abdou.daar.parser;

import java.util.ArrayList;

public class RegexTreeNode {
    private static final int NO_VALUE = -1;
    protected String root;
    private final int value;

    protected ArrayList<RegexTreeNode> children;

    public RegexTreeNode(String root) {
        this.root = root;
        this.value = NO_VALUE;
        this.children = new ArrayList<>();
    }

    public RegexTreeNode(int value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public RegexTreeNode getFirstChild() {
        return this.children.get(0);
    }

    public RegexTreeNode getNthChild(int n) {
        return this.children.get(n);
    }

    public int getChildrenSize() {
        return this.children.size();
    }

    public boolean hasValue() {
        return this.value != NO_VALUE;
    }

    public void addChildren(RegexTreeNode child) {
        this.children.add(child);
    }

    public boolean isExpression() {
        return this.root.equals(RegexTreeNodeLabel.Expression.getLabel());
    }

    public boolean isTerm() {
        return this.root.equals(RegexTreeNodeLabel.Term.getLabel());
    }

    public boolean isFactor() {
        return this.root.equals(RegexTreeNodeLabel.Factor.getLabel());
    }

    public boolean isAtom() {
        return this.root.equals(RegexTreeNodeLabel.Atom.getLabel());
    }

    public boolean isCharacter() {
        return this.root.equals(RegexTreeNodeLabel.Character.getLabel());
    }

    public char getValue() {
        return (char) this.value;
    }

    public String toString() {
        StringBuilder json = new StringBuilder("{");
        if (this.value > 0) {
            json.append("\"root\": \"").append(Character.toString(this.value)).append("\",");
        }
        else {
            json.append("\"root\": \"").append(this.root).append("\",");
        }
        json.append("\"children\": [");
        for (int i = 0; i < this.children.size(); i++) {
            json.append(this.children.get(i).toString());
            if (i < this.children.size() - 1) {
                json.append(",");
            }
        }
        json.append("]}");
        return json.toString();
    }
}
