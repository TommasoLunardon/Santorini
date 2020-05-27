package it.polimi.ingsw.client.view.gui;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

/**
 * this class check the number of divinity selected
 * @author Jing Huang
 */

public class CheckDivinityNum
{
    private Set<GroupButtonModel> models = new HashSet<GroupButtonModel>();
    private int groupSize;

    public CheckDivinityNum(int groupSize)
    {
        this.groupSize = groupSize;
    }

    public void register(JCheckBox checkBox)
    {
        ButtonModel groupModel = new GroupButtonModel();
        groupModel.setSelected ( checkBox.getModel().isSelected() );
        checkBox.setModel( groupModel );
    }


    private class GroupButtonModel extends JToggleButton.ToggleButtonModel
    {
        @Override
        public void setSelected(boolean selected)
        {
            if (!selected)
            {
                models.remove( this );
                super.setSelected( selected );
                return;
            }

            if (models.size() == groupSize)
            {
                System.out.println("Is selected more than " + groupSize + " divinity");
            }
            else
            {
                models.add( this );
                super.setSelected( selected );
            }

        }
    }
}