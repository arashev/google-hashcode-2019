package com.abcde.hashcode.service;

import com.abcde.hashcode.domain.Photo;
import com.abcde.hashcode.domain.Slide;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OptimalAlgorithm {

    public static List<Slide> apply(List<Photo> photos) {
        List<Slide> slides = new ArrayList<>();
        List<Photo> vertical = new ArrayList<>();
        for (Photo photo : photos) {
            if (photo.getOrientation() == 'H') {
                slides.add(new Slide(ImmutableList.of(photo)));
            } else {
                vertical.add(photo);
            }
        }

        vertical.sort((p1, p2) -> Integer.compare(p1.tags.size(), p2.getTags().size()));

        List<Photo> verticals = new ArrayList<>();
        for (Photo photo : vertical) {
            verticals.add(photo);
            if (verticals.size() == 2) {
                slides.add(new Slide(verticals));
                verticals = new ArrayList<>();
            }
        }

        Multimap<String, Slide> maps = HashMultimap.create();
        for (Slide slide : slides) {
            for (String tag : slide.getTags()) {
                maps.put(tag, slide);
            }
        }

//        slides.sort(Comparator.comparingInt(s -> sumOfTags(s, maps)));
        slides.sort(Comparator.comparingInt(s -> s.getTags().size()));
        slides.sort(Comparator.comparing(OptimalAlgorithm::concat).reversed());


        return slides;
    }


    private static int sumOfTags(Slide slide, Multimap<String, Slide> maps) {
        return slide.getTags().stream().mapToInt(t -> maps.get(t).size()).sum();
    }


    private static String concat(Slide slide) {
        return slide.getTags().stream().sorted().collect(Collectors.joining());
    }
}
