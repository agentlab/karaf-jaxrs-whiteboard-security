package ru.agentlab.security.tests.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileUtil {

    public static String readFileFromResourses(String fileName) throws UncheckedIOException {
        try {
            return readFile(Paths.get(FileUtil.class.getClassLoader().getResource(fileName).getFile()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String readFile(Path filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(filePath, StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }

        return contentBuilder.toString();
    }
}
