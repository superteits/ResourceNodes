package br.github.superteits.resourcenodes;

public class AutoSave {

    boolean enabled;
    long interval;

    public AutoSave(boolean enabled, long interval) {
        this.enabled = enabled;
        this.interval = interval;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public long getInterval() {
        return this.interval;
    }
}
