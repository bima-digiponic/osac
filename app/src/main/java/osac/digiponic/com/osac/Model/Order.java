package osac.digiponic.com.osac.Model;

public class Order {

    public static final String TABLE_NAME = "transaction";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPES_ID = "types_id";
    public static final String COLUMN_TYPES_NAME = "types_name";
    public static final String COLUMN_SERVICES_ID = "services_id";
    public static final String COLUMN_SERVICES_NAME = "services_name";
    public static final String COLUMN_SERVICES_PRICE = "services_price";

    private int id;
    private String types_id;
    private String types_name;
    private String services_id;
    private String services_name;
    private String services_price;

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TYPES_ID + " TEXT, " +
            COLUMN_TYPES_NAME + " TEXT, " +
            COLUMN_SERVICES_ID + " TEXT, " +
            COLUMN_SERVICES_NAME + " TEXT, " +
            COLUMN_SERVICES_PRICE + " TEXT, " + ")";

    public Order() {
    }

    public Order(String types_id, String types_name, String services_id, String services_name, String services_price) {
        this.types_id = types_id;
        this.types_name = types_name;
        this.services_id = services_id;
        this.services_name = services_name;
        this.services_price = services_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypes_id() {
        return types_id;
    }

    public void setTypes_id(String types_id) {
        this.types_id = types_id;
    }

    public String getTypes_name() {
        return types_name;
    }

    public void setTypes_name(String types_name) {
        this.types_name = types_name;
    }

    public String getServices_id() {
        return services_id;
    }

    public void setServices_id(String services_id) {
        this.services_id = services_id;
    }

    public String getServices_name() {
        return services_name;
    }

    public void setServices_name(String services_name) {
        this.services_name = services_name;
    }

    public String getServices_price() {
        return services_price;
    }

    public void setServices_price(String services_price) {
        this.services_price = services_price;
    }
}
