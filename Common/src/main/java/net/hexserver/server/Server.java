package net.hexserver.server;

import com.sun.net.httpserver.HttpServer;
import dev.architectury.event.events.client.ClientPlayerEvent;
import net.hexserver.HexServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    HttpServer server;

    public Server() {
        int port = 9000;
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new RootHandler());
            server.createContext("/hexPost", new HexPostHandler());
            server.createContext("/hexDebug", new HexDebugHandler());

            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.server = server;
    }

    public void stop() {
        this.server.stop(0);
    }
}
