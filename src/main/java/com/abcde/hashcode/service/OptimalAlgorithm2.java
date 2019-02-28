package com.abcde.hashcode.service;

import com.abcde.hashcode.domain.Photo;
import com.abcde.hashcode.domain.Slide;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;

public class OptimalAlgorithm2 {

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

        List<Slide> results = new ArrayList<>();
        Set<Slide> all = new HashSet<>(slides);

        Slide current = slides.get(0);
        all.remove(current);

        while (!all.isEmpty()) {

            Set<Slide> bests = findBests(maps, all, current);
            Slide best = findBest(current, bests);

            if (best != null) {
                current = best;
            } else {
                current = all.iterator().next();
            }
            all.remove(current);
            results.add(current);

            if (results.size() % 100 ==0) {
                System.out.println(results.size());
            }


        }


        return slides;
    }

    private static Slide findBest(Slide current, Set<Slide> bests) {
        Slide best = null;
        int bestScore = -1;
        for (Slide candidate : bests) {
            if (best == null) {
                best = candidate;
                bestScore = current.getScore(candidate);
                continue;
            }
            int score = current.getScore(candidate);
            if (score > bestScore) {
                best = candidate;
                bestScore = score;
            }
        }
        return best;
    }

    private static Set<Slide> findBests(Multimap<String, Slide> maps, Set<Slide> all, Slide current) {
        return current.getTags().stream().flatMap(t -> maps.get(t).stream()).filter(all::contains).limit(1).collect(Collectors.toSet());
    }
}
