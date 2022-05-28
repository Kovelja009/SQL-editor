package tree.implementation;

import resources.DBNode;
import resources.DBNodeComposite;
import resources.implementation.InformationResource;
import tree.Tree;
import tree.TreeItem;

import javax.swing.tree.DefaultTreeModel;
import java.util.List;

public class TreeImplementation implements Tree {

    @Override
    public DefaultTreeModel generateTree(InformationResource informationResource) {

        TreeItem root = new TreeItem(informationResource, informationResource.getName());
        connectChildren(root);
        return new DefaultTreeModel(root);
    }


    private void connectChildren(TreeItem current){

        if (!(current.getDbNode() instanceof DBNodeComposite)) return;

        List<DBNode> children = ((DBNodeComposite) current.getDbNode()).getChildren();
        for (int i = 0; i<children.size();i++){
            TreeItem child = new TreeItem(children.get(i), children.get(i).getName());
            current.insert(child,i);
            connectChildren(child);
        }

    }

}
