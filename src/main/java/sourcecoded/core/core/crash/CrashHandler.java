package sourcecoded.core.core.crash;

import net.minecraftforge.fml.common.ICrashCallable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import sourcecoded.core.core.launch.hooks.deps.Dependency;
import sourcecoded.core.core.launch.hooks.deps.DependencyLoader;
import sourcecoded.core.lib.SCCSharedData;

public enum CrashHandler implements ICrashCallable {
    INSTANCE;

    @Override
    public String getLabel() {
        return "SourceCodedCore";
    }

    @Override
    public String call() throws Exception {
        StringBuilder builder = new StringBuilder();

        builder.append("SourceCodedCore detected crash. Details are as follows: \n");
        builder.append("\t\tLoaded SCC Mods: \n");

        injectModData(builder, SCCSharedData.getContainer(SCCSharedData.getString("id")));

        for (ModContainer container : Loader.instance().getModList()) {
            for (ArtifactVersion vers : container.getRequirements())
                if (vers.getLabel().contains(SCCSharedData.getString("id")))
                    injectModData(builder, container);
        }

        builder.append("\t\tLoaded SCC Deps: \n");
        for (Dependency dep : DependencyLoader.INSTANCE.loadedDeps)
            injectDepData(builder, dep);

        builder.append("\t\tEnvironment status: \n");
        builder.append("\t\t\t" + (((Boolean)SCCSharedData.blackboard.get("devEnvironment")) ? "Development" : "Normal") + "\n");

        Runtime.getRuntime().addShutdownHook(new CrashUploaderThread());
        builder.append("\t\tCrash Shutdown Hook applied");

        return builder.toString();
    }

    void injectModData(StringBuilder builder, ModContainer container) {
        builder.append("\t\t\t" + container.getModId() + "@" + container.getVersion());
        builder.append("\n");
    }

    void injectDepData(StringBuilder builder, Dependency dependency) {
        builder.append("\t\t\t" + dependency.name + "@" + dependency.revision);
        if (dependency.downloadURL != null)
            builder.append("\t{depURL: " + dependency.downloadURL + "}");
        builder.append("\n");
    }
}
