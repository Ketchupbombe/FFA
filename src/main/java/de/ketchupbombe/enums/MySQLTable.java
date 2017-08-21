package de.ketchupbombe.enums;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public enum MySQLTable {

    LOCATION("FFA_Locations"),
    MAPS("FFA_Maps"),
    KIT("FFA_Kits");

    private String tablename;

    MySQLTable(String tablename) {
        this.tablename = tablename;
    }

    /**
     * To get name of table
     *
     * @return name of table
     */
    public String getTablename() {
        return tablename;
    }
}
