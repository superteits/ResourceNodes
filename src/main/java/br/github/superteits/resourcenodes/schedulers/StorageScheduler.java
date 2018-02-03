package br.github.superteits.resourcenodes.schedulers;

import br.github.superteits.resourcenodes.ResourceNodes;
import br.github.superteits.resourcenodes.storage.H2Storage;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

public class StorageScheduler {

    public void scheduleStorageUpdate() {
        Task.builder()
                .execute(() -> {
                    ResourceNodes.INSTANCE.getDatabase().updateLocations();
                    ResourceNodes.INSTANCE.getStorageQueue().get(H2Storage.ActionsEnum.REMOVE).clear();
                    ResourceNodes.INSTANCE.getStorageQueue().get(H2Storage.ActionsEnum.SAVE).clear();
                })
                .interval(ResourceNodes.INSTANCE.getConfig().getAutoSave().getInterval(), TimeUnit.SECONDS)
                .submit(ResourceNodes.INSTANCE);

    }

}
