package de.appdynamics.ace.apdex.util.api;

/**
 * Created by stefan.marx on 19.03.15.
 */
public class APIProxy {


    String username;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

    String host;
    int port;

    public APIProxy(String url) {
        if (url.contains(":")) {
            setPort(url.substring(url.lastIndexOf(':') + 1, url.length()));
            setHost(url.substring(0,url.lastIndexOf(':')));
        }   else {
            setHost(url);
            setPort(8080);
        }
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    String password;
}
