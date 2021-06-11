package collection;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashSet;

public class Product implements Comparable<Product>, Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float price; //Значение поля должно быть больше 0
    private String partNumber; //Значение этого поля должно быть уникальным, Поле не может быть null
    private Float manufactureCost; //Поле не может быть null
    private UnitOfMeasure unitOfMeasure; //Поле не может быть null
    private Organization manufacturer; //Поле может быть null
    private LinkedHashSet<Product> collection = new LinkedHashSet<>();

    public Product(LinkedHashSet<Product> collection){
        createId();
        this.creationDate = ZonedDateTime.now();
        this.collection = collection;
    }

    public Product(String name, Coordinates coordinates, float price, String partNumber, Float manufactureCost, UnitOfMeasure unitOfMeasure, Organization manufacturer,LinkedHashSet<Product> collection) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.price = price;
        this.partNumber = partNumber;
        this.manufactureCost = manufactureCost;
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = manufacturer;
        this.collection = collection;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\tid = " + id + ",\n" +
                "\tname = " + name + ",\n" +
                "\tcoordinates = " + coordinates + ",\n" +
                "\tcreationDate = " + (DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(creationDate)) + ",\n" +
                "\tprice = " + price + ",\n" +
                "\tpartNumber = " + partNumber + ",\n" +
                "\tmanufactureCost = " + manufactureCost + ",\n" +
                "\tunitOfMeasure = " + unitOfMeasure + ",\n" +
                "\tmanufacturer = " + manufacturer + "\n" +
                "}";
    }

    public String toStringForCSV() {
        return id + "," + name + "," + coordinates.toStringForCSV() + "," + price + "," + partNumber + "," + manufactureCost + "," + unitOfMeasure + "," + manufacturer.toStringForCSV();
    }

    public String getPartNumber() {
        return partNumber;
    }

    public Long getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Organization getManufacturer() {
        return manufacturer;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public int compareTo(Product product) {
        return this.creationDate.compareTo(product.creationDate);
    }

    public static Comparator<Product> byIdComparator = (p1, p2) -> {
        if (p1.id.equals(p2.id)) {
            System.out.println("Что-то пошло не так: у двух продуктов один id!");
            return 0;
        }
        return (p1.id < p2.id) ? -1 : 1;
    };

    public static Comparator<Product> byPriceComparator = (p1, p2) -> Float.compare(p1.price, p2.price);

    public void createId() {
        Long id = 1L;
        boolean isUnique;
        do {
            isUnique = true;
            for (Product product : collection) {
                if (product.getId() != null && product.getId().equals(id)) {
                    isUnique = false;
                    id++;
                    break;
                }
            }
        } while (!isUnique);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Float getManufactureCost() {
        return manufactureCost;
    }

    public void setManufacturer(Organization manufacturer) {
        this.manufacturer = manufacturer;
    }
}