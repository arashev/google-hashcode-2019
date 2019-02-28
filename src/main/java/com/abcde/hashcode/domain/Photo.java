package com.abcde.hashcode.domain;

import lombok.Value;

import java.util.Set;

@Value
public final class Photo {

    public final int id;
    public final char orientation;
    public final Set<String> tags;
}
