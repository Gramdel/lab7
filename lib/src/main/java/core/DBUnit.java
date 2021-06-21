package core;

import collection.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.sql.Types.NULL;
import static java.sql.Types.OTHER;

public class DBUnit {
    private final String url;
    private final String username;
    private final String password;
    private Connection connection;
    private final Logger logger;

    public DBUnit(String url, String username, String password, Logger logger) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.logger = logger;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void loadCollectionFromDB(LinkedHashSet<Product> collection, ArrayList<Organization> organizations) {
        System.out.println("Пытаемся загрузить коллекцию из базы данных...");
        try {
            PreparedStatement statement = getConnection().prepareStatement("select * from lab7_collection");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Product product = new Product(result.getString(2), new Coordinates(result.getDouble(3), result.getLong(4)), result.getFloat(5), result.getString(6), result.getFloat(7), UnitOfMeasure.fromString(result.getString(8)), new Organization(result.getString(9), result.getObject(10, Long.class), result.getObject(11, Long.class), result.getString(12) == null ? null : OrganizationType.fromString(result.getString(12))));
                if (Creator.createProduct(product, false) == null) {
                    System.out.println("Элемент с id " + result.getLong(1) + " не загружен из-за перечисленных выше ошибок формата данных!");
                } else {
                    Optional<Organization> optional = organizations.stream().filter(x -> x.equals(product.getManufacturer())).findAny();
                    if (optional.isPresent()) {
                        product.setManufacturer(optional.get());
                    } else {
                        product.getManufacturer().createId(organizations);
                        organizations.add(product.getManufacturer());
                    }
                    product.setId(result.getLong(1));
                    collection.add(product);
                }
            }
            System.out.println("Загружено элементов: " + collection.size() + ".");
        } catch (SQLException e) {
            System.out.println("При загрузке коллекции из базы данных возникла ошибка SQL: " + e.getMessage());
            logger.log(Level.WARNING, "При загрузке коллекции из базы данных возникла ошибка SQL: " + e.getMessage());
        }
    }

    public boolean addProductToDB(Product product) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("insert into lab7_collection values (?,?,?,?,?,?,?,?,?,?,?,?)");
            statement.setLong(1, product.getId());
            statement.setString(2, product.getName());
            statement.setDouble(3, product.getCoordinates().getX());
            statement.setLong(4, product.getCoordinates().getY());
            statement.setDouble(5, product.getPrice());
            statement.setString(6, product.getPartNumber());
            statement.setDouble(7, product.getManufactureCost());
            statement.setObject(8, product.getUnitOfMeasure(), OTHER);
            statement.setString(9, product.getManufacturer().getName());
            try {
                statement.setLong(10, product.getManufacturer().getAnnualTurnover());
            } catch (NullPointerException e) {
                statement.setNull(10, NULL);
            }
            try {
                statement.setLong(11, product.getManufacturer().getEmployeesCount());
            } catch (NullPointerException e) {
                statement.setNull(11, NULL);
            }
            try {
                statement.setObject(12, product.getManufacturer().getType(), OTHER);
            } catch (NullPointerException e) {
                statement.setNull(12, NULL);
            }
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            logger.log(Level.WARNING, "При добавлении элемента в базу данных возникла ошибка SQL: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProductInDB(Product product) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("update lab7_collection set name = ?, x = ?, y = ?, price = ?, \"partNumber\" = ?, \"manufactureCost\" = ?, \"unitOfMeasure\" = ?, \"manufacturerName\" = ?, \"annualTurnover\" = ?, \"employeesCount\" = ?, type = ? where id = " + product.getId());
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getCoordinates().getX());
            statement.setLong(3, product.getCoordinates().getY());
            statement.setDouble(4, product.getPrice());
            statement.setString(5, product.getPartNumber());
            statement.setDouble(6, product.getManufactureCost());
            statement.setObject(7, product.getUnitOfMeasure(), OTHER);
            statement.setString(8, product.getManufacturer().getName());
            try {
                statement.setLong(9, product.getManufacturer().getAnnualTurnover());
            } catch (NullPointerException e) {
                statement.setNull(9, NULL);
            }
            try {
                statement.setLong(10, product.getManufacturer().getEmployeesCount());
            } catch (NullPointerException e) {
                statement.setNull(10, NULL);
            }
            try {
                statement.setObject(11, product.getManufacturer().getType(), OTHER);
            } catch (NullPointerException e) {
                statement.setNull(11, NULL);
            }
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            logger.log(Level.WARNING, "При обновлении элемента в базе данных возникла ошибка SQL: " + e.getMessage());
            return false;
        }
    }

    public boolean removeProductFromDB(Product product) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("delete from lab7_collection where id = " + product.getId());
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            logger.log(Level.WARNING, "При удалении элемента из базы данных возникла ошибка SQL: " + e.getMessage());
            return false;
        }
    }
}
