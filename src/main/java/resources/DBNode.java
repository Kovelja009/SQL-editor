package resources;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class DBNode {

    private String name;
    @ToString.Exclude
    private DBNode parent;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DBNode) {
            DBNode otherObj = (DBNode) obj;
            return this.getName().equals(otherObj.getName());
        }
        return false;
    }

}
