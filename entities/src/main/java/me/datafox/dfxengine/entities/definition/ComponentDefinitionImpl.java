package me.datafox.dfxengine.entities.definition;

import lombok.*;
import me.datafox.dfxengine.entities.EntityComponentImpl;
import me.datafox.dfxengine.entities.api.Engine;
import me.datafox.dfxengine.entities.api.EntityComponent;
import me.datafox.dfxengine.entities.api.definition.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author datafox
 */
@Data
@NoArgsConstructor
public class ComponentDefinitionImpl implements ComponentDefinition {
    public String handle;
    public List<DataDefinition> data;
    public List<LinkDefinition> links;
    public List<ActionDefinition> actions;

    @Builder
    public ComponentDefinitionImpl(String handle,
                                   @Singular("data") List<DataDefinition> data,
                                   @Singular List<LinkDefinition> links,
                                   @Singular List<ActionDefinition> actions) {
        this.handle = handle;
        this.data = new ArrayList<>(data);
        this.links = new ArrayList<>(links);
        this.actions = new ArrayList<>(actions);
    }

    @Override
    public EntityComponent build(Engine engine) {
        return new EntityComponentImpl(this, engine);
    }

    @Override
    public void append(ComponentDefinition other) {
        Map<String,DataDefinition> dataMap = data.stream()
                .collect(Collectors.toMap(
                        DataDefinition::getHandle,
                        Function.identity()));
        Map<String,LinkDefinition> linkMap = links.stream()
                .collect(Collectors.toMap(
                        LinkDefinition::getHandle,
                        Function.identity()));
        Map<String,ActionDefinition> actionMap = actions.stream()
                .collect(Collectors.toMap(
                        ActionDefinition::getHandle,
                        Function.identity()));
        other.getData().forEach(oData -> appendData(oData, dataMap));
        other.getLinks().forEach(link -> appendLink(link, linkMap));
        other.getActions().forEach(action -> appendAction(action, actionMap));
    }

    private void appendData(DataDefinition oData, Map<String,DataDefinition> map) {
        if(map.containsKey(oData.getHandle())) {
            data.set(data.indexOf(map.get(oData.getHandle())), oData);
            return;
        }
        data.add(oData);
    }

    private void appendLink(LinkDefinition link, Map<String,LinkDefinition> map) {
        if(map.containsKey(link.getHandle())) {
            links.set(links.indexOf(map.get(link.getHandle())), link);
            return;
        }
        links.add(link);
    }

    private void appendAction(ActionDefinition action, Map<String,ActionDefinition> map) {
        if(map.containsKey(action.getHandle())) {
            actions.set(actions.indexOf(map.get(action.getHandle())), action);
            return;
        }
        actions.add(action);
    }
}
