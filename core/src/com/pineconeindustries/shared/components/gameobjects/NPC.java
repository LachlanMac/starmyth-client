package com.pineconeindustries.shared.components.gameobjects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pineconeindustries.client.manager.LogicController;
import com.pineconeindustries.server.ai.FiniteStateMachine;
import com.pineconeindustries.server.ai.pathfinding.AStarPath;
import com.pineconeindustries.server.ai.pathfinding.PathNode;
import com.pineconeindustries.server.ai.roles.Role;
import com.pineconeindustries.server.ai.roles.RoleManager;
import com.pineconeindustries.server.galaxy.Sector;
import com.pineconeindustries.server.net.packetdata.MoveData;
import com.pineconeindustries.shared.actions.types.Action;
import com.pineconeindustries.shared.actions.types.DataPackage;
import com.pineconeindustries.shared.components.structures.Structure;
import com.pineconeindustries.shared.components.ui.StatusBar;
import com.pineconeindustries.shared.data.Assets;
import com.pineconeindustries.shared.data.DebugTexture;
import com.pineconeindustries.shared.data.Global;
import com.pineconeindustries.shared.stats.Stats;
import com.pineconeindustries.shared.text.Text;
import com.pineconeindustries.shared.units.Units;
import com.pineconeindustries.shared.utils.VectorMath;

public class NPC extends Entity {

	private final float AGGRO_RADIUS = 2000f;

	private ArrayBlockingQueue<Entity> aggroList;

	private Sector sector;
	private FiniteStateMachine fsm;
	private Role profession;
	private int professionID;
	private Vector2 destination = null;
	private boolean destinationReached = false;
	private boolean spin = false;
	private boolean hasPath = false;
	private AStarPath pathfinder;
	private Structure structure;
	private LinkedList<PathNode> path;
	private Globals local;
	private LuaValue script;
	private boolean hasScript = false;

	private Entity aggroedTarget = null;

	private LuaFunction _ON_DEATH, _ON_HIT, _ON_NEW_PATH;

	private StatusBar hb;

	private Texture pathTexture;

	private DebugTexture startTexture, endTexture;

	// CLIENT CONSTRUCTOR
	public NPC(int id, String name, Vector2 loc, int sectorID, int structureID, int layer, int factionID) {
		super(id, name, loc, sectorID, structureID, layer, factionID);
		hb = new StatusBar(loc.x, loc.y);
		textName = new Text(getName(), getCenter(), 64);
		animSet = Assets.getInstance().getDefaultAnimations();
		currentFrame = animSet.getAnimation(lastDirectionFaced, 0, getAnimationCode());
	}

	// SERVER CONSTRUCTOR
	public NPC(int id, String name, Vector2 loc, Sector sector, int structureID, int layer, int factionID,
			int professionID) {
		super(id, name, loc, sector.getPort(), structureID, layer, factionID);
		this.professionID = professionID;
		this.sector = sector;
		this.structure = sector.getStructureByID(structureID);
		this.stats = new Stats();
		if (!Global.isHeadlessServer()) {
			textName = new Text(getName(), getCenter(), 64);
			animSet = Assets.getInstance().getDefaultAnimations();
			currentFrame = animSet.getAnimation(lastDirectionFaced, 0, getAnimationCode());
			pathTexture = Assets.getInstance().getManager().get("textures/path.png");
			Texture start = Assets.getInstance().getManager().get("textures/start.png");
			Texture end = Assets.getInstance().getManager().get("textures/end.png");
			startTexture = new DebugTexture(start, 0, 0);
			endTexture = new DebugTexture(end, 0, 0);

		} else {

			animSet = null;
		}

		profession = RoleManager.getProfessionByID(professionID);
		fsm = new FiniteStateMachine(this);
		aggroList = new ArrayBlockingQueue<Entity>(16);
		actionSet = profession.getActionSetByProfession();
		speed = Units.NPC_SPEED;
		goType = type.NPC;
		addDefaultEntityPassives();

	}

	@Override
	public void render(SpriteBatch b) {

		state += Gdx.graphics.getDeltaTime();

		currentFrame = animSet.getAnimation(lastDirectionFaced, velocity, getAnimationCode());

		if (velocity == 999 || spin == true) {
			TextureRegion t = currentFrame.getKeyFrame(state, true);

			b.draw(currentFrame.getKeyFrame(interval, true), renderLoc.x, renderLoc.y, t.getRegionWidth() / 2,
					t.getRegionHeight() / 2, t.getRegionWidth(), t.getRegionHeight(), 1, 1,
					3f + (state * Units.SPIN_SPEED), false);

		} else {

			if (Global.isClient()) {
				if (LogicController.getInstance().getPlayer().isPlayerTarget(this)) {
					b.draw(Assets.getInstance().getTargetAnimation().getKeyFrame(state * 50, true), renderLoc.x,
							renderLoc.y);

				}
				hb.render(b);
			}

			b.draw(currentFrame.getKeyFrame(state, true), renderLoc.x, renderLoc.y);

		}

		if (renderLoc.dst(loc) > 500) {
			renderLoc = loc;
		} else {

			Vector2 position = renderLoc;
			renderLoc.x += (getLoc().x - position.x) * Units.NPC_LERP * Gdx.graphics.getDeltaTime();
			renderLoc.y += (getLoc().y - position.y) * Units.NPC_LERP * Gdx.graphics.getDeltaTime();
			framesSinceLastMove++;
		}

		if (getFramesSinceLastMove() > 5) {
			if (velocity != 999)

				velocity = 0;
		}

		if (!Global.isHeadlessServer() && !Global.isClient()) {
			if (!hasPath) {
				return;
			}
			for (PathNode p : path) {
				b.draw(pathTexture, p.getX() * 32, p.getY() * 32);

				endTexture.render(b);

				startTexture.render(b);

			}
		}

	}

	public void setLocation(Vector2 loc) {
		this.loc = loc;
		this.renderLoc = loc;

	}

	public void move() {

		if (destination != null) {

			calculateMov(destination);
		} else {
			System.out.println("Destination is null");
		}
	}

	public void setDestination(Vector2 destination) {
		this.destination = destination;
	}

	public void calculateMov(Vector2 dest) {

		float distance = (float) Math.sqrt(Math.pow(dest.x - loc.x, 2) + Math.pow(dest.y - loc.y, 2));

		if (distance == 0) {
			destinationReached = true;
			destination = null;
			return;
		}
		float directionX = (dest.x - loc.x) / distance;
		float directionY = (dest.y - loc.y) / distance;

		// On update
		if (!destinationReached) {
			loc.x += (directionX * speed * (Gdx.graphics.getDeltaTime()));
			loc.y += (directionY * speed * (Gdx.graphics.getDeltaTime()));

			velocity = (Math.abs(loc.x) + Math.abs(loc.y)) / 2;

			setLastDirectionFaced(new Vector2(directionX, directionY));
			setVelocity(velocity);
			bounds.x = loc.x;
			bounds.y = loc.y;

			sendMoveData(VectorMath.getPacketDirection(directionX, directionY));

			if (dest.dst(loc) <= 5) {

				destinationReached = true;
				destination = null;
			}

		}
	}

	public void sendMoveData(String dir) {
		String data = new String(
				getID() + "x" + df.format(getLoc().x) + "x" + df.format(getLoc().y) + "x" + dir + "x" + layer + "=");

		structure.addNPCMovementData(new MoveData(data, structureID, layer));
	}

	public void moveOnPath() {

		if (destinationReached) {

			PathNode p = path.pop();
			if (!path.isEmpty()) {

				fsm.getOwner().setDestinationReached(false);
				Vector2 vector = structure
						.getGlobalVector(new Vector2(p.getX() * Units.GRID_INTERVAL, p.getY() * Units.GRID_INTERVAL));

				setDestination(vector);

			} else {

				hasPath = false;
			}

		}

		move();

	}

	public void clearPath() {

		destinationReached = false;
		path = null;
		hasPath = false;
		destination = null;

	}

	public void findPathTo(GameObject obj) {
		System.out.println("FIDNING PATH TO " + obj.getLoc());

		endTexture.setOn();
		startTexture.setOn();
		Vector2 localVector = obj.getCenter();

		GridTile t = obj.getStructure().getLayerByNumber(obj.getLayer()).getGridTileAt(localVector.x, localVector.y);

		startTexture.setLoc(t.getX() * 32, t.getY() * 32);
		System.out.println(t.getX() * 32 + ", " + t.getY() * 32);

		PathNode n = t.getPathNode();

		endTexture.setLoc(n.getX() * 32, n.getY() * 32);

		pathfinder = new AStarPath(structure.getGridWidth(), structure.getGridHeight(),
				new PathNode((int) (getLoc().x / Units.GRID_INTERVAL), (int) (getLoc().y / Units.GRID_INTERVAL)), n);
		pathfinder.setBlocks(structure.getLayerByNumber(layer).getBlocked());
		ArrayList<PathNode> pathArray = pathfinder.findPath();
		if (pathArray.size() == 0) {
			System.out.println("NO PATH FOUND!");
		} else {

			path = new LinkedList<PathNode>();
			for (PathNode p : pathArray) {
				path.add(p);
			}

			Vector2 firstVector = structure.getGlobalVector(
					new Vector2(path.peek().getX() * Units.GRID_INTERVAL, path.peek().getY() * Units.GRID_INTERVAL));

			if (path.size() == 0) {
				System.out.println("NO PATH!");
				System.exit(0);
				setLocation(firstVector);
				hasPath = false;
			} else {
				System.out.println("Setting Destination to " + firstVector + " because path is " + path.size());
				setDestination(firstVector);
				hasPath = true;
				path.pop();

			}
		}

	}

	public void resetSpeed() {
		this.speed = Units.NPC_SPEED;
	}

	public void findRandomPath() {

		PathNode end = structure.getLayerByNumber(layer).getRandomEndNode();
		pathfinder = new AStarPath(structure.getGridWidth(), structure.getGridHeight(),
				new PathNode((int) (getLoc().x / Units.GRID_INTERVAL), (int) (getLoc().y / Units.GRID_INTERVAL)), end);
		pathfinder.setBlocks(structure.getLayerByNumber(layer).getBlocked());
		ArrayList<PathNode> pathArray = pathfinder.findPath();
		if (pathArray.size() == 0) {
			// no path found
		} else {

			path = new LinkedList<PathNode>();
			for (PathNode p : pathArray) {
				path.add(p);
			}

			Vector2 firstVector = structure.getGlobalVector(
					new Vector2(path.peek().getX() * Units.GRID_INTERVAL, path.peek().getY() * Units.GRID_INTERVAL));

			if (path.size() == 0) {
				setLocation(firstVector);
				hasPath = false;
			} else {
				setDestination(firstVector);
				hasPath = true;
				path.pop();

			}
		}

	}

	public Vector2 getDestination() {
		return destination;
	}

	public boolean isDestinationReached() {
		return destinationReached;
	}

	public void setDestinationReached(boolean destinationReached) {
		this.destinationReached = destinationReached;
	}

	public boolean hasPath() {
		return hasPath;
	}

	@Override
	public void update() {

		if (Global.isServer()) {
			actionSet.update(Gdx.graphics.getDeltaTime());
			updateEffects(Gdx.graphics.getDeltaTime());
			fsm.performAction();
		}
		if (Global.isClient()) {
			hb.update(renderLoc.x, renderLoc.y, stats.getCurrentHP() / stats.getHp(),
					stats.getCurrentEnergy() / stats.getEnergy());
			updateText();
			onClick();
		}
	}

	public boolean lookAt(Entity e) {

		boolean okay = VectorMath.hasLineOfSight(structure.getLayerByNumber(layer),
				structure.getLocalVector(this.getCenter()), structure.getLocalVector(e.getCenter()));

		Vector2 dir = VectorMath.getDirection(this.getCenter(), e.getCenter());
		sendMoveData(VectorMath.getPacketDirection(dir.x, dir.y));

		return okay;
	}

	@Override
	public String getType() {
		return "n";
	}

	public Role getProfession() {
		return profession;
	}

	public void addToAggroList(Entity e) {

		if (!aggroList.contains(e)) {
			aggroList.add(e);
		}

	}

	public Action getAction(String name) {

		return null;
	}

	public void castDirectAction(String name, Entity target) {

		for (Action a : actionSet.getActions()) {

			if (a.getName().equals(name)) {

				DataPackage d = new DataPackage(this, target);
				a.use(d);
			}

		}
	}

	public void updateAggroList() {

		for (Entity e : aggroList) {

			if (e.isDowned()) {
				removeFromAggroList(e);
			}

		}

	}

	public Entity getClosestTarget() {
		Entity closest = null;
		float closestDistance = AGGRO_RADIUS;

		for (Entity entity : aggroList) {

			if (closest == null) {
				closest = entity;
				closestDistance = this.loc.dst(entity.getLoc());
			} else {
				float dist = this.loc.dst(entity.getLoc());
				if (dist <= closestDistance) {
					closest = entity;
				}
			}

		}
		return closest;

	}

	public void removeFromAggroList(Entity e) {
		aggroList.remove(e);
	}

	public ArrayBlockingQueue<Entity> getAggroList() {
		return aggroList;
	}

	public boolean inCombat() {

		if (aggroList.isEmpty()) {
			return false;
		} else {
			return true;
		}

	}

}
