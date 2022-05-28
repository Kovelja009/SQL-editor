package tree;

import lombok.Getter;
import lombok.Setter;
import resources.DBNodeComposite;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.Iterator;

@Getter
@Setter
public class TreeItem<DBNode> extends DefaultMutableTreeNode {


    private String name;
    private DBNode dbNode;

    public TreeItem(DBNode node) {
        this.dbNode = node;
    }

    public TreeItem(DBNode dbNode, String name) {
        this.name = name;
        this.dbNode = dbNode;
    }

    @Override
    public int getIndex(TreeNode node) {
        return findIndexByChild((TreeItem)node);
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return findChildByIndex(childIndex);
    }

    @Override
    public int getChildCount() {
        if(dbNode instanceof DBNodeComposite)
            return ((DBNodeComposite) dbNode).getChildren().size();
        return 0;
    }

    @Override
    public boolean getAllowsChildren() {
        if(dbNode instanceof DBNodeComposite)
            return true;
        return false;
    }

    @Override
    public boolean isLeaf() {
        if(dbNode instanceof DBNodeComposite)
            return false;
        return true;
    }

    @Override
    public Enumeration children() {
        if(dbNode instanceof DBNodeComposite)
            return (Enumeration) ((DBNodeComposite) dbNode).getChildren();
        return null;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof TreeItem) {
            TreeItem otherObj = (TreeItem) obj;
            return this.dbNode.equals(otherObj.dbNode);
        }
        return false;
    }

    private TreeNode findChildByIndex(int childIndex){

        if(dbNode instanceof DBNodeComposite){
            TreeItem<DBNode> toLookFor = new TreeItem (((DBNodeComposite) dbNode).getChildren().get(childIndex));

            Iterator childrenIterator = children.iterator();
            TreeNode current;

            while (childrenIterator.hasNext()){
                current = (TreeNode) childrenIterator.next();
                if (current.equals(toLookFor))
                    return current;
            }
        }

        return null;
    }

    private int findIndexByChild(TreeItem node){

        if(dbNode instanceof DBNodeComposite){
            return  ((DBNodeComposite) dbNode).getChildren().indexOf(node.getDbNode());
        }

        return -1;
    }

    @Override
    public String toString() {
        return name;
    }
}
