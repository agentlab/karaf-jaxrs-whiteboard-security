/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package ru.agentlab.security.tests.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;

public class FileUtil {

    public static String readFile(URL url) throws UncheckedIOException {
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
