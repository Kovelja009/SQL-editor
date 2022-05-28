package resources.implementation;

import lombok.Getter;
import lombok.Setter;
import resources.DBNode;
import resources.DBNodeComposite;

@Getter
@Setter
public class Entity extends DBNodeComposite {

    public Entity(String name, DBNode parent) {
        super(name, parent);
    }

    @Override
    public void addChild(DBNode child) {
        if (child != null && child instanceof Attribute){
            Attribute attribute = (Attribute) child;
            this.getChildren().add(attribute);
        }

    }
}
