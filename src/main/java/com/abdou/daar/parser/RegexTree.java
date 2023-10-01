package com.abdou.daar.parser;

import java.util.ArrayList;

public class RegexTree {
    protected int root;
    // this is for debug purpose
    protected String  rootString ;
    protected ArrayList<RegexTree> subTrees;
    public RegexTree(int root, ArrayList<RegexTree> subTrees) {
        this.root = root;
        this.rootString = rootToString();
        this.subTrees = subTrees;
    }
    //FROM TREE TO PARENTHESIS
    public String toString() {
        if (subTrees.isEmpty()) return rootToString();
        String result = rootToString()+"("+subTrees.get(0).toString();
        for (int i=1;i<subTrees.size();i++) result+=","+subTrees.get(i).toString();
        return result+")";
    }
    private String rootToString() {
        if (root == RegexParserOld.CONCAT) return ".";
        if (root== RegexParserOld.ETOILE) return "*";
        if (root== RegexParserOld.ALTERN) return "|";
        if (root== RegexParserOld.DOT) return ".";
        return Character.toString((char)this.root);
    }
}
