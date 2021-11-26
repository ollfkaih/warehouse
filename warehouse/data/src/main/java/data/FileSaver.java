package data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * General file-saving class that will work for any object type, given a working ObjectMapper.
 */
public class FileSaver<T> implements DataPersistence<T> {
  private final ObjectMapper objectMapper;
  private final String folder;
  private final String fileExtension;
  private final TypeReference<T> type;

  public FileSaver(TypeReference<T> type, ObjectMapper objectMapper, String folder, String fileExtension) {
    this.type = type;
    this.objectMapper = objectMapper;
    this.folder = folder;
    this.fileExtension = fileExtension;
    try {
      ensureFolderExists();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public FileSaver(TypeReference<T> type, String folder) {
    this(type, DataUtils.createObjectMapper(), folder, "json");
  }

  @Override
  public Collection<T> loadAll() throws IOException {
    return Files.walk(getFolderPath())
        .filter(Files::isRegularFile)
        .map(Path::toFile)
        .map(file -> {
          try {
            return this.loadFromFile(file);
          } catch (IOException e) {
            System.err.println("ERROR: could not load from file '" + file.getAbsolutePath() + "'");
            e.printStackTrace();
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public T load(String key) throws IOException {
    Path path = getFilePath(key);   
    return loadFromFile(path.toFile());
  }

  private T loadFromFile(File file) throws IOException {
    if (!file.exists()) {
      return null;
    }
    try (InputStream inputStream = new FileInputStream(file);) {
      return objectMapper.readValue(inputStream, type);
    }
  }

  @Override
  public void save(T object, String key) throws IOException {
    Path path = getFilePath(key);    
    try (OutputStream outputStream = new FileOutputStream(path.toFile())) {
      objectMapper.writeValue(outputStream, object);
    }
  }

  @Override
  public void delete(String key) throws IOException {
    deleteFile(getFilePath(key));
  }

  @Override
  public void deleteAll() throws IOException {
    Files.walk(getFolderPath())
        .filter(Files::isRegularFile)
        .forEach(filePath -> {
          try {
            deleteFile(filePath);
          } catch (IOException e) {
            System.err.println("ERROR: Could not delete file: " + filePath.toString());
            e.printStackTrace();
          }
        });
  }

  private void deleteFile(Path filePath) throws IOException {
    Files.delete(filePath);
  }

  private Path getFilePath(String fileName) {
    return getFolderPath().resolve(fileName + "." + fileExtension);
  }

  private Path getFolderPath() {
    return Path.of(System.getProperty("user.home"), "warehouse", folder);
  }

  private void ensureFolderExists() throws IOException {
    Files.createDirectories(getFolderPath());
  }

  public boolean exists(String key) {
    return getFilePath(key).toFile().exists();
  }
}
