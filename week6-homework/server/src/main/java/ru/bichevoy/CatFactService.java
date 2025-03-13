package ru.bichevoy;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class CatFactService {

    /**
     * Получение факта о котике из внешнего ресурса
     * @return
     * @throws IOException
     */
    public String getCatFact() throws IOException {
        Properties properties = ServerStarter.getProperties();
        URL url = new URL(properties.getProperty("CAT_SITE"));
        CatFact catFact = new ObjectMapper().readValue(url, CatFact.class);
        serializationCatFact(catFact);
        saveJSONCatFact(catFact);
        return catFact.getFact();
    }

    /**
     * Сохранение состояния класса CatFact в файл с помощью стандартной сериализации.
     * @param catFact
     */
    private void serializationCatFact(CatFact catFact) {
        Properties properties = ServerStarter.getProperties();
        File file = new File(getNextFileName(properties.getProperty("SERIALIZATION_FILES")) + ".ser");
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream out = new ObjectOutputStream(fos)) {
            out.writeObject(catFact);
        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    /**
     * Сохранение состояния класса CatFact в файл с помощью jackson библиотеки.
     * @param catFact
     */
    private void saveJSONCatFact(CatFact catFact) {
        ObjectMapper mapper = new ObjectMapper();
        Properties properties = ServerStarter.getProperties();

        try {
            String nextFileName = getNextFileName(properties.getProperty("JSON_FILES")) + ".json";
            mapper.writeValue(new File(nextFileName), catFact);
        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод получения очередного имени файла.
     * @param path = каталог куда планируется сохранять файл
     * @return
     */
    private String getNextFileName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        List<String> collect = Arrays.stream(files).map(File::getName).sorted().collect(Collectors.toList());
        String newFileName = String.format("%s/CatFact-1", file.getAbsolutePath());
        if (!collect.isEmpty()) {
            String filename = collect.get(collect.size() - 1);
            Pattern pattern = Pattern.compile("\\d+"); // Ищем число
            Matcher matcher = pattern.matcher(filename);
            if (matcher.find()) {
                int number = Integer.parseInt(matcher.group()); // Преобразуем в int
                newFileName = String.format("%s%sCatFact-%d", file.getAbsolutePath(), File.separator, ++number);
            }
        }
        return newFileName;
    }
}
