package resources.implementation;

import lombok.Getter;
import lombok.Setter;
import resources.DBNode;
import resources.enums.ConstraintType;

@Getter
@Setter
public class AttributeConstraint extends DBNode {

    private ConstraintType constraintType;

    public AttributeConstraint(String name, DBNode parent, ConstraintType constraintType) {
        super(name, parent);
        this.constraintType = constraintType;
    }

}
