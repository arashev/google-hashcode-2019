package com.abcde.hashcode.service;

import com.abcde.hashcode.domain.Photo;
import com.abcde.hashcode.domain.Slide;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BrutforceAlgorithm {

    public static List<Slide> apply(List<Photo> photos) throws InterruptedException {
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


        List<Slide> results = new ArrayList<>();
        Set<Slide> checker = new HashSet<>(slides);

        Slide current = slides.get(0);
        checker.remove(current);
        results.add(current);
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(!checker.isEmpty()) {

            List<Set<Slide>> partitions = partition(checker, 4);

            Set<Slide> bests = new HashSet<>();

            Slide a = current;

            Thread t1 = new Thread(() -> bests.add(findBest(partitions.get(0), a)));
            Thread t2 = new Thread(() -> bests.add(findBest(partitions.get(1), a)));
            Thread t3 = new Thread(() -> bests.add(findBest(partitions.get(2), a)));
            Thread t4 = new Thread(() -> bests.add(findBest(partitions.get(3), a)));

            t1.join();
            t2.join();
            t3.join();
            t4.join();

            current = findBest(bests, current);
            checker.remove(current);
            results.add(current);

            if (results.size() % 100 == 0) {
                System.out.println(results.size());
            }
        }

        return results;
    }

    private static Slide findBest(Set<Slide> checker, Slide current) {
        Slide best = null;
        int bestScore = -1;
        for (Slide slide : checker) {
            if (best == null) {
                best = slide;
                bestScore = current.getScore(slide);
                continue;
            }
            int score = current.getScore(slide);
            if (score > bestScore) {
                bestScore = score;
                best = slide;
            }
        }
        current = best;
        return current;
    }


    private static List<Set<Slide>> partition(Set<Slide> parent, int n) {
        List<Set<Slide>> theSets = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            theSets.add(new HashSet<>());
        }

        int index = 0;
        for (Slide object : parent) {
            theSets.get(index++ % n).add(object);
        }
        return theSets;
    }
}
