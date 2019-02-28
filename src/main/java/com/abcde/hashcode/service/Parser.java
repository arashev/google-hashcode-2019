package com.abcde.hashcode.service;

import com.abcde.hashcode.domain.Photo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public final class Parser {

    private int INDEX = 0;

    public List<Photo> load(Path file) throws IOException {
        return Files.readAllLines(file).stream().skip(1).map(this::parseRow).collect(Collectors.toList());
    }

    private Photo parseRow(String row) {
        String[] raw = row.split(" ");
        return new Photo(INDEX++, raw[0].charAt(0), new HashSet<>(Arrays.asList(raw).subList(2, raw.length)));
    }
}
