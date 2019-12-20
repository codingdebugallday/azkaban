package org.abigballofmud.azkaban.plugin.datax.entity;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2019/12/19 16:29
 * @since 1.0
 */
public class DataxReader {
    private String name;
    private String username;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
