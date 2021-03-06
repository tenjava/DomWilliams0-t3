package com.tenjava.entries.DomWilliams0.t3.disease.sporeclouds;

import com.tenjava.entries.DomWilliams0.t3.TenJava;
import com.tenjava.entries.DomWilliams0.t3.disease.Disease;
import com.tenjava.entries.DomWilliams0.t3.disease.WorldCorruption;
import com.tenjava.entries.DomWilliams0.t3.util.Effects;
import com.tenjava.entries.DomWilliams0.t3.util.Utils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * A cloud of toxic spores that corrupt the world and infect nearby entities
 */
public class MobileSporeCloud extends Disease
{
	private Location centre;
	private Vector direction;
	private WorldCorruption worldInfection;
	private int lastDirectionChange;

	public MobileSporeCloud(Location location)
	{
		this.centre = location;
		this.direction = Utils.randomVector();
		this.worldInfection = new WorldCorruption();
		this.lastDirectionChange = 0;
	}

	@Override
	public void tick()
	{
		Effects.sporeEffect(centre, 4);
		move();
		infectNearby(centre);
		worldInfection.tick(centre);
	}


	private void move()
	{
		// randomly change direction
		if (--lastDirectionChange <= 0)
		{
			direction = Utils.randomVector();
			lastDirectionChange = TenJava.RANDOM.nextInt(5) + 3;
		}

		// get path
		BlockIterator iterator = new BlockIterator(centre.getWorld(), centre.toVector(), direction, 0, 2);
		List<Block> blocks = new ArrayList<Block>();
		while (iterator.hasNext())
			blocks.add(iterator.next());

		for (int i = 0; i < blocks.size() - 1; i++)
		{
			Block block = blocks.get(i);
			Block next = blocks.get(i + 1);

			// hit wall
			if (block.getType().isTransparent() && !next.getType().isTransparent())
			{
				direction = Utils.randomVector();
				return;
			}

			blocks.set(i + 1, next);
		}

		centre = blocks.get(blocks.size() - 1).getLocation();
	}


}
