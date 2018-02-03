package br.github.superteits.resourcenodes.config;

import br.github.superteits.resourcenodes.AutoSave;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class AutoSaveSerializer {

        public AutoSave deserialize(ConfigurationNode value) {
            boolean enabled = value.getNode("enabled").getBoolean();
            long interval = value.getNode("interval").getLong();
            return new AutoSave(enabled, interval);
        }

        public void serialize(CommentedConfigurationNode value) {
            value.getNode("enabled").setValue(true);
            value.getNode("enabled").setComment("Enable auto save task.");
            value.getNode("interval").setValue(600);
            value.getNode("interval").setComment("Interval of the auto save task in seconds.");
        }

}
