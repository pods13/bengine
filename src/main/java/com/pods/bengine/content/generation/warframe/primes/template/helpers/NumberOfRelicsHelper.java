package com.pods.bengine.content.generation.warframe.primes.template.helpers;

import org.trimou.handlebars.BasicValueHelper;
import org.trimou.handlebars.Options;
import pl.allegro.finance.tradukisto.ValueConverters;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class NumberOfRelicsHelper extends BasicValueHelper {

    @Override
    public void execute(Options options) {
        Map<String, Set<String>> itemPartsToRelics = (Map<String, Set<String>>) options.getParameters().get(0);
        long numberOfRelics = itemPartsToRelics.values().stream()
                .mapToLong(Collection::size)
                .sum();
        append(options, ValueConverters.ENGLISH_INTEGER.asWords((int) numberOfRelics));
    }
}
