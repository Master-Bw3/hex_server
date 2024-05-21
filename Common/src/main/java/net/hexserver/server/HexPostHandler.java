package net.hexserver.server;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.hexserver.HexHandlerClient;
import net.hexserver.HexHandlerServer;
import net.hexserver.HexServer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HexPostHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        InputStreamReader isr = new InputStreamReader(he.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(query, parameters);


        //update hex
        if (parameters.containsKey("SNBT")) {
            String hexSNBT = (String) parameters.get("SNBT");
            HexHandlerClient.INSTANCE.castHex(hexSNBT, he);
            //HexHandlerClient handles sending a response
        } else {
            String body = "Bad Request: invalid SNBT data";
            he.sendResponseHeaders(400, body.length());
            OutputStream os = he.getResponseBody();
            os.write(body.getBytes());
            os.close();
        }
    }

    public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}