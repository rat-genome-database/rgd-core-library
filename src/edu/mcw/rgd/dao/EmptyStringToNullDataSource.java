package edu.mcw.rgd.dao;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.logging.Logger;

/**
 * DataSource wrapper that sends an empty string statement parameter as NULL, as Oracle does.
 * <p>
 * Oracle stores '' as NULL; PostgreSQL keeps '' as an empty string, and rgd_dev rejects '' in every
 * text column (ADR-017). Code written for Oracle often passes "" for "no value" (blank form fields,
 * pipeline fields), so every connection from {@link DataSourceFactory} goes through this wrapper:
 * setString, setNString and setObject with "" become setNull. Search conditions behave as in Oracle too:
 * "col = ?" with "" matches nothing. Spaces and all other values are passed through unchanged.
 */
public class EmptyStringToNullDataSource implements DataSource {

    private final DataSource target;

    public EmptyStringToNullDataSource(DataSource target) {
        this.target = target;
    }

    public DataSource getTarget() {
        return target;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return wrapConnection(target.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return wrapConnection(target.getConnection(username, password));
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return target.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        target.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        target.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return target.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return target.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return iface.isInstance(this) ? iface.cast(this) : target.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this) || target.isWrapperFor(iface);
    }

    static Connection wrapConnection(Connection conn) {
        if( conn == null ) {
            return null;
        }
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class}, (proxy, method, args) -> {
            if( isIdentityMethod(method) ) {
                return identity(proxy, method, args);
            }
            Object result = invoke(conn, method, args);
            if( result instanceof CallableStatement ) {
                return wrapStatement((CallableStatement) result, CallableStatement.class);
            }
            if( result instanceof PreparedStatement ) {
                return wrapStatement((PreparedStatement) result, PreparedStatement.class);
            }
            return result;
        });
    }

    static <T extends PreparedStatement> T wrapStatement(T ps, Class<T> iface) {
        InvocationHandler handler = (proxy, method, args) -> {
            if( isIdentityMethod(method) ) {
                return identity(proxy, method, args);
            }
            if( args != null && args.length >= 2 && "".equals(args[1]) ) {
                String name = method.getName();
                if( name.equals("setString") || name.equals("setNString") || name.equals("setObject") ) {
                    // setObject(index, value, targetSqlType[, scale]) keeps its target type; otherwise VARCHAR
                    int sqlType = name.equals("setNString") ? Types.NVARCHAR
                            : (args.length >= 3 && args[2] instanceof Integer) ? (Integer) args[2] : Types.VARCHAR;
                    if( args[0] instanceof Integer ) {
                        ps.setNull((Integer) args[0], sqlType);
                        return null;
                    }
                    if( args[0] instanceof String && ps instanceof CallableStatement ) {
                        ((CallableStatement) ps).setNull((String) args[0], sqlType);
                        return null;
                    }
                }
            }
            return invoke(ps, method, args);
        };
        return iface.cast(Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, handler));
    }

    private static boolean isIdentityMethod(Method method) {
        return (method.getName().equals("equals") && method.getParameterCount() == 1)
                || (method.getName().equals("hashCode") && method.getParameterCount() == 0);
    }

    // a wrapper equals only itself, so connection and statement identity checks keep working
    private static Object identity(Object proxy, Method method, Object[] args) {
        return method.getName().equals("equals") ? (Object) (proxy == args[0]) : (Object) System.identityHashCode(proxy);
    }

    private static Object invoke(Object target, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(target, args);
        } catch( InvocationTargetException e ) {
            throw e.getCause();
        }
    }
}
