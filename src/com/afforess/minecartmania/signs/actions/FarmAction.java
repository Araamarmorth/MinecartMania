package com.afforess.minecartmania.signs.actions;

import com.afforess.minecartmania.MinecartManiaMinecart;
import com.afforess.minecartmania.signs.MMSign;
import com.afforess.minecartmania.signs.SignAction;

public class FarmAction extends SignAction
{
  protected boolean deactivateAllFarming = false;
  protected String farmType = "";


  public boolean execute(MinecartManiaMinecart minecart)
  {
    if (minecart.isStorageMinecart())
    {
      if (this.deactivateAllFarming)
      {
        minecart.setDataValue("Farm", null);
        return true;
      }
      if (!this.farmType.equals(""))
      {
        minecart.setDataValue("Farm", this.farmType);
        return true;
      }
    }

    return false;
  }

  public boolean async()
  {
    return true;
  }

	public boolean process(String[] lines)   {
	    if (lines[0].toLowerCase().contains("[farm"))
	    {
	      String line = lines[1].toLowerCase();
	      if (line.contains("off"))
	      {
	        this.deactivateAllFarming = true;
	      }
	      else
	      {
	        if (line.startsWith("["))
	        {
	          line = line.substring(1, line.length() - 1);
	        }
	        this.farmType = line;
	      }

	    }
	    
    return (this.deactivateAllFarming) || (!this.farmType.equals(""));
  }

  public String getPermissionName()
  {
    return "farmsign";
  }

  public String getFriendlyName()
  {
    return "Farm Sign";
  }
}