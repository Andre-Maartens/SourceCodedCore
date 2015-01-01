package sourcecoded.core.core.launch.hooks.deps;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.launchwrapper.LaunchClassLoader;
import sourcecoded.core.lib.SCCSharedData;
import sourcecoded.core.lib.file.FileDownloader;
import sourcecoded.core.lib.launch.ILaunchCallHook;
import sourcecoded.core.lib.log.SourceLogger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public enum DependencyLoader implements ILaunchCallHook {
    INSTANCE;

    SourceLogger depLogger = new SourceLogger("SourceCodedCore|DepLoader");

    public List<Dependency> loadedDeps = new ArrayList<Dependency>();

    @Override
    public void call() {
        try {
            File sccRoot = SCCSharedData.getSCCRoot();
            sccRoot.mkdir();

            List<Dependency> loaded = loadedDeps(sccRoot);
            List<Dependency> allDeps = depsRequiringLoading(loaded);

            List<Dependency> newDeps = new ArrayList<Dependency>();

            for (Dependency dep : allDeps) {
                if (dep.isNew) {
                    depLogger.info("Dependency Found that requires Downloading: " + dep.name + "@" + dep.revision);
                    newDeps.add(dep);
                }
            }

            downloadDeps(newDeps, sccRoot);
            loadDeps(allDeps);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void downloadDeps(List<Dependency> newDeps, File output) {
        DownloadGUI gui = null;
        if (newDeps.size() != 0) {
            gui = DownloadGUI.createAndShowGUI(newDeps.size());

            for (Dependency dep : newDeps) {
                try {
                    depLogger.info("Starting download of dependency: " + dep.name + "@" + dep.revision);
                    gui.transition(dep.name);

                    File f = FileDownloader.downloadFile(new URL(dep.downloadURL), output);
                    dep.attach(f);
                    depLogger.info("Finished download of dependency: " + dep.name + "@" + dep.revision + " [" + f.getName() + "]");
                } catch (Exception e) {
                    depLogger.error("Could not download dependency: " + dep.name + "@" + dep.revision);
                    e.printStackTrace();
                }
            }

            gui.finish();
        }
    }

    void loadDeps(List<Dependency> deps) {
        LaunchClassLoader loader = (LaunchClassLoader) SCCSharedData.blackboard.get("classLoader");
        for (Dependency dep : deps) {
            if (dep.attachedFile != null) {
                try {
                    loader.addURL(dep.attachedFile.toURI().toURL());
                    this.loadedDeps.add(dep);
                } catch (MalformedURLException e) {
                    depLogger.error("Couldn't add a dep to classpath due to a Malformed URL. This should never happen. Weird.");
                }
            }
        }
    }

    List<Dependency> loadedDeps(File root) {
        FilenameFilter ff = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        };

        File[] deps = root.listFiles(ff);

        ArrayList<Dependency> loadedDeps = new ArrayList<Dependency>();

        for (File dep : deps) {
            try {
                Dependency dependency = Dependency.detach(dep);
                loadedDeps.add(dependency);
            } catch (IOException e) { //Could not load
            }
        }

        return loadedDeps;
    }

    List<Dependency> depsRequiringLoading(List<Dependency> loadedDeps) throws IOException {
        File modsDir = new File(SCCSharedData.getForgeRoot(), "mods");

        FilenameFilter ff = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        };

        File[] mods = modsDir.listFiles(ff);

        ArrayList<Dependency> depsForLoading = new ArrayList<Dependency>();

        depsForLoading.addAll(loadedDeps);

        for (File mod : mods) {
            JarFile jar = new JarFile(mod);
            if (jar.getManifest() != null) {
                Manifest man = jar.getManifest();
                Attributes main = man.getMainAttributes();
                if (main.containsKey("SCCDeps")) {
                    String json = main.getValue("SCCDeps");
                    parseDependencies(depsForLoading, json);
                }
            }
        }

        return depsForLoading;
    }

    void parseDependencies(ArrayList<Dependency> deps, String json) {
        JsonArray depArray = (JsonArray) new JsonParser().parse(json);
        ArrayList<Dependency> newDeps = new ArrayList<Dependency>();
        for (int i = 0; i < depArray.size(); i++) {
            JsonObject object = depArray.get(i).getAsJsonObject();
            if (object.has("downloadURL")) {
                Dependency dep = new Dependency();
                dep.revision = object.get("revision").getAsInt();
                dep.name = object.get("name").getAsString();
                dep.downloadURL = object.get("downloadURL").getAsString();
                dep.isNew = true;
                //deps.add(dep);

                Iterator<Dependency> loadedIt = deps.iterator();
                while (loadedIt.hasNext()) {
                    Dependency loaded = loadedIt.next();
                    if (loaded.name.equalsIgnoreCase(dep.name)) {
                        if (dep.revision > loaded.revision) {
                            //Required dep is newer
                            loadedIt.remove();
                            loaded.attachedFile.delete();
                            newDeps.add(dep);
                        }
                    }
                }
            }
        }

        deps.addAll(newDeps);
    }

}