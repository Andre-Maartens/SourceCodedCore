package sourcecoded.core.lib.proxy;

public interface IProxy {

    public void preInit();
    public void init();
    public void postInit();

    public void c_renderer();
    public void c_keybinds();
}
