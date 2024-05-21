# Hex Server

Hex Server is a utility mod for Hex Casting that allows for hexes to be cast remotely via http requests. The primary purpose for this is to allow programs like [Hexagon](https://github.com/Master-Bw3/Hexagon/tree/Dev?tab=readme-ov-file) to execute hexes ingame for testing purposes.

## Usage

### Casting a Hex
1. Launch Minecraft with this mod installed and enter a world
2. Send an http post request to the server on port 9000 to `*/hexPost`
   ```bash
   $ curl -X POST localhost:9000/hexPost -d "SNBT={\"hexcasting:type\": \"hexcasting:list\", \"hexcasting:data\":  ..."
   ```
   The request body must contain a key named `SNBT` with the corresponding value being a list iota in SNBT format
3. When the request is received, the hex will be automatically cast by the player
4. A response will be sent back with a string representation of the resultant stack from the cast

### Debugging a Hex
1. Launch Minecraft with this mod and [HexDebug](https://github.com/object-Object/HexDebug) installed and enter a world
2. Start a debugging session in your IDE
3. Send an http post request to the server on port 9000 to `*/hexDebug`
   ```bash
   $ curl -X POST localhost:9000/hexDebug -d "SNBT={\"hexcasting:type\": \"hexcasting:list\", \"hexcasting:data\":  ..."
   ```
   The request body must contain a key named `SNBT` with the corresponding value being a list iota in SNBT format
4. A response will be sent back informing you that the hex has been received
5. The hex will then be loaded by the debugger
