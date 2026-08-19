package com.eduardorichards.pages;

public class SkillsPage extends AbstractPage {

    private static final String URL_FRAGMENT = "/skill";

    public SkillsPage() {
        super();
    }

    @Override
    protected String getUrlFragment() {
        return URL_FRAGMENT;
    }

}
