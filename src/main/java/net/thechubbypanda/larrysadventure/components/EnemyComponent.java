package net.thechubbypanda.larrysadventure.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class EnemyComponent implements Component {

	public final Vector2 startPosition = new Vector2();
	public ArrayList<Vector2> patrolPoints = new ArrayList<>();
	public int nextPoint = 0;
	public float percent = 1000f;
	public boolean reverse = false;

	public EnemyComponent(ArrayList<Vector2> patrolPoints) {
		this.patrolPoints.addAll(patrolPoints);
	}
}
