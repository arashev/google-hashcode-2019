package com.abcde.hashcode;

import com.abcde.hashcode.domain.Photo;
import com.abcde.hashcode.domain.Slide;
import com.abcde.hashcode.service.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.nio.file.StandardOpenOption.*;

public final class Main {

    public static void main(String[] args) throws IOException {
        process("a_example");
        process("b_lovely_landscapes");
        process("c_memorable_moments");
        process("d_pet_pictures");
        process("e_shiny_selfies");
    }

    private static void process(String file) throws IOException {
        List<Photo> photos = new Parser().load(Paths.get("./" + file + ".txt"));
        List<Slide> slides = OptimalAlgorithm.apply(photos);

        StringBuilder result = new StringBuilder();
        result.append(slides.size()).append("\n");
        for (Slide slide : slides) {
            result.append(slide.print()).append("\n");
        }

        Path output = Paths.get("./results/" + file + "_result.txt");
        Files.write(output, result.toString().getBytes(StandardCharsets.UTF_8), CREATE, WRITE, TRUNCATE_EXISTING);
    }
}
