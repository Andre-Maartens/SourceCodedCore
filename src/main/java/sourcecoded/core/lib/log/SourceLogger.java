package sourcecoded.core.lib.log;

import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import static org.apache.logging.log4j.Level.*;

public class SourceLogger {

    public String id;

    public SourceLogger(String id) {
        this.id = id;
    }

    public void log(Level logLevel, Object logObj) {
        FMLLog.log(id, logLevel, String.valueOf(logObj));
    }

    public void all(Object logObj) {
        log(ALL, logObj);
    }

    public void fatal(Object logObj) {
        log(FATAL, logObj);
    }

    public void error(Object logObj) {
        log(ERROR, logObj);
    }

    public void warn(Object logObj) {
        log(WARN, logObj);
    }

    public void info(Object logObj) {
        log(INFO, logObj);
    }
}