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


public class IO {
    public static String stringFromStream(InputStream inputStream) throws IOException {
        Reader reader = new InputStreamReader(inputStream);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        for (int length = 0; (length = reader.read(buffer)) > 0; ) {
            writer.write(buffer, 0, length);
        }
        reader.close();
        writer.close();
        inputStream.close();
        return writer.toString();
    }
}
