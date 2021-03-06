package net.thechubbypanda.larrysescape.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import net.thechubbypanda.larrysescape.Drop;

import java.util.ArrayList;

public class EnemyComponent implements Component {

	public State state = State.patrolling;
	public int nextPatrolPoint = 0;

	public final Vector2 startPosition = new Vector2();
	public float percent = 1000f;

	public ArrayList<Vector2> patrolPoints = new ArrayList<>();
	public ArrayList<Vector2> returnPoints = new ArrayList<>();
	public boolean reverse = false;
	public int nextReturnPoint = 0;

	public Drop drop;

	public enum State {
		calculateReturn, returning, chasing, patrolling
	}

	public EnemyComponent(ArrayList<Vector2> patrolPoints, Drop drop) {
		this.patrolPoints.addAll(patrolPoints);
		this.drop = drop;
	}
}
