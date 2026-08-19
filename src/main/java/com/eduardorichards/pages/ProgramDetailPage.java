package com.eduardorichards.pages;

public class ProgramDetailPage extends AbstractPage {

    private static final String URL_FRAGMENT = "/training/";

    public ProgramDetailPage() {
        super();
    }

    @Override
    protected String getUrlFragment() {
        return URL_FRAGMENT;
    }

}
