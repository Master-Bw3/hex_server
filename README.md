# Hex Server

Hex Server is a utility mod for Hex Casting that allows for hexes to be cast remotely via http requests. The primary purpose for this is to allow programs like [Hexagon](https://github.com/Master-Bw3/Hexagon/tree/Dev?tab=readme-ov-file) evaluate hexes ingame for testing purposes.

## Usage
1. Launch Minecraft with this mod installed and enter a world
2. Send an http post request to the server on port 9000
   ```bash
   $ curl -X POST localhost:9000/hexPost -d "SNBT={\"hexcasting:type\": \"hexcasting:list\", \"hexcasting:data\":  ..."
   ```
   The request body must contain a key named `SNBT` with the corresponding value being a list iota in SNBT format
3. When the request is recieved, the hex will be automatically cast by the player
4. A response will be sent back with a string representation of the resultant stack from the cast
