package com.pineconeindustries.shared.actions.types;

import org.luaj.vm2.LuaFunction;

import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.server.galaxy.Galaxy;
import com.pineconeindustries.shared.components.gameobjects.Entity;
import com.pineconeindustries.shared.components.gameobjects.GameObject;
import com.pineconeindustries.shared.components.gameobjects.Projectile;
import com.pineconeindustries.shared.log.Log;
import com.pineconeindustries.shared.scripting.ScriptInterface;
import com.pineconeindustries.shared.utils.PseudoRandom;

public class DirectProjectileAction extends ActionBase {

	public static int projectileID = 0;
	LuaFunction f;

	public DirectProjectileAction(int id, String name) {
		super(id, name);
		init();

	}

	public void init() {

		script = ScriptInterface.getInstance().loadScript(name);

		load();
	}

	@Override
	public void use(ActionPackage data) {

		if (data.getTarget() == null) {
			return;
		}
		if (data.getCaster().isInRange(data.getTarget(), _range)) {
			onCast(data);
			projectileID++;
			float accuracy = data.getCaster().getStats().getAccuracy();
			float xOff = Math.max(0, PseudoRandom.getFloat(10, 40) - accuracy);
			float yOff = Math.max(0, PseudoRandom.getFloat(10, 40) - accuracy);

			int flipX = PseudoRandom.getInt(0, 2);
			int flipY = PseudoRandom.getInt(0, 2);

			if (flipX == 1) {
				xOff = -xOff;

			}
			if (flipY == 1) {
				yOff = -yOff;
			}

			Vector2 tmpLoc = new Vector2(data.getCaster().getCenter().x, data.getCaster().getCenter().y);
			Vector2 tmpDest = new Vector2(data.getTarget().getCenter().x + xOff, data.getTarget().getCenter().y + yOff);
			Vector2 dir = new Vector2(tmpDest.sub(tmpLoc)).nor();
			Projectile p = new Projectile("pew",
					new Vector2(data.getCaster().getCenter().x, data.getCaster().getCenter().y),
					new Vector2(dir.x, dir.y), data.getCaster().getLayer(), projectileID, _speed,
					data.getCaster().getSectorID(), data.getCaster().getStructureID(), _life, this, data);
			Galaxy.getInstance().getSectorByID(data.getCaster().getSectorID()).addProjectile(p);

		} else {
			//
		}

	}

	public String toString() {

		return new String(
				"Action  " + name + "   [ damage :" + _damage + ", cooldown :" + _cooldown + ", range :" + _range);

	}
}
