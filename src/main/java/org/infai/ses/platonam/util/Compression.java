/*
 * Copyright 2021 InfAI (CC SES)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.infai.ses.platonam.util;

import java.io.*;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.infai.ses.platonam.util.IO.stringFromStream;

public class Compression {
    public static String decompressToString(String data) throws IOException {
        return stringFromStream(new GZIPInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(data))));
    }

    public static InputStream decompressToStream(String data) throws IOException {
        return new GZIPInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(data)));
    }

    public static String compress(String str) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(Base64.getEncoder().withoutPadding().wrap(outputStream));
        gzipOutputStream.write(str.getBytes());
        gzipOutputStream.close();
        outputStream.close();
        return outputStream.toString();
    }

    public static OutputStream compress(OutputStream outputStream) throws IOException {
        return new GZIPOutputStream(Base64.getEncoder().withoutPadding().wrap(outputStream));
    }
}
