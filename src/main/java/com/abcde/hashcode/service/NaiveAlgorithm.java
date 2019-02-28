package com.abcde.hashcode.service;


import com.abcde.hashcode.domain.Photo;
import com.abcde.hashcode.domain.Slide;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class NaiveAlgorithm {

    public static List<Slide> apply(List<Photo> photos) {
        List<Slide> slides = new ArrayList<>();
        List<Photo> vertical = null;
        for (Photo photo : photos) {
            if (photo.orientation == 'H') {
                slides.add(new Slide(ImmutableList.of(photo)));
            } else {
                if (vertical == null) {
                    vertical = new ArrayList<>();
                }
                vertical.add(photo);
                if (vertical.size() == 2) {
                    slides.add(new Slide(vertical));
                    vertical = null;
                }
            }
        }
        return slides;
    }
}
