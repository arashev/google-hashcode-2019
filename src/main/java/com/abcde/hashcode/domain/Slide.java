package com.abcde.hashcode.domain;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Integer.min;
import static java.util.stream.Collectors.toSet;

@Getter
@Setter
@RequiredArgsConstructor
public final class Slide {

    private final List<Photo> photos;
    private Set<String> cachedTags;

    public int getScore(Slide that) {
        Set<String> tagsA = this.getTags();
        Set<String> tagsB = that.getTags();
        int difA = Sets.difference(tagsA, tagsB).size();
        int difB = Sets.difference(tagsB, tagsA).size();
        int common = Sets.intersection(tagsA, tagsB).size();

        return min(common, min(difA, difB));
    }

    public Set<String> getTags() {
        if (cachedTags == null) {
            cachedTags = photos.stream().map(Photo::getTags).flatMap(Set::stream).collect(toSet());
        }
        return cachedTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Slide slide = (Slide) o;
        return Objects.equal(photos, slide.photos);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(photos);
    }

    public String print() {
        return photos.stream().map(Photo::getId).map(Object::toString).collect(Collectors.joining(" "));
    }
}
