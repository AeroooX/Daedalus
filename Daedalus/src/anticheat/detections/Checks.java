package anticheat.detections;

import anticheat.Daedalus;
import anticheat.utils.Color;
import anticheat.utils.JsonMessage;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Created by XtasyCode on 11/08/2017.
 */

public class Checks {

    public static Daedalus ac;
    public ChecksType type;
    private String name, description;
    private boolean state;
    public static ArrayList<String> playersToBan = new ArrayList<>();

    private int weight;

    public Checks(String name, String description, ChecksType type, int weight, Daedalus ac, boolean state) {
        this.name = name;
        this.description = description;
        Checks.ac = ac;
        this.type = type;
        this.weight = weight;
        this.state = state;
        ac.getChecks();
        ChecksManager.getDetections().add(this);
    }

    public int getWeight() {
        return weight;
    }

    public void debug(String string) {
    	Bukkit.broadcastMessage(Color.Aqua + "DEBUG: " + string);

    }
    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void toggle() {
        this.setState(!this.state);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    protected void onEvent(Event event) {
    }

    public void Alert(Player p, String value) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.isOp() || player.hasPermission("daedalus.staff")) {
				JsonMessage msg = new JsonMessage();
				msg.addText(Color.translate(Daedalus.getAC().getConfig().getString("Alert_Message").replaceAll("%prefix%", Daedalus.getAC().getPrefix()).replaceAll("%player%", p.getName()).replaceAll("%check%", getName().toUpperCase()).replaceAll("%info%", value).replaceAll("%violations%", String.valueOf(Daedalus.getData().getProfil(p).getWeight())))
						).addHoverText(Color.Gray + "Teleport to " + p.getName() + "?").setClickEvent(JsonMessage.ClickableType.RunCommand, "/tp " + p.getName());
				for(Player online : Bukkit.getOnlinePlayers()) {
					if(online.hasPermission("daedalus.staff")) {
						msg.sendToPlayer(online);
					}
				}
			}
		}
	}

	public void kick(Player p) {
		Daedalus.getData().addDetecton(p, this);
		if (Daedalus.getData().getProfil(p).needKick()) {
			Daedalus.getAC().getServer().dispatchCommand(
					Daedalus.getAC().getServer().getConsoleSender(),
					Color.translate(Daedalus.getAC().getConfig().getString("Punish_Cmd").replaceAll("%player%", p.getName())));
			Bukkit.broadcastMessage(Color.translate(Daedalus.getAC().getConfig().getString("Punish_Broadcast")));
			
		}
	}
}