package de.ketchupbombe.enums;

/**
 * @author Ketchupbombe
 */
public enum MySQLTable {

    LOCATION("FFA_Locations"),
    MAPS("FFA_Maps");

    private String tablename;

    MySQLTable(String tablename) {
        this.tablename = tablename;
    }

    public String getTablename() {
        return tablename;
    }
}
