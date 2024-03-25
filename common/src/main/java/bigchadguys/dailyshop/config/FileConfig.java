package bigchadguys.dailyshop.config;

import bigchadguys.dailyshop.DailyShopMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;

public abstract class FileConfig extends Config {

    public abstract String getPath();

    private Path getConfigFile() {
        return Path.of("config", DailyShopMod.ID, this.getPath() + ".json");
    }

    @Override
    public void write() throws IOException {
        this.writeFile(this.getConfigFile(), this);
    }

    @Override
    public <T extends Config> T read() {
        try {
            return this.readFile(this.getConfigFile(), this.getClass());
        } catch(FileNotFoundException ignored) {
            this.reset();

            try {
                this.write();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return (T)this;
    }

}
