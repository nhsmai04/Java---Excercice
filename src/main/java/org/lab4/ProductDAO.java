package org.lab4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO {
    private static final String DEFAULT_DB_URL = "jdbc:postgresql://localhost:5432/lab4";
    private final Map<String, String> envFileValues = loadEnvFile();

    public void initializeDatabase() throws SQLException {
        createTable();
    }

    private Connection getConnection() throws SQLException {
        String url = getConfigOrDefault("POSTGRES_URL", DEFAULT_DB_URL);
        String user = getConfig("POSTGRES_USER");
        String password = getConfig("POSTGRES_PASSWORD");

        if (user == null || user.isBlank()) {
            return DriverManager.getConnection(url);
        }

        return DriverManager.getConnection(url, user, password == null ? "" : password);
    }

    private String getConfigOrDefault(String name, String defaultValue) {
        String value = getConfig(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private String getConfig(String name) {
        String value = System.getenv(name);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return envFileValues.get(name);
    }

    private Map<String, String> loadEnvFile() {
        Map<String, String> values = new HashMap<>();
        Path envPath = findEnvPath();

        if (envPath == null) {
            return values;
        }

        try {
            for (String line : Files.readAllLines(envPath)) {
                String trimmedLine = line.trim();
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                    continue;
                }

                int separatorIndex = trimmedLine.indexOf('=');
                if (separatorIndex <= 0) {
                    continue;
                }

                String key = trimmedLine.substring(0, separatorIndex).trim();
                String value = trimmedLine.substring(separatorIndex + 1).trim();
                values.put(key, removeWrappingQuotes(value));
            }
        } catch (IOException ignored) {
            return values;
        }

        return values;
    }

    private Path findEnvPath() {
        Path[] candidates = {
                Path.of(".env"),
                Path.of("src", "main", ".env")
        };

        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }

        return null;
    }

    private String removeWrappingQuotes(String value) {
        if (value.length() >= 2) {
            boolean doubleQuoted = value.startsWith("\"") && value.endsWith("\"");
            boolean singleQuoted = value.startsWith("'") && value.endsWith("'");
            if (doubleQuoted || singleQuoted) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }

    private void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS products (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    price NUMERIC(10, 2) NOT NULL,
                    brand VARCHAR(100) NOT NULL,
                    description TEXT NOT NULL,
                    image_path VARCHAR(255) NOT NULL,
                    CONSTRAINT products_name_brand_unique UNIQUE (name, brand)
                )
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public List<Product> findAll() throws SQLException {
        String sql = """
                SELECT id, name, price, brand, description, image_path
                FROM products
                ORDER BY id
                """;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return mapProducts(resultSet);
        }
    }

    public List<Product> searchByNameOrBrand(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }

        String sql = """
                SELECT id, name, price, brand, description, image_path
                FROM products
                WHERE LOWER(name) LIKE ? OR LOWER(brand) LIKE ?
                ORDER BY id
                """;
        String keywordPattern = "%" + keyword.trim().toLowerCase() + "%";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, keywordPattern);
            preparedStatement.setString(2, keywordPattern);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapProducts(resultSet);
            }
        }
    }

    private List<Product> mapProducts(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(new Product(
                    String.valueOf(resultSet.getInt("id")),
                    resultSet.getString("name"),
                    resultSet.getDouble("price"),
                    resultSet.getString("brand"),
                    resultSet.getString("description"),
                    resultSet.getString("image_path")
            ));
        }
        return products;
    }
}
