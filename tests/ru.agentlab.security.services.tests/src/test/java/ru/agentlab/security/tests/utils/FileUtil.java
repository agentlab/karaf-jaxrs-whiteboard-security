package ru.agentlab.security.tests.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileUtil {

    public static String readFile(Path filePath) throws UncheckedIOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(filePath, StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return contentBuilder.toString().replaceAll("\\s+", "").replace("\t", "");
    }

    public static String readFromUrl(URL url) throws UncheckedIOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            while (reader.ready()) {
                contentBuilder.append(reader.readLine());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return contentBuilder.toString().replaceAll("\\s+", "").replace("\t", "");
    }
}
