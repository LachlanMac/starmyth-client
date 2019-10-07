package com.pineconeindustries.shared.actions.types;

import com.pineconeindustries.shared.components.gameobjects.Entity;
import com.pineconeindustries.shared.components.gameobjects.GameObject;
import com.pineconeindustries.shared.components.gameobjects.NPC;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.components.structures.Tile;

public class DataPackage {
	private Entity target;
	private Entity caster, entityHit;
	private Tile tileHit;
	private String error;
	private NPC npc;
	private Structure structure;

	public DataPackage(Entity caster, Entity target, Entity entityHit) {
		this.caster = caster;
		this.target = target;
		this.entityHit = entityHit;
	}

	public DataPackage(Entity caster, Entity target, Tile tileHit) {
		this.caster = caster;
		this.target = target;
		this.tileHit = tileHit;
	}

	public DataPackage(Entity caster) {
		this.caster = caster;
	}

	public DataPackage(NPC npc, Structure structure) {
		this.npc = npc;
		this.structure = structure;
	}

	public NPC getNPC() {
		return npc;
	}

	public Structure getStructure() {
		return structure;
	}

	public DataPackage(Entity caster, Entity target) {
		this.caster = caster;
		this.target = target;
	}

	public DataPackage() {

	}

	public Entity getTarget() {
		return target;
	}

	public Entity getEntityHit() {
		return entityHit;
	}

	public Tile getTileHit() {
		return tileHit;
	}

	public Entity getCaster() {
		return caster;
	}

	public void setTileHit(Tile tileHit) {
		this.tileHit = tileHit;
	}

	public void setEntityHit(Entity entityHit) {
		this.entityHit = entityHit;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	public String error() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
