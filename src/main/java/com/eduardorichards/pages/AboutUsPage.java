package com.eduardorichards.pages;

public class AboutUsPage extends AbstractPage {

    private static final String URL_FRAGMENT = "/about";

    public AboutUsPage() {
        super();
    }

    @Override
    protected String getUrlFragment() {
        return URL_FRAGMENT;
    }

}
