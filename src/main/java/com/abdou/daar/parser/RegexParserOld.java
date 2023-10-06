package com.abdou.daar.parser;

import java.util.ArrayList;

public class RegexParserOld {

    static final int CONCAT = 0xC04CA7;
    static final int ETOILE = 0xE7011E;
    static final int ALTERN = 0xA17E54;
    static final int PROTECTION = 0xBADDAD;

    static final int PARENTHESEOUVRANT = 0x16641664;
    static final int PARENTHESEFERMANT = 0x51515151;
    static final int DOT = 0xD07;

    private static int charToRoot(char c) {
        if (c=='.') return DOT;
        if (c=='*') return ETOILE;
        if (c=='|') return ALTERN;
        if (c=='(') return PARENTHESEOUVRANT;
        if (c==')') return PARENTHESEFERMANT;
        return (int)c;
    }

    //FROM REGEX TO SYNTAX TREE
    public static RegexTree parse(String regEx) throws Exception {
        //BEGIN DEBUG: set conditionnal to true for debug example
        if (false) throw new Exception();
        RegexTree example = exampleAhoUllman();
        if (false) return example;
        //END DEBUG

        ArrayList<RegexTree> result = new ArrayList<>();
        for (int i=0;i<regEx.length();i++) {
            int root = charToRoot(regEx.charAt(i));
            ArrayList<RegexTree> emptyList = new ArrayList<>();
            result.add(new RegexTree(root,emptyList));
        }

        return parse(result);
    }

    private static RegexTree parse(ArrayList<RegexTree> result) throws Exception {
        while (containParenthese(result)) result=processParenthese(result);
        while (containEtoile(result)) result=processEtoile(result);
        while (containConcat(result)) result=processConcat(result);
        while (containAltern(result)) result=processAltern(result);

        if (result.size()>1) throw new Exception();

        return removeProtection(result.get(0));
    }
    private static boolean containParenthese(ArrayList<RegexTree> trees) {
        for (RegexTree t: trees) {
            if (t.root==PARENTHESEFERMANT || t.root==PARENTHESEOUVRANT) {
                return true;
            };
        }
        return false;
    }
    private static ArrayList<RegexTree> processParenthese(ArrayList<RegexTree> trees) throws Exception {
        ArrayList<RegexTree> result = new ArrayList<RegexTree>();
        boolean found = false;
        for (RegexTree t: trees) {
            if (!found && t.root==PARENTHESEFERMANT) {
                boolean done = false;
                ArrayList<RegexTree> content = new ArrayList<RegexTree>();
                while (!done && !result.isEmpty())
                    if (result.get(result.size()-1).root==PARENTHESEOUVRANT) { done = true; result.remove(result.size()-1); }
                    else content.add(0,result.remove(result.size()-1));
                if (!done) throw new Exception();
                found = true;
                ArrayList<RegexTree> subTrees = new ArrayList<RegexTree>();
                subTrees.add(parse(content));
                result.add(new RegexTree(PROTECTION, subTrees));
            } else {
                result.add(t);
            }
        }
        if (!found) throw new Exception();
        return result;
    }
    private static boolean containEtoile(ArrayList<RegexTree> trees) {
        for (RegexTree t: trees) if (t.root==ETOILE && t.subTrees.isEmpty()) return true;
        return false;
    }
    private static ArrayList<RegexTree> processEtoile(ArrayList<RegexTree> trees) throws Exception {
        ArrayList<RegexTree> result = new ArrayList<RegexTree>();
        boolean found = false;
        for (RegexTree t: trees) {
            if (!found && t.root==ETOILE && t.subTrees.isEmpty()) {
                if (result.isEmpty()) throw new Exception();
                found = true;
                RegexTree last = result.remove(result.size()-1);
                ArrayList<RegexTree> subTrees = new ArrayList<RegexTree>();
                subTrees.add(last);
                result.add(new RegexTree(ETOILE, subTrees));
            } else {
                result.add(t);
            }
        }
        return result;
    }
    private static boolean containConcat(ArrayList<RegexTree> trees) {
        boolean firstFound = false;
        for (RegexTree t: trees) {
            if (!firstFound && t.root!=ALTERN) { firstFound = true; continue; }
            if (firstFound) if (t.root!=ALTERN) return true; else firstFound = false;
        }
        return false;
    }
    private static ArrayList<RegexTree> processConcat(ArrayList<RegexTree> trees) throws Exception {
        ArrayList<RegexTree> result = new ArrayList<RegexTree>();
        boolean found = false;
        boolean firstFound = false;
        for (RegexTree t: trees) {
            if (!found && !firstFound && t.root!=ALTERN) {
                firstFound = true;
                result.add(t);
                continue;
            }
            if (!found && firstFound && t.root==ALTERN) {
                firstFound = false;
                result.add(t);
                continue;
            }
            if (!found && firstFound && t.root!=ALTERN) {
                found = true;
                RegexTree last = result.remove(result.size()-1);
                ArrayList<RegexTree> subTrees = new ArrayList<RegexTree>();
                subTrees.add(last);
                subTrees.add(t);
                result.add(new RegexTree(CONCAT, subTrees));
            } else {
                result.add(t);
            }
        }
        return result;
    }
    private static boolean containAltern(ArrayList<RegexTree> trees) {
        for (RegexTree t: trees) if (t.root==ALTERN && t.subTrees.isEmpty()) return true;
        return false;
    }
    private static ArrayList<RegexTree> processAltern(ArrayList<RegexTree> trees) throws Exception {
        ArrayList<RegexTree> result = new ArrayList<RegexTree>();
        boolean found = false;
        RegexTree gauche = null;
        boolean done = false;
        for (RegexTree t: trees) {
            if (!found && t.root==ALTERN && t.subTrees.isEmpty()) {
                if (result.isEmpty()) throw new Exception();
                found = true;
                gauche = result.remove(result.size()-1);
                continue;
            }
            if (found && !done) {
                if (gauche==null) throw new Exception();
                done=true;
                ArrayList<RegexTree> subTrees = new ArrayList<RegexTree>();
                subTrees.add(gauche);
                subTrees.add(t);
                result.add(new RegexTree(ALTERN, subTrees));
            } else {
                result.add(t);
            }
        }
        return result;
    }
    private static RegexTree removeProtection(RegexTree tree) throws Exception {
        if (tree.root==PROTECTION && tree.subTrees.size()!=1) throw new Exception();
        if (tree.subTrees.isEmpty()) return tree;
        if (tree.root==PROTECTION) return removeProtection(tree.subTrees.get(0));

        ArrayList<RegexTree> subTrees = new ArrayList<RegexTree>();
        for (RegexTree t: tree.subTrees) subTrees.add(removeProtection(t));
        return new RegexTree(tree.root, subTrees);
    }

    //EXAMPLE
    // --> RegEx from Aho-Ullman book Chap.10 Example 10.25
    private static RegexTree exampleAhoUllman() {
        RegexTree a = new RegexTree((int)'a', new ArrayList<RegexTree>());
        RegexTree b = new RegexTree((int)'b', new ArrayList<RegexTree>());
        RegexTree c = new RegexTree((int)'c', new ArrayList<RegexTree>());
        ArrayList<RegexTree> subTrees = new ArrayList<RegexTree>();
        subTrees.add(c);
        RegexTree cEtoile = new RegexTree(ETOILE, subTrees);
        subTrees = new ArrayList<RegexTree>();
        subTrees.add(b);
        subTrees.add(cEtoile);
        RegexTree dotBCEtoile = new RegexTree(CONCAT, subTrees);
        subTrees = new ArrayList<RegexTree>();
        subTrees.add(a);
        subTrees.add(dotBCEtoile);
        return new RegexTree(ALTERN, subTrees);
    }
}
