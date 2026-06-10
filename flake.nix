{
  description = "A Nix-flake-based Java development environment";

  inputs.nixpkgs.url = "https://flakehub.com/f/NixOS/nixpkgs/0.1";

  inputs.nixpkgs-idea2025-1.url = "github:nixos/nixpkgs/4eec65df50d796a8fd9c146a09b5007916ce376b";

  outputs = inputs:
    let
      javaVersion = 21; # Change this value to update the whole stack

 

      supportedSystems = [ "x86_64-linux" "aarch64-linux" "x86_64-darwin" "aarch64-darwin" ];
      forEachSupportedSystem = f: inputs.nixpkgs.lib.genAttrs supportedSystems (system: f {
        pkgs = import inputs.nixpkgs {
          inherit system;
          overlays = [ inputs.self.overlays.default ];
        };
        pkgsIdea = import inputs.nixpkgs-idea2025-1 {
            inherit system;
            overlays = [ inputs.self.overlays.default ];
          };
        });
    in
    {
      overlays.default = final: prev:
        let
          jdk = prev."jdk${toString javaVersion}";
        in
        {
          inherit jdk;
          maven = prev.maven.override { jdk_headless = jdk; };
          gradle = prev.gradle.override { java = jdk; };
          lombok = prev.lombok.override { inherit jdk; };
        };

      devShells = forEachSupportedSystem ({ pkgs, pkgsIdea }: {
        default = pkgs.mkShell {
          packages = with pkgs; [
            gcc
            gradle
            jdk
            maven
            ncurses
            patchelf
            zlib
            libglvnd
            # pkgsIdea.jetbrains.idea-oss
            jetbrains.jdk-21
          ];

          shellHook =
            let
              prev = "\${JAVA_TOOL_OPTIONS:+ $JAVA_TOOL_OPTIONS}";
            in
            ''
              export LD_LIBRARY_PATH="''${LD_LIBRARY_PATH}''${LD_LIBRARY_PATH:+:}${pkgs.libglvnd}/lib"
              export JAVA_TOOL_OPTIONS="${prev}"
              export JAVA_HOME="${pkgs.jetbrains.jdk-21}"
            '';
        };
      });
    };
}