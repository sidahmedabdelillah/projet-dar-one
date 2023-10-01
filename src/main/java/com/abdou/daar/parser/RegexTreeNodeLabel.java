package com.abdou.daar.parser;

public enum RegexTreeNodeLabel {
    Expression("Expression"),
    Term("Term"),
    Factor("Factor"),
    Character("Character"),
    Atom("Atom");

    private String label;
    RegexTreeNodeLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
