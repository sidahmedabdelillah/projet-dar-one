package com.abdou.daar.parser;

import java.util.ArrayList;

public class RegexParser {
    protected String regex;
    private int index;

    public RegexParser(String regex) {
        this.regex = regex;
        this.index = 0;
    }

    public RegexTreeNode parse() throws UnexpectedCharException {
        return parseExpression();
    }

    private char peek() {
        return regex.charAt(index);
    }

    private boolean hasMoreChars() {
        return index < regex.length();
    }

    private boolean isMetaChar(char ch) {
        return ch == '.' || ch == '+' || ch == '*' ;
    }

    private boolean match(char c) {
        if (peek() == c) {
            index++;
            return true;
        }
        return false;
    }

    private void expect(char c) throws UnexpectedCharException {
        if (!match(c)) {
            throw new UnexpectedCharException(c, peek(), index);
        }
    }

    private char next() {
        var ch = peek();
        index++;
        return ch;
    }

    private RegexTreeNode getChar() throws UnexpectedCharException {
        if (isMetaChar(peek())) {
            throw new UnexpectedCharException(peek(), " non meta character ", index);
        }

        if (peek() == '\\') {
            next();
            char c = next();
            RegexTreeNode node = new RegexTreeNode(RegexTreeNodeLabel.Character.getLabel());
            node.addChildren(new RegexTreeNode('\\'));
            node.addChildren(new RegexTreeNode(next()));
            return node;
        }

        RegexTreeNode node = new RegexTreeNode(RegexTreeNodeLabel.Character.getLabel());
        node.addChildren(new RegexTreeNode(next()));
        return node;
    }

    private RegexTreeNode parseAtom() throws UnexpectedCharException {
        char c = peek();
        if (peek() == '(') {
            match('(');
            RegexTreeNode expression = parseExpression();
            char c1 = peek();
            expect(')');
            RegexTreeNode node = new RegexTreeNode(RegexTreeNodeLabel.Atom.getLabel());

            node.addChildren(new RegexTreeNode('('));
            node.addChildren(expression);
            node.addChildren(new RegexTreeNode(')'));

            return node;
        }

        RegexTreeNode charNode = getChar();
        RegexTreeNode node = new RegexTreeNode(RegexTreeNodeLabel.Atom.getLabel());
        node.addChildren(charNode);

        return node;
    }

    private RegexTreeNode parseTerm() throws UnexpectedCharException {
        RegexTreeNode factor = parseFactor();

        if (hasMoreChars() && peek() != ')' && peek() != '|') {
            RegexTreeNode term = parseTerm();
            RegexTreeNode node = new RegexTreeNode(RegexTreeNodeLabel.Term.getLabel());

            node.addChildren(factor);
            node.addChildren(term);

            return node;
        }

        RegexTreeNode node = new RegexTreeNode(RegexTreeNodeLabel.Term.getLabel());
        node.addChildren(factor);

        return node;
    }

    private RegexTreeNode parseFactor()  throws UnexpectedCharException {
        RegexTreeNode atom = parseAtom();

        if(hasMoreChars() && isMetaChar(peek())){
            char meta = next();
            RegexTreeNode node = new RegexTreeNode(RegexTreeNodeLabel.Factor.getLabel());
            node.addChildren(atom);
            node.addChildren(new RegexTreeNode(meta));

            return node;
        }

        RegexTreeNode node = new RegexTreeNode(RegexTreeNodeLabel.Factor.getLabel());
        node.addChildren(atom);
        return node;
    }

    private RegexTreeNode parseExpression() throws UnexpectedCharException {
        RegexTreeNode term = parseTerm();
        if (hasMoreChars() && peek() == '|') {
            match('|');
            RegexTreeNode expression = parseExpression();
            RegexTreeNode node = new RegexTreeNode(RegexTreeNodeLabel.Expression.getLabel());

            node.addChildren(term);
            node.addChildren(new RegexTreeNode('|'));
            node.addChildren(expression);

            return node;
        }

        RegexTreeNode node = new RegexTreeNode(RegexTreeNodeLabel.Expression.getLabel());
        node.addChildren(term);
        return node;
    }

}
