///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.5.0

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "generate", mixinStandardHelpOptions = true, version = "1.0.0",
        description = "generate a business component")

public class generatecli implements Callable<Integer> {

    public static final String SRC_DIR = "./src/main/java";
    public static final String BASE_PACKAGE = "ch.dvbern.stip";

    @Parameters(index = "0", description = "Component name")
    private String name;

    public static void main(String... args) {
        int exitCode = new CommandLine(new generatecli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        try {
            createPackages();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    private void createPackages() throws IOException {
        var layers = new String[]{"entity", "repo", "resource", "service"};
        for (String layer : layers) {

            var layerPackageName = String.format("%s.%s.%s", BASE_PACKAGE, name, layer);
            var packagePath = Path.of(SRC_DIR, layerPackageName.replaceAll("\\.", "/"));

            if (!Files.exists(packagePath)) {
                Files.createDirectories(packagePath);
                System.out.printf("CREATE %s%n", packagePath.toString());
            }
        }
    }
}

