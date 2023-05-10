package com.akami.core.helpers;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Objects;

@Component
public class APIHelper {
    @SneakyThrows
    public String getFileContent(String filePathByData) {
        File file = new File(Objects.requireNonNull(APIHelper.class.getClassLoader().getResource(filePathByData)).getFile());
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }
}
