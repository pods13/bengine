package com.pods.bengine.core.minify;

import org.trimou.minify.AbstractMinifier;
import org.trimou.minify.HtmlCompressorMinifier;

import java.io.Reader;
import java.util.List;
import java.util.regex.Pattern;

public class HtmlMinifier extends AbstractMinifier {

    private HtmlCompressorMinifier htmlCompressorMinifier;

    public HtmlMinifier() {
        this.htmlCompressorMinifier = new CustomHtmlCompressorMinifier();
    }

    @Override
    public Reader minify(String mustacheName, Reader mustacheContents) {
        return htmlCompressorMinifier.minify(mustacheName, mustacheContents);
    }

    class CustomHtmlCompressorMinifier extends HtmlCompressorMinifier {

        private static final String MORE_TAG = "<!--more-->";

        CustomHtmlCompressorMinifier() {
            super();
            compressor.setPreservePatterns(List.of(Pattern.compile(MORE_TAG)));
        }
    }
}
