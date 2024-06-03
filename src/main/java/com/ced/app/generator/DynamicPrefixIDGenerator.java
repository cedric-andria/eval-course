package com.ced.app.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class DynamicPrefixIDGenerator implements IdentifierGenerator{
    // private final String prefix;
    private String sequencename;

    // public DynamicPrefixIDGenerator(String prefix, String sequencename) {
    //     this.prefix = prefix.contains("noprefix") ? prefix : "";
    //     this.sequencename = sequencename;
    // }
    public DynamicPrefixIDGenerator(String sequencename) {
        this.sequencename = sequencename;
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = session.getJdbcConnectionAccess().obtainConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT nextval('" + sequencename + "')");

            if (rs.next()) {
                int idValue = rs.getInt(1);
                return "" + idValue;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
